package info.ahaha.levelsystem.runnable;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class CoolTimeRunnable implements Runnable {
    private final LevelSystem plugin;
    private final Map<UUID, BossBar> coolBar;

    public CoolTimeRunnable(LevelSystem plugin, Map<UUID, BossBar> coolBar) {
        this.plugin = plugin;
        this.coolBar = coolBar;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            BossBar bar = Bukkit.createBossBar(ChatColor.YELLOW + "Title" + ChatColor.GRAY + " | " + ChatColor.AQUA + "残り time", BarColor.BLUE, BarStyle.SOLID);

            if (!coolBar.containsKey(player.getUniqueId()))
                coolBar.put(player.getUniqueId(), bar);
            if (player.getPersistentDataContainer().has(plugin.getUsedSkillKey(), PersistentDataType.STRING)) {
                if (player.getPersistentDataContainer().has(plugin.getCoolTimeKey(), PersistentDataType.INTEGER)) {
                    Skill skill = plugin.skills.get(SkillType.valueOf(player.getPersistentDataContainer().get(plugin.getUsedSkillKey(), PersistentDataType.STRING)));
                    int defTime = skill.getCoolTime();
                    int coolTime = player.getPersistentDataContainer().get(plugin.getCoolTimeKey(), PersistentDataType.INTEGER);
                    BossBar b = coolBar.get(player.getUniqueId());

                    if (coolTime - 1 < 0) {
                        player.getPersistentDataContainer().remove(plugin.getCoolTimeKey());
                        player.getPersistentDataContainer().remove(plugin.getUsedSkillKey());
                        plugin.message.sendMessage(player, ChatColor.YELLOW + skill.getName() + ChatColor.GREEN + " が使用可能になりました！");
                        b.setVisible(false);
                        b.removePlayer(player);
                        coolBar.remove(player.getUniqueId());
                        continue;
                    }
                    b.setTitle(ChatColor.YELLOW + skill.getName() + ChatColor.GRAY + " | " + ChatColor.AQUA + plugin.secToTime(coolTime));
                    b.setProgress(coolTime / Double.parseDouble(String.valueOf(defTime)));
                    if (!b.getPlayers().contains(player))
                        b.addPlayer(player);
                    if (!b.isVisible())
                        b.setVisible(true);
                    player.getPersistentDataContainer().set(plugin.getCoolTimeKey(), PersistentDataType.INTEGER, coolTime - 1);
                }
            }
        }
    }
}
