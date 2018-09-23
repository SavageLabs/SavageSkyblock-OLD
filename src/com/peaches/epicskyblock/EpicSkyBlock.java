package com.peaches.epicskyblock;

import com.peaches.epicskyblock.Inventories.Boosters;
import com.peaches.epicskyblock.Inventories.Missions;
import com.peaches.epicskyblock.Inventories.Upgrades;
import org.bukkit.*;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class EpicSkyBlock extends JavaPlugin implements Listener {
    private final static int CENTER_PX = 154;
    public static EpicSkyBlock getSkyblock;
    public Plugin plugin;
    private World world;

    //TODO:
    //IsTOP
    //IsWarp

    //Disable Nether

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
        load();
        System.out.print("-------------------------------");
        System.out.print("");
        System.out.print(getDescription().getName() + " Enabled!");
        System.out.print("");
        System.out.print("-------------------------------");
    }

    public void onDisable() {
        save();
        System.out.print("-------------------------------");
        System.out.print("");
        System.out.print(getDescription().getName() + " Disabled!");
        System.out.print("");
        System.out.print("-------------------------------");
    }

    private void registerEvents() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new Upgrades(), this);
        pm.registerEvents(new Missions(), this);
        pm.registerEvents(new Boosters(), this);
    }

    @EventHandler
    public void ontalk(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (User.getbyPlayer(p) == null) {
            User.users.add(new User(p.getName()));
        }
        if (User.getbyPlayer(p).getChat()) {
            e.setCancelled(true);
            for (String player : User.getbyPlayer(p).getIsland().getPlayers()) {
                Player member = Bukkit.getPlayer(player);
                if (member != null) {
                    member.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&l" + p.getName() + " &8» &e" + e.getMessage()));
                }
            }
        }
    }

    @EventHandler
    public void onspawn(SpawnerSpawnEvent e) {
        if (IslandManager.getislandviablock(e.getSpawner().getLocation().getBlock()).getSpawnerBoosterActive()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> e.getSpawner().setDelay(e.getSpawner().getDelay() / 4), 0);
        }
    }

    public ItemStack makeItem(Material material, int amount, int type, String name) {
        ItemStack item = new ItemStack(material, amount, (short) type);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(m);
        return item;
    }

    public ItemStack makeItem(Material material, int amount, int type, String name, ArrayList<String> lore) {
        ItemStack item = new ItemStack(material, amount, (short) type);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        m.setLore(lore);
        item.setItemMeta(m);
        return item;
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
            User.users.add(new User(e.getPlayer().getName()));
        }
    }

    public World getWorld() {
        return this.world;
    }

    @EventHandler
    public void onbreak(BlockBreakEvent e) {
        if (IslandManager.getislandviablock(e.getBlock()) != null) {
            if (!IslandManager.getislandviablock(e.getBlock()).getPlayers().contains(e.getPlayer().getName())) {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onplace(BlockPlaceEvent e) {
        if (IslandManager.getislandviablock(e.getBlock()) != null) {
            if (!IslandManager.getislandviablock(e.getBlock()).getPlayers().contains(e.getPlayer().getName())) {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void oninteract(PlayerInteractEvent e) {
        if (IslandManager.getislandviablock(e.getClickedBlock()) != null) {
            if (!IslandManager.getislandviablock(e.getClickedBlock()).getPlayers().contains(e.getPlayer().getName())) {
                e.setCancelled(true);
            }
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void ondmg(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player dmg = (Player) e.getDamager();
        }

    }


    private void save() {
        File im = new File("plugins//" + getDescription().getName() + "//IslandManager.yml");
        if (im.exists()) {
            im.delete();
        }
        try {
            im.createNewFile();
            YamlConfiguration config1 = YamlConfiguration.loadConfiguration(im);
            config1.set("Direction", IslandManager.getDirection().name());
            config1.set("NextLocation", IslandManager.getNextloc().getWorld().getName() + "," + IslandManager.getNextloc().getX() + "," + IslandManager.getNextloc().getY() + "," + IslandManager.getNextloc().getZ());
            config1.save(im);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File dir = new File("plugins//" + getDescription().getName() + "//Islands");
        if (!dir.exists()) {
            dir.mkdir();
        }
        for (Island island : IslandManager.getIslands()) {
            File file = new File("plugins//" + getDescription().getName() + "//Islands//" + island.getowner().getName() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (file.exists()) file.delete();
            try {
                file.createNewFile();
                config.set("Members", island.getPlayers());
                config.set("Home", island.gethome().getWorld().getName() + "," + island.gethome().getX() + "," + island.gethome().getY() + "," + island.gethome().getZ());
                config.set("Pos1", island.getPos1().getWorld().getName() + "," + island.getPos1().getX() + "," + island.getPos1().getY() + "," + island.getPos1().getZ());
                config.set("Pos2", island.getPos2().getWorld().getName() + "," + island.getPos2().getX() + "," + island.getPos2().getY() + "," + island.getPos2().getZ());
                config.set("Maxpos1", island.getMaxpos1().getWorld().getName() + "," + island.getMaxpos1().getX() + "," + island.getMaxpos1().getY() + "," + island.getMaxpos1().getZ());
                config.set("Maxpos2", island.getMaxpos2().getWorld().getName() + "," + island.getMaxpos2().getX() + "," + island.getMaxpos2().getY() + "," + island.getMaxpos2().getZ());
                config.save(file);
            } catch (Exception e) {

            }
        }
    }

    private void load() {

        File im = new File("plugins//" + getDescription().getName() + "//IslandManager.yml");
        if (im.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(im);
            if (config.contains("NextLocation")) {
                String[] Home = config.getString("NextLocation").split(",");
                Location home = new Location(Bukkit.getWorld(Home[0]), Double.parseDouble(Home[1]), Double.parseDouble(Home[2]), Double.parseDouble(Home[3]));
                IslandManager.setNextloc(home);
            }
            if (config.contains("Direction")) {
                IslandManager.setDirection(Direction.valueOf(config.getString("Direction")));
            }
            im.delete();
        }
        File DIR = new File("plugins//" + getDescription().getName() + "//Islands");
        if (DIR.exists()) {
            File[] files = DIR.listFiles();
            for (File file : files) {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String owner = file.getName().replace(".yml", "");
                String[] Home = config.getString("Home").split(",");
                String[] Pos1 = config.getString("Pos1").split(",");
                String[] Pos2 = config.getString("Pos2").split(",");
                String[] Maxpos1 = config.getString("Maxpos1").split(",");
                String[] Maxpos2 = config.getString("Maxpos2").split(",");
                Location home = new Location(Bukkit.getWorld(Home[0]), Double.parseDouble(Home[1]), Double.parseDouble(Home[2]), Double.parseDouble(Home[3]));
                Location pos1 = new Location(Bukkit.getWorld(Pos1[0]), Double.parseDouble(Pos1[1]), Double.parseDouble(Pos1[2]), Double.parseDouble(Pos1[3]));
                Location pos2 = new Location(Bukkit.getWorld(Pos2[0]), Double.parseDouble(Pos2[1]), Double.parseDouble(Pos2[2]), Double.parseDouble(Pos2[3]));
                Location maxpos1 = new Location(Bukkit.getWorld(Maxpos1[0]), Double.parseDouble(Maxpos1[1]), Double.parseDouble(Maxpos1[2]), Double.parseDouble(Maxpos1[3]));
                Location maxpos2 = new Location(Bukkit.getWorld(Maxpos2[0]), Double.parseDouble(Maxpos2[1]), Double.parseDouble(Maxpos2[2]), Double.parseDouble(Maxpos2[3]));
                Island island = new Island(owner, home, pos1, pos2, maxpos1, maxpos2, false);
                for (String member : config.getStringList("Members")) {
                    if (!member.equals(owner)) {
                        island.addUser(member);
                    }
                }
                IslandManager.addIsland(island);
                file.delete();
            }
        }
    }
}
