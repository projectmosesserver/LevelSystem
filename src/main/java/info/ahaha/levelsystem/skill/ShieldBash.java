package info.ahaha.levelsystem.skill;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class ShieldBash implements Skill {
    @Override
    public String getName() {
        return "ShieldBash";
    }

    @Override
    public SkillType getType() {
        return SkillType.SHIELD_BASH;
    }

    @Override
    public String getExplanation() {
        return "盾でガードしたときに攻撃してきたエンティティをノックバックさせる";
    }

    @Override
    public Material getIconMaterial() {
        return Material.SHIELD;
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
                ChatColor.YELLOW + "対応アイテム" + ChatColor.GRAY + " : " + ChatColor.GREEN + "盾",
                ChatColor.YELLOW + "発動条件" + ChatColor.GRAY + " : " + ChatColor.GREEN + "盾でガード",
                ChatColor.YELLOW + "クールタイム" + ChatColor.GRAY + " : " + ChatColor.GREEN + getCoolTime() + " 秒",
                ChatColor.YELLOW + "ダメージ" + ChatColor.GRAY + " : " + ChatColor.GREEN + (int)getAmount(),
                ChatColor.GRAY + "----------------------------"

        )));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getAcquisitionLevel() {
        return 5;
    }

    @Override
    public List<String> getAvailableTools() {
        return new ArrayList<>(Arrays.asList("SHIELD"));
    }

    @Override
    public int getCoolTime() {
        return 0;
    }

    @Override
    public double getAmount() {
        return 0;
    }

    @Override
    public void skillActive(Player player) {

    }
    public void skillActive(EntityDamageByEntityEvent e){
        if (!(e.getEntity() instanceof Player))return;
        Player player = (Player) e.getEntity();
        if (!player.getPersistentDataContainer().has(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING))return;
        if (!LevelSystem.plugin.skills.containsKey(SkillType.valueOf(player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING))))return;
        Skill skill = LevelSystem.plugin.skills.get(SkillType.valueOf(player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)));
        if (skill.getType() != SkillType.SHIELD_BASH)return;
        if (player.isBlocking()){
            pushAwayEntity(e.getDamager(),-3.5D,player);
        }
    }
    public void pushAwayEntity(Entity entity, double speed , LivingEntity victim) {
        Vector unitVector = victim.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
        entity.setVelocity(unitVector.multiply(speed));
    }
}
