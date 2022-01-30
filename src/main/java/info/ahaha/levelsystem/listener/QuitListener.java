package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.bukkit.Bukkit.getLogger;

public class QuitListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent e){

        LevelSystem.plugin.statusManager.statusReset(e.getPlayer());

    }
}
