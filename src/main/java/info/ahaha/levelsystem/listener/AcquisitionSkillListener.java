package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.event.PlayerAcquisitionSkillEvent;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AcquisitionSkillListener implements Listener {

    @EventHandler
    public void onAcquisition(PlayerAcquisitionSkillEvent e){
        LevelSystem.plugin.message.sendMessage(e.getPlayer(), ChatColor.YELLOW+e.getSkill().getName()+ChatColor.GREEN+" を習得しました！");
    }
}
