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
            inv.setItem(i + 9, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 18, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
        }
        inv.setItem(10, island.getFarmer().getItem());
        inv.setItem(11, island.getHunter().getItem());
        inv.setItem(12, island.getCompetitor().getItem());
        inv.setItem(13, island.getCollector().getItem());
        inv.setItem(14, island.getFisherman().getItem());
        inv.setItem(15, island.getBuilder().getItem());
        inv.setItem(16, island.getMiner().getItem());
        return inv;
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Inventories.Missions")))) {
            e.setCancelled(true);
        }
    }
}
