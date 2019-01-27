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

public class BoostersGUI implements Listener {

    public static Inventory inv(Island island) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Inventories.Boosters")));
        if (island == null) return inv;
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 9, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 8, " "));
            inv.setItem(i + 18, EpicSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
        }
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Are your spawners too slow? Buy this booster and increase spawner rates x2"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lInformation:"));
        if (island.getSpawnerBoosterActive()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Time Remaining: &b" + island.getSpawner() + "s"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Time Remaining: &bNot Active"));
        }
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Booster Cost: &b" + EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.Spawner") + " Crystals"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l[!] &bRight Click to Purchase this Booster."));
        inv.setItem(10, EpicSkyBlock.getSkyblock.makeItem(Material.MOB_SPAWNER, 1, 0, "&b&lIncreased Mobs", lore));
        lore.clear();

        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Are your crops too slow? Buy this booster and increase crop rates x2"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lInformation:"));
        if (island.getFarmingBoosterActive()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Time Remaining: &b" + island.getFarming() + "s"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Time Remaining: &bNot Active"));
        }
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Booster Cost: &b" + EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.Crops") + " Crystals"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l[!] &bRight Click to Purchase this Booster."));
        inv.setItem(12, EpicSkyBlock.getSkyblock.makeItem(Material.WHEAT, 1, 0, "&b&lIncreased Crops", lore));
        lore.clear();

        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Takes too long to get exp? Buy this booster and increase exp rates x2"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lInformation:"));
        if (island.getXPBoosterActive()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Time Remaining: &b" + island.getXp() + "s"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Time Remaining: &bNot Active"));
        }
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Booster Cost: &b" + EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.XP") + " Crystals"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l[!] &bRight Click to Purchase this Booster."));
        inv.setItem(14, EpicSkyBlock.getSkyblock.makeItem(Material.EXP_BOTTLE, 1, 0, "&b&lIncreased Experiance", lore));
        lore.clear();

        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Tired of falling off your island? Buy this booster and allow your member to use fly"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lInformation:"));
        if (island.getFlyBoosterActive()) {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Time Remaining: &b" + island.getFly() + "s"));
        } else {
            lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Time Remaining: &bNot Active"));
        }
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Booster Cost: &b" + EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.Fly") + " Crystals"));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l[!] &bRight Click to Purchase this Booster."));
        inv.setItem(16, EpicSkyBlock.getSkyblock.makeItem(Material.FEATHER, 1, 0, "&b&lIncreased Flight", lore));
        return inv;
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Island island = User.getbyPlayer(p).getIsland();
        if (e.getInventory() != null) {
            if (e.getInventory().getTitle().equals(inv(island).getTitle())) {
                e.setCancelled(true);
                if (e.getSlot() == 10) {
                    if (island.getCrystals() < EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.Spawners")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    }
                    if (island.getSpawnerBoosterActive()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eThis booster is already activated"));
                        return;
                    }
                    //Spawner Booster
                    island.setSpawnerBoosterActive(true);
                    island.startspawnercountdown(60 * 60);
                    island.removeCrystals(EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.Spawners"));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eSpawner Booster Activated"));
                }
                if (e.getSlot() == 12) {
                    if (island.getCrystals() < EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.Crops")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    }
                    if (island.getFarmingBoosterActive()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eThis booster is already activated"));
                        return;
                    }
                    //Farming Booster
                    island.setFarmingBoosterActive(true);
                    island.startfarmingcountdown(60 * 60);
                    island.removeCrystals(EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.Crops"));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eFarming Booster Activated"));
                }
                if (e.getSlot() == 14) {
                    if (island.getCrystals() < EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.XP")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    }
                    if (island.getXPBoosterActive()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eThis booster is already activated"));
                        return;
                    }
                    //Xp Booster
                    island.setXPBoosterActive(true);
                    island.startxpcountdown(60 * 60);
                    island.removeCrystals(EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.XP"));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eXP Booster Activated"));
                }
                if (e.getSlot() == 16) {
                    if (island.getCrystals() < EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.Fly")) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eYou do not have enough crystals to active this booster."));
                        return;
                    }
                    if (island.getFlyBoosterActive()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eThis booster is already activated"));
                        return;
                    }
                    //Fly Booster
                    island.setFlyBoosterActive(true);
                    island.startflycountdown(60 * 60);
                    island.removeCrystals(EpicSkyBlock.getSkyblock.getConfig().getInt("BoosterCost.Fly"));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eFlight Booster Activated"));
                }
                p.openInventory(inv(island));
            }
        }
    }
}
