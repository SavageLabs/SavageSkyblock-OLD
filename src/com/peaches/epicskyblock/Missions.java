package com.peaches.epicskyblock;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;

public class Missions implements Listener {

    public Missions() {
        getInstance = this;
    }

    public static Missions getInstance;
    public ArrayList<ItemStack> missions1 = new ArrayList<>();
    public ArrayList<ItemStack> missions2 = new ArrayList<>();
    public ArrayList<ItemStack> missions3 = new ArrayList<>();
    public HashMap<ItemStack, Integer> missions = new HashMap<>();

    public void put() {

        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Requirements:");
        lore.add(ChatColor.YELLOW + "Mine 1000 Ores");
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Rewards:");
        lore.add(ChatColor.YELLOW + "1 Island Crystal");
        missions1.add(EpicSkyBlock.getSkyblock.makeItem(Material.COAL_ORE, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Miner", lore));
        missions.put(EpicSkyBlock.getSkyblock.makeItem(Material.COAL_ORE, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Miner", lore), 1000);

        lore.clear();
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Requirements:");
        lore.add(ChatColor.YELLOW + "Get 50 Kills");
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Rewards:");
        lore.add(ChatColor.YELLOW + "2 Island Crystal");
        missions2.add(EpicSkyBlock.getSkyblock.makeItem(Material.DIAMOND_SWORD, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Soldier", lore));
        missions.put(EpicSkyBlock.getSkyblock.makeItem(Material.DIAMOND_SWORD, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Soldier", lore), 50);

        lore.clear();
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Requirements:");
        lore.add(ChatColor.YELLOW + "Gather 10,000 XP");
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Rewards:");
        lore.add(ChatColor.YELLOW + "5 Island Crystals");
        missions3.add(EpicSkyBlock.getSkyblock.makeItem(Material.EXP_BOTTLE, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Treasure Hunter", lore));
        missions.put(EpicSkyBlock.getSkyblock.makeItem(Material.EXP_BOTTLE, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Treasure Hunter", lore), 10000);
    }

}
