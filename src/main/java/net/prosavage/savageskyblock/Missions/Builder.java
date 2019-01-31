package net.prosavage.savageskyblock.Missions;

import net.prosavage.savageskyblock.SavageSkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class Builder {
    private Integer reward;
    private Integer current;

    public Builder() {
        this.reward = 20;
        this.current = 0;
    }

    public ItemStack getItem() {
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7Complete Island missions to gain crystals"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&7that can be spent on Boosters and Upgrades."));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&lInformation:"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Objective: &bPlace 10,000 Blocks"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Current Status: &b" + current + "/10000"));
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l * &7Reward: &b" + reward));
        lore.add("");
        lore.add(ChatColor.translateAlternateColorCodes('&', "&b&l[!] &bComplete this mission for rewards."));
        return SavageSkyBlock.getSkyblock.makeItem(Material.COBBLESTONE, 1, 0, "&b&lBuilder", lore);
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
