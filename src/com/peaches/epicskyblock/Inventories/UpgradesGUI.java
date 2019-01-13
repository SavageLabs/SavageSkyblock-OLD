package com.peaches.epicskyblock.Inventories;

import com.peaches.epicskyblock.EpicSkyBlock;
import com.peaches.epicskyblock.Island;
import com.peaches.epicskyblock.IslandManager;
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
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Inventories.Upgrades")));
        if (island == null) return inv;
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 9, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 8, " "));
            inv.setItem(i + 18, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
        }
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Upgrade the size of your island."));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&e&lTier:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Current Level: &n" + island.getSize()));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&e&lPerks:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Level 1 - &rIsland size of " + IslandManager.level1radius * 2 + "x" + IslandManager.level1radius * 2));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Level 2 - &rIsland size of " + IslandManager.level2radius * 2 + "x" + IslandManager.level2radius * 2));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Level 3 - &rIsland size of " + IslandManager.level3radius * 2 + "x" + IslandManager.level3radius * 2));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&e&lClick to &nUnlock"));
        inv.setItem(10, EpicSkyBlock.getSkyblock.makeItem(Material.GRASS, 1, 0, "&e&lUpgrade Island Size", lore));

        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Upgrade the number of members your island can have"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&e&lTier:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Current Level: &n" + island.getMemberCount()));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&e&lPerks:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Level 1 - &rMaximum Member Count of " + EpicSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members.1")));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Level 2 - &rMaximum Member Count of " + EpicSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members.2")));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Level 3 - &rMaximum Member Count of " + EpicSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members.3")));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&e&lClick to &nUnlock"));

        inv.setItem(13, EpicSkyBlock.getSkyblock.makeItem(Material.ARMOR_STAND, 1, 0, "&e&lUpgrade Member Count", lore));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Upgrade the number of warps your island can have"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&e&lTier:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Current Level: &n" + island.getWarpCount()));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&e&lPerks:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Level 1 - &rMaximum Warp Count of " + EpicSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Warps.1")));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Level 2 - &rMaximum Warp Count of " + EpicSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Warps.2")));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&f&l* &7Level 3 - &rMaximum Warp Count of " + EpicSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Warps.3")));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&e&lClick to &nUnlock"));
        inv.setItem(16, EpicSkyBlock.getSkyblock.makeItem(Material.ENDER_PORTAL_FRAME, 1, 0, "&e&lUpgrade Island Warps", lore));
        return inv;
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Island island = User.getbyPlayer(p).getIsland();
        if (e.getInventory().getTitle().equals(inv(null).getTitle())) {
            e.setCancelled(true);
            if (e.getSlot() == 10) {
                if (island.getSize() == 1) {
                    if (island.getCrystals() < 15) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(15);
                        island.setSize(2);
                        island.setPos1(island.getPos1().add(-(IslandManager.level2radius - IslandManager.level1radius), 0, -(IslandManager.level2radius - IslandManager.level1radius)));
                        island.setPos2(island.getPos2().add(IslandManager.level2radius - IslandManager.level1radius, 0, IslandManager.level2radius - IslandManager.level1radius));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the island size."));
                        p.openInventory(inv(island));
                        EpicSkyBlock.getSkyblock.sendIslandBoarder(p);
                        return;
                    }
                } else if (island.getSize() == 2) {
                    if (island.getCrystals() < 15) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(15);
                        island.setSize(3);
                        island.setPos1(island.getMaxpos1());
                        island.setPos2(island.getMaxpos2());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the island size."));
                        p.openInventory(inv(island));
                        EpicSkyBlock.getSkyblock.sendIslandBoarder(p);
                        return;
                    }
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eMaximum Upgrade reached."));
            }
            if (e.getSlot() == 13) {
                if (island.getMemberCount() == 1) {
                    if (island.getCrystals() < 15) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                    } else {
                        island.removeCrystals(15);
                        island.setMemberCount(2);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the member count."));
                        p.openInventory(inv(island));
                        return;
                    }
                } else if (island.getMemberCount() == 2) {
                    if (island.getCrystals() < 15) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(15);
                        island.setMemberCount(3);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the member count."));
                        p.openInventory(inv(island));
                        return;
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eMaximum Upgrade reached."));
                }
            }
            if (e.getSlot() == 16) {
                if (island.getWarpCount() == 1) {
                    if (island.getCrystals() < 15) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(15);
                        island.setWarpCount(2);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the member count."));
                        p.openInventory(inv(island));
                        return;
                    }
                } else if (island.getWarpCount() == 2) {
                    if (island.getCrystals() < 15) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(15);
                        island.setWarpCount(3);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the member count."));
                        p.openInventory(inv(island));
                        return;
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eMaximum Upgrade reached."));
                }
            }
        }
    }
}
