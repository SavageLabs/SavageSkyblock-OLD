package net.prosavage.savageskyblock.Inventories;

import net.prosavage.savageskyblock.SavageSkyBlock;
import net.prosavage.savageskyblock.Island;
import net.prosavage.savageskyblock.IslandManager;
import net.prosavage.savageskyblock.User;
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
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Inventories.Upgrades")));
        if (island == null) return inv;
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 9, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 18, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
        }
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Need more room to expand? Buy this"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7upgrade to increase your island size."));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lInformation:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Level: &b" + island.getSize()));
        if (island.getSize() == 1) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Size: &b" + IslandManager.level1radius * 2 + "x" + IslandManager.level1radius * 2 + " Blocks"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Upgrade Cost: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Size.1") + " Crystals"));
        }
        if (island.getSize() == 2) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Size: &b" + IslandManager.level2radius * 2 + "x" + IslandManager.level2radius * 2 + " Blocks"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Upgrade Cost: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Size.2") + " Crystals"));
        }
        if (island.getSize() == 3) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Size: &b" + IslandManager.level3radius * 2 + "x" + IslandManager.level3radius * 2 + " Blocks"));
        }
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lLevels:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Level 1: &b" + IslandManager.level1radius * 2 + "x" + IslandManager.level1radius * 2 + " Blocks"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Level 2: &b" + IslandManager.level2radius * 2 + "x" + IslandManager.level2radius * 2 + " Blocks"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Level 3: &b" + IslandManager.level3radius * 2 + "x" + IslandManager.level3radius * 2 + " Blocks"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l[!] &bRight Click to Purchase this Upgrade."));
        inv.setItem(10, SavageSkyBlock.getSkyblock.makeItem(Material.GRASS, 1, 0, "&b&lIsland Size", lore));

        lore.clear();

        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Want to invite more friends? Buy this"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7upgrade to increase your member size."));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lInformation:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Level: &b" + island.getMemberCount()));
        if (island.getMemberCount() == 1) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Size: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members.1") + " Members"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Upgrade Cost: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Members.1") + " Crystals"));
        }
        if (island.getMemberCount() == 2) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Size: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members.2") + " Members"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Upgrade Cost: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Members.2") + " Crystals"));
        }
        if (island.getMemberCount() == 3) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Size: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members.3") + " Members"));
        }
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lLevels:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Level 1: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members.1") + " Members"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Level 2: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members.2") + " Members"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Level 3: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members.3") + " Members"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l[!] &bRight Click to Purchase this Upgrade."));

        inv.setItem(13, SavageSkyBlock.getSkyblock.makeItem(Material.ARMOR_STAND, 1, 0, "&b&lIsland Team Size", lore));
        lore.clear();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Need some extra island warps? Buy this"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7upgrade to increase your warp limit."));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lInformation:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Level: &b" + island.getWarpCount()));
        if (island.getWarpCount() == 1) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Size: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Warps.1") + " Warps"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Upgrade Cost: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Warps.1") + " Crystals"));
        }
        if (island.getWarpCount() == 2) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Size: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Warps.2") + " Warps"));
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Upgrade Cost: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Warps.2") + " Crystals"));
        }
        if (island.getWarpCount() == 3) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Size: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Warps.3") + " Warps"));
        }
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lLevels:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Level 1: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Warps.1") + " Warps"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Level 2: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Warps.2") + " Warps"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Level 3: &b" + SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Warps.3") + " Warps"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l[!] &bRight Click to Purchase this Upgrade."));
        inv.setItem(16, SavageSkyBlock.getSkyblock.makeItem(Material.ENDER_PORTAL_FRAME, 1, 0, "&b&lIsland Warp Limit", lore));
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
                    if (island.getCrystals() < SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Size.1")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Size.1"));
                        island.setSize(2);
                        island.setPos1(island.getPos1().add(-(IslandManager.level2radius - IslandManager.level1radius), 0, -(IslandManager.level2radius - IslandManager.level1radius)));
                        island.setPos2(island.getPos2().add(IslandManager.level2radius - IslandManager.level1radius, 0, IslandManager.level2radius - IslandManager.level1radius));
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the island size."));
                        p.openInventory(inv(island));
                        SavageSkyBlock.getSkyblock.sendIslandBoarder(p);
                        return;
                    }
                } else if (island.getSize() == 2) {
                    if (island.getCrystals() < SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Size.2")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Size.2"));
                        island.setSize(3);
                        island.setPos1(island.getMaxpos1());
                        island.setPos2(island.getMaxpos2());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the island size."));
                        p.openInventory(inv(island));
                        SavageSkyBlock.getSkyblock.sendIslandBoarder(p);
                        return;
                    }
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eMaximum Upgrade reached."));
            }
            if (e.getSlot() == 13) {
                if (island.getMemberCount() == 1) {
                    if (island.getCrystals() < SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Members.1")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                    } else {
                        island.removeCrystals(SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Members.1"));
                        island.setMemberCount(2);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the member count."));
                        p.openInventory(inv(island));
                        return;
                    }
                } else if (island.getMemberCount() == 2) {
                    if (island.getCrystals() < SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Members.2")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Members.2"));
                        island.setMemberCount(3);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the member count."));
                        p.openInventory(inv(island));
                        return;
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eMaximum Upgrade reached."));
                }
            }
            if (e.getSlot() == 16) {
                if (island.getWarpCount() == 1) {
                    if (island.getCrystals() < SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Warps.1")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Warps.1"));
                        island.setWarpCount(2);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the warp count."));
                        p.openInventory(inv(island));
                        return;
                    }
                } else if (island.getWarpCount() == 2) {
                    if (island.getCrystals() < SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Warps.2")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    } else {
                        island.removeCrystals(SavageSkyBlock.getSkyblock.getConfig().getInt("UpgradeCost.Warps.2"));
                        island.setWarpCount(3);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou have upgraded the warp count."));
                        p.openInventory(inv(island));
                        return;
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eMaximum Upgrade reached."));
                }
            }
        }
    }
}
