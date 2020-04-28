package com.brohoof.brohoofbans.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.sweetiebelle.lib.SweetieLib;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Settings;

public class ReloadCommand extends AbstractCommand {

    public ReloadCommand(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(plugin, api, settings);
    }

    public boolean execute(CommandSender sender) {
        if (sender.hasPermission("brohoofbans.reload")) {
            sender.sendMessage(SweetieLib.NO_PERMISSION);
            return true;
        }
        settings.reloadSettings();
        sender.sendMessage(ChatColor.YELLOW + "[BrohoofBans] Config reloaded.");
        return true;
    }
}
