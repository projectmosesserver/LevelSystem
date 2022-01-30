package info.ahaha.levelsystem.util;

import info.ahaha.levelsystem.PlayerData;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getLogger;

public class StatusManager {

    private final DataManager manager;

    public StatusManager(DataManager manager) {
        this.manager = manager;
    }

    public void statusReset(Player player) {
        resetATK(player);
        resetDEF(player);
        resetSPEED(player);
        resetHP(player);
        resetKNOCKBACK(player);
    }

    public void reJoinUpdateStatus(Player player) {
        updateAtk(player);
        updateDef(player);
        updateSpeed(player);
        updateKnockBack(player);
        updateHP(player);
    }

    public void updateHP(Player player) {
        PlayerData data = null;
        for (PlayerData data1 : PlayerData.data) {
            if (data1.getUuid().equals(player.getUniqueId())) {
                data = data1;
                break;
            }
        }
        if (data == null) return;
        resetHP(player);
        double base = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double def = data.getHp();
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(truncate(base + def));

    }

    public void updateAtk(Player player) {
        PlayerData data = null;
        for (PlayerData data1 : PlayerData.data) {
            if (data1.getUuid().equals(player.getUniqueId())) {
                data = data1;
                break;
            }
        }
        if (data == null) return;
        resetATK(player);
        double base = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
        double def = data.getAtk();
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(truncate(base + def));

    }

    public void updateDef(Player player) {
        PlayerData data = null;
        for (PlayerData data1 : PlayerData.data) {
            if (data1.getUuid().equals(player.getUniqueId())) {
                data = data1;
                break;
            }
        }
        if (data == null) return;
        resetDEF(player);
        double base = player.getAttribute(Attribute.GENERIC_ARMOR).getBaseValue();
        double def = data.getDef();
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(truncate(base + def));

    }

    public void updateSpeed(Player player) {
        PlayerData data = null;
        for (PlayerData data1 : PlayerData.data) {
            if (data1.getUuid().equals(player.getUniqueId())) {
                data = data1;
                break;
            }
        }
        if (data == null) return;
        resetSPEED(player);
        double base = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        double def = data.getSpeed();
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(truncate(base + def));

    }

    public void updateKnockBack(Player player) {
        PlayerData data = null;
        for (PlayerData data1 : PlayerData.data) {
            if (data1.getUuid().equals(player.getUniqueId())) {
                data = data1;
                break;
            }
        }
        if (data == null) return;
        resetKNOCKBACK(player);
        double base = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue();
        double def = data.getKnockBack();
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(truncate(base + def));

    }


    public void resetHP(Player player) {
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
    }

    public void resetATK(Player player) {
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.0);
    }

    public void resetDEF(Player player) {
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
    }

    public void resetKNOCKBACK(Player player) {
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
    }

    public void resetSPEED(Player player) {
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1);
    }

    private double truncate(double value) {
        double scale = Math.pow(10, 3);
        return Math.round(value * scale) / scale;
    }
}
