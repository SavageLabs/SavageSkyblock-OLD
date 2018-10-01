package com.peaches.epicskyblock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class User {

    public static ArrayList<User> users = new ArrayList<>();
    private String player;
    private Island island;
    private Boolean chat = false;
    private Boolean bypass = false;
    private ArrayList<String> invites = new ArrayList<>();

    public User(String player) {
        this.player = player;
    }

    public static User getbyPlayer(Player player) {
        for (User user : users) {
            if (user.getPlayerName().equals(player.getName())) {
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

    public Boolean getBypass() {
        return bypass;
    }

    public void setBypass(Boolean bypass) {
        this.bypass = bypass;
    }

    public Boolean getChat() {
        return chat;
    }

    public void setChat(Boolean chat) {
        this.chat = chat;
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
