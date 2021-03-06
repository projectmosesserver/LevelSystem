package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.EntityData;
import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import static org.bukkit.Bukkit.getLogger;

public class EntityKillListener implements Listener {

    @EventHandler
    public void onKill(EntityDeathEvent e) {
        if (e.getEntity().getKiller() == null) return;
        PlayerData pdata = null;
        for (PlayerData data : PlayerData.data) {
            if (data.getUuid().equals(e.getEntity().getKiller().getUniqueId())) {
                pdata = data;
            }
        }
        if (pdata == null) return;
        for (EntityData data : EntityData.data) {
            if (e.getEntity().getType() == data.getType()) {
                int exp = 0;
                int money = 0;
                if (e.getEntity().getCustomName() != null) {
                    if (e.getEntity().getCustomName().contains("[Rare]")) {
                        exp = LevelSystem.plugin.manager.getConfig().getInt("Rare.Exp");
                        money = LevelSystem.plugin.manager.getConfig().getInt("Rare.Money");
                    }
                }
                pdata.levelUp((int) data.getResultExp(getLevel(e.getEntity())+exp));
                pdata.addMoney((int) data.getResultMoney(getLevel(e.getEntity())+money));
                break;
            }
        }
    }

    private int getLevel(LivingEntity entity) {
        if (entity.getCustomName() == null) {
            return 0;
        } else if (!entity.getCustomName().contains("Lv")) {
            return 0;
        } else {
            return Integer.parseInt(entity.getCustomName().split("Lv.")[1].split(" :")[0]);
        }
    }
}
