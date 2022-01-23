package info.ahaha.levelsystem.gui;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.*;

public class GUI {
    public static Map<UUID, GUI> guis = new HashMap<>();

    ItemStack b;
    ItemStack plus;
    ItemStack minus;

    public GUI(Player player) {
        guis.put(player.getUniqueId(), this);
        b = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        plus = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
        minus = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta pmeta = plus.getItemMeta();
        ItemMeta mmeta = minus.getItemMeta();
        ItemMeta bmeta = b.getItemMeta();
        pmeta.setDisplayName(ChatColor.GOLD + "+1");
        mmeta.setDisplayName(ChatColor.GOLD + "-1");
        bmeta.setDisplayName(" ");
        b.setItemMeta(bmeta);
        plus.setItemMeta(pmeta);
        minus.setItemMeta(mmeta);
    }

    public Inventory getSkillMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "SkillMenu");
        PlayerData data = getPlayerData(player);
        if (data == null) return null;
        inventory.setItem(4, getHead(player));
        int[] green = {19, 23, 28, 32, 37};
        int[] red = {21, 25, 30, 34, 39};
        for (int i : green) {
            inventory.setItem(i, plus);
        }
        for (int i : red) {
            inventory.setItem(i, minus);
        }
        inventory.setItem(20, getHP(player));
        inventory.setItem(24, getATK(player));
        inventory.setItem(29, getDEF(player));
        inventory.setItem(33, getSpeed(player));
        inventory.setItem(38, getKnockBack(player));

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) != null)continue;
            inventory.setItem(i,b);
        }
        return inventory;
    }

    public ItemStack getHP(Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return null;
        ItemStack item = new ItemStack(Material.APPLE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + ">> HP <<");
        meta.setLore(new ArrayList<>(Arrays.asList(
                ChatColor.YELLOW + "SkillPoint " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getSkillPoint(),
                ChatColor.YELLOW + "HP " + ChatColor.GRAY + ": " + ChatColor.AQUA + "20.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getHp()
        )));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getATK(Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return null;
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + ">> ATK <<");
        meta.setLore(new ArrayList<>(Arrays.asList(
                ChatColor.YELLOW + "SkillPoint " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getSkillPoint(),
                ChatColor.YELLOW + "ATK " + ChatColor.GRAY + ": " + ChatColor.AQUA + "1.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getAtk()
        )));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getDEF(Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return null;
        ItemStack item = new ItemStack(Material.IRON_CHESTPLATE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + ">> DEF <<");
        meta.setLore(new ArrayList<>(Arrays.asList(
                ChatColor.YELLOW + "SkillPoint " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getSkillPoint(),
                ChatColor.YELLOW + "DEF " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getDef()
        )));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getSpeed(Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return null;
        ItemStack item = new ItemStack(Material.IRON_BOOTS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + ">> SPEED <<");
        meta.setLore(new ArrayList<>(Arrays.asList(
                ChatColor.YELLOW + "SkillPoint " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getSkillPoint(),
                ChatColor.YELLOW + "SPEED " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getSpeed()
        )));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getKnockBack(Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return null;
        ItemStack item = new ItemStack(Material.SHIELD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + ">> KnockBack <<");
        meta.setLore(new ArrayList<>(Arrays.asList(
                ChatColor.YELLOW + "SkillPoint " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getSkillPoint(),
                ChatColor.YELLOW + "KnockBack " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getKnockBack()
        )));
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getHead(Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return null;
        ItemStack head = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        SkullMeta meta = (SkullMeta) head.getItemMeta();
        meta.setOwningPlayer(player);
        meta.setDisplayName(ChatColor.GOLD + "Information");
        meta.setLore(new ArrayList<>(Arrays.asList(
                ChatColor.GRAY + "----------------------------",
                ChatColor.YELLOW + "HP " + ChatColor.GRAY + ": " + ChatColor.AQUA + "20.0" + ChatColor.GREEN + " +" + data.getHp(),
                ChatColor.YELLOW + "ATK " + ChatColor.GRAY + ": " + ChatColor.AQUA + "1.0" + ChatColor.GREEN + " +" + data.getAtk(),
                ChatColor.YELLOW + "DEF " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.0" + ChatColor.GREEN + " +" + data.getDef(),
                ChatColor.YELLOW + "SPEED " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.7" + ChatColor.GREEN + " +" + data.getSpeed(),
                ChatColor.YELLOW + "KNOCKBACK " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.0" + ChatColor.GREEN + " +" + data.getKnockBack(),
                ChatColor.YELLOW + "SkillPoint " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getSkillPoint(),
                ChatColor.GRAY + "----------------------------"
        )));
        head.setItemMeta(meta);
        return head;
    }

    public void addStatus(int slot, Inventory inventory, Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return;
        if (data.getSkillPoint() == 0)return;
        if (slot == 19) {
            data.addHp();
            data.setSkillPoint(data.getSkillPoint()-1);
            inventory.setItem(4, getHead(player));
            inventory.setItem(20, getHP(player));
            inventory.setItem(24, getATK(player));
            inventory.setItem(29, getDEF(player));
            inventory.setItem(33, getSpeed(player));
            inventory.setItem(38, getKnockBack(player));
        } else if (slot == 23) {
            data.addAtk();
            data.setSkillPoint(data.getSkillPoint()-1);
            inventory.setItem(4, getHead(player));
            inventory.setItem(20, getHP(player));
            inventory.setItem(24, getATK(player));
            inventory.setItem(29, getDEF(player));
            inventory.setItem(33, getSpeed(player));
            inventory.setItem(38, getKnockBack(player));
        } else if (slot == 28) {
            data.addDef();
            data.setSkillPoint(data.getSkillPoint()-1);
            inventory.setItem(4, getHead(player));
            inventory.setItem(20, getHP(player));
            inventory.setItem(24, getATK(player));
            inventory.setItem(29, getDEF(player));
            inventory.setItem(33, getSpeed(player));
            inventory.setItem(38, getKnockBack(player));
        } else if (slot == 32) {
            data.addSpeed();
            data.setSkillPoint(data.getSkillPoint()-1);
            inventory.setItem(4, getHead(player));
            inventory.setItem(20, getHP(player));
            inventory.setItem(24, getATK(player));
            inventory.setItem(29, getDEF(player));
            inventory.setItem(33, getSpeed(player));
            inventory.setItem(38, getKnockBack(player));
        } else if (slot == 37) {
            data.addKnockBack();
            data.setSkillPoint(data.getSkillPoint()-1);
            inventory.setItem(4, getHead(player));
            inventory.setItem(20, getHP(player));
            inventory.setItem(24, getATK(player));
            inventory.setItem(29, getDEF(player));
            inventory.setItem(33, getSpeed(player));
            inventory.setItem(38, getKnockBack(player));
        }
    }

    public void subtractionStatus(int slot, Inventory inventory, Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return;
        double value;
        if (slot == 21) {
            value = data.getHp() - LevelSystem.plugin.manager.getConfig().getDouble("Status.HP");
            if (value < 0)return;
            data.setHp(value);
            data.setSkillPoint(data.getSkillPoint()+1);
            inventory.setItem(4, getHead(player));
            inventory.setItem(20, getHP(player));
            inventory.setItem(24, getATK(player));
            inventory.setItem(29, getDEF(player));
            inventory.setItem(33, getSpeed(player));
            inventory.setItem(38, getKnockBack(player));
        } else if (slot == 25) {
            value = data.getAtk() - LevelSystem.plugin.manager.getConfig().getDouble("Status.ATK");
            if (value < 0)return;
            data.setAtk(value);
            data.setSkillPoint(data.getSkillPoint()+1);
            inventory.setItem(4, getHead(player));
            inventory.setItem(20, getHP(player));
            inventory.setItem(24, getATK(player));
            inventory.setItem(29, getDEF(player));
            inventory.setItem(33, getSpeed(player));
            inventory.setItem(38, getKnockBack(player));
        } else if (slot == 30) {
            value = data.getDef() - LevelSystem.plugin.manager.getConfig().getDouble("Status.DEF");
            if (value < 0)return;
            data.setDef(value);
            data.setSkillPoint(data.getSkillPoint()+1);
            inventory.setItem(4, getHead(player));
            inventory.setItem(20, getHP(player));
            inventory.setItem(24, getATK(player));
            inventory.setItem(29, getDEF(player));
            inventory.setItem(33, getSpeed(player));
            inventory.setItem(38, getKnockBack(player));
        } else if (slot == 34) {
            value = data.getSpeed() - LevelSystem.plugin.manager.getConfig().getDouble("Status.SPEED");
            if (value < 0)return;
            data.setSpeed(value);
            data.setSkillPoint(data.getSkillPoint()+1);
            inventory.setItem(4, getHead(player));
            inventory.setItem(20, getHP(player));
            inventory.setItem(24, getATK(player));
            inventory.setItem(29, getDEF(player));
            inventory.setItem(33, getSpeed(player));
            inventory.setItem(38, getKnockBack(player));
        } else if (slot == 39) {
            value = data.getKnockBack() - LevelSystem.plugin.manager.getConfig().getDouble("Status.KNOCKBACK");
            if (value < 0)return;
            data.setKnockBack(value);
            data.setSkillPoint(data.getSkillPoint()+1);
            inventory.setItem(4, getHead(player));
            inventory.setItem(20, getHP(player));
            inventory.setItem(24, getATK(player));
            inventory.setItem(29, getDEF(player));
            inventory.setItem(33, getSpeed(player));
            inventory.setItem(38, getKnockBack(player));
        }
    }

    private PlayerData getPlayerData(Player player) {
        for (PlayerData data : PlayerData.data) {
            if (data.getUuid().equals(player.getUniqueId())) return data;
        }
        return null;
    }
}
