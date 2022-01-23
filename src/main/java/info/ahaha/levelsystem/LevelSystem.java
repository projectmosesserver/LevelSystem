package info.ahaha.levelsystem;

import info.ahaha.levelsystem.listener.CommandListener;
import info.ahaha.levelsystem.listener.EntityKillListener;
import info.ahaha.levelsystem.listener.JoinListener;
import info.ahaha.levelsystem.util.DataManager;
import info.ahaha.levelsystem.util.MessageUtil;
import info.ahaha.levelsystem.util.StatusManager;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.logging.Logger;

public final class LevelSystem extends JavaPlugin {

    public static LevelSystem plugin;
    public MessageUtil message;
    public DataManager manager;
    public StatusManager statusManager;
    private static Economy econ = null;
    private static final Logger log = Logger.getLogger("Minecraft");

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        plugin = this;
        this.message = new MessageUtil(this);
        this.manager = new DataManager(this);

        Path path = Paths.get(String.valueOf(plugin.getDataFolder()));
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                getLogger().info(e.toString());
            }
        }

        this.statusManager = new StatusManager(manager);

        for (EntityType type : EntityType.values()){
            if (manager.getConfig().getConfigurationSection(type.name()) == null)
                continue;
            new EntityData(
                    type,
                    manager.getConfig().getDouble(type.name()+".DefaultExp"),
                    manager.getConfig().getDouble(type.name()+".Magnification"),
                    manager.getConfig().getDouble(type.name()+".DropMoney")
            );
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = getPlayerData(player.getUniqueId());
            if (data == null) {
                new PlayerData(player);
            }
        }
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new EntityKillListener(), this);
    }

    private PlayerData getPlayerData(UUID uuid) {
        for (PlayerData data : PlayerData.data) {
            if (data.getUuid().equals(uuid))
                return data;
        }
        return null;
    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
