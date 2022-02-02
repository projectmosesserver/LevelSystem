package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.ItemData;
import info.ahaha.levelsystem.LevelSystem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class EXPBoostListener implements Listener {

    public static List<Player> players = new ArrayList<>();

    @EventHandler
    public void onBoost(PlayerItemConsumeEvent e) {
        Player player = e.getPlayer();
        if (e.getItem().getType() != Material.POTION) return;
        if (players.contains(player)) return;
        for (ItemData data : ItemData.data) {
            if (e.getItem().isSimilar(data.getItem())) {
                if (data.getItem().getItemMeta().getDisplayName().contains("Lv.1")) {
                    if (player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv2Key(), PersistentDataType.INTEGER)||
                            player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv3Key(), PersistentDataType.INTEGER)
                    ){
                        e.setCancelled(true);
                        LevelSystem.plugin.message.sendError(player,"すでにほかのブーストポーションを飲んでいるため使用できません！");
                        return;
                    }
                    int i = 0;
                    int t = 0;
                    if (player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv1Key(), PersistentDataType.INTEGER))
                        i = player.getPersistentDataContainer().get(LevelSystem.plugin.getBoostLv1Key(), PersistentDataType.INTEGER);
                    if (player.getPersistentDataContainer().has(LevelSystem.plugin.getTimeKey(), PersistentDataType.INTEGER))
                        t = player.getPersistentDataContainer().get(LevelSystem.plugin.getTimeKey(), PersistentDataType.INTEGER);
                    player.getPersistentDataContainer().set(LevelSystem.plugin.getBoostLv1Key(), PersistentDataType.INTEGER, i + data.getSec());
                    player.getPersistentDataContainer().set(LevelSystem.plugin.getTimeKey(), PersistentDataType.INTEGER, t + data.getSec());
                    players.add(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            players.remove(player);
                        }
                    }.runTaskLater(LevelSystem.plugin, 2);
                } else if (data.getItem().getItemMeta().getDisplayName().contains("Lv.2")) {
                    if (player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv1Key(), PersistentDataType.INTEGER)||
                            player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv3Key(), PersistentDataType.INTEGER)
                    ){
                        e.setCancelled(true);
                        LevelSystem.plugin.message.sendError(player,"すでにほかのブーストポーションを飲んでいるため使用できません！");
                        return;
                    }
                    int i = 0;
                    int t = 0;
                    if (player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv2Key(), PersistentDataType.INTEGER))
                        i = player.getPersistentDataContainer().get(LevelSystem.plugin.getBoostLv2Key(), PersistentDataType.INTEGER);
                    if (player.getPersistentDataContainer().has(LevelSystem.plugin.getTimeKey(), PersistentDataType.INTEGER))
                        t = player.getPersistentDataContainer().get(LevelSystem.plugin.getTimeKey(), PersistentDataType.INTEGER);
                    player.getPersistentDataContainer().set(LevelSystem.plugin.getBoostLv2Key(), PersistentDataType.INTEGER, i + data.getSec());
                    player.getPersistentDataContainer().set(LevelSystem.plugin.getTimeKey(), PersistentDataType.INTEGER, t + data.getSec());
                    getLogger().info("lv2");
                    players.add(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            players.remove(player);
                        }
                    }.runTaskLater(LevelSystem.plugin, 2);
                } else if (data.getItem().getItemMeta().getDisplayName().contains("Lv.3")) {
                    if (player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv2Key(), PersistentDataType.INTEGER)||
                            player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv1Key(), PersistentDataType.INTEGER)
                    ){
                        e.setCancelled(true);
                        LevelSystem.plugin.message.sendError(player,"すでにほかのブーストポーションを飲んでいるため使用できません！");
                        return;
                    }
                    int i = 0;
                    int t = 0;
                    if (player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv3Key(), PersistentDataType.INTEGER))
                        i = player.getPersistentDataContainer().get(LevelSystem.plugin.getBoostLv3Key(), PersistentDataType.INTEGER);
                    if (player.getPersistentDataContainer().has(LevelSystem.plugin.getTimeKey(), PersistentDataType.INTEGER))
                        t = player.getPersistentDataContainer().get(LevelSystem.plugin.getTimeKey(), PersistentDataType.INTEGER);
                    player.getPersistentDataContainer().set(LevelSystem.plugin.getBoostLv3Key(), PersistentDataType.INTEGER, i + data.getSec());
                    player.getPersistentDataContainer().set(LevelSystem.plugin.getTimeKey(), PersistentDataType.INTEGER, t + data.getSec());
                    players.add(player);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            players.remove(player);
                        }
                    }.runTaskLater(LevelSystem.plugin, 2);
                }
            }
        }
    }
}
