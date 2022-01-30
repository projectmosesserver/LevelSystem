package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.ItemData;
import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import info.ahaha.levelsystem.event.PlayerGainEXPEvent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.persistence.PersistentDataType;

public class ExpGainListener implements Listener {

    @EventHandler
    public void gainExp(PlayerGainEXPEvent e) {
        Player player = e.getPlayer();
        PlayerData pdata = null;
        for (PlayerData data : PlayerData.data){
            if (data.getUuid().equals(player.getUniqueId()))pdata = data;
        }
        if (pdata == null)return;
        int exp = e.getExp();
        ItemData data = null;
        for (ItemData data1 : ItemData.data) {
            if (player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv1Key(), PersistentDataType.INTEGER)) {
                if (data1.getName().contains("Lv.1")) {
                    data = data1;
                    break;
                }
            } else if (player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv2Key(), PersistentDataType.INTEGER)) {
                if (data1.getName().contains("Lv.2")) {
                    data = data1;
                    break;
                }
            } else if (player.getPersistentDataContainer().has(LevelSystem.plugin.getBoostLv3Key(), PersistentDataType.INTEGER)) {
                if (data1.getName().contains("Lv.3")) {
                    data = data1;
                    break;
                }
            }
        }
        if (data != null) {
            exp = (int) (exp * data.getMagnification());
        }
        LevelSystem.plugin.message.sendMessage(e.getPlayer(), "経験値を " + ChatColor.GOLD + exp + ChatColor.GREEN + " 獲得しました！");
    }
}
