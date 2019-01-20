package com.peaches.epicskyblock.Missions;

import com.peaches.epicskyblock.API.IslandMissionCompleteEvent;
import com.peaches.epicskyblock.Island;
import com.peaches.epicskyblock.User;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;

import java.util.ArrayList;

public class Missions implements Listener {

    public static Missions Instance;

    public ArrayList<Mission> mission1 = new ArrayList<>();
    public ArrayList<Mission> mission2 = new ArrayList<>();
    public ArrayList<Mission> mission3 = new ArrayList<>();

    public Enchanter enchanter = new Enchanter();
    public Farmer farmer = new Farmer();
    public Fisherman fisherman = new Fisherman();
    public Miner miner = new Miner();
    public Mob_Hunter mob_hunter = new Mob_Hunter();
    public Soldier soldier = new Soldier();
    public Treasure_Hunter treasure_hunter = new Treasure_Hunter();

    public Missions() {
        Instance = this;
        mission1.add(farmer);
        mission1.add(miner);
        mission2.add(soldier);
        mission2.add(mob_hunter);
        mission3.add(treasure_hunter);
        mission3.add(enchanter);
        mission3.add(fisherman);
    }

    @EventHandler
    public void onentitykill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            if (User.getbyPlayer(e.getEntity().getKiller()) == null) {
                User.users.add(new User(e.getEntity().getKiller().getName()));
            }
            User u = User.getbyPlayer(e.getEntity().getKiller());
            if (u.getIsland() != null) {
                Island island = u.getIsland();
                if (island.getMission2().getName().equalsIgnoreCase("Mob Hunter")) {
                    if (island.getMission2Data() < island.getMission2().getTotal()) {
                        island.setMission2Data(island.getMission2Data() + 1);
                    }
                    if (island.getMission2Data() == island.getMission2().getTotal()) {
                        if (!island.getMission2Complete()) {
                            island.addCrystals(2);
                            island.setMission2Complete(true);
                            Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(island, 2, "Mob Hunter"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onkill(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() != null) {
            if (User.getbyPlayer(e.getEntity().getKiller()) == null) {
                User.users.add(new User(e.getEntity().getKiller().getName()));
            }
            User u = User.getbyPlayer(e.getEntity().getKiller());
            if (u.getIsland() != null) {
                Island island = u.getIsland();
                if (island.getMission2().getName().equalsIgnoreCase("Soldier")) {
                    if (island.getMission2Data() < island.getMission2().getTotal()) {
                        island.setMission2Data(island.getMission2Data() + 1);
                    }
                    if (island.getMission2Data() == island.getMission2().getTotal()) {
                        if (!island.getMission2Complete()) {
                            island.addCrystals(2);
                            island.setMission2Complete(true);
                            Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(island, 2, "Soldier"));
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void onxp(PlayerExpChangeEvent e) {
        if (User.getbyPlayer(e.getPlayer()) == null) {
            User.users.add(new User(e.getPlayer().getName()));
        }
        User u = User.getbyPlayer(e.getPlayer());
        if (u.getIsland() != null) {
            Island island = u.getIsland();
            if (island.getMission3().getName().equalsIgnoreCase("Treasure Hunter")) {
                if (island.getMission3Data() < island.getMission3().getTotal()) {
                    island.setMission3Data(island.getMission3Data() + e.getAmount());
                }
                if (island.getMission3Data() == island.getMission3().getTotal()) {
                    if (!island.getMission3Complete()) {
                        island.addCrystals(5);
                        island.setMission3Complete(true);
                        Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(island, 5, "Treasure Hunter"));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onfish(PlayerFishEvent e) {
        if (e.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            if (User.getbyPlayer(e.getPlayer()) == null) {
                User.users.add(new User(e.getPlayer().getName()));
            }
            User u = User.getbyPlayer(e.getPlayer());
            if (u.getIsland() != null) {
                Island island = u.getIsland();
                if (island.getMission3().getName().equalsIgnoreCase("Fisherman")) {
                    if (island.getMission3Data() < island.getMission3().getTotal()) {
                        island.setMission3Data(island.getMission3Data() + 1);
                    }
                    if (island.getMission3Data() == island.getMission3().getTotal()) {
                        if (!island.getMission3Complete()) {
                            island.addCrystals(5);
                            island.setMission3Complete(true);
                            Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(island, 5, "Fisherman"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onbreak(BlockBreakEvent e) {
        if (User.getbyPlayer(e.getPlayer()) == null) {
            User.users.add(new User(e.getPlayer().getName()));
        }
        User u = User.getbyPlayer(e.getPlayer());
        if (u.getIsland() != null) {
            Island island = u.getIsland();
            if (island.getMission1().getName().equalsIgnoreCase("Farmer")) {
                if (e.getBlock().getType().equals(Material.SUGAR_CANE_BLOCK)) {
                    if (island.getMission1Data() < island.getMission1().getTotal()) {
                        island.setMission1Data(island.getMission1Data() + 1);
                    }
                    if (island.getMission1Data() == island.getMission1().getTotal()) {
                        if (!island.getMission1Complete()) {
                            island.addCrystals(1);
                            island.setMission1Complete(true);
                            Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(island, 1, "Farmer"));
                        }
                    }
                }
            }
            if (island.getMission1().getName().equalsIgnoreCase("Miner")) {
                if (e.getBlock().getType().name().endsWith("ORE")) {
                    if (island.getMission1Data() < island.getMission1().getTotal()) {
                        island.setMission1Data(island.getMission1Data() + 1);
                    }
                    if (island.getMission1Data() == island.getMission1().getTotal()) {
                        if (!island.getMission1Complete()) {
                            island.addCrystals(1);
                            island.setMission1Complete(true);
                            Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(island, 1, "Miner"));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onenchant(EnchantItemEvent e) {
        if (User.getbyPlayer(e.getEnchanter()) == null) {
            User.users.add(new User(e.getEnchanter().getName()));
        }
        User u = User.getbyPlayer(e.getEnchanter());
        if (u.getIsland() != null) {
            Island island = u.getIsland();
            if (island.getMission3().getName().equalsIgnoreCase("Enchanter")) {
                if (island.getMission3Data() < island.getMission3().getTotal()) {
                    island.setMission3Data(island.getMission3Data() + 1);
                }
                if (island.getMission3Data() == island.getMission3().getTotal()) {
                    if (!island.getMission3Complete()) {
                        island.addCrystals(3);
                        island.setMission3Complete(true);
                        Bukkit.getPluginManager().callEvent(new IslandMissionCompleteEvent(island, 3, "Enchanter"));
                    }
                }
            }
        }
    }
}
