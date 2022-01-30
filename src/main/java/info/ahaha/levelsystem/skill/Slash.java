package info.ahaha.levelsystem.skill;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class Slash implements Skill {
    @Override
    public String getName() {
        return "Slash";
    }

    @Override
    public SkillType getType() {
        return SkillType.SLASH;
    }

    @Override
    public String getExplanation() {
        return "前方向に斬撃を放つ";
    }

    @Override
    public Material getIconMaterial() {
        return Material.IRON_SWORD;
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
                ChatColor.YELLOW + "ダメージ" + ChatColor.GRAY + " : " + ChatColor.GREEN + (int)getAmount() + " + ATK",
                ChatColor.GRAY + "----------------------------"

        )));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getAcquisitionLevel() {
        return 3;
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
        return 5;
    }

    @Override
    public void skillActive(Player player) {
        if (!player.getPersistentDataContainer().has(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING)) {
            if (!player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING).equalsIgnoreCase(getType().name()))
                return;
            if (!isUse(player)) return;
            player.getPersistentDataContainer().set(LevelSystem.plugin.getCoolTimeKey(), PersistentDataType.INTEGER, getCoolTime());
            player.getPersistentDataContainer().set(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING, getType().name());
            player.swingMainHand();
            Location loc = player.getEyeLocation();
            Location particleLoc = loc.clone();
            Vector vector = particleLoc.getDirection().multiply(0.7);
            new BukkitRunnable() {
                int range = 0;
                final int maxRange = 5;

                @Override
                public void run() {
                    if (maxRange >= range) {
                        for (Entity entity : particleLoc.getWorld().getNearbyEntities(particleLoc, 0.5, 0.5, 0.5)) {
                            if (entity instanceof LivingEntity) {
                                if (!(entity.getName().equalsIgnoreCase(player.getName()))) {
                                    LivingEntity living = (LivingEntity) entity;
                                    EntityDamageByEntityEvent entityDamageByEntityEvent = new EntityDamageByEntityEvent(player, entity, EntityDamageEvent.DamageCause.CUSTOM, getAmount() + player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue());
                                    getServer().getPluginManager().callEvent(entityDamageByEntityEvent);
                                    if (!entityDamageByEntityEvent.isCancelled()) {
                                        living.damage(getAmount() + player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue(), player);
                                    }
                                }
                            }
                        }
                        particleLoc.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLoc, 1, 0, 0, 0, 0);
                        particleLoc.getWorld().playSound(particleLoc, Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1, 1);
                        particleLoc.add(vector);
                        range++;
                    } else {
                        this.cancel();
                    }
                }
            }.runTaskTimer(LevelSystem.plugin, 0, 2);
        } else {
            LevelSystem.plugin.message.sendError(player, "まだ" + getName() + "が使用可能になっていません！");

        }
    }
}
