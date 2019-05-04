package net.prosavage.savageskyblock;

import net.prosavage.savageskyblock.Inventories.*;
import net.prosavage.savageskyblock.Missions.*;
import net.prosavage.savageskyblock.NMS.*;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class SavageSkyBlock extends JavaPlugin implements Listener {

    private final static int CENTER_PX = 154;
    public static SavageSkyBlock getSkyblock;
    private World world;

    private Schematic schematic;

    public SavageSkyBlock() {
    }

    public void onEnable() {
        registerEvents();
        getSkyblock = this;
        ConfigManager.getInstance().setup(this);
        makeworld();
        getCommand("Island").setExecutor(new Command(this));
        load();
        addmissionstoIslands();
        startCounting();
        saveint();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> calculateworth(), 0, 20 * 60);
        new Metrics(this);
        try {
            this.schematic = Schematic.loadSchematic(ConfigManager.getInstance().getSchematicFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.print("-------------------------------");
        System.out.print("");
        System.out.print(getDescription().getName() + " Enabled!");
        System.out.print("");
        System.out.print("-------------------------------");
    }

    public void saveint() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> save(), 0, 20 * 60);
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
        pm.registerEvents(new WarpGUI(), this);
        pm.registerEvents(new Members(), this);
        pm.registerEvents(new Events(), this);
    }

    public Location getnewhome(Island island, Location loc) {
        Block b;
        b = getWorld().getHighestBlockAt(loc);
        if (issafe(b.getLocation())) {
            return b.getLocation().add(0.5, 1, 0.5);
        }

        for (double X = island.getPos1().getX(); X <= island.getPos2().getX(); X++) {
            for (double Z = island.getPos1().getZ(); Z <= island.getPos2().getZ(); Z++) {
                b = getWorld().getHighestBlockAt((int) X, (int) Z);
                if (issafe(b.getLocation())) {
                    return b.getLocation().add(0.5, 1, 0.5);
                }
            }
        }
        return null;
    }

    public boolean issafe(Location loc) {
        return (loc.getBlock().getType().equals(Material.AIR) && (!loc.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR) && !loc.clone().add(0, -1, 0).getBlock().isLiquid()));
    }

    public void sendIslandBoarder(Player p) {
        if (p.getLocation().getWorld().equals(SavageSkyBlock.getSkyblock.getWorld())) {
            Island island = IslandManager.getislandviablock(p.getLocation().getBlock());
            if (island != null) {
                int radius = 0;
                if (island.getSize() == 1) {
                    radius = IslandManager.level1radius;
                }
                if (island.getSize() == 2) {
                    radius = IslandManager.level2radius;
                }
                if (island.getSize() == 3) {
                    radius = IslandManager.level3radius;
                }
                SavageSkyBlock.getSkyblock.sendBorder(p, island.getCenter().getX(), island.getCenter().getZ(), radius - 1, ColorType.BLUE);
            }
        }
    }

    public void sendBorder(Player p, double x, double z, double radius, ColorType colorType) {
        if (Version.getVersion().equals(Version.v1_8_R2)) NMS_v1_8_R2.sendBorder(p, x, z, radius, colorType);
        if (Version.getVersion().equals(Version.v1_8_R3)) NMS_v1_8_R3.sendBorder(p, x, z, radius, colorType);
        if (Version.getVersion().equals(Version.v1_9_R1)) NMS_v1_9_R1.sendBorder(p, x, z, radius, colorType);
        if (Version.getVersion().equals(Version.v1_9_R2)) NMS_v1_9_R2.sendBorder(p, x, z, radius, colorType);
        if (Version.getVersion().equals(Version.v1_10_R1)) NMS_v1_10_R1.sendBorder(p, x, z, radius, colorType);
        if (Version.getVersion().equals(Version.v1_11_R1)) NMS_v1_11_R1.sendBorder(p, x, z, radius, colorType);
        if (Version.getVersion().equals(Version.v1_12_R1)) NMS_v1_12_R1.sendBorder(p, x, z, radius, colorType);
        if (Version.getVersion().equals(Version.v1_13_R1)) NMS_v1_13_R1.sendBorder(p, x, z, radius, colorType);
        if (Version.getVersion().equals(Version.v1_13_R2)) NMS_v1_13_R2.sendBorder(p, x, z, radius, colorType);
    }

    public void sendTitle(Player p, String text, int in, int stay, int out) {
        if (Version.getVersion().equals(Version.v1_8_R2)) NMS_v1_8_R2.sendTitle(p, text, in, stay, out, "TITLE");
        if (Version.getVersion().equals(Version.v1_8_R3)) NMS_v1_8_R3.sendTitle(p, text, in, stay, out, "TITLE");
        if (Version.getVersion().equals(Version.v1_9_R1)) NMS_v1_9_R1.sendTitle(p, text, in, stay, out, "TITLE");
        if (Version.getVersion().equals(Version.v1_9_R2)) NMS_v1_9_R2.sendTitle(p, text, in, stay, out, "TITLE");
        if (Version.getVersion().equals(Version.v1_10_R1)) NMS_v1_10_R1.sendTitle(p, text, in, stay, out, "TITLE");
        if (Version.getVersion().equals(Version.v1_11_R1)) NMS_v1_11_R1.sendTitle(p, text, in, stay, out, "TITLE");
        if (Version.getVersion().equals(Version.v1_12_R1)) NMS_v1_12_R1.sendTitle(p, text, in, stay, out, "TITLE");
        if (Version.getVersion().equals(Version.v1_13_R1)) NMS_v1_13_R1.sendTitle(p, text, in, stay, out, "TITLE");
        if (Version.getVersion().equals(Version.v1_13_R2)) NMS_v1_13_R2.sendTitle(p, text, in, stay, out, "TITLE");
    }

    public void sendsubTitle(Player p, String text, int in, int stay, int out) {
        if (Version.getVersion().equals(Version.v1_8_R2)) NMS_v1_8_R2.sendTitle(p, text, in, stay, out, "SUBTITLE");
        if (Version.getVersion().equals(Version.v1_8_R3)) NMS_v1_8_R3.sendTitle(p, text, in, stay, out, "SUBTITLE");
        if (Version.getVersion().equals(Version.v1_9_R1)) NMS_v1_9_R1.sendTitle(p, text, in, stay, out, "SUBTITLE");
        if (Version.getVersion().equals(Version.v1_9_R2)) NMS_v1_9_R2.sendTitle(p, text, in, stay, out, "SUBTITLE");
        if (Version.getVersion().equals(Version.v1_10_R1)) NMS_v1_10_R1.sendTitle(p, text, in, stay, out, "SUBTITLE");
        if (Version.getVersion().equals(Version.v1_11_R1)) NMS_v1_11_R1.sendTitle(p, text, in, stay, out, "SUBTITLE");
        if (Version.getVersion().equals(Version.v1_12_R1)) NMS_v1_12_R1.sendTitle(p, text, in, stay, out, "SUBTITLE");
        if (Version.getVersion().equals(Version.v1_13_R1)) NMS_v1_13_R1.sendTitle(p, text, in, stay, out, "SUBTITLE");
        if (Version.getVersion().equals(Version.v1_13_R2)) NMS_v1_13_R2.sendTitle(p, text, in, stay, out, "SUBTITLE");
    }

    public void calculateworth() {
        for (Island is : IslandManager.getIslands()) {
            is.calculateworth();
        }
    }

    public boolean isSurroundedByWater(Location fromLoc) {
        // Sets gives better performance than arrays when used wisely
        Set<Block> blocksSet = new HashSet<>(Arrays.asList(fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() + 1, fromLoc.getBlockY(), fromLoc.getBlockZ()),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX() - 1, fromLoc.getBlockY(), fromLoc.getBlockZ()),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() + 1),
                fromLoc.getWorld().getBlockAt(fromLoc.getBlockX(), fromLoc.getBlockY(), fromLoc.getBlockZ() - 1)));

        for (Block b : blocksSet) {
            if (b.getType().toString().contains("WATER")) return true;
            continue;
        }
        return false;

    }

    public void addMissions(Island island) {
        island.setBuilder(new Builder());
        island.setCollector(new Collector());
        island.setCompetitor(new Competitor());
        island.setFarmer(new Farmer());
        island.setFisherman(new Fisherman());
        island.setHunter(new Hunter());
        island.setMiner(new Miner());
    }

    private void startCounting() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, () -> {
            if (getTime() == 0) {
                addmissionstoIslands();
            }
        }, 20, 20);
    }

    public void addmissionstoIslands() {
        for (Island island : IslandManager.getIslands()) {
            addMissions(island);
        }
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
        String worldname = "SavageSkyblock";
        if (Bukkit.getWorld(worldname) == null) {
            WorldCreator wc = new WorldCreator(worldname);
            wc.generateStructures(false);
            wc.generator(new SkyblockGenerator());
            Bukkit.getServer().createWorld(wc);
        }
        this.world = Bukkit.getWorld(worldname);
        new WorldCreator(world.getName()).generator(new SkyblockGenerator());
    }

    public void sendCenteredMessage(CommandSender player, String message) {
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

    public World getWorld() {
        return this.world;
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
        config1.set("NextID", IslandManager.getNextid());
        if (IslandManager.getDirection() != null) {
            config1.set("Direction", IslandManager.getDirection().name());
        }
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
            saveisland(island);
        }
    }

    public void saveisland(Island island) {
        File file = new File("plugins//" + getDescription().getName() + "//Islands//" + island.getId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) file.delete();
        try {
            file.createNewFile();
            config.set("Owner", island.getownername());
            config.set("Members", island.getPlayers());
            config.set("Crystals", island.getCrystals());
            config.set("Home", island.gethome().getWorld().getName() + "," + island.gethome().getX() + "," + island.gethome().getY() + "," + island.gethome().getZ());
            config.set("Pos1", island.getPos1().getWorld().getName() + "," + island.getPos1().getX() + "," + island.getPos1().getY() + "," + island.getPos1().getZ());
            config.set("Pos2", island.getPos2().getWorld().getName() + "," + island.getPos2().getX() + "," + island.getPos2().getY() + "," + island.getPos2().getZ());
            config.set("Maxpos1", island.getMaxpos1().getWorld().getName() + "," + island.getMaxpos1().getX() + "," + island.getMaxpos1().getY() + "," + island.getMaxpos1().getZ());
            config.set("Maxpos2", island.getMaxpos2().getWorld().getName() + "," + island.getMaxpos2().getX() + "," + island.getMaxpos2().getY() + "," + island.getMaxpos2().getZ());
            config.set("Center", island.getCenter().getWorld().getName() + "," + island.getCenter().getX() + "," + island.getCenter().getY() + "," + island.getCenter().getZ());
            config.set("SizeLevel", island.getSize());
            config.set("MemberLevel", island.getMemberCount());
            config.set("WarpsLevel", island.getWarpCount());
            config.save(file);
        } catch (Exception ignored) {
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
            if (config.contains("NextID")) {
                IslandManager.setNextid(Integer.parseInt(config.getString("NextID")));
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
                String owner = config.getString("Owner");
                String[] Home = config.getString("Home").split(",");
                String[] Pos1 = config.getString("Pos1").split(",");
                String[] Pos2 = config.getString("Pos2").split(",");
                String[] Maxpos1 = config.getString("Maxpos1").split(",");
                String[] Maxpos2 = config.getString("Maxpos2").split(",");
                String[] Center = config.getString("Center").split(",");
                Location home = new Location(Bukkit.getWorld(Home[0]), Double.parseDouble(Home[1]), Double.parseDouble(Home[2]), Double.parseDouble(Home[3]));
                Location pos1 = new Location(Bukkit.getWorld(Pos1[0]), Double.parseDouble(Pos1[1]), Double.parseDouble(Pos1[2]), Double.parseDouble(Pos1[3]));
                Location pos2 = new Location(Bukkit.getWorld(Pos2[0]), Double.parseDouble(Pos2[1]), Double.parseDouble(Pos2[2]), Double.parseDouble(Pos2[3]));
                Location maxpos1 = new Location(Bukkit.getWorld(Maxpos1[0]), Double.parseDouble(Maxpos1[1]), Double.parseDouble(Maxpos1[2]), Double.parseDouble(Maxpos1[3]));
                Location maxpos2 = new Location(Bukkit.getWorld(Maxpos2[0]), Double.parseDouble(Maxpos2[1]), Double.parseDouble(Maxpos2[2]), Double.parseDouble(Maxpos2[3]));
                Location center = new Location(Bukkit.getWorld(Center[0]), Double.parseDouble(Center[1]), Double.parseDouble(Center[2]), Double.parseDouble(Center[3]));
                Island island = new Island(owner, home, pos1, pos2, maxpos1, maxpos2, center, false);
                island.setCrystals(config.getInt("Crystals"));
                for (String member : config.getStringList("Members")) {
                    if (!member.equals(owner)) {
                        island.addUser(member);
                    }
                }
                island.setSize(config.getInt("SizeLevel"));
                island.setMemberCount(config.getInt("MemberLevel"));
                island.setWarpCount(config.getInt("WarpsLevel"));
                IslandManager.addIsland(island);
                file.delete();
            }
        }
    }

    public Schematic getSchematic() {
        return schematic;
    }
}
