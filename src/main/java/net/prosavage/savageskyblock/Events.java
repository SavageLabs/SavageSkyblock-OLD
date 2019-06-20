package net.prosavage.savageskyblock;

import net.prosavage.savageskyblock.API.IslandMissionCompleteEvent;
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
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;

import java.util.ArrayList;
import java.util.Random;

public class Events implements Listener {

    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        if (e.getLocation().getWorld().equals(SavageSkyBlock.getSkyblock.getWorld())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTalk(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (User.getbyPlayer(p) == null) {
            User.users.put(p.getName(), new User(p.getName()));
        }
        User u = User.getbyPlayer(p);
        if (u.getIsland() != null) {
            if (u.getChat()) {
                e.setCancelled(true);
                for (String player : u.getIsland().getPlayers()) {
                    Player member = Bukkit.getPlayer(player);
                    if (member != null) {
                        member.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.IslandChatFormat").replace("%player%", p.getName()).replace("%message%", e.getMessage())));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onCropGrow(BlockGrowEvent e) {
        Island island = IslandManager.getIslandViaBlock(e.getBlock());
        if (island != null) {
            if (island.getFarmingBoosterActive()) {
                e.getBlock().setData((byte) (e.getBlock().getData() + 1));
            }
        }
    }

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        Player p = e.getEntity().getKiller();
        if (p == null) return;
        if (User.getbyPlayer(p) == null) {
            User.users.put(p.getName(), new User(p.getName()));
        }
        User u = User.getbyPlayer(p);
        if (u.getIsland() != null) {
            if (u.getIsland().getXPBoosterActive()) {
                e.setDroppedExp(e.getDroppedExp() * ConfigManager.getInstance().getConfig().getInt("Options.XpMultiplier"));
            }
        }
    }

    @EventHandler
    public void onSpawn(SpawnerSpawnEvent e) {
        if (IslandManager.getIslandViaBlock(e.getSpawner().getLocation().getBlock()) == null) return;
        if (IslandManager.getIslandViaBlock(e.getSpawner().getLocation().getBlock()).getSpawnerBoosterActive()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(SavageSkyBlock.getSkyblock, () -> e.getSpawner().setDelay(e.getSpawner().getDelay() / ConfigManager.getInstance().getConfig().getInt("Options.SpawnerMultiplier")), 0);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        User.getbyPlayer(e.getPlayer());
        SavageSkyBlock.getSkyblock.sendIslandBoarder(e.getPlayer());
    }

    @EventHandler
    public void onEntityKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            if (User.getbyPlayer(e.getEntity().getKiller()) == null) {
                    User.users.put(e.getEntity().getKiller().getName(), new User((e.getEntity().getKiller().getName())));
            }
            User u = User.getbyPlayer(e.getEntity().getKiller());
            if (u.getIsland() != null) {
                if(u.getIsland().getHunter().getCurrent() != 7500){
                    u.getIsland().getHunter().setCurrent(u.getIsland().getHunter().getCurrent()+1);
                }
                if(u.getIsland().getHunter().getCurrent() == 7500 && !u.getIsland().getHunter().getCompleted()){
                    u.getIsland().addCrystals(u.getIsland().getHunter().getReward());
                    u.getIsland().getHunter().setCompleted(true);
                    Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(u.getIsland(), u.getIsland().getHunter().getReward(), "Hunter"));
                }
            }
        }
    }

    @EventHandler
    public void onFish(PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (User.getbyPlayer(e.getPlayer()) == null) {
                User.users.put(e.getPlayer().getName(), new User(e.getPlayer().getName()));
            }
            User u = User.getbyPlayer(e.getPlayer());
            if (u.getIsland() != null) {
                if(u.getIsland().getFisherman().getCurrent() != 1500){
                    u.getIsland().getFisherman().setCurrent(u.getIsland().getFisherman().getCurrent()+1);
                }
                if(u.getIsland().getFisherman().getCurrent() == 1500 && !u.getIsland().getFisherman().getCompleted()){
                    u.getIsland().getFisherman().setCompleted(true);
                    u.getIsland().addCrystals(u.getIsland().getFisherman().getReward());
                    Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(u.getIsland(), u.getIsland().getFisherman().getReward(), "Fisherman"));
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        if (e.getBlock().getLocation().getWorld().equals(SavageSkyBlock.getSkyblock.getWorld())) {
            User u = User.getbyPlayer(e.getPlayer());
            if (u.getIsland() != null) {
                if (!u.getIsland().isblockinisland(e.getBlock().getX(), e.getBlock().getZ())) {
                    if (u.getBypass()) return;
                    e.setCancelled(true);
                }else{
                    if(e.getBlock().getType().name().endsWith("ORE")){
                        if(u.getIsland().getCollector().getCurrent() != 7500){
                            u.getIsland().getCollector().setCurrent(u.getIsland().getCollector().getCurrent()+1);
                        }
                        if(u.getIsland().getCollector().getCurrent() == 7500 && !u.getIsland().getCollector().getCompleted()){
                            u.getIsland().getCollector().setCompleted(true);
                            u.getIsland().addCrystals(u.getIsland().getCollector().getReward());
                            Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(u.getIsland(), u.getIsland().getCollector().getReward(), "Collector"));
                        }
                    }
                    if(e.getBlock().getType()==Material.SUGAR_CANE){
                        if(u.getIsland().getFarmer().getCurrent() != 5000){
                            u.getIsland().getFarmer().setCurrent(u.getIsland().getFarmer().getCurrent()+1);
                        }
                        if(u.getIsland().getFarmer().getCurrent() == 5000 && !u.getIsland().getFarmer().getCompleted()){
                            u.getIsland().addCrystals(u.getIsland().getFarmer().getReward());
                            u.getIsland().getFarmer().setCompleted(true);
                            Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(u.getIsland(), u.getIsland().getFarmer().getReward(), "Farmer"));
                        }
                    }
                    if(u.getIsland().getMiner().getCurrent() != 5000){
                        u.getIsland().getMiner().setCurrent(u.getIsland().getMiner().getCurrent()+1);
                    }
                    if(u.getIsland().getMiner().getCurrent() == 5000 && !u.getIsland().getMiner().getCompleted()){
                        u.getIsland().addCrystals(u.getIsland().getMiner().getReward());
                        u.getIsland().getMiner().setCompleted(true);
                        Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(u.getIsland(), u.getIsland().getMiner().getReward(), "Miner"));
                    }
                }
            }else{
                if (u.getBypass()) return;
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (e.getBlock().getLocation().getWorld().equals(SavageSkyBlock.getSkyblock.getWorld())) {
            User u = User.getbyPlayer(e.getPlayer());
            if (u.getIsland() != null) {
                if (!u.getIsland().isblockinisland(e.getBlockPlaced().getX(), e.getBlockPlaced().getZ())) {
                    if (u.getBypass()) return;
                    e.setCancelled(true);
                }else{
                    if(u.getIsland().getBuilder().getCurrent() != 10000){
                        u.getIsland().getBuilder().setCurrent(u.getIsland().getBuilder().getCurrent()+1);
                    }
                    if(u.getIsland().getBuilder().getCurrent() == 10000 && !u.getIsland().getBuilder().getCompleted()){
                        u.getIsland().addCrystals(u.getIsland().getBuilder().getReward());
                        u.getIsland().getBuilder().setCompleted(true);
                        Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(u.getIsland(), u.getIsland().getBuilder().getReward(), "Builder"));
                    }
                }
            }else{
                if (u.getBypass()) return;
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getLocation().getWorld().equals(SavageSkyBlock.getSkyblock.getWorld())) {
                if (e.getClickedBlock() == null) return;
                User u = User.getbyPlayer(e.getPlayer());
                if (u.getIsland() != null) {
                    if (!u.getIsland().isblockinisland(e.getClickedBlock().getX(), e.getClickedBlock().getZ())) {
                        if (u.getBypass()) return;
                        e.setCancelled(true);
                    }
                }else{
                    if (u.getBypass()) return;
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        SavageSkyBlock.getSkyblock.sendIslandBoarder(e.getPlayer());
        if (IslandManager.getIslandViaBlock(e.getTo().getBlock()) != null) {
            Island island = IslandManager.getIslandViaBlock(e.getTo().getBlock());
            if (!island.equals(IslandManager.getIslandViaBlock(e.getFrom().getBlock()))) {
                SavageSkyBlock.getSkyblock.sendTitle(e.getPlayer(), "&e&l" + island.getownername() + "'s Island", 20, 40, 20);
            }

        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (e.getTo().getWorld().equals(SavageSkyBlock.getSkyblock.getWorld())) {
            if (e.getTo().getY() <= 0) {
                //Send to island home
                Player p = e.getPlayer();
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                u.setFalldmg(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(SavageSkyBlock.getSkyblock, () -> u.setFalldmg(false), 20);
                if (u.getIsland() != null) {
                    u.getIsland().teleportHome(p);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("TeleportToIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                }
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player dmg = (Player) e.getDamager();
            User u = User.getbyPlayer(p);
            if (u == null) {
                User.users.put(p.getName(), new User(p.getName()));
            }
            if (IslandManager.getIslandViaBlock(e.getEntity().getLocation().getBlock()) != null) {
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
            Bukkit.getScheduler().runTask(SavageSkyBlock.getSkyblock, () -> {
                if (b.getType().equals(Material.COBBLESTONE) || b.getType().equals(Material.STONE)) {
                    if (!SavageSkyBlock.getSkyblock.isSurroundedByWater(fromLoc)) {
                        return;
                    }

                    Random r = new Random();
                    ArrayList<String> items = new ArrayList<>();
                    for (String item : SavageSkyBlock.getSkyblock.getConfig().getStringList("OreGen")) {
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

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getCause().equals(EntityDamageEvent.DamageCause.FALL)) {
                User u = User.getbyPlayer(p);
                if (u != null) {
                    if (u.getFalldmg()) {
                        e.setCancelled(true);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMission(IslandMissionCompleteEvent e) {
        for (String player : e.getIsland().getPlayers()) {
            Player p = Bukkit.getPlayer(player);
            if (p != null) {
                SavageSkyBlock.getSkyblock.sendTitle(p, "&e&lCompleted Mission: " + e.getName(), 20, 40, 20);
                if (e.getReward() > 1) {
                    SavageSkyBlock.getSkyblock.sendSubTitle(p, "&eReward: &7" + e.getReward() + " Crystals", 20, 40, 20);
                } else {
                    SavageSkyBlock.getSkyblock.sendSubTitle(p, "&eReward: &7" + e.getReward() + " Crystal", 20, 40, 20);
                }
            }
        }
    }
}
