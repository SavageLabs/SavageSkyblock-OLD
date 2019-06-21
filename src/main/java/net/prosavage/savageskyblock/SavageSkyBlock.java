package net.prosavage.savageskyblock;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.prosavage.savageskyblock.Inventories.BoostersGUI;
import net.prosavage.savageskyblock.Inventories.Members;
import net.prosavage.savageskyblock.Inventories.MissionsGUI;
import net.prosavage.savageskyblock.Inventories.UpgradesGUI;
import net.prosavage.savageskyblock.Inventories.WarpGUI;
import net.prosavage.savageskyblock.Missions.Builder;
import net.prosavage.savageskyblock.Missions.Collector;
import net.prosavage.savageskyblock.Missions.Competitor;
import net.prosavage.savageskyblock.Missions.Farmer;
import net.prosavage.savageskyblock.Missions.Fisherman;
import net.prosavage.savageskyblock.Missions.Hunter;
import net.prosavage.savageskyblock.Missions.Miner;
import net.prosavage.savageskyblock.nms.NMSHandler;
import net.prosavage.savageskyblock.nms.Version;

public class SavageSkyBlock extends JavaPlugin implements Listener {

   private final static int CENTER_PX = 154;
   private static SavageSkyBlock instance;

   private World world;
   private Schematic schematic;
   private NMSHandler nmsHandler;

   public static SavageSkyBlock getInstance() {
      return instance;
   }

   public void onEnable() {
      try {
         this.nmsHandler = (NMSHandler) Class.forName("net.prosavage.savageskyblock.nms.NMSHandler_" + Version.getRawVersionString()).newInstance();
      } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
         e.printStackTrace();
      }
      instance = this;
      registerEvents();
      ConfigManager.getInstance().setup(this);
      makeWorld();
      getCommand("Island").setExecutor(new Command(this));
      load();
      addMissionsToIslands();
      startCounting();
      saveInt();
      Bukkit.getScheduler().scheduleSyncRepeatingTask(this, this::calculateWorth, 0, 20 * 60);
      new Metrics(this);
      try {
         this.schematic = Schematic.loadSchematic(ConfigManager.getInstance().getSchematicFile());
      } catch (IOException e) {
         e.printStackTrace();
      }
      if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
         getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "Loading Placeholders...");
         new PlacholderAPI().register();
      }
      System.out.print("-------------------------------");
      System.out.print("");
      System.out.print(getDescription().getName() + " Enabled!");
      System.out.print("");
      System.out.print("-------------------------------");

      try {
         URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=62480");
         URLConnection con = url.openConnection();
         String version = (new BufferedReader(new InputStreamReader(con.getInputStream()))).readLine();

         if (ConfigManager.getInstance().getConfig().getBoolean("Options.AutoUpdate") && version != getDescription().getVersion()) {
            System.out.println("Trying to update");
            Updater.Update("http://ci.prosavage.net/repository/download/SavageSkyblock_Build/234:id/SavageSkyblock-" + version + ".jar", version);
         }
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public NMSHandler getNMSHandler() {
      return nmsHandler;
   }

   public void saveInt() {
      Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> save(), 0, 20 * 60);
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

   public Location getNewHome(Island island, Location loc) {
      Block b;
      b = getWorld().getHighestBlockAt(loc);
      if (isSafe(b.getLocation())) {
         return b.getLocation().add(0.5, 1, 0.5);
      }

      for (double X = island.getPos1().getX(); X <= island.getPos2().getX(); X++) {
         for (double Z = island.getPos1().getZ(); Z <= island.getPos2().getZ(); Z++) {
            b = getWorld().getHighestBlockAt((int) X, (int) Z);
            if (isSafe(b.getLocation())) {
               return b.getLocation().add(0.5, 1, 0.5);
            }
         }
      }
      return null;
   }

   public boolean isSafe(Location loc) {
      return (loc.getBlock().getType().equals(Material.AIR)
            && (!loc.clone().add(0, -1, 0).getBlock().getType().equals(Material.AIR) && !loc.clone().add(0, -1, 0).getBlock().isLiquid()));
   }

   public void sendIslandBoarder(Player p) {
      if (p.getLocation().getWorld().equals(SavageSkyBlock.getInstance().getWorld())) {
         Island island = IslandManager.getIslandViaBlock(p.getLocation().getBlock());
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
            nmsHandler.sendBorder(p, island.getCenter().getX(), island.getCenter().getZ(), radius - 1);
         }
      }
   }

   public void calculateWorth() {
      for (Island is : IslandManager.getIslands()) {
         is.calculateWorth();
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
            addMissionsToIslands();
         }
      }, 20, 20);
   }

   public void addMissionsToIslands() {
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

   private void makeWorld() {
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
         if (c == '�') {
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
      config1.set("NextLocation", IslandManager.getNextloc().getWorld().getName() + "," + IslandManager.getNextloc().getX() + "," + IslandManager.getNextloc().getY() + ","
            + IslandManager.getNextloc().getZ());
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
         saveIsland(island);
      }
   }

   public void saveIsland(Island island) {
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
