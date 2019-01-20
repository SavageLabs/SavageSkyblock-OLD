package com.peaches.epicskyblock.Missions;

import com.peaches.epicskyblock.EpicSkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Fisherman extends Mission {

    public ItemStack getItem() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Requirements:");
        lore.add(ChatColor.YELLOW + "Catch 500 Fish");
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Rewards:");
        lore.add(ChatColor.YELLOW + "5 Island Crystals");
        return EpicSkyBlock.getSkyblock.makeItem(Material.FISHING_ROD, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Fisherman", lore);
    }

    public int getTotal() {
        return 500;
    }

    public String getName() {
        return "Fisherman";
    }
}
