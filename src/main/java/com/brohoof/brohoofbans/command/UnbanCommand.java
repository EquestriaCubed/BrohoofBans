package com.brohoof.brohoofbans.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Data;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.handlers.AbstractCommandHandler;

public class UnbanCommand extends AbstractCommand {

    public UnbanCommand(BrohoofBansPlugin plugin, Data data, Settings settings) {
        super(plugin, data, settings);
    }

    public boolean execute(CommandSender sender, Ban ban) {
        if (!sender.hasPermission("brohoofbans.unban")) {
            sender.sendMessage(AbstractCommandHandler.NO_PERMISSION);
            return true;
        }
        data.unban(ban);
        sender.sendMessage(ChatColor.YELLOW + ban.getVictimName() + " unbanned.");
        return true;
    }

    public boolean execute(CommandSender sender, String playerName) {
        if (!sender.hasPermission("brohoofbans.unban")) {
            sender.sendMessage(AbstractCommandHandler.NO_PERMISSION);
            return true;
        }
        sender.sendMessage(ChatColor.RED + playerName + " is not banned.");
        return true;
    }
}
