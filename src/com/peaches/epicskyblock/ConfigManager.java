package com.peaches.epicskyblock;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

public class ConfigManager {

    private static final ConfigManager instance = new ConfigManager();
    private FileConfiguration config;
    private File configfile;
    private File schematicFile;
    private YamlConfiguration Messages;
    private File mfile;

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
            if (!this.configfile.exists()) {
                p.saveResource("config.yml", false);
            }
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

        this.mfile = new File(p.getDataFolder(), "Messages.yml");
        if (p.getResource("Messages.yml") != null) {
            if (!this.mfile.exists()) {
                p.saveResource("Messages.yml", false);
            }
        }

        this.Messages = YamlConfiguration.loadConfiguration(this.mfile);

    }

    public FileConfiguration getConfig() {
        return this.config;
    }

    public void reloadConfig() {
        this.config = YamlConfiguration.loadConfiguration(this.configfile);
    }

    public void reloadMessages() {
        this.Messages = YamlConfiguration.loadConfiguration(this.mfile);
    }

    public File getSchematicFile() {
        return this.schematicFile;
    }

    public YamlConfiguration getMessages() {
        return Messages;
    }
}
