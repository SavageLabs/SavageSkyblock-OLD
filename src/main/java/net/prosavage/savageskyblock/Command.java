package net.prosavage.savageskyblock;

import net.prosavage.savageskyblock.Inventories.*;
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
    private static SavageSkyBlock plugin;

    public Command(SavageSkyBlock pl) {
        plugin = pl;
    }

    public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (cs instanceof Player) {
                Player p = (Player) cs;
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() == null) {
                    for (String message : ConfigManager.getInstance().getConfig().getStringList("help")) {
                        if (message.contains("%centered%")) {
                            plugin.sendCenteredMessage(cs, ChatColor.translateAlternateColorCodes('&', message.replace("%centered%", "")));
                        } else {
                            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        }
                    }
                    return true;
                } else {
                    u.setFalldmg(true);
                    Bukkit.getScheduler().scheduleAsyncDelayedTask(SavageSkyBlock.getSkyblock, () -> u.setFalldmg(false), 20);
                    u.getIsland().teleporthome(p);
                    SavageSkyBlock.getSkyblock.sendIslandBoarder(p);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("TeleportToIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
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
                        User.users.put(p.getName(), new User(p.getName()));
                    }
                    User u = User.getbyPlayer(p);
                    if (u.getIsland() != null) {
                        SavageSkyBlock.getSkyblock.sendTitle(p, "&e&lYou have recieved " + args[2] + " Island Crystals.", 20, 40, 20);
                        Island island = u.getIsland();
                        island.addCrystals(Integer.parseInt(args[2]));
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("GiveCrystals").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName()).replace("%amount%", args[2])));
                        return true;
                    }
                    cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                    return true;
                }
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
        } catch (Exception e) {
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + " &e/is givecrystals <Playername> <Amount>."));
            return true;
        }
        try {
            if (args[0].equalsIgnoreCase("visit")) {
                Player p = Bukkit.getPlayer(args[1]);
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    Island island = u.getIsland();
                    ((Player) cs).teleport(island.gethome());
                    SavageSkyBlock.getSkyblock.sendIslandBoarder((Player) cs);
                    return true;
                }
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                return true;
            }
        } catch (Exception e) {
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + " &e/is visit <Playername>."));
            return true;
        }
        if (args[0].equalsIgnoreCase("recalculate")) {
            if (cs.hasPermission("EpciSkyblock.recalculate")) {
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + " Recalculating Island Top."));
                long ms = System.currentTimeMillis();
                SavageSkyBlock.getSkyblock.calculateworth();
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + " Completed took " + (System.currentTimeMillis() - ms) + "ms."));
            }
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
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
                SavageSkyBlock.getSkyblock.sendCenteredMessage(p, "&8&m----------------&7 &8< &eRichest Islands &8> &8&m----------------&7");
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
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    u.getIsland().regen();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("RegeneratingIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("warps") || args[0].equalsIgnoreCase("warp")) {
                p.openInventory(WarpGUI.inv(User.getbyPlayer(p).getIsland()));
                return true;
            }
            if (args[0].equalsIgnoreCase("setwarp")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    Island is = User.getbyPlayer(p).getIsland();
                    if (is.getWarp1() == null) {
                        is.setWarp1(p.getLocation());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("WarpSet").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (is.getWarp2() == null && is.getWarpCount() > 1) {
                        is.setWarp2(p.getLocation());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("WarpSet").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (is.getWarp3() == null && is.getWarpCount() > 1) {
                        is.setWarp3(p.getLocation());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("WarpSet").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (is.getWarp4() == null && is.getWarpCount() > 2) {
                        is.setWarp4(p.getLocation());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("WarpSet").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (is.getWarp5() == null && is.getWarpCount() > 2) {
                        is.setWarp5(p.getLocation());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("WarpSet").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoWarps").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("crystals")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CrystalAmount").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%amount%", u.getIsland().getCrystals().toString())));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("bypass")) {
                if (p.hasPermission("EpicSkyblock.bypass")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.put(p.getName(), new User(p.getName()));
                    }
                    User u = User.getbyPlayer(p);
                    u.setBypass(!u.getBypass());
                    if (u.getBypass()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("BypassEnabled").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("BypassDisabled").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("fly")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    if (u.getIsland().getFlyBoosterActive()) {
                        if (p.getAllowFlight()) {
                            p.setAllowFlight(false);
                            p.setFlying(false);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("FlyDisabled").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        } else {
                            p.setAllowFlight(true);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("FlyEnabled").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("chat")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    u.setChat(!u.getChat());
                    if (u.getChat()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("ChatEnabled").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("ChatDisabled").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("upgrade") || args[0].equalsIgnoreCase("upgrades")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    p.openInventory(UpgradesGUI.inv(u.getIsland()));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("boosters") || args[0].equalsIgnoreCase("booster")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    p.openInventory(BoostersGUI.inv(u.getIsland()));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("missions") || args[0].equalsIgnoreCase("mission")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() != null) {
                    p.openInventory(MissionsGUI.inv(User.getbyPlayer(p).getIsland()));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    if (u.getIsland().getownername().equals(p.getName())) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("TransferOwnership").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    u.getIsland().removeUser(p.getName());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("LeftIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("sethome")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    if (u.getIsland().getownername().equals(p.getName())) {
                        if (IslandManager.getislandviablock(p.getLocation().getBlock()) == u.getIsland()) {
                            u.getIsland().setHome(p.getLocation());
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("SetHome").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("MustBeOnIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("OwnerOnly").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("delete")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    Island island = u.getIsland();
                    if (island.getownername().equals(p.getName())) {
                        IslandManager.deleteIsland(p);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("IslandDeleted").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("OwnerOnly").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("home")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User u = User.getbyPlayer(p);
                if (u.getIsland() == null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                } else {
                    u.setFalldmg(true);
                    Bukkit.getScheduler().scheduleAsyncDelayedTask(SavageSkyBlock.getSkyblock, () ->u.setFalldmg(false), 20);
                    u.getIsland().teleporthome(p);
                    SavageSkyBlock.getSkyblock.sendIslandBoarder(p);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("TeleportToIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("list")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                User user = User.getbyPlayer(p);
                if (user.getIsland() != null) {
                    p.openInventory(Members.inv(user.getIsland()));
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("create")) {
                if (User.getbyPlayer(p) == null) {
                    User.users.put(p.getName(), new User(p.getName()));
                }
                if (User.getbyPlayer(p).getIsland() == null) {
                    IslandManager.createIsland(p);
                    SavageSkyBlock.getSkyblock.sendIslandBoarder(p);
                    SavageSkyBlock.getSkyblock.sendTitle(p, "&e&lIsland Created", 20, 40, 20);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("IslandCreated").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("AlreadyHaveAnIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("about") ) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPlugin Name : &eSavageSkyBlock"));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPlugin Version : &e" + plugin.getDescription().getVersion()));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPlugin Author : &ePeaches_MLG"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("EpicSkyblock.reload")) {
                    ConfigManager.getInstance().reloadConfig();
                    ConfigManager.getInstance().reloadMessages();
                    SavageSkyBlock.getSkyblock.reloadConfig();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("Reloaded").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
            try {
                if (args[0].equalsIgnoreCase("join")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (User.getbyPlayer(p) == null) {
                        User.users.put(p.getName(), new User(p.getName()));
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.put(player.getName(), new User(player.getName()));
                    }
                    User u = User.getbyPlayer(p);
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotFound").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    User u1 = User.getbyPlayer(player);
                    if (u.getIsland() != null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("AlreadyHaveAnIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (u1.getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerDoesntHaveIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player.getName())));
                        return true;
                    }
                    if (u.getInvites().contains(u.getIsland().getownername())) {
                        if (u1.getIsland().getPlayers().size() >= SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members." + u1.getIsland().getMemberCount())) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("MaximumPlayers").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        }
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("JoinedIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        u1.getIsland().addUser(p.getName());
                        u.getIsland().teleporthome(p);
                        for (String pla : u1.getIsland().getPlayers()) {
                            Player i = Bukkit.getPlayer(pla);
                            if (i != null) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerHasJoinedIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                            }

                        }
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoInvite").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &e/is join <PlayerName>."));
                e.printStackTrace();
                return true;
            }
            try {
                if (args[0].equalsIgnoreCase("leader") || args[0].equalsIgnoreCase("owner")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.put(p.getName(), new User(p.getName()));
                    }
                    User u = User.getbyPlayer(p);
                    String player = args[1];
                    if (u.getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (u.getIsland().getownername().equalsIgnoreCase(p.getName())) {
                        if (p.getName().equalsIgnoreCase(player)) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CannotGiveYourselfLeader").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        }
                        if (u.getIsland().getPlayers().contains(player)) {
                            u.getIsland().setowner(player);
                            if (Bukkit.getPlayer(player) != null) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("YouHaveBeenGivenOwner").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("TransferedOwner").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player)));
                            return true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotInIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player)));
                            return true;
                        }
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("OwnerOnly").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &e/is leader <PlayerName>."));
                return true;
            }
            try {
                if (args[0].equalsIgnoreCase("kick")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.put(p.getName(), new User(p.getName()));
                    }
                    User u = User.getbyPlayer(p);
                    String player = args[1];
                    if (User.getbyPlayer(player) == null) {
                        User.users.put(player, new User(player));
                    }
                    User user = User.getbyPlayer(player);
                    if (u.getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (p.getName().equalsIgnoreCase(player)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CannotKickYourself").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (u.getIsland().getownername().equalsIgnoreCase(player)) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CannotKickTheOwner").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
                    if (u.getIsland().getPlayers().contains(player)) {
                        user.getIsland().getPlayers().remove(player);
                        user.setIsland(null);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("KickedPlayer").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player)));
                        if (Bukkit.getPlayer(player) != null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("Kicked").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                        }
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotInIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player)));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &e/is kick <PlayerName>."));
                return true;
            }
            try {
                if (args[0].equalsIgnoreCase("deinvite") || args[0].equalsIgnoreCase("uninvite")) {
                    if (User.getbyPlayer(p) == null) {
                        User.users.put(p.getName(), new User(p.getName()));
                    }
                    User u = User.getbyPlayer(p);
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotFound").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (User.getbyPlayer(player) == null) {
                        User.users.put(player.getName(), new User(player.getName()));
                    }
                    User user = User.getbyPlayer(player);
                    if (u.getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (p == player) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CannotInviteYourself").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (user.getInvites().contains(User.getbyPlayer(p).getIsland().getownername())) {
                        user.getInvites().remove(User.getbyPlayer(p).getIsland().getownername());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("InviteRevoked").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player.getName())));
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoInvites").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &e/is uninvite <PlayerName>."));
                return true;
            }
            try {
                if (args[0].equalsIgnoreCase("invite")) {
                    Player player = Bukkit.getPlayer(args[1]);
                    if (User.getbyPlayer(p) == null) {
                        User.users.put(p.getName(), new User(p.getName()));
                    }
                    User u = User.getbyPlayer(p);
                    if (User.getbyPlayer(player) == null) {
                        User.users.put(player.getName(), new User(player.getName()));
                    }
                    User user = User.getbyPlayer(player);
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotFound").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (u.getIsland() == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (p == player) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CannotInviteYourself").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    if (user.getInvites().contains(User.getbyPlayer(p).getIsland().getownername())) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("HasInvite").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    User.getbyPlayer(player).getInvites().add(User.getbyPlayer(p).getIsland().getownername());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("InviteSent").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player.getName())));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("Invited").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                    return true;
                }
            } catch (Exception e) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + "  &e/is invite <PlayerName>."));
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