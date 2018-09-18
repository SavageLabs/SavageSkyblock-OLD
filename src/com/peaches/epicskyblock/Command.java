package com.peaches.epicskyblock;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

class Command implements Listener, CommandExecutor {
    private static EpicSkyBlock plugin;

    public Command(EpicSkyBlock pl) {
        plugin = pl;
    }

    public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String label, String[] args) {
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("perms") || args[0].equalsIgnoreCase("permissions")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p));
                    }
                    if (User.getbyPlayer(p).getIsland() != null) {
                        if(User.getbyPlayer(p).getIsland().getrank(p)==Rank.OWNER){
                            p.openInventory(plugin.Permissions());
                        }else{
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eNo permissions"));
                        }
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("leave")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p));
                    }
                    if (User.getbyPlayer(p).getIsland() != null) {
                        if (User.getbyPlayer(p).getIsland().getowner() == p) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eTo leave your island you must transfer ownership to another player."));
                            return true;
                        }
                        User.getbyPlayer(p).getIsland().getPlayers().remove(p.getName());
                        User.getbyPlayer(p).setIsland(null);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have left your island"));
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("sethome")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p));
                    }
                    if (User.getbyPlayer(p).getIsland() != null) {
                        if (User.getbyPlayer(p).getIsland().getowner() == p) {
                            if (IslandManager.getislandviablock(p.getLocation().getBlock()) == User.getbyPlayer(p).getIsland()) {
                                User.getbyPlayer(p).getIsland().setHome(p.getLocation());
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eIsland home set at your location."));
                                return true;
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou must be on your island"));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eOnly the island owner can do this"));
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("delete")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p));
                    }
                    IslandManager.deleteIsland(p);
                    return true;
                }
                if (args[0].equalsIgnoreCase("home")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p));
                    }
                    if (User.getbyPlayer(p).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                        return true;
                    } else {
                        p.teleport(User.getbyPlayer(p).getIsland().gethome());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eTeleporting to island..."));
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("list")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p));
                    }
                    User user = User.getbyPlayer(p);
                    if (user.getIsland() != null) {
                        StringBuilder players = new StringBuilder("Island Members: ");
                        for (String player : user.getIsland().getPlayers().keySet()) {
                            if (players.toString() == "Island Members: ") {
                                players.append(player);
                            } else {
                                players.append(", ").append(player);
                            }
                        }
                        p.sendMessage(players.toString());
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("create")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p));
                    }
                    if (User.getbyPlayer(p).getIsland() == null) {
                        IslandManager.createIsland(p);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eIsland Created"));
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou already have an island"));
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("version")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ePlugin Name : &6&lEpicSkyBlock"));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ePlugin Version : &6&l" + plugin.getDescription().getVersion()));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&ePlugin Author : &6&lPeaches_MLG"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("reload")) {
                    ConfigManager.getInstance().reloadConfig();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &ePlugin Reloaded"));
                    return true;
                }
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("join")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p));
                    }
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &ePlayer not found"));
                        return true;
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player));
                    }
                    if (User.getbyPlayer(p).getIsland() != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou are already apart of an island"));
                        return true;
                    }
                    if (User.getbyPlayer(player).getIsland() != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThat player is not apart of an island"));
                        return true;
                    }
                    if (User.getbyPlayer(p).getInvites().contains(User.getbyPlayer(player).getIsland().getowner().getName())) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have joined an island"));
                        User.getbyPlayer(player).getIsland().addUser(p);
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an invite for that island"));
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("deinvite") || args[0].equalsIgnoreCase("uninvite")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p));
                    }
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &ePlayer not found"));
                        return true;
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player));
                    }
                    if (User.getbyPlayer(p).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                        return true;
                    }
                    if (p == player) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou cannot invite yourself"));
                        return true;
                    }
                    if (User.getbyPlayer(player).getInvites().contains(User.getbyPlayer(p).getIsland().getowner().getName())) {
                        User.getbyPlayer(player).getInvites().remove(User.getbyPlayer(p).getIsland().getowner().getName());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eInvite has been revoked from " + player.getName()));
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis player has no active Invite to your island"));
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("invite")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p));
                    }
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &ePlayer not found"));
                        return true;
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player));
                    }
                    if (User.getbyPlayer(p).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                        return true;
                    }
                    if (p == player) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou cannot invite yourself"));
                        return true;
                    }
                    if (User.getbyPlayer(player).getInvites().contains(User.getbyPlayer(p).getIsland().getowner().getName())) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis player already has an active invite"));
                        return true;
                    }
                    User.getbyPlayer(player).getInvites().add(User.getbyPlayer(p).getIsland().getowner().getName());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eInvite sent to " + player.getName()));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have been invited to join " + player.getName() + "'s Island"));
                    return true;
                }
            }
            for (String message : ConfigManager.getInstance().getConfig().getStringList("help")) {
                if (message.contains("%centered%")) {
                    plugin.sendCenteredMessage(p, ChatColor.translateAlternateColorCodes('&', message.replace("%centered%", "")));
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                }
            }
        } else {
            cs.sendMessage("This command must be executed by a player");
            return true;
        }
        return false;
    }
}
