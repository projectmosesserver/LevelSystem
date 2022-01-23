package info.ahaha.levelsystem.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MessageUtil {
    JavaPlugin plugin;

    public MessageUtil(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.GOLD + "[ " + plugin.getName() + " ] " + ChatColor.GREEN + message);
    }

    public void sendError(Player player, String error) {
        player.sendMessage(ChatColor.GOLD + "[ " + plugin.getName() + " ] " + ChatColor.RED + error);
    }
}
