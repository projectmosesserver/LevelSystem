package info.ahaha.levelsystem.util;

import info.ahaha.levelsystem.LevelSystem;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;

public class DataManager {

    private LevelSystem plugin;
    private FileConfiguration config = null;
    private File configfile = null;

    public DataManager(LevelSystem plugin) {
        this.plugin = plugin;
        saveDefaultConfig();
    }

    public void reloadConfig() {
        if (config == null)
            configfile = new File(plugin.getDataFolder(), "config.yml");

        config = YamlConfiguration.loadConfiguration(configfile);

        InputStream configstream = plugin.getResource("config.yml");

        if (configstream != null) {
            YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(configstream));
            config.setDefaults(configuration);
        }
    }

    public FileConfiguration getConfig() {
        if (config == null)
            reloadConfig();
        return config;
    }

    public void saveConfig() {
        if (config == null || configfile == null)
            return;
        try {
            getConfig().save(configfile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, configfile + "がありません！", e);
        }
    }

    public void saveDefaultConfig() {
        if (configfile == null)
            configfile = new File(plugin.getDataFolder(), "config.yml");
        if (!configfile.exists())
            plugin.saveResource("config.yml", false);
    }
}