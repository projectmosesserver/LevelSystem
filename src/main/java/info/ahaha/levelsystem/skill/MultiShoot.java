package info.ahaha.levelsystem.skill;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.bukkit.Bukkit.getLogger;

public class MultiShoot implements Skill {
    @Override
    public String getName() {
        return "MultiShoot";
    }

    @Override
    public SkillType getType() {
        return SkillType.MULTI_SHOOT;
    }

    @Override
    public String getExplanation() {
        return "矢を撃つときに複数本扇状に撃つことができる";
    }

    @Override
    public Material getIconMaterial() {
        return Material.ARROW;
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
                ChatColor.YELLOW + "対応アイテム" + ChatColor.GRAY + " : " + ChatColor.GREEN + "弓",
                ChatColor.YELLOW + "発動条件" + ChatColor.GRAY + " : " + ChatColor.GREEN + "矢を放つ",
                ChatColor.YELLOW + "クールタイム" + ChatColor.GRAY + " : " + ChatColor.GREEN + getCoolTime()+" 秒",
                ChatColor.YELLOW + "ダメージ" + ChatColor.GRAY + " : " + ChatColor.GREEN + (int) getAmount(),
                ChatColor.GRAY + "----------------------------"

        )));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getAcquisitionLevel() {
        return 23;
    }

    @Override
    public List<String> getAvailableTools() {
        return new ArrayList<>(Arrays.asList("BOW"));
    }

    @Override
    public int getCoolTime() {
        return 10;
    }

    @Override
    public double getAmount() {
        return 6;
    }

    @Override
    public void skillActive(Player player) {

    }

    public void skillActive(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (!player.getPersistentDataContainer().has(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING)) {
                if (!player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING).equalsIgnoreCase(getType().name()))
                    return;
                if (!isUse(player)) return;
                player.getPersistentDataContainer().set(LevelSystem.plugin.getCoolTimeKey(), PersistentDataType.INTEGER, getCoolTime());
                player.getPersistentDataContainer().set(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING, getType().name());
                Arrow source = (Arrow) e.getProjectile();
                for (int i = 5; i <= 45; i += 5) {
                    Arrow arrow = player.getWorld().spawn(player.getEyeLocation(), Arrow.class);
                    arrow.setDamage(getAmount());
                    arrow.setKnockbackStrength(source.getKnockbackStrength());
                    arrow.setShooter(player);
                    arrow.setVelocity(e.getProjectile().getVelocity().rotateAroundY(Math.toRadians(-50+i)));

                    Arrow arrow2 = player.getWorld().spawn(player.getEyeLocation(), Arrow.class);
                    arrow2.setDamage(getAmount());
                    arrow2.setKnockbackStrength(source.getKnockbackStrength());
                    arrow2.setShooter(player);
                    arrow2.setVelocity(e.getProjectile().getVelocity().rotateAroundY(Math.toRadians(50-i)));
                }

            }
        }
    }
}
