package net.prosavage.savageskyblock.Inventories;

import net.prosavage.savageskyblock.SavageSkyBlock;
import net.prosavage.savageskyblock.Island;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MissionsGUI implements Listener {

    public static Inventory inv(Island island) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Inventories.Missions")));
        if (island == null) return inv;
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 9, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 8, " "));
            inv.setItem(i + 18, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
        }
        if (island.getMission1() == null) {
            SavageSkyBlock.getSkyblock.addMissions(island);
        }
        ItemStack item1 = island.getMission1().getItem();
        ItemStack item2 = island.getMission2().getItem();
        ItemStack item3 = island.getMission3().getItem();
        ItemMeta im1 = item1.getItemMeta();
        ItemMeta im2 = item2.getItemMeta();
        ItemMeta im3 = item3.getItemMeta();

        ArrayList<String> lore1 = new ArrayList<>();
        lore1.add(ChatColor.RED + "" + ChatColor.BOLD + "Status:");
        if (island.getMission1Data() >= island.getMission1().getTotal()) {
            lore1.add(ChatColor.YELLOW + "Complete");
        } else {
            lore1.add(ChatColor.YELLOW + island.getMission1Data().toString() + "/" + island.getMission1().getTotal());
        }
        lore1.addAll(item1.getItemMeta().getLore());

        ArrayList<String> lore2 = new ArrayList<>();
        lore2.add(ChatColor.RED + "" + ChatColor.BOLD + "Status:");
        if (island.getMission2Data() >= island.getMission2().getTotal()) {
            lore2.add(ChatColor.YELLOW + "Complete");
        } else {
            lore2.add(ChatColor.YELLOW + island.getMission2Data().toString() + "/" + island.getMission2().getTotal());
        }
        lore2.addAll(item2.getItemMeta().getLore());

        ArrayList<String> lore3 = new ArrayList<>();
        lore3.add(ChatColor.RED + "" + ChatColor.BOLD + "Status:");
        if (island.getMission3Data() >= island.getMission3().getTotal()) {
            lore3.add(ChatColor.YELLOW + "Complete");
        } else {
            lore3.add(ChatColor.YELLOW + island.getMission3Data().toString() + "/" + island.getMission3().getTotal());
        }
        lore3.addAll(item3.getItemMeta().getLore());


        im1.setLore(lore1);
        im2.setLore(lore2);
        im3.setLore(lore3);

        item1.setItemMeta(im1);
        item2.setItemMeta(im2);
        item3.setItemMeta(im3);

        inv.setItem(11, item1);
        inv.setItem(13, item2);
        inv.setItem(15, item3);
        return inv;
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getInventory().getTitle().equals(inv(null).getTitle())) {
            e.setCancelled(true);
        }
    }
}
