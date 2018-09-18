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
import java.util.HashMap;

public class Island {

    public Location pos1; // Bottom left corner
    public Location pos2; // Bottom right corner
    final Location maxpos1; // Bottom left corner
    final Location maxpos2; // Bottom right corner
    private Player owner;
    private Location home;
    private HashMap<String, Rank> players = new HashMap<>();

    //OWNER Has all permissions by default
    private Permission MEMBER = new Permission(true, true, true, true, true, true, true, true, true, true, true, true, true, true, false);
    private Permission GUEST = new Permission(false, false, false, false, false, false, false, false, false, false, false, false, false, false, false);
    private Permission COOWNER = new Permission(true, true, true, true, true, true, true, true, true, true, true, true, true, false, true);

    public Island(Player owner, Location home, Location pos1, Location pos2, Location mpos1, Location mpos2) {
        this.owner = owner;
        this.home = home;
        this.pos1 = pos1;
        this.pos2 = pos2;
        this.maxpos1 = mpos1;
        this.maxpos2 = mpos2;
        players.put(owner.getName(), Rank.OWNER);
        //Loads island.schematic
        loadSchematic(owner);
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

        for (String player : players.keySet()) {
            User.getbyPlayer(Bukkit.getPlayer(player)).setIsland(null);
        }
        this.setowner(null);
    }

    public boolean canbuild(Player player) {
        return players.containsKey(player.getName());
    }

    public Player getowner() {
        return owner;
    }

    public void setowner(Player owner) {
        this.owner = owner;
    }

    public Location gethome() {
        return home;
    }

    public void setHome(Location home) {
        this.home = home;
    }

    public HashMap<String, Rank> getPlayers() {
        return players;
    }

    public void addUser(Player player) {
        players.put(player.getName(), Rank.MEMBER);
        User.getbyPlayer(player).setIsland(this);
    }

    public Rank getrank(Player player){
        if(players.containsKey(player.getName())){
            return players.get(player.getName());
        }

        return null;
    }

    public Permission getpermissions(Player player){
        if(players.containsKey(player.getName())){
            if(players.get(player.getName()).equals(Rank.MEMBER)){
                return MEMBER;
            }
            if(players.get(player.getName()).equals(Rank.COOWNER)){
                return COOWNER;
            }
        }else{
            return GUEST;
        }
        return null;
    }
}
