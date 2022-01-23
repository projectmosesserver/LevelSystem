package info.ahaha.levelsystem;

import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

public class EntityData {

    private final EntityType type;
    private final double defaultExp;
    private final double magnification;
    private final double dropMoney;

    public static List<EntityData>data = new ArrayList<>();

    public EntityData(EntityType type , double defaultExp , double magnification , double dropMoney){
        this.type = type;
        this.defaultExp = defaultExp;
        this.magnification = magnification;
        this.dropMoney = dropMoney;

        data.add(this);

    }

    public EntityType getType() {
        return type;
    }

    public double getDefaultExp() {
        return defaultExp;
    }

    public double getMagnification() {
        return magnification;
    }

    public double getResultExp(int level){
        return defaultExp + (level * magnification);
    }

    public double getDropMoney() {
        return dropMoney;
    }

    public double getResultMoney(int level){
        return dropMoney + (level * magnification);
    }
}
