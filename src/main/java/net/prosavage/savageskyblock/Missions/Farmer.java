package net.prosavage.savageskyblock.Missions;

import net.prosavage.savageskyblock.SavageSkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Farmer {
    private Integer reward;
    private Integer current;

    public Farmer() {

        this.reward = 10;
        this.current = 0;
    }

    public ItemStack getItem() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&bComplete island missions to gain crystals that can be spent on Boosters and Upgrades."));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lInformation:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Objective: &bHarvest 5,000 Sugar Cane"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Current Status: &b" + current + "/5000"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l* &7Reward: &b" + reward));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l[!] &bComplete this mission for rewards."));
        return SavageSkyBlock.getSkyblock.makeItem(Material.SUGAR_CANE, 1, 0, "&b&lFarmer", lore);
    }

    public Integer getReward() {
        return reward;
    }

    public void setReward(Integer reward) {
        this.reward = reward;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }
}
