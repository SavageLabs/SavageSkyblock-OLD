package net.prosavage.savageskyblock.Missions;

import net.prosavage.savageskyblock.SavageSkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Mob_Hunter extends Mission {

    public ItemStack getItem() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Requirements:");
        lore.add(ChatColor.YELLOW + "Kill 1000 Mobs");
        lore.add("");
        lore.add(ChatColor.RED + "" + ChatColor.BOLD + "Rewards:");
        lore.add(ChatColor.YELLOW + "2 Island Crystals");
        return SavageSkyBlock.getSkyblock.makeItem(Material.DIAMOND_SWORD, 1, 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Mob Hunter", lore);
    }

    public int getTotal() {
        return 1000;
    }

    public String getName() {
        return "Mob Hunter";
    }
}
