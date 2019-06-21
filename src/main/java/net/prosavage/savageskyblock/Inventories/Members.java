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
import org.bukkit.inventory.meta.SkullMeta;

public class Members implements Listener {

    public static Inventory inv(Island island) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getInstance().getConfig().getString("Inventories.Members")));
        if (island == null) return inv;
        for (String player : island.getPlayers()) {
            if (!(player == null || player.equalsIgnoreCase(""))) {
                if (island.getownername().equals(player)) {
                    ItemStack head = SavageSkyBlock.getInstance().makeItem(Material.SKULL_ITEM, 1, 3, "&c&l" + player);
                    SkullMeta m = (SkullMeta) head.getItemMeta();
                    m.setOwner(player);
                    head.setItemMeta(m);
                    inv.addItem(head);
                } else {
                    ItemStack head = SavageSkyBlock.getInstance().makeItem(Material.SKULL_ITEM, 1, 3, "&7" + player);
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
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getClickedInventory() != null) {
            if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getInstance().getConfig().getString("Inventories.Members")))) {
                e.setCancelled(true);
            }
        }
    }

}
