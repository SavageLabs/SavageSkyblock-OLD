package com.peaches.epicskyblock;

import com.peaches.epicskyblock.Inventories.BoostersGUI;
import com.peaches.epicskyblock.Inventories.MissionsGUI;
import com.peaches.epicskyblock.Inventories.UpgradesGUI;
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
            if (args.length == 0) {
                for (String message : ConfigManager.getInstance().getConfig().getStringList("help")) {
                    if (message.contains("%centered%")) {
                        plugin.sendCenteredMessage(p, ChatColor.translateAlternateColorCodes('&', message.replace("%centered%", "")));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("bypass")) {
                if (p.hasPermission("EpicSkyblock.bypass")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p.getName()));
                    }
                    User.getbyPlayer(p).setBypass(!User.getbyPlayer(p).getBypass());
                    if (User.getbyPlayer(p).getBypass()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eBypass Mode Enabled"));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eBypass Mode Disabled"));
                    }
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("fly")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    if (User.getbyPlayer(p).getIsland().getFlyBoosterActive()) {
                        if (p.getAllowFlight()) {
                            p.setAllowFlight(false);
                            p.setFlying(false);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eFly disabled"));
                        } else {
                            p.setAllowFlight(true);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eFly enabled"));
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have permissions"));
                    }
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                return true;
            }
            if (args[0].equalsIgnoreCase("chat")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    User.getbyPlayer(p).setChat(!User.getbyPlayer(p).getChat());
                    if (User.getbyPlayer(p).getChat()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eIsland chat has been enabled"));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eIsland chat has been disabled"));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("upgrade") || args[0].equalsIgnoreCase("upgrades")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    p.openInventory(UpgradesGUI.inv(User.getbyPlayer(p).getIsland()));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                return true;
            }
            if (args[0].equalsIgnoreCase("boosters") || args[0].equalsIgnoreCase("booster")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    p.openInventory(BoostersGUI.inv(User.getbyPlayer(p).getIsland()));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                return true;
            }
            if (args[0].equalsIgnoreCase("missions") || args[0].equalsIgnoreCase("mission")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    p.openInventory(MissionsGUI.inv(User.getbyPlayer(p).getIsland()));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    if (User.getbyPlayer(p).getIsland().getownername().equals(p.getName())) {
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
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    if (User.getbyPlayer(p).getIsland().getownername().equals(p.getName())) {
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
                    User.users.add(new User(p.getName()));
                }
                IslandManager.deleteIsland(p);
                return true;
            }
            if (args[0].equalsIgnoreCase("home")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
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
                    User.users.add(new User(p.getName()));
                }
                User user = User.getbyPlayer(p);
                if (user.getIsland() != null) {
                    StringBuilder players = new StringBuilder("Island Members: ");
                    for (String player : user.getIsland().getPlayers()) {
                        if (players.toString().equals("Island Members: ")) {
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
                    User.users.add(new User(p.getName()));
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
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPlugin Name : &eEpicSkyBlock"));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPlugin Version : &e" + plugin.getDescription().getVersion()));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPlugin Author : &ePeaches_MLG"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                ConfigManager.getInstance().reloadConfig();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &ePlugin Reloaded"));
                return true;
            }
            try {
                if (args[0].equalsIgnoreCase("join")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p.getName()));
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player.getName()));
                    }
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &ePlayer not found"));
                        return true;
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player.getName()));
                    }
                    if (User.getbyPlayer(p).getIsland() != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou are already apart of an island"));
                        return true;
                    }
                    if (User.getbyPlayer(player).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThat player is not apart of an island"));
                        return true;
                    }
                    if (User.getbyPlayer(p).getInvites().contains(User.getbyPlayer(player).getIsland().getownername())) {
                        if (User.getbyPlayer(player).getIsland().getPlayers().size() >= EpicSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members." + User.getbyPlayer(player).getIsland().getMemberCount())) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThe maximum amount of players has already been reached"));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have joined an island"));
                        p.teleport(User.getbyPlayer(p).getIsland().gethome());
                        for (String pla : User.getbyPlayer(player).getIsland().getPlayers()) {
                            Player i = Bukkit.getPlayer(pla);
                            if (i != null) {
                                i.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &e" + p.getName() + " has joined your island"));
                            }

                        }
                        User.getbyPlayer(player).getIsland().addUser(p.getName());
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an invite for that island"));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &e/is join <PlayerName>"));
                return true;
            }
            try {
                if (args[0].equalsIgnoreCase("leader") || args[0].equalsIgnoreCase("owner")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p.getName()));
                    }
                    String player = args[1];
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player));
                    }
                    if (User.getbyPlayer(p).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                        return true;
                    }
                    if (User.getbyPlayer(p).getIsland().getownername().equalsIgnoreCase(p.getName())) {
                        if (p.getName().equalsIgnoreCase(player)) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou cannot give yourself the leader role"));
                            return true;
                        }
                        if (User.getbyPlayer(p).getIsland().getPlayers().contains(player)) {
                            User.getbyPlayer(p).getIsland().setowner(player);
                            if (Bukkit.getPlayer(player) != null) {
                                Bukkit.getPlayer(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have been given the owner role by " + p.getName()));
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have transfered ownership to " + player));
                            return true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis player is not in your island"));
                            return true;
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eOnly the island owner can transfer ownership"));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &e/is leader <PlayerName>"));
                return true;
            }
            try {
                if (args[0].equalsIgnoreCase("kick")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p.getName()));
                    }
                    String player = args[1];
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player));
                    }
                    if (User.getbyPlayer(p).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                        return true;
                    }
                    if (p.getName().equalsIgnoreCase(player)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou cannot kick yourself"));
                        return true;
                    }
                    if (User.getbyPlayer(p).getIsland().getownername().equalsIgnoreCase(player)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou cannot kick the owner"));
                        return true;
                    }
                    if (User.getbyPlayer(p).getIsland().getPlayers().contains(player)) {
                        User.getbyPlayer(player).getIsland().getPlayers().remove(player);
                        User.getbyPlayer(player).setIsland(null);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have kicked " + player));
                        if (Bukkit.getPlayer(player) != null) {
                            Bukkit.getPlayer(player).sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have beed kicked by " + p.getName()));
                        }
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis player is not in your island"));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &e/is kick <PlayerName>"));
                return true;
            }
            try {
                if (args[0].equalsIgnoreCase("deinvite") || args[0].equalsIgnoreCase("uninvite")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p.getName()));
                    }
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &ePlayer not found"));
                        return true;
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player.getName()));
                    }
                    if (User.getbyPlayer(p).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                        return true;
                    }
                    if (p == player) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou cannot invite yourself"));
                        return true;
                    }
                    if (User.getbyPlayer(player).getInvites().contains(User.getbyPlayer(p).getIsland().getownername())) {
                        User.getbyPlayer(player).getInvites().remove(User.getbyPlayer(p).getIsland().getownername());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eInvite has been revoked from " + player.getName()));
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis player has no active Invite to your island"));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &e/is uninvite <PlayerName>"));
                return true;
            }
            try {
                if (args[0].equalsIgnoreCase("invite")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p.getName()));
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player.getName()));
                    }
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &ePlayer not found"));
                        return true;
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player.getName()));
                    }
                    if (User.getbyPlayer(p).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou do not have an island"));
                        return true;
                    }
                    if (p == player) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou cannot invite yourself"));
                        return true;
                    }
                    if (User.getbyPlayer(player).getInvites().contains(User.getbyPlayer(p).getIsland().getownername())) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eThis player already has an active invite"));
                        return true;
                    }
                    User.getbyPlayer(player).getInvites().add(User.getbyPlayer(p).getIsland().getownername());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eInvite sent to " + player.getName()));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &eYou have been invited to join " + p.getName() + "'s Island"));
                    return true;
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lSkyBlock &8» &e/is invite <PlayerName>"));
                return true;
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