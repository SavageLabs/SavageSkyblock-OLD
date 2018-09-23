package com.peaches.epicskyblock;

import com.sk89q.worldedit.CuboidClipboard;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.schematic.MCEditSchematicFormat;
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

    public Location pos1; // Bottom left corner
    public Location pos2; // Bottom right corner
    final Location maxpos1; // Bottom left corner
    final Location maxpos2; // Bottom right corner
    private String owner;
    private Location home;
    private Boolean SpawnerBoosterActive = false;
    private Boolean FarmingBoosterActive = false;
    private Boolean XPBoosterActive = false;
    private Boolean FlyBoosterActive = false;
    private Boolean MobCoinsBoosterActive = false;
    private Integer spawner = 0;
    private Integer Farming = 0;
    private Integer Xp = 0;
    private Integer Fly = 0;
    private Integer MobCoins = 0;
    private Integer SpawnerCode;
    private ArrayList<String> players = new ArrayList<>();

    public Island(String owner, Location home, Location pos1, Location pos2, Location mpos1, Location mpos2, Boolean schem) {
        this.owner = owner;
        this.home = home;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.maxpos1 = mpos1;
        this.maxpos2 = mpos2;
        players.add(owner);

        if (User.getbyPlayer(owner) == null) {
            User.users.add(new User(owner));
        }
        User.getbyPlayer(owner).setIsland(this);
        //Loads island.schematic
        if (schem) loadSchematic(Bukkit.getPlayer(owner));
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

    public void startspawnercountdown(int i) {
        spawner = i;
        SpawnerCode = Bukkit.getScheduler().scheduleSyncRepeatingTask(EpicSkyBlock.getSkyblock, () -> {
            if (spawner <= 0) {
                Bukkit.getScheduler().cancelTask(SpawnerCode);
                SpawnerBoosterActive = false;
                return;
            } else {
                this.spawner--;
            }
        }, 20L, 20L);
    }

    public void loadSchematic(Player player) {
        Location location = this.home;
        WorldEditPlugin worldEditPlugin = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
        File schematic = ConfigManager.getInstance().getSchematicFile();
        EditSession session = worldEditPlugin.getWorldEdit().getEditSessionFactory().getEditSession(new BukkitWorld(location.getWorld()), 10000);
        try {
            CuboidClipboard clipboard = MCEditSchematicFormat.getFormat(schematic).load(schematic);
            clipboard.paste(session, new Vector(location.getX(), location.getY(), location.getZ()), false);
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
                        Bukkit.getScheduler().scheduleSyncDelayedTask(EpicSkyBlock.getSkyblock.plugin, () -> b.setType(Material.AIR), 5);
                    }
                }
            }

        }

        for (String player : players) {
            if (User.getbyPlayer(player) != null) {
                User.getbyPlayer(player).setIsland(null);
            }
        }
        this.owner = "";
    }

    public boolean canbuild(Player player) {
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

    public Player getowner() {
        return Bukkit.getPlayer(owner);
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
