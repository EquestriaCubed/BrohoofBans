package com.brohoof.brohoofbans.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.sweetiebelle.lib.SweetieLib;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Settings;

public class UnbanCommand extends AbstractCommand {

    public UnbanCommand(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(plugin, api, settings);
    }

    public boolean execute(CommandSender sender, Ban ban) {
        if (!sender.hasPermission("brohoofbans.unban")) {
            sender.sendMessage(SweetieLib.NO_PERMISSION);
            return true;
        }
        api.unban(ban);
        sender.sendMessage(ChatColor.YELLOW + ban.getVictimName() + " unbanned.");
        return true;
    }

    public boolean execute(CommandSender sender, String playerName) {
        if (!sender.hasPermission("brohoofbans.unban")) {
            sender.sendMessage(SweetieLib.NO_PERMISSION);
            return true;
        }
        sender.sendMessage(ChatColor.RED + playerName + " is not banned.");
        return true;
    }
}
