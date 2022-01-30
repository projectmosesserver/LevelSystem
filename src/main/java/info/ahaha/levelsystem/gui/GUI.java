package info.ahaha.levelsystem.gui;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.PlayerData;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;

import static org.bukkit.Bukkit.getLogger;

public class GUI {
    public static Map<UUID, GUI> guis = new HashMap<>();

    ItemStack b;
    ItemStack plus;
    ItemStack minus;

    private final double defaultAtk;
    private final double defaultDef;
    private final double defaultSpeed;
    private final double defaultKnockBack;
    private final double defaultHp;
    private final double defaultSkillPoint;

    private double fluctuatAtk;
    private double fluctuatDef;
    private double fluctuatSpeed;
    private double fluctuatKnockBack;
    private double fluctuatHp;

    private boolean success = false;

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
        PlayerData data = getPlayerData(player);
        this.defaultAtk = data.getAtk();
        this.defaultDef = data.getDef();
        this.defaultSpeed = data.getSpeed();
        this.defaultKnockBack = data.getKnockBack();
        this.defaultHp = data.getHp();
        this.defaultSkillPoint = data.getSkillPoint();
    }

    public Inventory getSkillMenu(Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null)return null;
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "SkillMenu" + ChatColor.GRAY + " - " + ChatColor.DARK_GREEN + player.getName());
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, b);
        }
        if (player.getPersistentDataContainer().has(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING)){
           inv.setItem(20,LevelSystem.plugin.skills.get(SkillType.valueOf(player.getPersistentDataContainer().get(LevelSystem.plugin.getSkillKey(), PersistentDataType.STRING))).getIcon());
        }else inv.clear(20);
        int[] skills = {14,15,16,23,24,25,32,33,34,41,42,43};
        for (int i : skills){
            inv.clear(i);
        }
        int i = 0;
        for (String s : data.getSkillList()){
            if (s.equalsIgnoreCase(""))continue;
            inv.setItem(skills[i],LevelSystem.plugin.skills.get(SkillType.valueOf(s)).getIcon());
            i++;
        }
        return inv;
    }

    public Inventory getStatusMenu(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "StatusMenu" + ChatColor.GRAY + " - " + ChatColor.DARK_GREEN + player.getName());
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
        ItemStack success = new ItemStack(Material.ANVIL);
        ItemMeta meta = success.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Success");
        success.setItemMeta(meta);
        inventory.setItem(20, getHP(player));
        inventory.setItem(24, getATK(player));
        inventory.setItem(29, getDEF(player));
        inventory.setItem(33, getSpeed(player));
        inventory.setItem(38, getKnockBack(player));
        inventory.setItem(49, success);

        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) != null) continue;
            inventory.setItem(i, b);
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
                ChatColor.YELLOW + "HP " + ChatColor.GRAY + ": " + ChatColor.AQUA + "20.0" + ChatColor.GRAY + " + "
                        + ChatColor.GREEN + data.getHp() + ChatColor.GRAY + " + " + ChatColor.GREEN + truncate(fluctuatHp)
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
                ChatColor.YELLOW + "ATK " + ChatColor.GRAY + ": " + ChatColor.AQUA + "1.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getAtk() + ChatColor.GRAY + " + " + ChatColor.GREEN + truncate(fluctuatAtk)
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
                ChatColor.YELLOW + "DEF " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getDef() + ChatColor.GRAY + " + " + ChatColor.GREEN + truncate(fluctuatDef)
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
                ChatColor.YELLOW + "SPEED " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.1" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getSpeed() + ChatColor.GRAY + " + " + ChatColor.GREEN + truncate(fluctuatSpeed)
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
                ChatColor.YELLOW + "KnockBack " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getKnockBack() + ChatColor.GRAY + " + " + ChatColor.GREEN + truncate(fluctuatKnockBack)
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
                ChatColor.YELLOW + "Level " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getLevel(),
                ChatColor.YELLOW + "SkillPoint " + ChatColor.GRAY + ": " + ChatColor.AQUA + data.getSkillPoint(),
                ChatColor.YELLOW + "HP " + ChatColor.GRAY + ": " + ChatColor.AQUA + "20.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getHp() + ChatColor.GRAY + " + " + ChatColor.GREEN + truncate(fluctuatHp),
                ChatColor.YELLOW + "ATK " + ChatColor.GRAY + ": " + ChatColor.AQUA + "1.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getAtk() + ChatColor.GRAY + " + " + ChatColor.GREEN + truncate(fluctuatAtk),
                ChatColor.YELLOW + "DEF " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getDef() + ChatColor.GRAY + " + " + ChatColor.GREEN + truncate(fluctuatDef),
                ChatColor.YELLOW + "SPEED " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.1" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getSpeed() + ChatColor.GRAY + " + " + ChatColor.GREEN + truncate(fluctuatSpeed),
                ChatColor.YELLOW + "KNOCKBACK " + ChatColor.GRAY + ": " + ChatColor.AQUA + "0.0" + ChatColor.GRAY + " + " + ChatColor.GREEN + data.getKnockBack() + ChatColor.GRAY + " + " + ChatColor.GREEN + truncate(fluctuatKnockBack),
                ChatColor.GRAY + "----------------------------"
        )));
        head.setItemMeta(meta);
        return head;
    }

    public void addStatus(int slot, Inventory inventory, Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return;
        if (data.getSkillPoint() == 0) return;
        if (slot == 19) {
            addHp();
            data.setSkillPoint(data.getSkillPoint() - 1);
            update(inventory, player);
        } else if (slot == 23) {
            addAtk();
            data.setSkillPoint(data.getSkillPoint() - 1);
            update(inventory, player);
        } else if (slot == 28) {
            addDef();
            data.setSkillPoint(data.getSkillPoint() - 1);
            update(inventory, player);
        } else if (slot == 32) {
            addSpeed();
            data.setSkillPoint(data.getSkillPoint() - 1);
            update(inventory, player);
        } else if (slot == 37) {
            addKnockBack();
            data.setSkillPoint(data.getSkillPoint() - 1);
            update(inventory, player);
        }
    }

    public void subtractionStatus(int slot, Inventory inventory, Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return;
        double value;
        if (slot == 21) {
            value = fluctuatHp - LevelSystem.plugin.manager.getConfig().getDouble("Status.HP");
            if (value < 0) return;
            fluctuatHp = truncate(value);
            data.setSkillPoint(data.getSkillPoint() + 1);
            update(inventory, player);
        } else if (slot == 25) {
            value = fluctuatAtk - LevelSystem.plugin.manager.getConfig().getDouble("Status.ATK");
            if (value < 0) return;
            fluctuatAtk = truncate(value);
            data.setSkillPoint(data.getSkillPoint() + 1);
            update(inventory, player);
        } else if (slot == 30) {
            value = fluctuatDef - LevelSystem.plugin.manager.getConfig().getDouble("Status.DEF");
            if (value < 0) return;
            fluctuatDef = truncate(value);
            data.setSkillPoint(data.getSkillPoint() + 1);
            update(inventory, player);
        } else if (slot == 34) {
            value = fluctuatSpeed - LevelSystem.plugin.manager.getConfig().getDouble("Status.SPEED");
            if (value < 0) return;
            fluctuatSpeed = truncate(value);
            data.setSkillPoint(data.getSkillPoint() + 1);
            update(inventory, player);
        } else if (slot == 39) {
            value = fluctuatKnockBack - LevelSystem.plugin.manager.getConfig().getDouble("Status.KNOCKBACK");
            if (value < 0) return;
            fluctuatKnockBack = truncate(value);
            data.setSkillPoint(data.getSkillPoint() + 1);
            update(inventory, player);
        }
    }

    public void notSuccessClose(Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return;
        data.setHp(defaultHp);
        data.setAtk(defaultAtk);
        data.setDef(defaultDef);
        data.setSpeed(defaultSpeed);
        data.setKnockBack(defaultKnockBack);
        data.setSkillPoint(defaultSkillPoint);

    }

    public void success(Player player) {
        PlayerData data = getPlayerData(player);
        if (data == null) return;
        data.setHp(data.getHp() + fluctuatHp);
        data.setAtk(data.getAtk() + fluctuatAtk);
        data.setDef(data.getDef() + fluctuatDef);
        data.setSpeed(data.getSpeed() + fluctuatSpeed);
        data.setKnockBack(data.getKnockBack() + fluctuatSpeed);

    }

    public void successMessage(Player player) {
        if (fluctuatHp != 0) {
            LevelSystem.plugin.message.sendMessage(player, "HPを " + ChatColor.GOLD + fluctuatHp + ChatColor.GREEN + " 強化し、"
                    + ChatColor.GOLD + player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + ChatColor.GREEN + "になりました！");
        }
        if (fluctuatAtk != 0) {
            LevelSystem.plugin.message.sendMessage(player, "ATKを " + ChatColor.GOLD + fluctuatAtk + ChatColor.GREEN + " 強化し、"
                    + ChatColor.GOLD + player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue() + ChatColor.GREEN + "になりました！");
        }
        if (fluctuatDef != 0) {
            LevelSystem.plugin.message.sendMessage(player, "DEFを " + ChatColor.GOLD + fluctuatDef + ChatColor.GREEN + " 強化し、"
                    + ChatColor.GOLD + player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue() + ChatColor.GREEN + "になりました！");
        }
        if (fluctuatSpeed != 0) {
            LevelSystem.plugin.message.sendMessage(player, "SPEEDを " + ChatColor.GOLD + fluctuatSpeed + ChatColor.GREEN + " 強化し、"
                    + ChatColor.GOLD + player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue() + ChatColor.GREEN + "になりました！");
        }
        if (fluctuatKnockBack != 0) {
            LevelSystem.plugin.message.sendMessage(player, "KNOCKBACKを " + ChatColor.GOLD + fluctuatKnockBack + ChatColor.GREEN + " 強化し、"
                    + ChatColor.GOLD + player.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue() + ChatColor.GREEN + "になりました！");
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    private double truncate(double value) {
        double scale = Math.pow(10, 3);
        return Math.round(value * scale) / scale;
    }

    public void update(Inventory inventory, Player player) {
        inventory.setItem(4, getHead(player));
        inventory.setItem(20, getHP(player));
        inventory.setItem(24, getATK(player));
        inventory.setItem(29, getDEF(player));
        inventory.setItem(33, getSpeed(player));
        inventory.setItem(38, getKnockBack(player));
    }

    public void addAtk() {

        double value = LevelSystem.plugin.manager.getConfig().getDouble("Status.ATK");
        fluctuatAtk += truncate(value);
    }

    public void addDef() {

        double value = LevelSystem.plugin.manager.getConfig().getDouble("Status.DEF");
        if (fluctuatDef + value > 30.0) return;
        fluctuatDef += truncate(value);

    }


    public void addSpeed() {

        double value = LevelSystem.plugin.manager.getConfig().getDouble("Status.SPEED");
        fluctuatSpeed += truncate(value);
    }


    public void addHp() {

        double value = LevelSystem.plugin.manager.getConfig().getDouble("Status.HP");
        fluctuatHp += truncate(value);
    }


    public void addKnockBack() {

        double value = LevelSystem.plugin.manager.getConfig().getDouble("Status.KNOCKBACK");
        if (fluctuatKnockBack + value > 1.0) return;
        fluctuatKnockBack += truncate(value);
    }

    private PlayerData getPlayerData(Player player) {
        for (PlayerData data : PlayerData.data) {
            if (data.getUuid().equals(player.getUniqueId())) return data;
        }
        return null;
    }
}
