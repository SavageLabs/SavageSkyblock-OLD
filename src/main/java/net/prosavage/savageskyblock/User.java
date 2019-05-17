package net.prosavage.savageskyblock;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class User {

    public static HashMap<String, User> users = new HashMap<>();
    private String player;
    private Island island;
    private Boolean chat = false;
    private Boolean Falldmg = false;
    private Boolean bypass = false;
    private ArrayList<String> invites = new ArrayList<>();

    public User(String player) {
        this.player = player;
        users.put(player, this);
    }

    public static User getbyPlayer(Player player) {
        return getbyPlayer(player.getName());
    }

    public static User getbyPlayer(String player) {
        if(users.containsKey(player)){
            return users.get(player);
        }else{
            return new User(player);
        }
    }

    public Boolean getFalldmg() {
        return Falldmg;
    }

    public void setFalldmg(Boolean falldmg) {
        Falldmg = falldmg;
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

    public Player getPlayer() {
        return Bukkit.getPlayer(player);
    }

    public String getPlayerName() {
        return player;
    }

    public Island getIsland() {
        return this.island;
    }

    public void setIsland(Island island) {
        this.island = island;
    }

    public ArrayList<String> getInvites() {
        return invites;
    }
}
