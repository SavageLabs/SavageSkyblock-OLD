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


    //TODO: Rewrite this horror into modular classes.
    public boolean onCommand(CommandSender cs, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length == 0) {
            if (cs instanceof Player) {
                Player p = (Player) cs;
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
                    u.getIsland().teleportHome(p);
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
                if (cs.hasPermission("savageskyblock.givecrystals")) {
                    Player p = Bukkit.getPlayer(args[1]);
                    User u = User.getbyPlayer(p);
                    if (u.getIsland() != null) {
                        SavageSkyBlock.getSkyblock.sendTitle(p, "&e&lA gift from the gods", 20, 40, 20);
                        SavageSkyBlock.getSkyblock.sendSubTitle(p, "&7&lYou have received " + args[2] + " Island Crystals.", 20, 40, 20);
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
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    if (u.getIsland().isPublic()) {
                        Island island = u.getIsland();
                        ((Player) cs).teleport(island.gethome());
                        SavageSkyBlock.getSkyblock.sendIslandBoarder((Player) cs);
                        return true;
                    } else {
                        cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("IsPrivate").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
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
                SavageSkyBlock.getSkyblock.calculateWorth();
                cs.sendMessage(ChatColor.translateAlternateColorCodes('&', SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix") + " Completed took " + (System.currentTimeMillis() - ms) + "ms."));
            }
            cs.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoPermissions").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
            return true;
        }
        if (cs instanceof Player) {
            Player p = (Player) cs;
            if (args[0].equalsIgnoreCase("value")) {
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    HashMap<String, Integer> worth = new HashMap<>();
                    for (Island island : IslandManager.getIslands()) {
                        worth.put(island.getownername(), island.getLevel());
                    }

                    int i = 1;
                    Map<String, Integer> sorted = worth.entrySet().stream().sorted(Collections.reverseOrder(Map.Entry.comparingByValue())).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

                    for (String name : sorted.keySet()) {
                        if (!name.equals("")) {
                            if (name.equalsIgnoreCase(u.getIsland().getownername())) {
                                p.sendMessage("#" + i + ". " + ChatColor.GRAY + name + " - " + ChatColor.YELLOW + "$" + sorted.get(name));
                                return true;
                            }
                            i++;
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                }
                return true;
            }
            if (args[0].equalsIgnoreCase("public")) {
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    if (u.getIsland().getownername().equalsIgnoreCase(p.getName())) {
                        if (u.getIsland().isPublic()) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("AlreadyPublic").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        } else {
                            u.getIsland().setPublic(true);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("SetPublic").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("private")) {
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    if (u.getIsland().getownername().equalsIgnoreCase(p.getName())) {
                        if (!u.getIsland().isPublic()) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("AlreadyPrivate").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        } else {
                            u.getIsland().setPublic(false);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("SetPrivate").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        }
                    }
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
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
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    if (u.getIsland().regen()) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("RegeneratingIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("Cooldown").replace("%s%", u.getIsland().getRegencooldown() + "").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    }
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
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    Island is = u.getIsland();
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
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("CrystalAmount").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%amount%", u.getIsland().getCrystals().toString())));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("bypass")) {
                if (p.hasPermission("savageskyblock.bypass")) {
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
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    p.openInventory(UpgradesGUI.inv(u.getIsland()));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("boosters") || args[0].equalsIgnoreCase("booster")) {
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    p.openInventory(BoostersGUI.inv(u.getIsland()));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("missions") || args[0].equalsIgnoreCase("mission")) {
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    p.openInventory(MissionsGUI.inv(u.getIsland()));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                return true;
            }
            if (args[0].equalsIgnoreCase("leave")) {
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
                User u = User.getbyPlayer(p);
                if (u.getIsland() != null) {
                    if (u.getIsland().getownername().equals(p.getName())) {
                        if (IslandManager.getIslandViaBlock(p.getLocation().getBlock()) == u.getIsland()) {
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
                User u = User.getbyPlayer(p);
                if (u.getIsland() == null) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoIslandSelf").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                } else {
                    u.setFalldmg(true);
                    Bukkit.getScheduler().scheduleAsyncDelayedTask(SavageSkyBlock.getSkyblock, () -> u.setFalldmg(false), 20);
                    u.getIsland().teleportHome(p);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("TeleportToIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("list")) {
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
                User u = User.getbyPlayer(p);
                if (u.getIsland() == null) {
                    u.setFalldmg(true);
                    Bukkit.getScheduler().scheduleSyncDelayedTask(SavageSkyBlock.getSkyblock, () -> u.setFalldmg(false), 20);
                    IslandManager.createIsland(p);
                    SavageSkyBlock.getSkyblock.sendTitle(p, "&e&lIsland Created", 20, 40, 20);
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("IslandCreated").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("AlreadyHaveAnIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                    return true;
                }
            }
            if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("about")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPlugin Name : &eSavageSkyBlock"));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPlugin Version : &e" + plugin.getDescription().getVersion()));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6&lPlugin Author : &ePeaches_MLG"));
                return true;
            }
            if (args[0].equalsIgnoreCase("reload")) {
                if (p.hasPermission("savageskyblock.reload")) {
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
                    if (player != null) {
                        User user = User.getbyPlayer(p);
                        User u = User.getbyPlayer(player);
                        if (user.getIsland() != null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("AlreadyHaveAnIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        }
                        if (u.getIsland() == null) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerDoesntHaveIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player.getName())));
                            return true;
                        }
                        if (user.getInvites().contains(u.getIsland().getownername())) {
                            if (u.getIsland().getPlayers().size() >= SavageSkyBlock.getSkyblock.getConfig().getInt("Upgrades.Members." + u.getIsland().getMemberCount())) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("MaximumPlayers").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                                return true;
                            }
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("JoinedIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            u.getIsland().addUser(p.getName());
                            u.getIsland().teleportHome(p);
                            for (String pla : u.getIsland().getPlayers()) {
                                if (!pla.equals(p.getName())) {
                                    Player i = Bukkit.getPlayer(pla);
                                    if (i != null) {
                                        i.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerHasJoinedIsland").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
                                    }
                                }
                            }
                            return true;
                        } else {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("NoInvite").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                            return true;
                        }

                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotFound").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
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
                    User u = User.getbyPlayer(p);
                    String player = args[1];
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
                    User u = User.getbyPlayer(p);
                    Player player = Bukkit.getPlayer(args[1]);
                    if (player == null) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("PlayerNotFound").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
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
                    if (user.getInvites().contains(u.getIsland().getownername())) {
                        user.getInvites().remove(u.getIsland().getownername());
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
                    User u = User.getbyPlayer(p);
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
                    if (user.getInvites().contains(u.getIsland().getownername())) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("HasInvite").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix"))));
                        return true;
                    }
                    user.getInvites().add(u.getIsland().getownername());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("InviteSent").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", player.getName())));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', ConfigManager.getInstance().getMessages().getString("Invited").replace("%prefix%", SavageSkyBlock.getSkyblock.getConfig().getString("Options.Prefix")).replace("%player%", p.getName())));
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