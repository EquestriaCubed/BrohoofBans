package com.brohoof.brohoofbans.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.sweetiebelle.lib.SweetieLib;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Settings;

public class IsBannedCommand extends AbstractCommand {

    public IsBannedCommand(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(plugin, api, settings);
    }

    public boolean execute(CommandSender sender, Ban ban) {
        if (!sender.hasPermission("brohoofbans.isbanned")) {
            sender.sendMessage(SweetieLib.NO_PERMISSION);
            return true;
        }
        if (ban.isSuspension()) {
            sender.sendMessage(ChatColor.YELLOW + ban.getVictimName() + " " + ChatColor.RED + "is" + ChatColor.YELLOW + " suspended.");
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW + ban.getVictimName() + " " + ChatColor.RED + "is" + ChatColor.YELLOW + " banned.");
        return true;
    }

    public boolean execute(CommandSender sender, String playerName) {
        if (!sender.hasPermission("brohoofbans.isbanned")) {
            sender.sendMessage(SweetieLib.NO_PERMISSION);
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW + playerName + " is " + ChatColor.RED + "not" + ChatColor.YELLOW + " banned.");
        return true;
    }
}
