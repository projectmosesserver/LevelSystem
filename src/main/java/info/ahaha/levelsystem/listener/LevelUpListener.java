package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.event.PlayerLevelUpEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LevelUpListener implements Listener {

    @EventHandler
    public void levelUp(PlayerLevelUpEvent e){
        LevelSystem.plugin.message.sendMessage(e.getPlayer(), "レベルが " + ChatColor.GOLD + e.getAfterLevel() + ChatColor.GREEN + " になりました！");
        LevelSystem.plugin.message.sendMessage(e.getPlayer(), "スキルポイントを " + ChatColor.GOLD + 1 + ChatColor.GREEN + " 獲得しました！");
    }
}
