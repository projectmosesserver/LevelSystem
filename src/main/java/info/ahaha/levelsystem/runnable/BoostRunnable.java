package info.ahaha.levelsystem.runnable;

import info.ahaha.levelsystem.LevelSystem;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Map;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class BoostRunnable implements Runnable {

    private LevelSystem plugin;
    private Map<UUID, BossBar> bars;

    public BoostRunnable(LevelSystem plugin, Map<UUID, BossBar> bars) {
        this.plugin = plugin;
        this.bars = bars;
    }

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            BossBar bar = Bukkit.createBossBar(ChatColor.YELLOW + "Title" + ChatColor.GRAY + " | " + ChatColor.AQUA + "残り time", BarColor.GREEN, BarStyle.SOLID);
            if (!bars.containsKey(player.getUniqueId()))
                bars.put(player.getUniqueId(), bar);
            if (player.getPersistentDataContainer().has(plugin.getTimeKey(), PersistentDataType.INTEGER)) {
                if (player.getPersistentDataContainer().has(plugin.getBoostLv1Key(), PersistentDataType.INTEGER)) {
                    int i = player.getPersistentDataContainer().get(plugin.getBoostLv1Key(), PersistentDataType.INTEGER);
                    int timer = player.getPersistentDataContainer().get(plugin.getTimeKey(), PersistentDataType.INTEGER);
                    BossBar b = bars.get(player.getUniqueId());
                    if (i - 1 < 0) {
                        player.getPersistentDataContainer().remove(plugin.getBoostLv1Key());
                        player.getPersistentDataContainer().remove(plugin.getTimeKey());
                        player.sendMessage(ChatColor.GREEN + "EXPBoost Lv1 の効果が切れました！");
                        b.setVisible(false);
                        bars.remove(player.getUniqueId());
                        continue;
                    }
                    b.setTitle(ChatColor.YELLOW + "EXPBoost Lv1" + ChatColor.GRAY + " | " + ChatColor.AQUA + plugin.secToTime(i));
                    b.setProgress(i / Double.parseDouble(String.valueOf(timer)));
                    if (b.getPlayers().size() == 0) {
                        b.addPlayer(player);
                    }
                    if (!b.isVisible())
                        b.setVisible(true);
                    player.getPersistentDataContainer().set(plugin.getBoostLv1Key(), PersistentDataType.INTEGER, i - 1);
                } else if (player.getPersistentDataContainer().has(plugin.getBoostLv2Key(), PersistentDataType.INTEGER)) {
                    int i = player.getPersistentDataContainer().get(plugin.getBoostLv2Key(), PersistentDataType.INTEGER);
                    int timer = player.getPersistentDataContainer().get(plugin.getTimeKey(), PersistentDataType.INTEGER);
                    getLogger().info("lv2runnable");
                    BossBar b = bars.get(player.getUniqueId());
                    if (i - 1 < 0) {
                        player.getPersistentDataContainer().remove(plugin.getBoostLv2Key());
                        player.getPersistentDataContainer().remove(plugin.getTimeKey());
                        player.sendMessage(ChatColor.GREEN + "EXPBoost Lv2 の効果が切れました！");
                        b.setVisible(false);
                        bars.remove(player.getUniqueId());
                        continue;
                    }
                    b.setTitle(ChatColor.YELLOW + "EXPBoost Lv2" + ChatColor.GRAY + " | " + ChatColor.AQUA + plugin.secToTime(i));
                    b.setProgress(i / Double.parseDouble(String.valueOf(timer)));
                    if (b.getPlayers().size() == 0) {
                        b.addPlayer(player);
                    }
                    if (!b.isVisible())
                        b.setVisible(true);
                    player.getPersistentDataContainer().set(plugin.getBoostLv2Key(), PersistentDataType.INTEGER, i - 1);
                } else if (player.getPersistentDataContainer().has(plugin.getBoostLv3Key(), PersistentDataType.INTEGER)) {
                    int i = player.getPersistentDataContainer().get(plugin.getBoostLv3Key(), PersistentDataType.INTEGER);
                    int timer = player.getPersistentDataContainer().get(plugin.getTimeKey(), PersistentDataType.INTEGER);
                    BossBar b = bars.get(player.getUniqueId());
                    if (i - 1 < 0) {
                        player.getPersistentDataContainer().remove(plugin.getBoostLv3Key());
                        player.getPersistentDataContainer().remove(plugin.getTimeKey());
                        player.sendMessage(ChatColor.GREEN + "EXPBoost Lv3 の効果が切れました！");
                        b.setVisible(false);
                        bars.remove(player.getUniqueId());
                        continue;
                    }
                    b.setTitle(ChatColor.YELLOW + "EXPBoost Lv3" + ChatColor.GRAY + " | " + ChatColor.AQUA + plugin.secToTime(i));
                    b.setProgress(i / Double.parseDouble(String.valueOf(timer)));
                    if (b.getPlayers().size() == 0) {
                        b.addPlayer(player);
                    }
                    if (!b.isVisible())
                        b.setVisible(true);
                    player.getPersistentDataContainer().set(plugin.getBoostLv3Key(), PersistentDataType.INTEGER, i - 1);
                }
            }
        }
    }
}

