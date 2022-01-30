package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import info.ahaha.levelsystem.gui.GUI;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import static org.bukkit.Bukkit.getServer;

public class StatusMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        PlayerData data = getPlayerData(player);
        if (data == null) return;
        if (!e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_AQUA + "StatusMenu"+ChatColor.GRAY+" - "+ChatColor.DARK_GREEN+player.getName())) return;
        e.setCancelled(true);
        Inventory menu = e.getView().getTopInventory();
        GUI gui = GUI.guis.get(player.getUniqueId());
        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().equals(menu)) return;
        if (menu.getItem(e.getSlot()).getType() == Material.GREEN_STAINED_GLASS_PANE) {
            gui.addStatus(e.getSlot(), menu, player);
        } else if (menu.getItem(e.getSlot()).getType() == Material.RED_STAINED_GLASS_PANE) {
            gui.subtractionStatus(e.getSlot(), menu, player);
        } else if (menu.getItem(e.getSlot()).getType() == Material.ANVIL) {
            gui.setSuccess(true);
            gui.success(player);
            LevelSystem.plugin.statusManager.reJoinUpdateStatus(player);
            gui.successMessage(player);
            player.closeInventory();
            GUI.guis.remove(player.getUniqueId());
            player.getWorld().playSound(player.getLocation(),Sound.BLOCK_ANVIL_USE,2f,3f);

        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (!GUI.guis.containsKey(player.getUniqueId())) return;
        GUI gui = GUI.guis.get(player.getUniqueId());
        if (!gui.isSuccess()) {
            gui.notSuccessClose(player);
            GUI.guis.remove(player.getUniqueId());
        }

    }

    @EventHandler
    public void onSkill(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (e.getHand() != EquipmentSlot.HAND) return;
            PlayerData data = getPlayerData(e.getPlayer());
            if (data == null) return;
            if (e.getPlayer().getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(),PersistentDataType.STRING) == null)return;
            Skill skill = LevelSystem.plugin.skills.get(SkillType.valueOf(e.getPlayer().getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)));
            if (skill == null) return;
            skill.skillActive(e.getPlayer());
        }
    }

    private PlayerData getPlayerData(Player player) {
        for (PlayerData data : PlayerData.data) {
            if (data.getUuid().equals(player.getUniqueId())) return data;
        }
        return null;
    }
}