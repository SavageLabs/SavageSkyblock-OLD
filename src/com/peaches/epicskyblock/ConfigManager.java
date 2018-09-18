package com.peaches.epicskyblock;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigManager {

    private static final ConfigManager instance = new ConfigManager();
    private Plugin p;
    private FileConfiguration config;
    private File configfile;
    private File schematicFile;

    public static ConfigManager getInstance() {
        return instance;
    }

    public void setup(Plugin p) {

        if (!p.getDataFolder().exists()) {
            p.getDataFolder().mkdir();
        }

        File file = new File(p.getDataFolder(), "schematics");
        if (!file.exists()) {
            file.mkdir();
        }

        this.configfile = new File(p.getDataFolder(), "config.yml");

        if (p.getResource("config.yml") != null) {
            p.saveResource("config.yml", false);
        }

        File schematicFolder = new File(p.getDataFolder(), "schematics");
        if (!schematicFolder.exists()) {
            schematicFolder.mkdir();
        }
        this.schematicFile = new File(schematicFolder, "island.schematic");

        if (!schematicFile.exists()) {
            if (p.getResource("schematics/island.schematic") != null) {
                p.saveResource("schematics/island.schematic", false);
            }
        }

        this.config = YamlConfiguration.loadConfiguration(this.configfile);

    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configfile);
    }

    public File getSchematicFile() {
        return this.schematicFile;
    }
}
