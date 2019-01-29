package net.prosavage.savageskyblock.Missions;

import net.prosavage.savageskyblock.SavageSkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Treasure_Hunter extends Mission {

    public ItemStack getItem() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Requirements:");
        lore.add(ChatColor.YELLOW + "Gather 10,000 XP");
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Rewards:");
        lore.add(ChatColor.YELLOW + "5 Island Crystals");
        return SavageSkyBlock.getSkyblock.makeItem(Material.EXP_BOTTLE, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Treasure Hunter", lore);
    }

    public int getTotal() {
        return 10000;
    }

    public String getName() {
        return "Treasure Hunter";
    }
}
