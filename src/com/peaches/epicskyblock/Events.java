package com.peaches.epicskyblock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.Random;

public class Events implements Listener {

    @EventHandler
    public void onexplode(EntityExplodeEvent e) {
        e.setCancelled(true);
    }

    @EventHandler
    public void ontalk(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (User.getbyPlayer(p) == null) {
            User.users.add(new User(p.getName()));
        }
        if (User.getbyPlayer(p).getIsland() != null) {
            if (User.getbyPlayer(p).getChat()) {
                e.setCancelled(true);
                for (String player : User.getbyPlayer(p).getIsland().getPlayers()) {
                    Player member = Bukkit.getPlayer(player);
                    if (member != null) {
                        member.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l" + p.getName() + " &8» &e" + e.getMessage()));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCropGrow(BlockGrowEvent e) {
        Island island = IslandManager.getislandviablock(e.getBlock());
        if (island != null) {
            if (island.getFarmingBoosterActive()) {
                e.getBlock().setData((byte) (e.getBlock().getData() + 1));
            }
        }
    }

    @EventHandler
    public void onkill(EntityDeathEvent e) {
        Player p = e.getEntity().getKiller();
        if (p == null) return;
        if (User.getbyPlayer(p) == null) {
            User.users.add(new User(p.getName()));
        }
        User u = User.getbyPlayer(p);
        if (u.getIsland() != null) {
            if (u.getIsland().getXPBoosterActive()) {
                e.setDroppedExp(e.getDroppedExp() * ConfigManager.getInstance().getConfig().getInt("Options.XpMultiplier"));
            }
        }
    }

    @EventHandler
    public void onspawn(SpawnerSpawnEvent e) {
        if (IslandManager.getislandviablock(e.getSpawner().getLocation().getBlock()) == null) return;
        if (IslandManager.getislandviablock(e.getSpawner().getLocation().getBlock()).getSpawnerBoosterActive()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(EpicSkyBlock.getSkyblock, () -> e.getSpawner().setDelay(e.getSpawner().getDelay() / ConfigManager.getInstance().getConfig().getInt("Options.SpawnerMultiplier")), 0);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (User.getbyPlayer(e.getPlayer()) == null) {
            User.users.add(new User(e.getPlayer().getName()));
        }
    }

    @EventHandler
    public void onbreak(BlockBreakEvent e) {
        if (IslandManager.getislandviablock(e.getBlock()) != null) {
            if (!IslandManager.getislandviablock(e.getBlock()).getPlayers().contains(e.getPlayer().getName())) {
                if (User.getbyPlayer(e.getPlayer()).getBypass()) return;
                e.setCancelled(true);
            }
            return;
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onplace(BlockPlaceEvent e) {
        if (IslandManager.getislandviablock(e.getBlockPlaced()) != null) {
            if (!IslandManager.getislandviablock(e.getBlockPlaced()).getPlayers().contains(e.getPlayer().getName())) {
                if (User.getbyPlayer(e.getPlayer()).getBypass()) return;
                e.setCancelled(true);
            }
            return;
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void oninteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (IslandManager.getislandviablock(e.getClickedBlock()) != null) {
            if (!IslandManager.getislandviablock(e.getClickedBlock()).getPlayers().contains(e.getPlayer().getName())) {
                if (User.getbyPlayer(e.getPlayer()).getBypass()) return;
                e.setCancelled(true);
                for (String player : IslandManager.getislandviablock(e.getClickedBlock()).getPlayers()) {
                    System.out.print(player);
                }
            }
            return;
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onmove(PlayerMoveEvent e) {
        if (IslandManager.getislandviablock(e.getPlayer().getLocation().getBlock()) != null) {
            Island island = IslandManager.getislandviablock(e.getPlayer().getLocation().getBlock());
            int radius = 0;
            if (island.getSize() == 1) {
                radius = IslandManager.level1radius;
            }
            if (island.getSize() == 2) {
                radius = IslandManager.level2radius;
            }
            if (island.getSize() == 3) {
                radius = IslandManager.level3radius;
            }
            EpicSkyBlock.getSkyblock.sendBorder(e.getPlayer(), island.getCenter().getX(), island.getCenter().getZ(), radius - 1);
        }
        if (e.getTo().getY() <= 0) {
            //Send to island home
            Player p = e.getPlayer();
            if (User.getbyPlayer(p) == null) {
                User.users.add(new User(p.getName()));
            }
            if (User.getbyPlayer(p).getIsland() != null) {
                p.teleport(User.getbyPlayer(p).getIsland().gethome());
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eTeleporting to island..."));
            }
        }
    }

    @EventHandler
    public void ondmg(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player dmg = (Player) e.getDamager();
            User u = User.getbyPlayer(p);
            if (u == null) {
                User.users.add(new User(p.getName()));
            }
            if (IslandManager.getislandviablock(e.getEntity().getLocation().getBlock()) != null) {
                e.setCancelled(true);
                return;
            }
            if (u.getIsland() == null) return;
            if (u.getIsland().getPlayers().contains(dmg.getName())) e.setCancelled(true);
        }
    }

    @EventHandler
    public void onFromTo(BlockFromToEvent e) {
        if (e.getFace() != BlockFace.DOWN) {
            Block b = e.getToBlock();
            Location fromLoc = b.getLocation();
            Bukkit.getScheduler().runTask(EpicSkyBlock.getSkyblock, () -> {
                if (b.getType().equals(Material.COBBLESTONE) || b.getType().equals(Material.STONE)) {
                    if (!EpicSkyBlock.getSkyblock.isSurroundedByWater(fromLoc)) {
                        return;
                    }

                    Random r = new Random();
                    ArrayList<String> items = new ArrayList<>();
                    for (String item : EpicSkyBlock.getSkyblock.getConfig().getStringList("OreGen")) {
                        Integer i1 = Integer.parseInt(item.split(":")[1]);
                        for (int i = 0; i <= i1; i++) {
                            items.add(item.split(":")[0]);
                        }
                    }
                    String item = items.get(r.nextInt(items.size()));
                    if (Material.getMaterial(item) == null) return;
                    e.setCancelled(true);
                    b.setType(Material.getMaterial(item));
                    b.getState().update(true);
                }
            });
        }
    }
}
