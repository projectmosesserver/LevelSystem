package info.ahaha.levelsystem;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface Skill {

    String getName();

    SkillType getType();

    String getExplanation();

    Material getIconMaterial();

    ItemStack getIcon();

    int getAcquisitionLevel();

    List<String> getAvailableTools();

    int getCoolTime();

    double getAmount();

    void skillActive(Player player);

    default boolean isUse(Player player) {
        for (String s : getAvailableTools()) {
            if (player.getInventory().getItemInMainHand().getType().name().equalsIgnoreCase(s)) return true;
        }
        return false;
    }

}
