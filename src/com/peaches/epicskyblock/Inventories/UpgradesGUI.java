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

import java.util.ArrayList;

public class UpgradesGUI implements Listener {

    public static Inventory inv(Island island) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&e&lIsland Upgrades"));
        if (island == null) return inv;
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 9, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 8, " "));
            inv.setItem(i + 18, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
        }
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6&lClick to upgrade"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&6&lCost &e&l15 Crystals"));
        inv.setItem(10, EpicSkyBlock.getSkyblock.makeItem(Material.GRASS, 1, 0, "&a&lIsland Size", lore));
        inv.setItem(13, EpicSkyBlock.getSkyblock.makeItem(Material.ARMOR_STAND, 1, 0, "&a&lMember Count", lore));
        inv.setItem(16, EpicSkyBlock.getSkyblock.makeItem(Material.ENDER_PORTAL_FRAME, 1, 0, "&a&lIsland Warps", lore));
        return inv;
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Island island = User.getbyPlayer(p).getIsland();
        if (e.getInventory().getTitle().equals(inv(null).getTitle())) {
            e.setCancelled(true);
            if (e.getSlot() == 13) {
                if (island.getMemberCount() == 1) {
                    if (island.getCrystals() < 15) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(15);
                        island.setMemberCount(2);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have upgraded the member count."));
                    }
                } else if (island.getMemberCount() == 2) {
                    if (island.getCrystals() < 15) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(15);
                        island.setMemberCount(3);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have upgraded the member count."));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eMaximum Upgrade reached."));
                }
                p.closeInventory();
            }
        }
    }

}
