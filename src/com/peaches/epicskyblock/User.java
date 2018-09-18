package com.peaches.epicskyblock;

import org.bukkit.entity.Player;

import java.util.ArrayList;

public class User {

    static ArrayList<User> users = new ArrayList<>();
    private Player player;
    private Island island;
    private ArrayList<String> invites;

    public User(Player player) {
        this.player = player;
    }

    public static User getbyPlayer(Player player) {
        for (User user : users) {
            if (user.getPlayer().equals(player)) {
                return user;
            }
        }
        return null;
    }

    private Player getPlayer() {
        return player;
    }

    public Island getIsland() {
        return island;
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    public ArrayList<String> getInvites() {
        return invites;
    }
}
