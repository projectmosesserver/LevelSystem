package info.ahaha.levelsystem.util;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;

public class FunctionItems {

    public FunctionItems(){

    }

    public ItemStack getLv1BoostExpBottle(){
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"ExpBoostPotion Lv1");
        meta.setColor(Color.AQUA);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getLv2BoostExpBottle(){
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"ExpBoostPotion Lv2");
        meta.setColor(Color.BLUE);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getLv3BoostExpBottle(){
        ItemStack item = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD+"ExpBoostPotion Lv3");
        meta.setColor(Color.WHITE);
        item.setItemMeta(meta);
        return item;
    }
}
