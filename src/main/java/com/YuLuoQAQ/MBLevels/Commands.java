/**
 * Author: YuLuoQAQ (Github)
 * Github Page: https://github.com/YuLuoQAQ/
 * Respository: https://github.com/YuLuoQAQ/MBLevels
 * Version: 1.0
 */
package com.YuLuoQAQ.MBLevels;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    private final MBLevels plugin;

    public Commands(MBLevels plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp()) {
                    player.sendMessage("§cYou do not have permission to use this command!");
                    return true;
                }
            } else if (!sender.isOp()) {
                sender.sendMessage("§cYou do not have permission to use this command!");
                return true;
            }
            plugin.reloadConfig();
            sender.sendMessage("§aConfig reloaded and cache cleared!");
            return true;
        }
        sender.sendMessage("§cUsage: /bwlevel reload");
        return false;
    }
}
