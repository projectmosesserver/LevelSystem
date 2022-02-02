package info.ahaha.levelsystem.skill;

import info.ahaha.levelsystem.LevelSystem;
import info.ahaha.levelsystem.Skill;
import info.ahaha.levelsystem.SkillType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.bukkit.Bukkit.getLogger;

public class AutoSmelt implements Skill {
    @Override
    public String getName() {
        return "AutoSmelt";
    }

    @Override
    public SkillType getType() {
        return SkillType.AUTO_SMELT;
    }

    @Override
    public String getExplanation() {
        return "鉱石を壊したときにドロップする原石をインゴットに自動精錬する";
    }

    @Override
    public Material getIconMaterial() {
        return Material.IRON_PICKAXE;
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
                ChatColor.YELLOW + "対応アイテム" + ChatColor.GRAY + " : " + ChatColor.GREEN + "ピッケル",
                ChatColor.YELLOW + "発動条件" + ChatColor.GRAY + " : " + ChatColor.GREEN + "ピッケルで鉱石を採掘",
                ChatColor.YELLOW + "クールタイム" + ChatColor.GRAY + " : " + ChatColor.GREEN + getCoolTime() + " 秒",
                ChatColor.YELLOW + "ダメージ" + ChatColor.GRAY + " : " + ChatColor.GREEN + (int) getAmount(),
                ChatColor.GRAY + "----------------------------"

        )));
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public int getAcquisitionLevel() {
        return 10;
    }

    @Override
    public List<String> getAvailableTools() {
        return LevelSystem.plugin.materialToolUtil.getPickAxes();
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

    public void skillActive(BlockDropItemEvent e, Material block) {
        if (!isUse(e.getPlayer()))return;
        if (block.name().contains("IRON_ORE") ||
                block.name().contains("GOLD_ORE") ||
                block.name().contains("COPPER_ORE")
        ) {
            final int size = e.getItems().get(0).getItemStack().getAmount();
            if (size == 0)return;
            e.getItems().clear();
            Material material = null;
            if (block.name().contains("IRON_ORE")) material = Material.IRON_INGOT;
            if (block.name().contains("GOLD_ORE")) material = Material.GOLD_INGOT;
            if (block.name().contains("COPPER_ORE")) material = Material.COPPER_INGOT;
            if (material == null) return;
            for (int i = 0; i < size; i++) {
                e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), new ItemStack(material));
            }
        }
    }
}
