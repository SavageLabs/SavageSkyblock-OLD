package com.peaches.epicskyblock;

import com.peaches.epicskyblock.Inventories.*;
import com.peaches.mobcoins.MobCoinsGiveEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.SpawnerSpawnEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class EpicSkyBlock extends JavaPlugin implements Listener {
    private final static int CENTER_PX = 154;
    public static EpicSkyBlock getSkyblock;
    private World world;

    //TODO:
    //IsWarp
    //IsUpgrades

    public EpicSkyBlock() {
    }

    public void onEnable() {
        ConfigManager.getInstance().setup(this);
        registerEvents();
        makeworld();
        getCommand("Island").setExecutor(new Command(this));
        getSkyblock = this;
        new Missions().put();
        load();
        startCounting(this);
        new Metrics(this);
        save();
        saveint();
        System.out.print("-------------------------------");
        System.out.print("");
        System.out.print(getDescription().getName() + " Enabled!");
        System.out.print("");
        System.out.print("-------------------------------");
    }

    public void saveint() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            //AutoSaves Island every 10 mins
            save();
        }, 0, 20 * 60 * getConfig().getInt("Options.SaveInt"));
    }

    @EventHandler
    public void onexplode(EntityExplodeEvent e) {
        e.setCancelled(true);
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
        pm.registerEvents(new UpgradesGUI(), this);
        pm.registerEvents(new MissionsGUI(), this);
        pm.registerEvents(new BoostersGUI(), this);
        pm.registerEvents(new Missions(), this);
        pm.registerEvents(new WarpGUI(), this);
        pm.registerEvents(new Members(), this);
    }


    public void addMissions(Island island) {
        Random r = new Random();
        island.setMission1(r.nextInt(Missions.getInstance.missions1.size()));
        island.setMission2(r.nextInt(Missions.getInstance.missions2.size()));
        island.setMission3(r.nextInt(Missions.getInstance.missions3.size()));
        island.setMission1Data(0);
        island.setMission2Data(0);
        island.setMission3Data(0);
    }

    private void startCounting(Plugin plugin) {
        final Timer timer = new Timer(true); // We use a timer cause the Bukkit scheduler is affected by server lags
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (!plugin.isEnabled()) { // Plugin was disabled
                    timer.cancel();
                    return;
                }
                // Nevertheless we want our code to run in the Bukkit main thread, so we have to use the Bukkit scheduler
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (getTime() == 0) {
                        for (Island island : IslandManager.getIslands()) {
                            addMissions(island);
                        }
                    }
                });
            }
        }, 1000, 1000); // Runs every second (1000 milliseconds)
    }

    private long getTime() { // Seconds until midnight (When MissionsGUI Change)
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        return TimeUnit.MILLISECONDS.toSeconds(c.getTimeInMillis() - System.currentTimeMillis());
    }

    @EventHandler
    public void ontalk(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (User.getbyPlayer(p) == null) {
            User.users.add(new User(p.getName()));
        }
        if (User.getbyPlayer(p).getIsland() != null) {
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
    }

    @EventHandler
    public void oncoin(MobCoinsGiveEvent e) {
        User u = User.getbyPlayer(e.getPlayer());
        if (u.getIsland() != null) {
            if (u.getIsland().getMobCoinsBoosterActive()) {
                e.setAmount(ConfigManager.getInstance().getConfig().getInt("Options.MobCoinsMultiplier"));
            }

        }
    }

    @EventHandler
    public void onCropGrow(BlockGrowEvent e) {
        Island island = IslandManager.getislandviablock(e.getBlock());
        if (island != null) {
            if (island.getFarmingBoosterActive()) {
                e.getBlock().setData((byte) (e.getBlock().getData() + 1));
            }
        }
    }

    @EventHandler
    public void onkill(EntityDeathEvent e) {
        Player p = e.getEntity().getKiller();
        if (p == null) return;
        if (User.getbyPlayer(p) == null) {
            User.users.add(new User(p.getName()));
        }
        User u = User.getbyPlayer(p);
        if (u.getIsland() != null) {
            if (u.getIsland().getXPBoosterActive()) {
                e.setDroppedExp(e.getDroppedExp() * ConfigManager.getInstance().getConfig().getInt("Options.XpMultiplier"));
            }
        }
    }

    @EventHandler
    public void onspawn(SpawnerSpawnEvent e) {
        if (IslandManager.getislandviablock(e.getSpawner().getLocation().getBlock()) == null) return;
        if (IslandManager.getislandviablock(e.getSpawner().getLocation().getBlock()).getSpawnerBoosterActive()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> e.getSpawner().setDelay(e.getSpawner().getDelay() / ConfigManager.getInstance().getConfig().getInt("Options.SpawnerMultiplier")), 0);
        }
    }

    public ItemStack makeItem(String type, int amount, String name) {
        int ty = 0;
        if (type.contains(":")) {
            String[] b = type.split(":");
            type = b[0];
            ty = Integer.parseInt(b[1]);
        }
        Material m = Material.matchMaterial(type);
        ItemStack item = new ItemStack(m, amount, (short) ty);
        ItemMeta me = item.getItemMeta();
        me.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(me);
        return item;
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
        new WorldCreator(world.getName()).generator(new SkyblockGenerator());
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
        if (User.getbyPlayer(e.getPlayer()) == null) {
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
                if (User.getbyPlayer(e.getPlayer()).getBypass()) return;
                e.setCancelled(true);
            }
            return;
        } else {
            e.setCancelled(true);
        }
    }

    public void onplace(BlockPlaceEvent e) {
        if (IslandManager.getislandviablock(e.getBlock()) != null) {
            if (!IslandManager.getislandviablock(e.getBlock()).getPlayers().contains(e.getPlayer().getName())) {
                if (User.getbyPlayer(e.getPlayer()).getBypass()) return;
                e.setCancelled(true);
            }
            return;
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void oninteract(PlayerInteractEvent e) {
        if (e.getClickedBlock() == null) return;
        if (IslandManager.getislandviablock(e.getClickedBlock()) != null) {
            if (!IslandManager.getislandviablock(e.getClickedBlock()).getPlayers().contains(e.getPlayer().getName())) {
                if (User.getbyPlayer(e.getPlayer()).getBypass()) return;
                e.setCancelled(true);
                for (String player : IslandManager.getislandviablock(e.getClickedBlock()).getPlayers()) {
                    System.out.print(player);
                }
            }
            return;
        } else {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onmove(PlayerMoveEvent e) {
        if (e.getTo().getY() <= 0) {
            //Send to spawn
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + e.getPlayer().getName());
        }
    }

    @EventHandler
    public void ondmg(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            Player dmg = (Player) e.getDamager();
            User u = User.getbyPlayer(p);
            if (u == null) {
                User.users.add(new User(p.getName()));
            }
            if (IslandManager.getislandviablock(e.getEntity().getLocation().getBlock()) != null) {
                e.setCancelled(true);
                return;
            }
            if (u.getIsland() == null) return;
            if (u.getIsland().getPlayers().contains(dmg.getName())) e.setCancelled(true);
        }

    }


    public void save() {
        File im = new File("plugins//" + getDescription().getName() + "//IslandManager.yml");
        if (im.exists()) {
            im.delete();
        }
        try {
            im.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        YamlConfiguration config1 = YamlConfiguration.loadConfiguration(im);
        config1.set("Direction", IslandManager.getDirection().name());
        config1.set("NextLocation", IslandManager.getNextloc().getWorld().getName() + "," + IslandManager.getNextloc().getX() + "," + IslandManager.getNextloc().getY() + "," + IslandManager.getNextloc().getZ());
        try {
            config1.save(im);
        } catch (IOException e) {
            e.printStackTrace();
        }
        File dir = new File("plugins//" + getDescription().getName() + "//Islands");
        if (!dir.exists()) {
            dir.mkdir();
        }
        for (Island island : IslandManager.getIslands()) {
            File file = new File("plugins//" + getDescription().getName() + "//Islands//" + island.getownername() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            if (file.exists()) file.delete();
            try {
                file.createNewFile();
                config.set("Members", island.getPlayers());
                config.set("Crystals", island.getCrystals());
                config.set("Home", island.gethome().getWorld().getName() + "," + island.gethome().getX() + "," + island.gethome().getY() + "," + island.gethome().getZ());
                config.set("Pos1", island.getPos1().getWorld().getName() + "," + island.getPos1().getX() + "," + island.getPos1().getY() + "," + island.getPos1().getZ());
                config.set("Pos2", island.getPos2().getWorld().getName() + "," + island.getPos2().getX() + "," + island.getPos2().getY() + "," + island.getPos2().getZ());
                config.set("Maxpos1", island.getMaxpos1().getWorld().getName() + "," + island.getMaxpos1().getX() + "," + island.getMaxpos1().getY() + "," + island.getMaxpos1().getZ());
                config.set("Maxpos2", island.getMaxpos2().getWorld().getName() + "," + island.getMaxpos2().getX() + "," + island.getMaxpos2().getY() + "," + island.getMaxpos2().getZ());
                config.save(file);
            } catch (Exception ignored) {

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
                island.setCrystals(config.getInt("Crystals"));
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

    @EventHandler
    public void onFromTo(BlockFromToEvent e) {
        if (e.getFace() != BlockFace.DOWN) {
            Block b = e.getToBlock();
            Location fromLoc = b.getLocation();
            Bukkit.getScheduler().runTask(this, () -> {
                if (b.getType().equals(Material.COBBLESTONE) || b.getType().equals(Material.STONE)) {
                    if (!isSurroundedByWater(fromLoc)) {
                        return;
                    }

                    Random r = new Random();
                    ArrayList<String> items = new ArrayList<>();
                    for (String item : getConfig().getStringList("OreGen")) {
                        Integer i1 = Integer.parseInt(item.split(":")[1]);
                        for (int i = 0; i <= i1; i++) {
                            items.add(item.split(":")[0]);
                        }
                    }
                    String item = items.get(r.nextInt(items.size()));
                    if (Material.getMaterial(item) == null) return;
                    e.setCancelled(true);
                    b.setType(Material.getMaterial(item));
                    b.getState().update(true);
                }
            });
        }
    }

    public boolean isSurroundedByWater(Location fromLoc) {
        Block[] blocks = {
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() + 1, fromLoc.getBlockY(), fromLoc.getBlockZ()),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() - 1, fromLoc.getBlockY(), fromLoc.getBlockZ()),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() + 1),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() - 1)};

        for (Block b : blocks) {
            if (b.getType().toString().contains("WATER")) {
                return true;
            }
        }
        return false;

    }
}
