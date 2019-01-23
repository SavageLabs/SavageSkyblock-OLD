package com.peaches.epicskyblock;

import com.peaches.epicskyblock.Inventories.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

class Command implements CommandExecutor {
    private static EpicSkyBlock plugin;

    public Command(EpicSkyBlock pl) {
        plugin = pl;
    }

    public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (cs instanceof Player) {
                Player p = (Player) cs;
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() == null) {
                    for (String message : ConfigManager.getInstance().getConfig().getStringList("help")) {
                        if (message.contains("%centered%")) {
                            plugin.sendCenteredMessage(cs, ChatColor.translateAlternateColorCodes('&', message.replace("%centered%", "")));
                        } else {
                            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        }
                    }
                    return true;
                } else {
                    User.getbyPlayer(p).setFalldmg(true);
                    Bukkit.getScheduler().scheduleAsyncDelayedTask(EpicSkyBlock.getSkyblock, () -> User.getbyPlayer(p).setFalldmg(false), 20);
                    p.teleport(User.getbyPlayer(p).getIsland().gethome());
                    EpicSkyBlock.getSkyblock.sendIslandBoarder(p);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("TeleportToIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            } else {
                for (String message : ConfigManager.getInstance().getConfig().getStringList("help")) {
                    if (message.contains("%centered%")) {
                        plugin.sendCenteredMessage(cs, ChatColor.translateAlternateColorCodes('&', message.replace("%centered%", "")));
                    } else {
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    }
                }
            }
            return true;
        }
        try {
            if (args[0].equalsIgnoreCase("givecrystals")) {
                if (cs.hasPermission("EpciSkyblock.givecrystals")) {
                    Player p = Bukkit.getPlayer(args[1]);
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p.getName()));
                    }
                    User u = User.getbyPlayer(p);
                    if (u.getIsland() != null) {
                        EpicSkyBlock.getSkyblock.sendTitle(p, "&e&lYou have recieved " + args[2] + " Island Crystals.", 20, 40, 20);
                        Island island = u.getIsland();
                        island.addCrystals(Integer.parseInt(args[2]));
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("GiveCrystals").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName()).replace("%amount%", args[2])));
                        return true;
                    }
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                    return true;
                }
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
        } catch (Exception e) {
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + " &e/is givecrystals <Playername> <Amount>."));
            return true;
        }
        try {
            if (args[0].equalsIgnoreCase("visit")) {
                Player p = Bukkit.getPlayer(args[1]);
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    Island island = u.getIsland();
                    ((Player) cs).teleport(island.gethome());
                    EpicSkyBlock.getSkyblock.sendIslandBoarder((Player) cs);
                    return true;
                }
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                return true;
            }
        } catch (Exception e) {
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + " &e/is visit <Playername>."));
            return true;
        }
        if (args[0].equalsIgnoreCase("recalculate")) {
            if (cs.hasPermission("EpciSkyblock.recalculate")) {
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + " Recalculating Island Top."));
                long ms = System.currentTimeMillis();
                EpicSkyBlock.getSkyblock.calculateworth();
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + " Completed took " + (System.currentTimeMillis() - ms) + "ms."));
            }
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
            return true;
        }
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (args[0].equalsIgnoreCase("top")) {
                HashMap<String, Integer> worth = new HashMap<>();
                for (Island island : IslandManager.getIslands()) {
                    worth.put(island.getownername(), island.getLevel());
                }
                // Order HashMap
                EpicSkyBlock.getSkyblock.sendCenteredMessage(p, "&8&m----------------&7 &8< &eRichest Islands &8> &8&m----------------&7");
                int i = 1;
                Map<String, Integer> sorted = worth.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

                for (String name : sorted.keySet()) {
                    if (!name.equals("")) {
                        if (i == 10) return true;
                        p.sendMessage("#" + i + ". " + ChatColor.GRAY + name + " - " + ChatColor.YELLOW + "$" + sorted.get(name));
                        i++;
                    }
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("regen")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    User.getbyPlayer(p).getIsland().regen();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("RegeneratingIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("warps") || args[0].equalsIgnoreCase("warp")) {
                p.openInventory(WarpGUI.inv(User.getbyPlayer(p).getIsland()));
                return true;
            }
            if (args[0].equalsIgnoreCase("setwarp")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    Island is = User.getbyPlayer(p).getIsland();
                    if (is.getWarp1() == null) {
                        is.setWarp1(p.getLocation());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("WarpSet").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (is.getWarp2() == null && is.getWarpCount() > 1) {
                        is.setWarp2(p.getLocation());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("WarpSet").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (is.getWarp3() == null && is.getWarpCount() > 1) {
                        is.setWarp3(p.getLocation());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("WarpSet").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (is.getWarp4() == null && is.getWarpCount() > 2) {
                        is.setWarp4(p.getLocation());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("WarpSet").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (is.getWarp5() == null && is.getWarpCount() > 2) {
                        is.setWarp5(p.getLocation());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("WarpSet").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoWarps").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("crystals")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CrystalAmount").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%amount%", u.getIsland().getCrystals().toString())));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("bypass")) {
                if (p.hasPermission("EpicSkyblock.bypass")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p.getName()));
                    }
                    User.getbyPlayer(p).setBypass(!User.getbyPlayer(p).getBypass());
                    if (User.getbyPlayer(p).getBypass()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("BypassEnabled").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("BypassDisabled").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
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
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("FlyDisabled").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        } else {
                            p.setAllowFlight(true);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("FlyEnabled").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("chat")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    User.getbyPlayer(p).setChat(!User.getbyPlayer(p).getChat());
                    if (User.getbyPlayer(p).getChat()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("ChatEnabled").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("ChatDisabled").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
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
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
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
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
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
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    if (User.getbyPlayer(p).getIsland().getownername().equals(p.getName())) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("TransferOwnership").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    User.getbyPlayer(p).getIsland().removeUser(p.getName());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("LeftIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
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
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("SetHome").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("MustBeOnIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("OwnerOnly").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("delete")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    Island island = User.getbyPlayer(p).getIsland();
                    if (island.getownername().equals(p.getName())) {
                        IslandManager.deleteIsland(p);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("IslandDeleted").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("OwnerOnly").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("home")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() == null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                } else {
                    User.getbyPlayer(p).setFalldmg(true);
                    Bukkit.getScheduler().scheduleAsyncDelayedTask(EpicSkyBlock.getSkyblock, () -> User.getbyPlayer(p).setFalldmg(false), 20);
                    p.teleport(User.getbyPlayer(p).getIsland().gethome());
                    EpicSkyBlock.getSkyblock.sendIslandBoarder(p);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("TeleportToIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("list")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                User user = User.getbyPlayer(p);
                if (user.getIsland() != null) {
                    p.openInventory(Members.inv(user.getIsland()));
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.add(new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() == null) {
                    IslandManager.createIsland(p);
                    EpicSkyBlock.getSkyblock.sendTitle(p, "&e&lIsland Created", 20, 40, 20);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("IslandCreated").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("AlreadyHaveAnIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
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
                if (p.hasPermission("EpicSkyblock.reload")) {
                    ConfigManager.getInstance().reloadConfig();
                    ConfigManager.getInstance().reloadMessages();
                    EpicSkyBlock.getSkyblock.reloadConfig();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("Reloaded").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
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
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotFound").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player.getName()));
                    }
                    if (User.getbyPlayer(p).getIsland() != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("AlreadyHaveAnIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (User.getbyPlayer(player).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerDoesntHaveIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player.getName())));
                        return true;
                    }
                    if (User.getbyPlayer(p).getInvites().contains(User.getbyPlayer(player).getIsland().getownername())) {
                        if (User.getbyPlayer(player).getIsland().getPlayers().size() >= EpicSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members." + User.getbyPlayer(player).getIsland().getMemberCount())) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("MaximumPlayers").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("JoinedIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        User.getbyPlayer(player).getIsland().addUser(p.getName());
                        p.teleport(User.getbyPlayer(p).getIsland().gethome());
                        for (String pla : User.getbyPlayer(player).getIsland().getPlayers()) {
                            Player i = Bukkit.getPlayer(pla);
                            if (i != null) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerHasJoinedIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                            }

                        }
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoInvite").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &e/is join <PlayerName>."));
                e.printStackTrace();
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
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (User.getbyPlayer(p).getIsland().getownername().equalsIgnoreCase(p.getName())) {
                        if (p.getName().equalsIgnoreCase(player)) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CannotGiveYourselfLeader").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        }
                        if (User.getbyPlayer(p).getIsland().getPlayers().contains(player)) {
                            User.getbyPlayer(p).getIsland().setowner(player);
                            if (Bukkit.getPlayer(player) != null) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("YouHaveBeenGivenOwner").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("TransferedOwner").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player)));
                            return true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotInIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player)));
                            return true;
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("OwnerOnly").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &e/is leader <PlayerName>."));
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
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (p.getName().equalsIgnoreCase(player)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CannotKickYourself").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (User.getbyPlayer(p).getIsland().getownername().equalsIgnoreCase(player)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CannotKickTheOwner").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
                    if (User.getbyPlayer(p).getIsland().getPlayers().contains(player)) {
                        User.getbyPlayer(player).getIsland().getPlayers().remove(player);
                        User.getbyPlayer(player).setIsland(null);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("KickedPlayer").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player)));
                        if (Bukkit.getPlayer(player) != null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("Kicked").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                        }
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotInIsland").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player)));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &e/is kick <PlayerName>."));
                return true;
            }
            try {
                if (args[0].equalsIgnoreCase("deinvite") || args[0].equalsIgnoreCase("uninvite")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.add(new User(p.getName()));
                    }
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotFound").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player.getName()));
                    }
                    if (User.getbyPlayer(p).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (p == player) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CannotInviteYourself").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (User.getbyPlayer(player).getInvites().contains(User.getbyPlayer(p).getIsland().getownername())) {
                        User.getbyPlayer(player).getInvites().remove(User.getbyPlayer(p).getIsland().getownername());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("InviteRevoked").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player.getName())));
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoInvites").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &e/is uninvite <PlayerName>."));
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
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotFound").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.add(new User(player.getName()));
                    }
                    if (User.getbyPlayer(p).getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (p == player) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CannotInviteYourself").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (User.getbyPlayer(player).getInvites().contains(User.getbyPlayer(p).getIsland().getownername())) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("HasInvite").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    User.getbyPlayer(player).getInvites().add(User.getbyPlayer(p).getIsland().getownername());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("InviteSent").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player.getName())));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("Invited").replace("%prefix%", EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                    return true;
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', EpicSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &e/is invite <PlayerName>."));
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