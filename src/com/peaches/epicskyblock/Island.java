package com.peaches.epicskyblock;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.SchematicFormat;
import com.sk89q.worldedit.world.DataException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Island {

    private final Location maxpos1; // Bottom left corner
    private final Location maxpos2; // Bottom right corner
    public Location pos1; // Bottom left corner
    public Location pos2; // Bottom right corner
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
    private Integer Mission1;
    private Integer Mission2;
    private Integer Mission3;
    private Integer Mission1Data;
    private Integer Mission2Data;
    private Integer Mission3Data;
    private Boolean Mission1Complete;
    private Boolean Mission2Complete;
    private Boolean Mission3Complete;
    private Integer Size;
    private Integer MemberCount;
    private Integer WarpCount;
    private Integer crystals;

    private Integer id;

    private Location warp1;
    private Location warp2;
    private Location warp3;
    private Location warp4;
    private Location warp5;

    private Integer level = 0;
    private ArrayList<String> players = new ArrayList<>();

    public Island(String owner, Location home, Location pos1, Location pos2, Location mpos1, Location mpos2, Boolean schem) {
        this.owner = owner;
        this.home = home;
        this.pos1 = pos1; //-50, -50
        this.pos2 = pos2; //+50, +50
        this.maxpos1 = mpos1;
        this.maxpos2 = mpos2;
        this.Size = 1;
        this.MemberCount = 1;
        this.WarpCount = 1;
        this.crystals = 0;

        this.id = IslandManager.getNextid();
        IslandManager.setNextid(id + 1);

        this.SpawnerBoosterActive = false;
        this.FarmingBoosterActive = false;
        this.FarmingBoosterActive = false;
        this.XPBoosterActive = false;
        this.FlyBoosterActive = false;
        this.MobCoinsBoosterActive = false;

        this.Mission1Complete = false;
        this.Mission2Complete = false;
        this.Mission3Complete = false;

        addUser(owner);
        //Loads island.schematic
        if (schem) {

            for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
                for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                    for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                        Block b = new Location(EpicSkyBlock.getSkyblock.getWorld(), X, Y, Z).getBlock();
                        if (b.getType() != Material.AIR) {
                            b.setType(Material.AIR);
                        }
                    }
                }
            }
            loadSchematic();
        }
        EpicSkyBlock.getSkyblock.addMissions(this);
        Bukkit.getScheduler().runTaskAsynchronously(EpicSkyBlock.getSkyblock, () -> EpicSkyBlock.getSkyblock.saveisland(this));
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

    public void regen() {

        for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
            for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                    Block b = new Location(EpicSkyBlock.getSkyblock.getWorld(), X, Y, Z).getBlock();
                    if (b.getType() != Material.AIR) {
                        b.setType(Material.AIR);
                    }
                }
            }
        }
        loadSchematic();
    }


    public void calculateworth() {
        if (ConfigManager.getInstance().getConfig().getBoolean("Options.EnableIsTop")) {
            int level = 0;
            for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
                for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                    for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                        String b = new Location(EpicSkyBlock.getSkyblock.getWorld(), X, Y, Z).getBlock().getType().name();
                        if (EpicSkyBlock.getSkyblock.getConfig().contains("IsTop.Blocks." + b)) {
                            level = level + EpicSkyBlock.getSkyblock.getConfig().getInt("IsTop.Blocks." + b);
                        }
                    }
                }
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

    public Boolean getMission1Complete() {
        return Mission1Complete;
    }

    public void setMission1Complete(Boolean mission1Complete) {
        Mission1Complete = mission1Complete;
    }

    public Boolean getMission2Complete() {
        return Mission2Complete;
    }

    public void setMission2Complete(Boolean mission2Complete) {
        Mission2Complete = mission2Complete;
    }

    public Boolean getMission3Complete() {
        return Mission3Complete;
    }

    public void setMission3Complete(Boolean mission3Complete) {
        Mission3Complete = mission3Complete;
    }

    public Integer getMission1Data() {
        return Mission1Data;
    }

    public void setMission1Data(Integer mission1Data) {
        Mission1Data = mission1Data;
    }

    public Integer getMission2Data() {
        return Mission2Data;
    }

    public void setMission2Data(Integer mission2Data) {
        Mission2Data = mission2Data;
    }

    public Integer getMission3Data() {
        return Mission3Data;
    }

    public void setMission3Data(Integer mission3Data) {
        Mission3Data = mission3Data;
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

    public Integer getMission1() {
        return Mission1;
    }

    public void setMission1(Integer mission1) {
        Mission1 = mission1;
    }

    public Integer getMission2() {
        return Mission2;
    }

    public void setMission2(Integer mission2) {
        Mission2 = mission2;
    }

    public Integer getMission3() {
        return Mission3;
    }

    public void setMission3(Integer mission3) {
        Mission3 = mission3;
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
        SpawnerCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(EpicSkyBlock.getSkyblock, () -> {
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
        FarmingCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(EpicSkyBlock.getSkyblock, () -> {
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
        XPCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(EpicSkyBlock.getSkyblock, () -> {
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
        FlyCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(EpicSkyBlock.getSkyblock, () -> {
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
        MobcoinsCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(EpicSkyBlock.getSkyblock, () -> {
            if (MobCoins <= 0) {
                Bukkit.getScheduler().cancelTask(MobcoinsCode);
                MobCoinsBoosterActive = false;
            } else {
                this.MobCoins--;
            }
        }, 20L, 20L);
    }

    public void loadSchematic() {
        Location location = this.home;
        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        File schematic = ConfigManager.getInstance().getSchematicFile();
        EditSession editSession = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 10000);
        try {
            SchematicFormat.getFormat(schematic).load(schematic).paste(editSession, new Vector(location.getX(), location.getY(), location.getZ()), true, false);
            editSession.flushQueue();
        } catch (MaxChangedBlocksException | DataException | IOException e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        //Deleting Island Blocks
        for (double X = maxpos1.getX(); X <= maxpos2.getX(); X++) {
            for (double Y = maxpos1.getY(); Y <= maxpos2.getY(); Y++) {
                for (double Z = maxpos1.getZ(); Z <= maxpos2.getZ(); Z++) {
                    Block b = new Location(EpicSkyBlock.getSkyblock.getWorld(), X, Y, Z).getBlock();
                    if (b.getType() != Material.AIR) {
                        b.setType(Material.AIR);
                    }
                }
            }
        }

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
        EpicSkyBlock.getSkyblock.save();
    }

    public boolean canbuild(Player player) {
        if (User.getbyPlayer(player.getName()) == null) {
            User.users.add(new User(player.getName()));
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
            User.users.add(new User(player));
        }
        User.getbyPlayer(player).setIsland(this);
    }

    public void removeUser(String player) {
        players.remove(player);
        if (User.getbyPlayer(player) == null) {
            User.users.add(new User(player));
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
