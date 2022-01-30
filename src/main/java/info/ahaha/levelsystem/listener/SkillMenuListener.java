package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.gui.GUI;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public class SkillMenuListener implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        PlayerData data = getPlayerData(player);
        if (data == null) return;
        if (!e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_AQUA + "SkillMenu" + ChatColor.GRAY + " - " + ChatColor.DARK_GREEN + player.getName()))
            return;
        e.setCancelled(true);
        Inventory menu = e.getView().getTopInventory();
        if (e.getClickedInventory() == null) return;
        if (!e.getClickedInventory().equals(menu)) return;
        if (e.getSlot() == 20) {
            if (menu.getItem(20) != null) {
                LevelSystem.plugin.message.sendMessage(player,menu.getItem(20).getItemMeta().getDisplayName()+ChatColor.GREEN+" を解除しました！");
                player.getPersistentDataContainer().remove(LevelSystem.plugin.getSkillKey());
                menu.clear(20);
            }
        }else {
            if (e.getCurrentItem() == null)return;
            for (Skill skill : LevelSystem.plugin.skills.values()){
                if (e.getCurrentItem().isSimilar(skill.getIcon())){
                    menu.setItem(20,e.getCurrentItem());
                    player.getPersistentDataContainer().set(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING,skill.getType().name());
                    LevelSystem.plugin.message.sendMessage(player,ChatColor.YELLOW+skill.getName()+ChatColor.GREEN+" をセットしました！");
                    return;
                }
            }
        }

    }

    private PlayerData getPlayerData(Player player){
        for (PlayerData data : PlayerData.data){
            if (data.getUuid().equals(player.getUniqueId()))return data;
        }
        return null;
    }
}

