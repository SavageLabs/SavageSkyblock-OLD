package net.prosavage.savageskyblock.Missions;

import net.prosavage.savageskyblock.SavageSkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Enchanter extends Mission {

    public ItemStack getItem() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Requirements:");
        lore.add(ChatColor.YELLOW + "Enchant 100 Items");
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Rewards:");
        lore.add(ChatColor.YELLOW + "3 Island Crystals");
        return SavageSkyBlock.getSkyblock.makeItem(Material.ENCHANTED_BOOK, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Enchanter", lore);
    }

    public int getTotal() {
        return 100;
    }

    public String getName() {
        return "Enchanter";
    }
}
