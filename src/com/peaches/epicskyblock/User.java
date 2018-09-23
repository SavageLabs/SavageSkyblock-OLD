package com.peaches.epicskyblock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class User {

    static ArrayList<User> users = new ArrayList<>();
    private String player;
    private Island island;
    private Boolean chat = false;
    private ArrayList<String> invites = new ArrayList<>();

    public User(String player) {
        this.player = player;
    }

    public Boolean getChat() {
        return chat;
    }

    public void setChat(Boolean chat) {
        this.chat = chat;
    }

    public static User getbyPlayer(Player player) {
        for (User user : users) {
            if (user.getPlayer().getName().equals(player.getName())) {
                return user;
            }
        }
        return null;
    }

    public static User getbyPlayer(String player) {
        for (User user : users) {
            if (user.getPlayerName().equals(player)) {
                return user;
            }
        }
        return null;
    }

    private Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    private String getPlayerName() {
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
