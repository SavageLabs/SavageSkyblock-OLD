package com.peaches.epicskyblock.Inventories;

import com.peaches.epicskyblock.EpicSkyBlock;
import com.peaches.epicskyblock.Island;
import com.peaches.epicskyblock.Missions;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class MissionsGUI implements Listener {

    public static Inventory inv(Island island) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&e&lIsland Missions"));
        if (island == null) return inv;
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 9, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 8, " "));
            inv.setItem(i + 18, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
        }
        if (island.getMission1() == null) {
            EpicSkyBlock.getSkyblock.addMissions(island);
        }
        inv.setItem(11, Missions.getInstance.missions1.get(island.getMission1()));
        inv.setItem(13, Missions.getInstance.missions2.get(island.getMission2()));
        inv.setItem(15, Missions.getInstance.missions3.get(island.getMission3()));
        return inv;
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equals(inv(null).getTitle())) {
            e.setCancelled(true);
        }
    }

}
