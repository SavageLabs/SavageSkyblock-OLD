package net.prosavage.savageskyblock.Inventories;

import net.prosavage.savageskyblock.SavageSkyBlock;
import net.prosavage.savageskyblock.Island;
import net.prosavage.savageskyblock.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class WarpGUI implements Listener {

    public static Inventory inv(Island island) {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Inventories.Warp")));
        if (island == null) return inv;
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 9, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(i + 18, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 15, " "));
            inv.setItem(9, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 1, "&b&lWarp 1"));
            inv.setItem(11, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 1, "&b&lWarp 2"));
            inv.setItem(13, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 1, "&b&lWarp 3"));
            inv.setItem(15, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 1, "&b&lWarp 4"));
            inv.setItem(17, SavageSkyBlock.getSkyblock.makeItem(Material.STAINED_GLASS_PANE, 1, 1, "&b&lWarp 5"));
        }
        return inv;
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Island island = User.getbyPlayer(p).getIsland();
        if (island == null) return;
        if (e.getView().getTitle().equals(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Inventories.Warp")))) {
            if (e.getSlot() == 9) {
                if (island.getWarp1() != null) {
                    p.teleport(island.getWarp1());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eThis warp does not exist, do /is setwarp to set one"));
                }
            }
            if (e.getSlot() == 11) {
                if (island.getWarp2() != null) {
                    p.teleport(island.getWarp2());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eThis warp does not exist, do /is setwarp to set one"));
                }
            }
            if (e.getSlot() == 13) {
                if (island.getWarp3() != null) {
                    p.teleport(island.getWarp3());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eThis warp does not exist, do /is setwarp to set one"));
                }
            }
            if (e.getSlot() == 15) {
                if (island.getWarp4() != null) {
                    p.teleport(island.getWarp4());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eThis warp does not exist, do /is setwarp to set one"));
                }
            }
            if (e.getSlot() == 17) {
                if (island.getWarp5() != null) {
                    p.teleport(island.getWarp5());
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &eThis warp does not exist, do /is setwarp to set one"));
                }
            }
            e.setCancelled(true);
        }
    }
}
