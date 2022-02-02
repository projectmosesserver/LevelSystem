package info.ahaha.levelsystem.listener;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.util.ItemSerializable;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static org.bukkit.Bukkit.getLogger;

public class DimensionBoxListener implements Listener {
    public static Map<UUID, ArmorStand> stands = new HashMap<>();
    private final NamespacedKey dimensionKey = new NamespacedKey(LevelSystem.plugin, "DimensionBox");

    @EventHandler
    public void onOpen(PlayerInteractAtEntityEvent e) {
        if (e.getHand() != EquipmentSlot.HAND) return;
        Player player = e.getPlayer();
        for (Entity entity : player.getWorld().getNearbyEntities(player.getLocation(), 1, 1, 1)) {
            if (entity instanceof ArmorStand) {
                if (entity.getCustomName() == null) continue;
                if (entity.getCustomName().equalsIgnoreCase("DimensionBox")) {
                    Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "DimensionBox" + ChatColor.GRAY + " - " + ChatColor.DARK_GREEN + player.getName());
                    if (player.getPersistentDataContainer().has(dimensionKey, PersistentDataType.STRING)) {
                        try {
                            inv.setContents(Objects.requireNonNull(ItemSerializable.itemStackArrayFromBase64(player.getPersistentDataContainer().get(dimensionKey, PersistentDataType.STRING))));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        player.getPersistentDataContainer().set(dimensionKey, PersistentDataType.STRING, ItemSerializable.itemStackArrayToBase64(inv.getContents()));
                    }
                    player.openInventory(inv);
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onSave(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        if (e.getView().getTitle().equalsIgnoreCase(ChatColor.DARK_AQUA + "DimensionBox" + ChatColor.GRAY + " - " + ChatColor.DARK_GREEN + player.getName())) {
            Inventory inv = e.getInventory();
            player.getPersistentDataContainer().set(dimensionKey, PersistentDataType.STRING, ItemSerializable.itemStackArrayToBase64(inv.getContents()));
        }
    }
}
