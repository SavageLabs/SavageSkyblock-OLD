package net.prosavage.savageskyblock;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class IslandManager {

    private static ArrayList<Island> Islands = new ArrayList<>();

    private static Location nextloc = new Location(SavageSkyBlock.getSkyblock.getWorld(), 0, 88, 0);

    private static Integer nextid = 1;

    private static Direction direction = Direction.UNDIFINED;

    public static Integer level1radius = SavageSkyBlock.getSkyblock.getConfig().getInt("Options.Level1Size");
    public static Integer level2radius = SavageSkyBlock.getSkyblock.getConfig().getInt("Options.Level2Size");
    public static Integer level3radius = SavageSkyBlock.getSkyblock.getConfig().getInt("Options.Level3Size");

    public static Location getNextloc() {
        return nextloc;
    }

    public static void setNextloc(Location nextloc) {
        IslandManager.nextloc = nextloc;
    }

    public static Direction getDirection() {
        return direction;
    }

    public static void setDirection(Direction direction) {
        IslandManager.direction = direction;
    }

    public static void addIsland(Island island) {
        Islands.add(island);
    }

    public static void createIsland(Player player) {
        for (Island island : Islands) {
            if (island.getownername().equals("")) {
                island.setowner(player);
                island.loadSchematic();
                island.addUser(player.getName());
                island.teleporthome(player);
                SavageSkyBlock.getSkyblock.sendIslandBoarder(player);
                return;
            }
        }
        Boolean i = true;
        while (i) {
            if (getislandviablock(nextloc.getBlock()) == null) {
                i = false;
            }
            if (direction == Direction.UNDIFINED) {
                direction = Direction.NORTH;
                if (!i) {
                    Island island = new Island(player.getName(), nextloc.clone().add(0.5, -3, -1.5), nextloc.clone().add(-level1radius, -120, -level1radius), nextloc.clone().add(level1radius, 100, level1radius), nextloc.clone().add(-level3radius, -120, -level3radius), nextloc.clone().add(level3radius, 100, level3radius), nextloc.clone(), true);
                    Islands.add(island);
                    User.getbyPlayer(player).setIsland(island);
                    island.teleporthome(player);
                    return;
                }
            }
            if (direction == Direction.NORTH) {
                nextloc.add(level3radius * 2, 0, 0);
                if (!i) {
                    // Check if an island is available in east
                    if (getislandviablock(nextloc.clone().add(0, 0, level3radius * 2).getBlock()) == null) {
                        direction = Direction.EAST;
                    }
                    Island island = new Island(player.getName(), nextloc.clone().add(0.5, -3, -1.5), nextloc.clone().add(-level1radius, -120, -level1radius), nextloc.clone().add(level1radius, 100, level1radius), nextloc.clone().add(-level3radius, -120, -level3radius), nextloc.clone().add(level3radius, 100, level3radius), nextloc.clone(), true);
                    Islands.add(island);
                    User.getbyPlayer(player).setIsland(island);
                    island.teleporthome(player);
                    return;
                }
            }
            if (direction == Direction.EAST) {
                nextloc.add(0, 0, level3radius * 2);
                if (!i) {
                    // Check if an island is available in east
                    if (getislandviablock(nextloc.clone().add(-level3radius * 2, 0, 0).getBlock()) == null) {
                        direction = Direction.SOUTH;
                    }
                    Island island = new Island(player.getName(), nextloc.clone().add(0.5, -3, -1.5), nextloc.clone().add(-level1radius, -120, -level1radius), nextloc.clone().add(level1radius, 100, level1radius), nextloc.clone().add(-level3radius, -120, -level3radius), nextloc.clone().add(level3radius, 100, level3radius), nextloc.clone(), true);
                    Islands.add(island);
                    User.getbyPlayer(player).setIsland(island);
                    island.teleporthome(player);
                    return;
                }
            }
            if (direction == Direction.SOUTH) {
                nextloc.add(-level3radius * 2, 0, 0);
                if (!i) {
                    // Check if an island is available in east
                    if (getislandviablock(nextloc.clone().add(0, 0, -level3radius * 2).getBlock()) == null) {
                        direction = Direction.WEST;
                    }
                    Island island = new Island(player.getName(), nextloc.clone().add(0.5, -3, -1.5), nextloc.clone().add(-level1radius, -120, -level1radius), nextloc.clone().add(level1radius, 100, level1radius), nextloc.clone().add(-level3radius, -120, -level3radius), nextloc.clone().add(level3radius, 100, level3radius), nextloc.clone(), true);
                    Islands.add(island);
                    User.getbyPlayer(player).setIsland(island);
                    island.teleporthome(player);
                    return;
                }
            }
            if (direction == Direction.WEST) {
                nextloc.add(0, 0, -level3radius * 2);
                if (!i) {
                    // Check if an island is available in east
                    if (getislandviablock(nextloc.clone().add(level3radius * 2, 0, 0).getBlock()) == null) {
                        direction = Direction.NORTH;
                    }
                    Island island = new Island(player.getName(), nextloc.clone().add(0.5, -3, -1.5), nextloc.clone().add(-level1radius, -120, -level1radius), nextloc.clone().add(level1radius, 100, level1radius), nextloc.clone().add(-level3radius, -120, -level3radius), nextloc.clone().add(level3radius, 100, level3radius), nextloc.clone(), true);
                    Islands.add(island);
                    User.getbyPlayer(player).setIsland(island);
                    island.teleporthome(player);
                    return;
                }
            }
        }
    }

    public static void deleteIsland(Island island) {
        island.delete();
    }

    public static void deleteIsland(Player player) {
        User u = User.getbyPlayer(player);
        for (String players : u.getIsland().getPlayers()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + players);
        }
        u.getIsland().delete();
        player.getInventory().clear();
        u.setIsland(null);
    }

    public static Island getislandviablock(Block b) {
        Location loc = b.getLocation();
        for (Island island : Islands) {
            if (island.isblockinisland((int) loc.getX(), (int) loc.getZ())) {
                return island;
            }
        }
        return null;
    }

    public static ArrayList<Island> getIslands() {
        return Islands;
    }

    public static Integer getNextid() {
        return nextid;
    }

    public static void setNextid(Integer nextid) {
        IslandManager.nextid = nextid;
    }
}
