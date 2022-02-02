package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import info.ahaha.levelsystem.skill.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class SkillActiveListener implements Listener {


    public static Map<UUID, Material> blocks = new HashMap<>();

    @EventHandler
    public void onSkill(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getHand() != EquipmentSlot.HAND) return;

            PlayerData data = getPlayerData(e.getPlayer());
            if (data == null) return;
            if (e.getPlayer().getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING) == null)
                return;

            Skill skill = LevelSystem.plugin.skills.get(SkillType.valueOf(e.getPlayer().getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)));
            if (skill == null) return;
            if (skill.getType() == SkillType.AUTO_HARVEST){
                AutoHarvest autoHarvest = (AutoHarvest) skill;
                autoHarvest.skillActive(e.getPlayer(),e.getClickedBlock());
                return;
            }
            skill.skillActive(e.getPlayer());
        }
    }


    @EventHandler
    public void onSkill(EntityShootBowEvent e){
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (player.getPersistentDataContainer().has(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)) {
                Skill skill = LevelSystem.plugin.skills.get(SkillType.valueOf(player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)));
                if (skill != null) {
                    if (skill.getType() == SkillType.MULTI_SHOOT) {
                        MultiShoot shoot = (MultiShoot) skill;
                        shoot.skillActive(e);
                    }else if (skill.getType() == SkillType.EXPLODE_ARROW){
                        ExplodeArrow explodeArrow = (ExplodeArrow) skill;
                        explodeArrow.skillActive(e);
                    }
                }
            }
        }
    }


    @EventHandler
    public void onSkill(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if (player.getPersistentDataContainer().has(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)) {
                Skill skill = LevelSystem.plugin.skills.get(SkillType.valueOf(player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)));
                if (skill != null) {
                    if (skill.getType() == SkillType.SHIELD_BASH) {
                        ShieldBash bash = (ShieldBash) skill;
                        bash.skillActive(e);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();

        if (player.getPersistentDataContainer().has(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)) {
            Skill skill = LevelSystem.plugin.skills.get(SkillType.valueOf(player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)));
            if (skill != null) {
                if (skill.getType() == SkillType.AUTO_SMELT) {
                    if (!blocks.containsKey(player.getUniqueId())) {
                        blocks.put(player.getUniqueId(), e.getBlock().getType());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSkill(BlockDropItemEvent e) {
        Player player = e.getPlayer();

        if (player.getPersistentDataContainer().has(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)) {
            Skill skill = LevelSystem.plugin.skills.get(SkillType.valueOf(player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)));
            if (skill != null) {
                if (skill.getType() == SkillType.AUTO_SMELT) {
                    AutoSmelt autoSmelt = (AutoSmelt) skill;
                    autoSmelt.skillActive(e, blocks.get(player.getUniqueId()));
                    blocks.remove(player.getUniqueId());
                }
            }
        }
    }

    private PlayerData getPlayerData(Player player) {
        for (PlayerData data : PlayerData.data) {
            if (data.getUuid().equals(player.getUniqueId())) return data;
        }
        return null;
    }
}
