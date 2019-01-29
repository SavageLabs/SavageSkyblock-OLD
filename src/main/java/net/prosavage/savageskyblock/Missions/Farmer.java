package net.prosavage.savageskyblock.Missions;

import net.prosavage.savageskyblock.SavageSkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Farmer extends Mission {

    public ItemStack getItem() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Requirements:");
        lore.add(ChatColor.YELLOW + "Harvest 5000 Sugar Cane");
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Rewards:");
        lore.add(ChatColor.YELLOW + "1 Island Crystal");
        return SavageSkyBlock.getSkyblock.makeItem(Material.SUGAR_CANE, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Farmer", lore);
    }

    public int getTotal() {
        return 5000;
    }

    public String getName() {
        return "Farmer";
    }
}
