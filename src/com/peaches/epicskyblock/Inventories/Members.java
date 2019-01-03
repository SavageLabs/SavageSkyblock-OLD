package com.peaches.epicskyblock.Inventories;

import com.peaches.epicskyblock.EpicSkyBlock;
import com.peaches.epicskyblock.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

public class Members implements Listener {

    public static Inventory inv(Island island) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Inventories.Members")));
        if (island == null) return inv;
        for (String player : island.getPlayers()) {
            if (!(player == null || player.equalsIgnoreCase(""))) {
                if (island.getownername().equals(player)) {
                    ItemStack head = EpicSkyBlock.getSkyblock.makeItem("397:3", 1, "&c&l" + player);
                    SkullMeta m = (SkullMeta) head.getItemMeta();
                    m.setOwner(player);
                    head.setItemMeta(m);
                    inv.addItem(head);
                } else {
                    ItemStack head = EpicSkyBlock.getSkyblock.makeItem("397:3", 1, "&7" + player);
                    SkullMeta m = (SkullMeta) head.getItemMeta();
                    m.setOwner(player);
                    head.setItemMeta(m);
                    inv.addItem(head);
                }
            }
        }

        return inv;
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory().getTitle().equals(inv(null).getTitle())) {
            e.setCancelled(true);
        }
    }

}
