package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.PlayerData;
import info.ahaha.levelsystem.gui.GUI;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class SkillMenuListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e){
        Player player = (Player) e.getWhoClicked();
        PlayerData data = getPlayerData(player);
        if (data == null)return;
        if (!e.getView().getTitle().contains("SkillMenu"))return;
        e.setCancelled(true);
        Inventory menu = e.getView().getTopInventory();
        GUI gui = GUI.guis.get(player.getUniqueId());
        if (e.getClickedInventory() == null)return;
        if (!e.getClickedInventory().equals(menu))return;
        if (menu.getItem(e.getSlot()).getType() == Material.GREEN_STAINED_GLASS_PANE){
            gui.addStatus(e.getSlot(),menu,player);
        }else if (menu.getItem(e.getSlot()).getType() == Material.RED_STAINED_GLASS_PANE){
            gui.subtractionStatus(e.getSlot(),menu,player);
        }
    }

    private PlayerData getPlayerData(Player player){
        for (PlayerData data : PlayerData.data){
            if (data.getUuid().equals(player.getUniqueId()))return data;
        }
        return null;
    }
}