package com.peaches.epicskyblock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class EpicSkyBlock extends JavaPlugin implements Listener {
    private final static int CENTER_PX = 154;
    public static EpicSkyBlock getSkyblock;
    public Plugin plugin;
    private World world;

    //3:26

    public EpicSkyBlock(EpicSkyBlock pl) {
    }

    public EpicSkyBlock() {
        PluginDescriptionFile pdf = getDescription();
    }

    public void onEnable() {
        ConfigManager.getInstance().setup(this);
        saveDefaultConfig();
        registerEvents();
        makeworld();
        getCommand("Island").setExecutor(new Command(this));
        getSkyblock = this;
        plugin = this;
        System.out.print("-------------------------------");
        System.out.print("");
        System.out.print(getDescription().getName() + " Enabled!");
        System.out.print("");
        System.out.print("-------------------------------");
    }

    public void onDisable() {
        System.out.print("-------------------------------");
        System.out.print("");
        System.out.print(getDescription().getName() + " Disabled!");
        System.out.print("");
        System.out.print("-------------------------------");
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, this);
    }

    private void makeworld() {
        String worldname = "EpicSkyblock";
        if (Bukkit.getWorld(worldname) == null) {
            WorldCreator wc = new WorldCreator(worldname);
            wc.generateStructures(false);
            wc.generator(new SkyblockGenerator());
            Bukkit.getServer().createWorld(wc);
        }
        this.world = Bukkit.getWorld(worldname);
    }

    public void sendCenteredMessage(Player player, String message) {
        if (message == null || message.equals("")) player.sendMessage("");
        message = ChatColor.translateAlternateColorCodes('&', message);

        int messagePxSize = 0;
        boolean previousCode = false;
        boolean isBold = false;

        for (char c : message.toCharArray()) {
            if (c == '§') {
                previousCode = true;
                continue;
            } else if (previousCode) {
                previousCode = false;
                if (c == 'l' || c == 'L') {
                    isBold = true;
                    continue;
                } else isBold = false;
            } else {
                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                messagePxSize++;
            }
        }

        int halvedMessageSize = messagePxSize / 2;
        int toCompensate = CENTER_PX - halvedMessageSize;
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder();
        while (compensated < toCompensate) {
            sb.append(" ");
            compensated += spaceLength;
        }
        player.sendMessage(sb.toString() + message);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (User.getbyPlayer(e.getPlayer()) != null) {
            User.users.add(new User(e.getPlayer()));
        }
    }

    public World getWorld() {
        return this.world;
    }

    @EventHandler
    public void onbreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        if (IslandManager.getislandviablock(e.getBlock()) != null) {
            if(!IslandManager.getislandviablock(e.getBlock()).getpermissions(player).getBREAK()){
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onplace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        if (IslandManager.getislandviablock(e.getBlock()) != null) {
            if(!IslandManager.getislandviablock(e.getBlock()).getpermissions(player).getBUILD()){
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    public Inventory Permissions() {
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', "&8Island Permissions"));
        return inv;
    }
}
