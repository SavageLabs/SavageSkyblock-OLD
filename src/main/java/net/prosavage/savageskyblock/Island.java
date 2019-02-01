package net.prosavage.savageskyblock;

import net.prosavage.savageskyblock.Missions.*;
import net.prosavage.savageskyblock.NMS.*;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Island {
    private final Location maxpos1; // Bottom left corner
    private final Location maxpos2; // Bottom right corner
    public Location pos1; // Bottom left corner
    public Location pos2; // Bottom right corner
    private Location center;
    private String owner;
    private Location home;
    private Boolean SpawnerBoosterActive;
    private Boolean FarmingBoosterActive;
    private Boolean XPBoosterActive;
    private Boolean FlyBoosterActive;
    private Boolean MobCoinsBoosterActive;
    private Integer spawner = 0;
    private Integer Farming = 0;
    private Integer Xp = 0;
    private Integer Fly = 0;
    private Integer MobCoins = 0;
    private Integer SpawnerCode;
    private Integer FarmingCode;
    private Integer XPCode;
    private Integer FlyCode;
    private Integer MobcoinsCode;
    private Integer Size = 1;
    private Integer MemberCount = 1;
    private Integer WarpCount = 1;
    private Integer crystals;

    private Integer id;
    // What about a Set of Locations? So Owner can easily config how many warps
    private Location warp1;
    private Location warp2;
    private Location warp3;
    private Location warp4;
    private Location warp5;

    private Builder builder;
    private Collector collector;
    private Competitor competitor;
    private Farmer farmer;
    private Fisherman fisherman;
    private Hunter hunter;
    private Miner miner;

    private Integer level = 0;
    private ArrayList<String> players = new ArrayList<>();

    private ArrayList<Chunk> chunks = new ArrayList<>();

    public Island(String owner, Location home, Location pos1, Location pos2, Location mpos1, Location mpos2, Location center, Boolean schem) {
        this.owner = owner;
        this.home = home;
        this.pos1 = pos1; //-50, -50
        this.pos2 = pos2; //+50, +50
        this.maxpos1 = mpos1;
        this.maxpos2 = mpos2;
        this.center = center;
        this.crystals = 0;

        this.id = IslandManager.getNextid();
        IslandManager.setNextid(id + 1);

        this.SpawnerBoosterActive = false;
        this.FarmingBoosterActive = false;
        this.FarmingBoosterActive = false;
        this.XPBoosterActive = false;
        this.FlyBoosterActive = false;
        this.MobCoinsBoosterActive = false;

        //Optimise this
        for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
            for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                if (!chunks.contains(SavageSkyBlock.getSkyblock.getWorld().getChunkAt(new Location(SavageSkyBlock.getSkyblock.getWorld(), X, 10, Z)))) {
                    chunks.add(SavageSkyBlock.getSkyblock.getWorld().getChunkAt(new Location(SavageSkyBlock.getSkyblock.getWorld(), X, 10, Z)));
                }
            }
        }

        addUser(owner);
        //Loads island.schematic
        if (schem) {
            loadSchematic();
        }
        SavageSkyBlock.getSkyblock.addMissions(this);
        Bukkit.getScheduler().runTaskAsynchronously(SavageSkyBlock.getSkyblock, () -> SavageSkyBlock.getSkyblock.saveisland(this));
    }

    public Builder getBuilder() {
        return builder;
    }

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public Collector getCollector() {
        return collector;
    }

    public void setCollector(Collector collector) {
        this.collector = collector;
    }

    public Competitor getCompetitor() {
        return competitor;
    }

    public void setCompetitor(Competitor competitor) {
        this.competitor = competitor;
    }

    public Farmer getFarmer() {
        return farmer;
    }

    public void setFarmer(Farmer farmer) {
        this.farmer = farmer;
    }

    public Fisherman getFisherman() {
        return fisherman;
    }

    public void setFisherman(Fisherman fisherman) {
        this.fisherman = fisherman;
    }

    public Hunter getHunter() {
        return hunter;
    }

    public void setHunter(Hunter hunter) {
        this.hunter = hunter;
    }

    public Miner getMiner() {
        return miner;
    }

    public void setMiner(Miner miner) {
        this.miner = miner;
    }

    public boolean isblockinisland(int x, int z) {
        if (x > pos1.getX() && x <= pos2.getX()) {
            if (z > pos1.getZ() && z <= pos2.getZ()) {
                return true;
            }
        }
        return false;
    }

    public Location getWarp1() {
        return warp1;
    }

    public void setWarp1(Location warp1) {
        this.warp1 = warp1;
    }

    public Location getWarp2() {
        return warp2;
    }

    public void setWarp2(Location warp2) {
        this.warp2 = warp2;
    }

    public Location getWarp3() {
        return warp3;
    }

    public void setWarp3(Location warp3) {
        this.warp3 = warp3;
    }

    public Location getWarp4() {
        return warp4;
    }

    public void setWarp4(Location warp4) {
        this.warp4 = warp4;
    }

    public Location getWarp5() {
        return warp5;
    }

    public void setWarp5(Location warp5) {
        this.warp5 = warp5;
    }

    public void setPos1(Location pos1) {
        this.pos1 = pos1;
    }

    public void setPos2(Location pos2) {
        this.pos2 = pos2;
    }

    public Location getCenter() {
        return center;
    }

    public void regen() {
        deleteblocks();
        loadSchematic();
    }


    public void calculateworth() {
        if (ConfigManager.getInstance().getConfig().getBoolean("Options.EnableIsTop")) {
            int level = 0;
            for (Object object : chunks) {
                if (Version.getVersion().equals(Version.v1_8_R2)) level += NMS_v1_8_R2.calculate(object, this);
                if (Version.getVersion().equals(Version.v1_8_R3)) level += NMS_v1_8_R3.calculate(object, this);
                if (Version.getVersion().equals(Version.v1_9_R1)) level += NMS_v1_9_R1.calculate(object, this);
                if (Version.getVersion().equals(Version.v1_9_R2)) level += NMS_v1_9_R2.calculate(object, this);
                if (Version.getVersion().equals(Version.v1_10_R1)) level += NMS_v1_10_R1.calculate(object, this);
                if (Version.getVersion().equals(Version.v1_11_R1)) level += NMS_v1_11_R1.calculate(object, this);
                if (Version.getVersion().equals(Version.v1_12_R1)) level += NMS_v1_12_R1.calculate(object, this);
                if (Version.getVersion().equals(Version.v1_13_R1)) level += NMS_v1_13_R1.calculate(object, this);
                if (Version.getVersion().equals(Version.v1_13_R2)) level += NMS_v1_13_R2.calculate(object, this);
            }
            setLevel(level);
        }
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getCrystals() {
        return crystals;
    }

    public void setCrystals(Integer crystals) {
        this.crystals = crystals;
    }

    public void addCrystals(Integer crystals) {
        this.crystals += crystals;
    }

    public void removeCrystals(Integer crystals) {
        this.crystals -= crystals;
    }

    public Integer getSize() {
        return Size;
    }

    public void setSize(Integer size) {
        Size = size;
    }

    public Integer getMemberCount() {
        return MemberCount;
    }

    public void setMemberCount(Integer memberCount) {
        MemberCount = memberCount;
    }

    public Integer getWarpCount() {
        return WarpCount;
    }

    public void setWarpCount(Integer warpCount) {
        WarpCount = warpCount;
    }

    public Integer getSpawner() {
        return spawner;
    }

    public Integer getFarming() {
        return Farming;
    }

    public Integer getXp() {
        return Xp;
    }

    public Integer getFly() {
        return Fly;
    }

    public Integer getMobCoins() {
        return MobCoins;
    }

    public Integer getSpawnerCode() {
        return SpawnerCode;
    }

    public Integer getId() {
        return id;
    }

    public void startspawnercountdown(int i) {
        spawner = i;
        SpawnerCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(SavageSkyBlock.getSkyblock, () -> {
            if (spawner <= 0) {
                Bukkit.getScheduler().cancelTask(SpawnerCode);
                SpawnerBoosterActive = false;
            } else {
                this.spawner--;
            }
        }, 20L, 20L);
    }

    public void startfarmingcountdown(int i) {
        Farming = i;
        FarmingCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(SavageSkyBlock.getSkyblock, () -> {
            if (Farming <= 0) {
                Bukkit.getScheduler().cancelTask(FarmingCode);
                FarmingBoosterActive = false;
            } else {
                this.Farming--;
            }
        }, 20L, 20L);
    }

    public void startxpcountdown(int i) {
        Xp = i;
        XPCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(SavageSkyBlock.getSkyblock, () -> {
            if (Xp <= 0) {
                Bukkit.getScheduler().cancelTask(XPCode);
                XPBoosterActive = false;
            } else {
                this.Xp--;
            }
        }, 20L, 20L);
    }

    public void startflycountdown(int i) {
        Fly = i;
        FlyCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(SavageSkyBlock.getSkyblock, () -> {
            if (Fly <= 0) {
                Bukkit.getScheduler().cancelTask(FlyCode);
                FlyBoosterActive = false;
            } else {
                this.Fly--;
            }
        }, 20L, 20L);
    }

    public void startmobcoinscountdown(int i) {
        MobCoins = i;
        MobcoinsCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(SavageSkyBlock.getSkyblock, () -> {
            if (MobCoins <= 0) {
                Bukkit.getScheduler().cancelTask(MobcoinsCode);
                MobCoinsBoosterActive = false;
            } else {
                this.MobCoins--;
            }
        }, 20L, 20L);
    }

    public void loadSchematic() {
        if (Version.getVersion().equals(Version.v1_8_R2))
            NMS_v1_8_R2.pasteSchematic(ConfigManager.getInstance().getSchematicFile(), this.home.clone().add(-3, -9, -1));
        if (Version.getVersion().equals(Version.v1_8_R3))
            NMS_v1_8_R3.pasteSchematic(ConfigManager.getInstance().getSchematicFile(), this.home.clone().add(-3, -9, -1));
        if (Version.getVersion().equals(Version.v1_9_R1))
            NMS_v1_9_R1.pasteSchematic(ConfigManager.getInstance().getSchematicFile(), this.home.clone().add(-3, -9, -1));
        if (Version.getVersion().equals(Version.v1_10_R1))
            NMS_v1_10_R1.pasteSchematic(ConfigManager.getInstance().getSchematicFile(), this.home.clone().add(-3, -9, -1));
        if (Version.getVersion().equals(Version.v1_11_R1))
            NMS_v1_11_R1.pasteSchematic(ConfigManager.getInstance().getSchematicFile(), this.home.clone().add(-3, -9, -1));
        if (Version.getVersion().equals(Version.v1_12_R1))
            NMS_v1_12_R1.pasteSchematic(ConfigManager.getInstance().getSchematicFile(), this.home.clone().add(-3, -9, -1));
        if (Version.getVersion().equals(Version.v1_13_R1))
            NMS_v1_13_R1.pasteSchematic(ConfigManager.getInstance().getSchematicFile(), this.home.clone().add(-3, -9, -1));
        if (Version.getVersion().equals(Version.v1_13_R2))
            NMS_v1_13_R2.pasteSchematic(ConfigManager.getInstance().getSchematicFile(), this.home.clone().add(-3, -9, -1));
    }

    public void deleteblocks() {
        //Deleting Island Blocks Using NMSs
        //1 layer at a time
        if (Version.getVersion().equals(Version.v1_8_R2)) {
            for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
                for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                    for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                        NMS_v1_8_R2.setBlockSuperFast((int) X, (int) Y, (int) Z, 0, (byte) 0, false);
                    }
                }
            }
        } else if (Version.getVersion().equals(Version.v1_8_R3)) {
            for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
                for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                    for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                        NMS_v1_8_R3.setBlockSuperFast((int) X, (int) Y, (int) Z, 0, (byte) 0, false);
                    }
                }
            }
        } else if (Version.getVersion().equals(Version.v1_9_R1)) {
            for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
                for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                    for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                        NMS_v1_9_R1.setBlockSuperFast((int) X, (int) Y, (int) Z, 0, (byte) 0, false);
                    }
                }
            }
        } else if (Version.getVersion().equals(Version.v1_10_R1)) {
            for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
                for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                    for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                        NMS_v1_10_R1.setBlockSuperFast((int) X, (int) Y, (int) Z, 0, (byte) 0, false);
                    }
                }
            }
        } else if (Version.getVersion().equals(Version.v1_11_R1)) {
            for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
                for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                    for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                        NMS_v1_11_R1.setBlockSuperFast((int) X, (int) Y, (int) Z, 0, (byte) 0, false);
                    }
                }
            }
        } else if (Version.getVersion().equals(Version.v1_12_R1)) {
            for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
                for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                    for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                        NMS_v1_12_R1.setBlockSuperFast((int) X, (int) Y, (int) Z, 0, (byte) 0, false);
                    }
                }
            }
        } else {
            for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
                for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                    for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                        SavageSkyBlock.getSkyblock.getWorld().getBlockAt((int) X, (int) Y, (int) Z).setType(Material.AIR, false);
                    }
                }
            }
        }
    }

    public void delete() {
        deleteblocks();
        for (String player : players) {
            if (User.getbyPlayer(player) != null) {
                User.getbyPlayer(player).setIsland(null);
            }
        }
        if (this.MobcoinsCode != null) Bukkit.getScheduler().cancelTask(this.MobcoinsCode);
        if (this.FarmingCode != null) Bukkit.getScheduler().cancelTask(this.FarmingCode);
        if (this.XPCode != null) Bukkit.getScheduler().cancelTask(this.XPCode);
        if (this.SpawnerCode != null) Bukkit.getScheduler().cancelTask(this.SpawnerCode);
        if (this.FlyCode != null) Bukkit.getScheduler().cancelTask(this.FlyCode);
        this.level = 0;
        this.players.clear();
        this.owner = "";
        this.MobCoinsBoosterActive = false;
        this.SpawnerBoosterActive = false;
        this.FlyBoosterActive = false;
        this.FarmingBoosterActive = false;
        this.XPBoosterActive = false;
        setSize(1);
        setWarpCount(1);
        setMemberCount(1);
        SavageSkyBlock.getSkyblock.save();
    }

    public boolean canbuild(Player player) {
        if (User.getbyPlayer(player.getName()) == null) {
            User.users.put(player.getName(), new User(player.getName()));
        }
        User u = User.getbyPlayer(player.getName());
        if (u.getBypass()) return true;
        return players.contains(player.getName());
    }

    public Boolean getSpawnerBoosterActive() {
        return SpawnerBoosterActive;
    }

    public void setSpawnerBoosterActive(Boolean spawnerBoosterActive) {
        SpawnerBoosterActive = spawnerBoosterActive;
    }

    public Boolean getFarmingBoosterActive() {
        return FarmingBoosterActive;
    }

    public void setFarmingBoosterActive(Boolean farmingBoosterActive) {
        FarmingBoosterActive = farmingBoosterActive;
    }

    public Boolean getXPBoosterActive() {
        return XPBoosterActive;
    }

    public void setXPBoosterActive(Boolean XPBoosterActive) {
        this.XPBoosterActive = XPBoosterActive;
    }

    public Boolean getFlyBoosterActive() {
        return FlyBoosterActive;
    }

    public void setFlyBoosterActive(Boolean flyBoosterActive) {
        FlyBoosterActive = flyBoosterActive;
    }

    public Boolean getMobCoinsBoosterActive() {
        return MobCoinsBoosterActive;
    }

    public void setMobCoinsBoosterActive(Boolean mobCoinsBoosterActive) {
        MobCoinsBoosterActive = mobCoinsBoosterActive;
    }

    public String getownername() {
        return owner;
    }

    public void setowner(Player owner) {
        this.owner = owner.getName();
    }

    public void setowner(String owner) {
        this.owner = owner;
    }

    public Location gethome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public ArrayList<String> getPlayers() {
        return players;
    }

    public void addUser(String player) {
        players.add(player);
        if (User.getbyPlayer(player) == null) {
            User.users.put(player, new User(player));
        }
        User.getbyPlayer(player).setIsland(this);
    }

    public void removeUser(String player) {
        players.remove(player);
        if (User.getbyPlayer(player) == null) {
            User.users.put(player, new User(player));
        }
        User.getbyPlayer(player).setIsland(null);
    }

    public Location getPos1() {
        return pos1;
    }

    public Location getPos2() {
        return pos2;
    }

    public Location getMaxpos1() {
        return maxpos1;
    }

    public Location getMaxpos2() {
        return maxpos2;
    }
}
