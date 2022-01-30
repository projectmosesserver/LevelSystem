package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.ItemData;
import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerData data = getPlayerData(player.getUniqueId());
        if (data == null) {
            data = new PlayerData(player);
        }
        LevelSystem.plugin.statusManager.reJoinUpdateStatus(player);
    }

    public PlayerData getPlayerData(UUID uuid) {
        for (PlayerData data : PlayerData.data) {
            if (data.getUuid().equals(uuid))
                return data;
        }
        return null;
    }
}
