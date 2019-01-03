package com.peaches.epicskyblock.Inventories;

import com.peaches.epicskyblock.EpicSkyBlock;
import com.peaches.epicskyblock.Island;
import com.peaches.epicskyblock.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class WarpGUI implements Listener {

    public static Inventory inv(Island island) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Inventories.Warp")));
        if (island == null) return inv;
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 9, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 8, " "));
            inv.setItem(i + 18, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(9, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 1, "&e&lWarp 1"));
            inv.setItem(11, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 1, "&e&lWarp 2"));
            inv.setItem(13, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 1, "&e&lWarp 3"));
            inv.setItem(15, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 1, "&e&lWarp 4"));
            inv.setItem(17, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 1, "&e&lWarp 5"));
        }
        return inv;
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Island island = User.getbyPlayer(p).getIsland();
        if (island == null) return;
        if (e.getInventory().getTitle().equals(inv(null).getTitle())) {
            if (e.getSlot() == 9) {
                if (island.getWarp1() != null) {
                    p.teleport(island.getWarp1());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis warp does not exist, do /is setwarp to set one"));
                }
            }
            if (e.getSlot() == 11) {
                if (island.getWarp2() != null) {
                    p.teleport(island.getWarp2());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis warp does not exist, do /is setwarp to set one"));
                }
            }
            if (e.getSlot() == 13) {
                if (island.getWarp3() != null) {
                    p.teleport(island.getWarp3());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis warp does not exist, do /is setwarp to set one"));
                }
            }
            if (e.getSlot() == 15) {
                if (island.getWarp4() != null) {
                    p.teleport(island.getWarp4());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis warp does not exist, do /is setwarp to set one"));
                }
            }
            if (e.getSlot() == 17) {
                if (island.getWarp5() != null) {
                    p.teleport(island.getWarp5());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis warp does not exist, do /is setwarp to set one"));
                }
            }
            e.setCancelled(true);
        }
    }
}
