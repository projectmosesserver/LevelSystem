package info.ahaha.levelsystem.cmd;

import info.ahaha.levelsystem.ItemData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Cmd implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))return true;
        Player player = (Player) sender;
        if (!player.isOp())return true;
        for (ItemData data : ItemData.data){
            if (args[0].equalsIgnoreCase(data.getName())){
                player.getInventory().addItem(data.getItem());
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (!(sender instanceof Player))return null;
        if (args.length == 1){
            List<String>list = new ArrayList<>();
            for (ItemData data : ItemData.data){
                list.add(data.getName());
            }
            return list;
        }
        return null;
    }
}
