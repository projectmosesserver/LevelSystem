package info.ahaha.levelsystem;

import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemData {

    private final ItemStack item;
    private final String name;
    private final int sec;
    private final double magnification;

    public static List<ItemData> data = new ArrayList<>();

    public ItemData(ItemStack item, String name, int sec, double magnification) {
        this.item = item;
        this.name = name;
        this.sec = sec;
        this.magnification = magnification;
        data.add(this);
    }

    public String getName() {
        return name;
    }

    public ItemStack getItem() {
        return item;
    }

    public int getSec() {
        return sec;
    }

    public double getMagnification() {
        return magnification;
    }
}
