package info.ahaha.levelsystem.skill;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class JumpSlash implements Skill {
    @Override
    public String getName() {
        return "JumpSlash";
    }

    @Override
    public SkillType getType() {
        return SkillType.JUMP_SLASH;
    }

    @Override
    public String getExplanation() {
        return "ジャンプして着地した地点の周りに斬撃を放つ";
    }

    @Override
    public Material getIconMaterial() {
        return Material.DIAMOND_SWORD;
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
        return 20;
    }

    @Override
    public List<String> getAvailableTools() {
        return LevelSystem.plugin.materialToolUtil.getSwords();
    }

    @Override
    public int getCoolTime() {
        return 10;
    }

    @Override
    public double getAmount() {
        return 12;
    }

    @Override
    public void skillActive(Player player) {
        if (!player.getPersistentDataContainer().has(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING)) {
            if (!player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING).equalsIgnoreCase(getType().name()))
                return;
            if (!isUse(player)) return;
            Block b = player.getWorld().getBlockAt(player.getLocation().subtract(0, 2, 0));
            Vector v = player.getLocation().getDirection().multiply(0.7).setY(0.5);

            if (!b.getType().equals(Material.AIR)) {
                player.getPersistentDataContainer().set(LevelSystem.plugin.getCoolTimeKey(), PersistentDataType.INTEGER, getCoolTime());
                player.getPersistentDataContainer().set(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING, getType().name());
                double amount = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
                player.setVelocity(v);
                player.getWorld().playSound(player.getLocation(),Sound.ENTITY_HORSE_JUMP,2f,1f);
                for (int i = 0; i < 5; i++)
                    player.getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, player.getLocation().add(0, 0.2, 0), 3, 0.1, 0.2, 0.1, 0);

                new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        i++;
                        if (i > 1) {
                            if (player.isOnGround()) {
                                player.swingMainHand();
                                LevelSystem.plugin.particleUtil.createCircle(player.getLocation().add(0, 1, 0), Particle.SWEEP_ATTACK, 2, 3);
                                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP,2f,3f);
                                for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 2.5, 0.5, 2.5)) {
                                    if (entity instanceof LivingEntity) {
                                        if (!entity.getName().equalsIgnoreCase(player.getName())) {
                                            ((LivingEntity) entity).damage(getAmount() + amount, player);

                                        }
                                    }
                                }
                                this.cancel();
                            }
                        }
                    }
                }.runTaskTimer(LevelSystem.plugin, 0, 0);
            }
        }
    }
}
