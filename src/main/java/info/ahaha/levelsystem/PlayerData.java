package info.ahaha.levelsystem;

import info.ahaha.levelsystem.event.PlayerAcquisitionSkillEvent;
import info.ahaha.levelsystem.event.PlayerGainEXPEvent;
import info.ahaha.levelsystem.event.PlayerLevelUpEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.logging.Level;

import static info.ahaha.levelsystem.LevelSystem.getEcon;
import static info.ahaha.levelsystem.LevelSystem.plugin;
import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class PlayerData {

    private final UUID uuid;
    private final Map<String, NamespacedKey> keys = new HashMap<>();
    private NamespacedKey skillList;
    private int[] expArray = {
            0, 9, 32, 82, 187, 379, 725, 1324, 2286, 3683, 5832, 8679, 12346, 17276,
            23854, 32446, 43467, 57385, 74066, 93837, 117047, 144276, 175950, 212520, 254465, 302289, 356829, 418690,
            488512, 566971, 655169, 753907, 864030, 986429, 1122536, 1273397, 1440115, 1623851, 1825826, 2047979, 2291734,
            2558591, 2850126, 3168801, 3516442, 3894972, 4306414, 4753868, 5239665, 5766257
    };

    public static List<PlayerData> data = new ArrayList<>();

    public PlayerData(Player player) {
        this.uuid = player.getUniqueId();
        NamespacedKey levelKey = new NamespacedKey(LevelSystem.plugin, "level");
        NamespacedKey skillPointKey = new NamespacedKey(LevelSystem.plugin, "skillPoint");
        NamespacedKey expKey = new NamespacedKey(LevelSystem.plugin, "exp");
        NamespacedKey hpKey = new NamespacedKey(LevelSystem.plugin, "hp");
        NamespacedKey atkKey = new NamespacedKey(LevelSystem.plugin, "atk");
        NamespacedKey defKey = new NamespacedKey(LevelSystem.plugin, "def");
        NamespacedKey knockBackKey = new NamespacedKey(LevelSystem.plugin, "knockBack");
        NamespacedKey speedKey = new NamespacedKey(LevelSystem.plugin, "speed");
        skillList = new NamespacedKey(LevelSystem.plugin, "skillList");

        keys.put("level", levelKey);
        keys.put("skillPoint", skillPointKey);
        keys.put("exp", expKey);
        keys.put("hp", hpKey);
        keys.put("atk", atkKey);
        keys.put("def", defKey);
        keys.put("knockBack", knockBackKey);
        keys.put("speed", speedKey);
        for (NamespacedKey keys : keys.values()) {
            if (!player.getPersistentDataContainer().has(keys, PersistentDataType.DOUBLE)) {
                if (keys.equals(levelKey)) {
                    player.getPersistentDataContainer().set(keys, PersistentDataType.DOUBLE, 1D);
                } else {
                    player.getPersistentDataContainer().set(keys, PersistentDataType.DOUBLE, 0D);
                }
            }
        }
        if (!getEcon().hasAccount(player)) {
            getEcon().createPlayerAccount(player);
        }
        if (!player.getPersistentDataContainer().has(skillList, PersistentDataType.STRING))
            player.getPersistentDataContainer().set(skillList, PersistentDataType.STRING, "");
        data.add(this);
    }

    private double truncate(double value) {
        double scale = Math.pow(10, 3);
        return Math.round(value * scale) / scale;
    }

    public UUID getUuid() {
        return uuid;
    }

    public double getMoney() {
        return getEcon().getBalance(Bukkit.getOfflinePlayer(uuid));
    }

    public void addMoney(int value) {
        getEcon().depositPlayer(Bukkit.getOfflinePlayer(uuid), value);
        if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
            Player player = Bukkit.getPlayer(uuid);
            LevelSystem.plugin.message.sendMessage(player, ChatColor.GOLD + "" + value + ChatColor.GREEN + " " + getEcon().currencyNameSingular() + "獲得しました！");
        }
    }

    public String getSkills() {
        Player player = Bukkit.getPlayer(uuid);
        return player.getPersistentDataContainer().get(skillList, PersistentDataType.STRING);
    }

    public void addSkill(String s) {
        Player player = Bukkit.getPlayer(uuid);
        LevelSystem.plugin.containerUtil.addContainerToString(player, skillList, s);
    }

    public List<String> getSkillList() {
        List<String> list = new ArrayList<>();
        list.addAll(Arrays.asList(getSkills().split(",")));
        return list;
    }

    public void acquisitionSkill(int level) {
        Player player = Bukkit.getPlayer(uuid);
        for (Skill skill : LevelSystem.plugin.skills.values()) {
            if (skill.getAcquisitionLevel() <= level) {
                if (!getSkillList().contains(skill.getType().name())) {
                    PlayerAcquisitionSkillEvent playerAcquisitionSkillEvent = new PlayerAcquisitionSkillEvent(player, skill);
                    getServer().getPluginManager().callEvent(playerAcquisitionSkillEvent);
                    if (!playerAcquisitionSkillEvent.isCancelled()) {
                        addSkill(playerAcquisitionSkillEvent.getSkill().getType().name());
                    }
                }
            }
        }
    }

    public int getLevel() {
        Player player = Bukkit.getPlayer(uuid);
        double def = player.getPersistentDataContainer().get(getKeys().get("level"), PersistentDataType.DOUBLE);
        return (int) def;
    }

    public int getSkillPoint() {
        Player player = Bukkit.getPlayer(uuid);
        double def = player.getPersistentDataContainer().get(getKeys().get("skillPoint"), PersistentDataType.DOUBLE);
        return (int) def;
    }

    public int getExp() {
        Player player = Bukkit.getPlayer(uuid);
        double def = player.getPersistentDataContainer().get(getKeys().get("exp"), PersistentDataType.DOUBLE);
        return (int) def;
    }

    public double getAtk() {
        Player player = Bukkit.getPlayer(uuid);
        return truncate(player.getPersistentDataContainer().get(getKeys().get("atk"), PersistentDataType.DOUBLE));
    }

    public double getDef() {
        Player player = Bukkit.getPlayer(uuid);
        return truncate(player.getPersistentDataContainer().get(getKeys().get("def"), PersistentDataType.DOUBLE));
    }

    public double getHp() {
        Player player = Bukkit.getPlayer(uuid);
        return truncate(player.getPersistentDataContainer().get(getKeys().get("hp"), PersistentDataType.DOUBLE));
    }

    public double getSpeed() {
        Player player = Bukkit.getPlayer(uuid);
        return truncate(player.getPersistentDataContainer().get(getKeys().get("speed"), PersistentDataType.DOUBLE));
    }

    public double getKnockBack() {
        Player player = Bukkit.getPlayer(uuid);
        return truncate(player.getPersistentDataContainer().get(getKeys().get("knockBack"), PersistentDataType.DOUBLE));
    }

    public Map<String, NamespacedKey> getKeys() {
        return keys;
    }

    public void addLevel(int value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("level");
        double def = player.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        if (!(getLevel() >= 50)) {
            PlayerLevelUpEvent playerLevelUpEvent = new PlayerLevelUpEvent(player, getLevel(), (int) (def + value));
            getServer().getPluginManager().callEvent(playerLevelUpEvent);
            if (!playerLevelUpEvent.isCancelled()) {
                player.getWorld().playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2f, 3f);
                player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, (double) playerLevelUpEvent.getAfterLevel());
                acquisitionSkill(getLevel());
            }
        }
    }

    public void setLevel(double value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("level");
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
    }

    public void addSkillPoint(int value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("skillPoint");
        double def = player.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, def + value);
    }

    public void setSkillPoint(double value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("skillPoint");
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
    }

    public void addExp(int value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("exp");
        double def = player.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        PlayerGainEXPEvent playerGainEXPEvent = new PlayerGainEXPEvent(player, value);
        getServer().getPluginManager().callEvent(playerGainEXPEvent);
        if (!playerGainEXPEvent.isCancelled()) {
            player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, def + playerGainEXPEvent.getExp());
        }
    }

    public void setExp(double value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("exp");
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, value);
    }

    public void addAtk() {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("atk");
        double def = player.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        double value = LevelSystem.plugin.manager.getConfig().getDouble("Status.ATK");
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, truncate(def + value));

    }

    public void setAtk(double value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("atk");
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, truncate(value));
    }

    public void addDef() {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("def");
        double def = player.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        double value = LevelSystem.plugin.manager.getConfig().getDouble("Status.DEF");
        if (truncate(def + value) > 30.0) return;
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, truncate(def + value));
    }

    public void setDef(double value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("def");
        if (value > 30.0) return;
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, truncate(value));
    }

    public void addSpeed() {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("speed");
        double value = LevelSystem.plugin.manager.getConfig().getDouble("Status.SPEED");
        double def = player.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, truncate(def + value));
    }

    public void setSpeed(double value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("speed");
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, truncate(value));
    }

    public void addHp() {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("hp");
        double value = LevelSystem.plugin.manager.getConfig().getDouble("Status.HP");
        double def = player.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, truncate(def + value));
    }

    public void setHp(double value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("hp");
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, truncate(value));
    }

    public void addKnockBack() {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("knockBack");
        double value = LevelSystem.plugin.manager.getConfig().getDouble("Status.KNOCKBACK");
        double def = player.getPersistentDataContainer().get(key, PersistentDataType.DOUBLE);
        if (truncate(def + value) > 1.0) return;
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, truncate(def + value));
    }

    public void setKnockBack(double value) {
        Player player = Bukkit.getPlayer(uuid);
        NamespacedKey key = getKeys().get("knockBack");
        if (value > 1.0) return;
        player.getPersistentDataContainer().set(key, PersistentDataType.DOUBLE, truncate(value));
    }

    public void levelUp(int exp) {
        Player player = Bukkit.getPlayer(uuid);
        addExp(exp);
        for (int i = getLevel(); i < expArray.length; i++) {
            if (getExp() >= expArray[i]) {
                addLevel(1);
                addSkillPoint(1);
            } else break;
        }

    }

    public Skill getUsedSkill(Player player) {
        if (player.getPersistentDataContainer().has(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING)) {
            if (LevelSystem.plugin.skills.containsKey(SkillType.valueOf(player.getPersistentDataContainer().get(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING))))
                return LevelSystem.plugin.skills.get(SkillType.valueOf(player.getPersistentDataContainer().get(LevelSystem.plugin.getUsedSkillKey(), PersistentDataType.STRING)));
        }
        return null;
    }


    public void statusReset() {

        setLevel(1);
        setExp(0);
        setHp(0);
        setSkillPoint(0);
        setAtk(0);
        setDef(0);
        setSpeed(0);

    }

    public int getRemainingExp() {
        return expArray[getLevel()] - getExp();
    }

}
