package net.prosavage.savageskyblock.API;

import net.prosavage.savageskyblock.Island;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class IslandMissionCompleteEvent extends Event {

    Island island;
    int reward;
    String name;
    private static final HandlerList handlers = new HandlerList();

    public IslandMissionCompleteEvent(Island island, int reward, String name) {
        this.island = island;
        this.reward = reward;
        this.name = name;
    }

    public Island getIsland() {
        return island;
    }

    public int getReward() {
        return reward;
    }

    public String getName() {
        return name;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public HandlerList getHandlers() {
        return handlers;
    }
}