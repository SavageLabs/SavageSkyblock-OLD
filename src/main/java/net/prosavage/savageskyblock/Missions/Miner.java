package net.prosavage.savageskyblock.Missions;

import net.prosavage.savageskyblock.SavageSkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Miner extends Mission {

    public ItemStack getItem() {
        ArrayList<String> lore = new ArrayList<>();
        lore.clear();
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Requirements:");
        lore.add(ChatColor.YELLOW + "Mine 1000 Ores");
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Rewards:");
        lore.add(ChatColor.YELLOW + "1 Island Crystal");
        return SavageSkyBlock.getSkyblock.makeItem(Material.COAL_ORE, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Miner", lore);
    }

    public int getTotal() {
        return 1000;
    }

    public String getName() {
        return "Miner";
    }
}
