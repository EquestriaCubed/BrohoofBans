package com.brohoof.brohoofbans.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Data;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.handlers.AbstractCommandHandler;

public class KickCommand extends AbstractCommand {

    public KickCommand(BrohoofBansPlugin plugin, Data data, Settings settings) {
        super(plugin, data, settings);
    }

    public boolean execute(CommandSender sender) {
        sender.sendMessage(ChatColor.RED + "That player is not online.");
        return true;
    }

    public boolean execute(CommandSender sender, Player target, String reason) {
        if (!sender.hasPermission("brohoofbans.kick")) {
            sender.sendMessage(AbstractCommandHandler.NO_PERMISSION);
            return true;
        }
        target.kickPlayer(reason);
        broadcastKickMessage(target, reason);
        if (sender instanceof Player)
            plugin.getLogger().info(((Player) sender).getName() + " kicked " + target.getName() + " for " + reason + ".");
        else
            plugin.getLogger().info("CONSOLE kicked " + target.getName() + " for " + reason + ".");
        return true;
    }
}
