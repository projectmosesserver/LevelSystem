package info.ahaha.levelsystem;

import info.ahaha.levelsystem.cmd.Cmd;
import info.ahaha.levelsystem.listener.*;
import info.ahaha.levelsystem.runnable.BoostRunnable;
import info.ahaha.levelsystem.runnable.CoolTimeRunnable;
import info.ahaha.levelsystem.skill.*;
import info.ahaha.levelsystem.util.*;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.checkerframework.checker.units.qual.C;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

public final class LevelSystem extends JavaPlugin {

    public static LevelSystem plugin;
    public MessageUtil message;
    public DataManager manager;
    public StatusManager statusManager;
    public FunctionItems items;
    public ContainerUtil containerUtil;
    public MaterialToolUtil materialToolUtil;
    public ParticleUtil particleUtil;
    public Map<SkillType , Skill> skills = new HashMap<>();
    private static Economy econ = null;
    private static final Logger log = Logger.getLogger("Minecraft");
    private NamespacedKey timeKey;
    private NamespacedKey boostLv1Key;
    private NamespacedKey boostLv2Key;
    private NamespacedKey boostLv3Key;
    private NamespacedKey coolTimeKey;
    private NamespacedKey skillKey;
    private NamespacedKey usedSkillKey;

    private Map<UUID, BossBar> bars = new HashMap<>();
    private Map<UUID, BossBar> coolBars = new HashMap<>();

    @Override
    public void onEnable() {
        // Plugin startup logic
        if (!setupEconomy()) {
            log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        plugin = this;
        this.message = new MessageUtil(this);
        this.manager = new DataManager(this);
        this.items = new FunctionItems();
        this.containerUtil = new ContainerUtil();
        this.materialToolUtil = new MaterialToolUtil();
        this.particleUtil = new ParticleUtil();

        Path path = Paths.get(String.valueOf(plugin.getDataFolder()));
        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                getLogger().info(e.toString());
            }
        }

        this.statusManager = new StatusManager(manager);

        for (EntityType type : EntityType.values()) {
            if (manager.getConfig().getConfigurationSection(type.name()) == null)
                continue;
            new EntityData(
                    type,
                    manager.getConfig().getDouble(type.name() + ".DefaultExp"),
                    manager.getConfig().getDouble(type.name() + ".Magnification"),
                    manager.getConfig().getDouble(type.name() + ".DropMoney")
            );
        }

        for (int i = 1; i <= manager.getConfig().getInt("Items.EXPBoost.MaxLevel"); i++) {
            ItemStack item = new ItemStack(Material.POTION);
            PotionMeta meta = (PotionMeta) item.getItemMeta();
            String name = "EXPBoostPotion_Lv." + i;
            int sec = manager.getConfig().getInt("Items.EXPBoost." + i + ".Sec");
            double magnification = manager.getConfig().getDouble("Items.EXPBoost." + i + ".Magnification");
            meta.setDisplayName(ChatColor.GOLD + name);
            meta.setColor(Color.AQUA);
            meta.setLore(new ArrayList<>(Arrays.asList(
                    ChatColor.GREEN + "使用することで" + sec + "秒" + magnification + "倍経験値獲得することができます！"
            )));
            item.setItemMeta(meta);
            new ItemData(item, name, sec, magnification);
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            PlayerData data = getPlayerData(player.getUniqueId());
            if (data == null) {
                new PlayerData(player);
            }
            statusManager.reJoinUpdateStatus(player);
        }
        this.boostLv1Key = new NamespacedKey(this, "boostLv1");
        this.boostLv2Key = new NamespacedKey(this, "boostLv2");
        this.boostLv3Key = new NamespacedKey(this, "boostLv3");
        this.timeKey = new NamespacedKey(this, "time");
        this.coolTimeKey = new NamespacedKey(this,"coolTime");
        this.skillKey = new NamespacedKey(this,"skill");
        this.usedSkillKey = new NamespacedKey(this,"usedSkill");

        skillPut();

        getCommand("levelsystem").setExecutor(new Cmd());
        getCommand("levelsystem").setTabCompleter(new Cmd());

        getServer().getPluginManager().registerEvents(new EXPBoostListener(), this);
        getServer().getPluginManager().registerEvents(new JoinListener(), this);
        getServer().getPluginManager().registerEvents(new QuitListener(), this);
        getServer().getPluginManager().registerEvents(new CommandListener(), this);
        getServer().getPluginManager().registerEvents(new EntityKillListener(), this);
        getServer().getPluginManager().registerEvents(new StatusMenuListener(), this);
        getServer().getPluginManager().registerEvents(new SkillMenuListener(), this);
        getServer().getPluginManager().registerEvents(new LevelUpListener(), this);
        getServer().getPluginManager().registerEvents(new AcquisitionSkillListener(), this);
        getServer().getPluginManager().registerEvents(new ExpGainListener(), this);
        getServer().getPluginManager().registerEvents(new SkillActiveListener(), this);
        getServer().getPluginManager().registerEvents(new DimensionBoxListener(), this);

        getServer().getScheduler().runTaskTimer(this, new BoostRunnable(this, this.bars), 0, 20);
        getServer().getScheduler().runTaskTimer(this, new CoolTimeRunnable(this,this.coolBars), 0, 20);

    }

    public String secToTime(int sec) {
        int min = (sec % 3600) / 60;
        int s = sec % 60;
        String ss = String.valueOf(s);
        if (s < 10) {
            ss = "0" + s;
        }
        if (min < 10) {
            return "0" + min + " : " + ss;
        }

        return min + " : " + ss;
    }

    public NamespacedKey getCoolTimeKey() {
        return coolTimeKey;
    }

    public NamespacedKey getTimeKey() {
        return timeKey;
    }

    public NamespacedKey getSkillKey() {
        return skillKey;
    }

    public NamespacedKey getUsedSkillKey() {
        return usedSkillKey;
    }

    private PlayerData getPlayerData(UUID uuid) {
        for (PlayerData data : PlayerData.data) {
            if (data.getUuid().equals(uuid))
                return data;
        }
        return null;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }

    public NamespacedKey getBoostLv1Key() {
        return boostLv1Key;
    }

    public NamespacedKey getBoostLv2Key() {
        return boostLv2Key;
    }

    public NamespacedKey getBoostLv3Key() {
        return boostLv3Key;
    }

    private void skillPut(){
        skills.put(SkillType.SLASH,new Slash());
        skills.put(SkillType.SHIELD_BASH,new ShieldBash());
        skills.put(SkillType.AUTO_SMELT,new AutoSmelt());
        skills.put(SkillType.JUMP_SLASH,new JumpSlash());
        skills.put(SkillType.EXPLODE_ARROW,new ExplodeArrow());
        skills.put(SkillType.TELEPORT,new Teleport());
        skills.put(SkillType.MULTI_SHOOT,new MultiShoot());
        skills.put(SkillType.AUTO_HARVEST,new AutoHarvest());
        skills.put(SkillType.DIMENSION_BOX,new DimensionBox());
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        for (Player player : Bukkit.getOnlinePlayers()) {
            statusManager.statusReset(player);
        }
    }
}
