package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import info.ahaha.levelsystem.gui.GUI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import static info.ahaha.levelsystem.LevelSystem.getEcon;

public class CommandListener implements Listener {

    @EventHandler
    public void onCommand(AsyncPlayerChatEvent e) {
        if (e.getMessage().equalsIgnoreCase("!level")) {
            e.setCancelled(true);
            Player player = e.getPlayer();
            for (PlayerData data : PlayerData.data) {
                if (data.getUuid().equals(e.getPlayer().getUniqueId())) {
                    player.sendMessage(
                            ChatColor.GRAY + "----------+ " + ChatColor.GOLD + "Information" + ChatColor.GRAY + " +-------------",
                            ChatColor.YELLOW + "Level " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getLevel(),
                            ChatColor.YELLOW + "SkillPoint " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getSkillPoint(),
                            ChatColor.YELLOW + "Money " + ChatColor.GRAY + ": " + ChatColor.AQUA + (int) data.getMoney() + " " + getEcon().currencyNameSingular(),
                            ChatColor.YELLOW + "HP " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getHp(),
                            ChatColor.YELLOW + "ATK " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getAtk(),
                            ChatColor.YELLOW + "DEF " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getDef(),
                            ChatColor.YELLOW + "SPEED " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getSpeed(),
                            ChatColor.YELLOW + "KnockBackDef " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getKnockBack(),
                            ChatColor.YELLOW + "NextLevelUp " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getRemainingExp(),
                            ChatColor.GRAY + "------------------------------------"
                    );
                    break;
                }
            }
        }else if (e.getMessage().equalsIgnoreCase("!skill")){
            e.setCancelled(true);
            GUI gui = new GUI(e.getPlayer());
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().openInventory(gui.getSkillMenu(e.getPlayer()));
                }
            }.runTask(LevelSystem.plugin);
        }
    }
}
