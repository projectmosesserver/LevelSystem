package info.ahaha.levelsystem.skill;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import info.ahaha.levelsystem.listener.DimensionBoxListener;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class DimensionBox implements Skill {
    @Override
    public String getName() {
        return "DimensionBox";
    }

    @Override
    public SkillType getType() {
        return SkillType.DIMENSION_BOX;
    }

    @Override
    public String getExplanation() {
        return "目の前にゲートを出現させアイテムの出し入れができる";
    }

    @Override
    public Material getIconMaterial() {
        return Material.ENDER_CHEST;
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
        return 25;
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
        return 0;
    }

    @Override
    public void skillActive(Player player) {
        Map<UUID, ArmorStand> stands = DimensionBoxListener.stands;
        if (!player.getPersistentDataContainer().has(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING)) {
            if (!player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING).equalsIgnoreCase(getType().name()))
                return;
            if (!isUse(player)) return;
            player.getPersistentDataContainer().set(LevelSystem.plugin.getCoolTimeKey(), PersistentDataType.INTEGER, getCoolTime());
            player.getPersistentDataContainer().set(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING, getType().name());
            if (!stands.containsKey(player.getUniqueId())) {
                ArmorStand stand = player.getWorld().spawn(player.getLocation().add(player.getLocation().getDirection().multiply(1)), ArmorStand.class);
                stand.setCustomName("DimensionBox");
                stand.setVisible(false);
                stand.setCustomNameVisible(false);
                stands.put(player.getUniqueId(), stand);
                new BukkitRunnable() {
                    int i = 0;
                    Location loc = stand.getLocation();

                    @Override
                    public void run() {
                        if (loc.getY() == stands.get(player.getUniqueId()).getLocation().getY())
                            loc.setY(stand.getLocation().getY() + 1);
                        i++;
                        stand.getWorld().spawnParticle(Particle.REDSTONE, loc, 10, 0.1, 0.2, -0.1, new Particle.DustOptions(Color.BLACK, 1));
                        stand.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 3, 0.1, 0.2, -0.1, 0);
                        if (i >= 10 * 30) {
                            stands.get(player.getUniqueId()).remove();
                            stands.remove(player.getUniqueId());
                            this.cancel();
                        }
                    }
                }.runTaskTimer(LevelSystem.plugin, 0, 2);
            }
        }
    }
}
