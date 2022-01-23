package info.ahaha.levelsystem.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class StatusManager {

    private final DataManager manager;

    public StatusManager(DataManager manager){
        this.manager = manager;
    }

    public void addHP(Player player){
        double base = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
        double def = manager.getConfig().getDouble("Status.HP");

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(base+def);
    }

    public void resetHP(Player player){
        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20.0);
    }

    public void addATK(Player player){
        double base = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
        double def = manager.getConfig().getDouble("Status.ATK");

        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(base+def);
    }

    public void resetATK(Player player){
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1.0);
    }

    public void addDEF(Player player){
        double base = player.getAttribute(Attribute.GENERIC_ARMOR).getBaseValue();
        double def = manager.getConfig().getDouble("Status.DEF");

        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(base+def);
    }

    public void resetDEF(Player player){
        player.getAttribute(Attribute.GENERIC_ARMOR).setBaseValue(0);
    }

    public void addKNOCKBACK(Player player){
        double base = player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue();
        double def = manager.getConfig().getDouble("Status.KNOCKBACK");

        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(base+def);
    }

    public void resetKNOCKBACK(Player player){
        player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(0);
    }

    public void addSPEED(Player player){
        double base = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
        double def = manager.getConfig().getDouble("Status.SPEED");

        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(base+def);
    }

    public void resetSPEED(Player player){
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.7);
    }

}
