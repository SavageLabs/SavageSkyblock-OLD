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
        ArrayList<String> spawnerlore = new ArrayList<>();
        if (island.getSpawner() != 0) {
            spawnerlore.add(ChatColor.translateAlternateColorCodes('&', "&6&lTime: &e&l" + island.getSpawner() / 60 + "m " + island.getSpawner() % 60 + "s"));
        } else {
            spawnerlore.add(ChatColor.translateAlternateColorCodes('&', "&6&lClick to activate"));
        }
        ArrayList<String> farminglore = new ArrayList<>();
        if (island.getFarming() != 0) {
            farminglore.add(ChatColor.translateAlternateColorCodes('&', "&6&lTime: &e&l" + island.getFarming() / 60 + "m " + island.getFarming() % 60 + "s"));
        } else {
            farminglore.add(ChatColor.translateAlternateColorCodes('&', "&6&lClick to activate"));
        }
        ArrayList<String> xplore = new ArrayList<>();
        if (island.getXp() != 0) {
            xplore.add(ChatColor.translateAlternateColorCodes('&', "&6&lTime: &e&l" + island.getXp() / 60 + "m " + island.getXp() % 60 + "s"));
        } else {
            xplore.add(ChatColor.translateAlternateColorCodes('&', "&6&lClick to activate"));
        }
        ArrayList<String> flylore = new ArrayList<>();
        if (island.getFly() != 0) {
            flylore.add(ChatColor.translateAlternateColorCodes('&', "&6&lTime: &e&l" + island.getFly() / 60 + "m " + island.getFly() % 60 + "s"));
        } else {
            flylore.add(ChatColor.translateAlternateColorCodes('&', "&6&lClick to activate"));
        }
        inv.setItem(10, EpicSkyBlock.getSkyblock.makeItem(Material.MOB_SPAWNER, 1, 0, "&e&lSpawner Booster", spawnerlore));
        inv.setItem(12, EpicSkyBlock.getSkyblock.makeItem(Material.WHEAT, 1, 0, "&e&lFarming Booster", farminglore));
        inv.setItem(14, EpicSkyBlock.getSkyblock.makeItem(Material.EXP_BOTTLE, 1, 0, "&e&lXp Booster", xplore));
        inv.setItem(16, EpicSkyBlock.getSkyblock.makeItem(Material.FEATHER, 1, 0, "&e&lFly Booster", flylore));
        return inv;
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Island island = User.getbyPlayer(p).getIsland();
        if (e.getInventory().getTitle().equals(inv(island).getTitle())) {
            e.setCancelled(true);
            if (e.getSlot() == 10) {
                if (island.getCrystals() < 3) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have enough crystals to active this booster."));
                    return;
                }
                if (island.getSpawnerBoosterActive()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis booster is already activated"));
                    return;
                }
                //Spawner Booster
                island.setSpawnerBoosterActive(true);
                island.startspawnercountdown(60 * 60);
                island.removeCrystals(3);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eSpawner Booster Activated"));
                p.closeInventory();
            }
            if (e.getSlot() == 12) {
                if (island.getCrystals() < 3) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have enough crystals to active this booster."));
                    return;
                }
                if (island.getFarmingBoosterActive()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis booster is already activated"));
                    return;
                }
                //Farming Booster
                island.setFarmingBoosterActive(true);
                island.startfarmingcountdown(60 * 60);
                island.removeCrystals(3);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eFarming Booster Activated"));
                p.closeInventory();
            }
            if (e.getSlot() == 14) {
                if (island.getCrystals() < 3) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have enough crystals to active this booster."));
                    return;
                }
                if (island.getXPBoosterActive()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis booster is already activated"));
                    return;
                }
                //Xp Booster
                island.setXPBoosterActive(true);
                island.startxpcountdown(60 * 60);
                island.removeCrystals(3);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eXP Booster Activated"));
                p.closeInventory();
            }
            if (e.getSlot() == 16) {
                if (island.getCrystals() < 3) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have enough crystals to active this booster."));
                    return;
                }
                if (island.getFlyBoosterActive()) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis booster is already activated"));
                    return;
                }
                //Fly Booster
                island.setFlyBoosterActive(true);
                island.startflycountdown(60 * 60);
                island.removeCrystals(3);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eFlight Booster Activated"));
                p.closeInventory();
            }
        }
    }
}
