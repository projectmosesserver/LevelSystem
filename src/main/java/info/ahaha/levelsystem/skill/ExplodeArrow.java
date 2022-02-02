package info.ahaha.levelsystem.skill;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
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

public class ExplodeArrow implements Skill {
    @Override
    public String getName() {
        return "ExplodeArrow";
    }

    @Override
    public SkillType getType() {
        return SkillType.EXPLODE_ARROW;
    }

    @Override
    public String getExplanation() {
        return "矢が着弾した場所を爆発させる";
    }

    @Override
    public Material getIconMaterial() {
        return Material.TNT;
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
                ChatColor.YELLOW + "クールタイム" + ChatColor.GRAY + " : " + ChatColor.GREEN + getCoolTime() + " 秒",
                ChatColor.YELLOW + "ダメージ" + ChatColor.GRAY + " : " + ChatColor.GREEN + (int)getAmount(),
                ChatColor.GRAY + "----------------------------"

        )));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getAcquisitionLevel() {
        return 16;
    }

    @Override
    public List<String> getAvailableTools() {
        return new ArrayList<>(Arrays.asList("BOW"));
    }

    @Override
    public int getCoolTime() {
        return 6;
    }

    @Override
    public double getAmount() {
        return 10;
    }

    @Override
    public void skillActive(Player player) {

    }

    public void skillActive(EntityShootBowEvent e) {
        if (e.getEntity() instanceof Player){
            Player player = (Player) e.getEntity();
            if (!player.getPersistentDataContainer().has(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING)) {
                if (!player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING).equalsIgnoreCase(getType().name()))
                    return;
                if (!isUse(player)) return;
                player.getPersistentDataContainer().set(LevelSystem.plugin.getCoolTimeKey(), PersistentDataType.INTEGER, getCoolTime());
                player.getPersistentDataContainer().set(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING, getType().name());
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (e.getProjectile().isOnGround() || e.getProjectile().isDead()){
                            e.getProjectile().getWorld().spawnParticle(Particle.EXPLOSION_HUGE,e.getProjectile().getLocation(),1);
                            e.getProjectile().getWorld().playSound(e.getProjectile().getLocation(), Sound.ENTITY_GENERIC_EXPLODE,3f,1f);
                            for (Entity entity : e.getProjectile().getWorld().getNearbyEntities(e.getProjectile().getLocation(),2.5,2.5,2.5)){
                                if (entity instanceof LivingEntity){
                                    if (entity.getName().equalsIgnoreCase(player.getName()))continue;
                                    ((LivingEntity) entity).damage(getAmount(),player);
                                }
                            }
                            this.cancel();
                        }else {
                            boolean isHit = false;
                            for (Entity entity : e.getProjectile().getWorld().getNearbyEntities(e.getProjectile().getLocation(),0.5,0.5,0.5)){
                                if (entity instanceof LivingEntity){
                                    if (entity.getName().equalsIgnoreCase(player.getName()))continue;
                                    isHit = true;
                                    break;
                                }
                            }
                            if (isHit){
                                e.getProjectile().getWorld().spawnParticle(Particle.EXPLOSION_HUGE,e.getProjectile().getLocation(),1);
                                e.getProjectile().getWorld().playSound(e.getProjectile().getLocation(), Sound.ENTITY_GENERIC_EXPLODE,3f,1f);
                                for (Entity entity : e.getProjectile().getWorld().getNearbyEntities(e.getProjectile().getLocation(),2.5,2.5,2.5)){
                                    if (entity instanceof LivingEntity){
                                        if (entity.getName().equalsIgnoreCase(player.getName()))continue;
                                        ((LivingEntity) entity).damage(getAmount(),player);
                                    }
                                }
                                this.cancel();
                            }
                        }
                    }
                }.runTaskTimer(LevelSystem.plugin,0,2);
            }
        }
    }
}
