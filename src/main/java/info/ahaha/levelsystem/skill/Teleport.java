package info.ahaha.levelsystem.skill;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Teleport implements Skill {
    @Override
    public String getName() {
        return "Teleport";
    }

    @Override
    public SkillType getType() {
        return SkillType.TELEPORT;
    }

    @Override
    public String getExplanation() {
        return "自分の向いている方向に15マス分テレポートする";
    }

    @Override
    public Material getIconMaterial() {
        return Material.ENDER_PEARL;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack item = new ItemStack(getIconMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + getName());
        meta.setLore(new ArrayList<>(Arrays.asList(
                ChatColor.GRAY + "----------------------------",
                ChatColor.GREEN + getExplanation(),
                "",
                ChatColor.YELLOW + "対応アイテム" + ChatColor.GRAY + " : " + ChatColor.GREEN + "剣",
                ChatColor.YELLOW + "発動条件" + ChatColor.GRAY + " : " + ChatColor.GREEN + "アイテムを持ち右クリック",
                ChatColor.YELLOW + "クールタイム" + ChatColor.GRAY + " : " + ChatColor.GREEN + getCoolTime() + " 秒",
                ChatColor.YELLOW + "ダメージ" + ChatColor.GRAY + " : " + ChatColor.GREEN + (int)getAmount(),
                ChatColor.GRAY + "----------------------------"

        )));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getAcquisitionLevel() {
        return 19;
    }

    @Override
    public List<String> getAvailableTools() {
        return LevelSystem.plugin.materialToolUtil.getSwords();
    }

    @Override
    public int getCoolTime() {
        return 5;
    }

    @Override
    public double getAmount() {
        return 0;
    }

    @Override
    public void skillActive(Player player) {
        if (!player.getPersistentDataContainer().has(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING)) {
            if (!player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING).equalsIgnoreCase(getType().name()))
                return;
            if (!isUse(player)) return;
            player.getPersistentDataContainer().set(LevelSystem.plugin.getCoolTimeKey(), PersistentDataType.INTEGER, getCoolTime());
            player.getPersistentDataContainer().set(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING, getType().name());
            Location startLoc = player.getLocation();

            Location warpLoc = startLoc.clone();

            Vector dir = startLoc.getDirection();

            Vector vecOffset = dir.clone().multiply(0.5).normalize();
            for (int i = 0; i < 15; i++) {
                if (!warpLoc.getBlock().getType().isSolid()) {
                    warpLoc.add(vecOffset);
                }
            }
            player.setFallDistance(-999);
            player.teleport(warpLoc);
            player.playEffect(EntityEffect.TELEPORT_ENDER);
        }
    }
}
