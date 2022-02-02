package info.ahaha.levelsystem.skill;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AutoHarvest implements Skill {
    @Override
    public String getName() {
        return "AutoHarvest";
    }

    @Override
    public SkillType getType() {
        return SkillType.AUTO_HARVEST;
    }

    @Override
    public String getExplanation() {
        return "成長しきった作物を自動で収穫し植える";
    }

    @Override
    public Material getIconMaterial() {
        return Material.IRON_HOE;
    }

    @Override
    public ItemStack getIcon() {
        ItemStack item = new ItemStack(getIconMaterial());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + getName());
        meta.setLore(new ArrayList<>(Arrays.asList(
                ChatColor.GRAY + "----------------------------",
                ChatColor.GREEN + getExplanation(),
                "",
                ChatColor.YELLOW + "対応アイテム" + ChatColor.GRAY + " : " + ChatColor.GREEN + "クワ",
                ChatColor.YELLOW + "発動条件" + ChatColor.GRAY + " : " + ChatColor.GREEN + "クワで右クリック",
                ChatColor.YELLOW + "クールタイム" + ChatColor.GRAY + " : " + ChatColor.GREEN + getCoolTime() + " 秒",
                ChatColor.YELLOW + "ダメージ" + ChatColor.GRAY + " : " + ChatColor.GREEN + (int)getAmount(),
                ChatColor.GRAY + "----------------------------"

        )));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getAcquisitionLevel() {
        return 13;
    }

    @Override
    public List<String> getAvailableTools() {
        return LevelSystem.plugin.materialToolUtil.getHoes();
    }

    @Override
    public int getCoolTime() {
        return 0;
    }

    @Override
    public double getAmount() {
        return 0;
    }

    @Override
    public void skillActive(Player player) {

    }

    public void skillActive(Player player, Block block) {
        if (block != null) {
            if (block.getBlockData() instanceof Ageable) {
                Ageable ageable = (Ageable) block.getBlockData();
                Material material = block.getType();
                Material seed = null;
                if (!isUse(player)) return;
                if (ageable.getAge() == ageable.getMaximumAge()) {
                    Random r = new Random();
                    switch (material) {
                        case WHEAT:
                            seed = Material.WHEAT_SEEDS;
                            break;
                        case POTATOES:
                            material = Material.POTATO;
                            break;
                        case CARROTS:
                            material = Material.CARROT;
                            break;
                        case BEETROOTS:
                            material = Material.BEETROOT;
                            seed = Material.BEETROOT_SEEDS;
                            break;
                        case SWEET_BERRY_BUSH:
                            material = Material.SWEET_BERRIES;
                            break;
                        case MELON_STEM:
                        case PUMPKIN_STEM:
                            material = null;
                            break;


                    }
                    if (material == null) return;
                    try {
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(material, r.nextInt(3)));
                        ageable.setAge(0);
                        block.setBlockData(ageable);
                        if (seed != null) {
                            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(seed, r.nextInt(3)));
                        }
                    } catch (IllegalArgumentException ec) {
                        return;
                    }
                }
            }
        }
    }
}
