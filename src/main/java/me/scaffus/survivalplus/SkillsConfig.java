package me.scaffus.survivalplus;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class SkillsConfig {
    private static File file;
    private static FileConfiguration configFile;

    public void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("SurvivalPlus").getDataFolder(), "skills.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        configFile = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return configFile;
    }

    public void save() {
        try {
            configFile.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reload() {
        configFile = YamlConfiguration.loadConfiguration(file);
    }
}
