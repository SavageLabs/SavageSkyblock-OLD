package net.prosavage.savageskyblock.Missions;

import net.prosavage.savageskyblock.SavageSkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Soldier extends Mission {

    public ItemStack getItem() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Requirements:");
        lore.add(ChatColor.YELLOW + "Get 50 Kills");
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Rewards:");
        lore.add(ChatColor.YELLOW + "1 Island Crystal");
        return SavageSkyBlock.getSkyblock.makeItem(Material.DIAMOND_SWORD, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Soldier", lore);
    }

    public int getTotal() {
        return 50;
    }

    public String getName() {
        return "Soldier";
    }
}