package info.ahaha.levelsystem.util;

import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ContainerUtil {

    public ContainerUtil() {

    }

    public void addContainerToString(Player player, NamespacedKey key, String string) {
        List<String> stringList = new ArrayList<>();
        if (player.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            if (player.getPersistentDataContainer().get(key, PersistentDataType.STRING).contains(",")) {
                for (String s : player.getPersistentDataContainer().get(key, PersistentDataType.STRING).split(",")) {
                    if (s.equalsIgnoreCase(string)) continue;
                    stringList.add(s);
                }

            } else {
                stringList.add(player.getPersistentDataContainer().get(key, PersistentDataType.STRING));
            }
        }
        stringList.add(string);
        player.getPersistentDataContainer().set(key, PersistentDataType.STRING, String.join(",", stringList));
    }

    public void addContainerToString(TileState state, NamespacedKey key, String string) {
        List<String> stringList = new ArrayList<>();
        if (state.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            if (state.getPersistentDataContainer().get(key, PersistentDataType.STRING).contains(",")) {
                for (String s : state.getPersistentDataContainer().get(key, PersistentDataType.STRING).split(",")) {
                    if (s.equalsIgnoreCase(string)) continue;
                    stringList.add(s);
                }

            } else {
                stringList.add(state.getPersistentDataContainer().get(key, PersistentDataType.STRING));
            }
        }
        stringList.add(string);
        state.getPersistentDataContainer().set(key, PersistentDataType.STRING, String.join(",", stringList));
    }

    public void addContainerToString(ItemStack item, NamespacedKey key, String string) {
        List<String> stringList = new ArrayList<>();
        if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            if (item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).contains(",")) {
                for (String s : item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).split(",")) {
                    if (s.equalsIgnoreCase(string)) continue;
                    stringList.add(s);
                }

            } else {
                stringList.add(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING));
            }
        }
        stringList.add(string);
        item.getItemMeta().getPersistentDataContainer().set(key, PersistentDataType.STRING, String.join(",", stringList));
    }

    public void addContainerToStringList(Player player, NamespacedKey key, List<String> list) {
        List<String> stringList = new ArrayList<>();
        if (player.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            if (player.getPersistentDataContainer().get(key, PersistentDataType.STRING).contains(",")) {
                for (String s : player.getPersistentDataContainer().get(key, PersistentDataType.STRING).split(",")) {
                    if (list.contains(s)) continue;
                    stringList.add(s);
                }

            } else {
                stringList.add(player.getPersistentDataContainer().get(key, PersistentDataType.STRING));
            }
        }
        stringList.addAll(list);
        player.getPersistentDataContainer().set(key, PersistentDataType.STRING, String.join(",", stringList));
    }

    public void addContainerToStringList(TileState state, NamespacedKey key, List<String> list) {
        List<String> stringList = new ArrayList<>();
        if (state.getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            if (state.getPersistentDataContainer().get(key, PersistentDataType.STRING).contains(",")) {
                for (String s : state.getPersistentDataContainer().get(key, PersistentDataType.STRING).split(",")) {
                    if (list.contains(s)) continue;
                    stringList.add(s);
                }

            } else {
                stringList.add(state.getPersistentDataContainer().get(key, PersistentDataType.STRING));
            }
        }
        stringList.addAll(list);
        state.getPersistentDataContainer().set(key, PersistentDataType.STRING, String.join(",", stringList));
    }

    public void addContainerToStringList(ItemStack item, NamespacedKey key, List<String> list) {
        List<String> stringList = new ArrayList<>();
        if (item.getItemMeta().getPersistentDataContainer().has(key, PersistentDataType.STRING)) {
            if (item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).contains(",")) {
                for (String s : item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING).split(",")) {
                    if (list.contains(s)) continue;
                    stringList.add(s);
                }

            } else {
                stringList.add(item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING));
            }
        }
        stringList.addAll(list);
        item.getItemMeta().getPersistentDataContainer().set(key, PersistentDataType.STRING, String.join(",", stringList));
    }
}
