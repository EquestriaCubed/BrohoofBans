package com.brohoof.brohoofbans.command;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Data;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.handlers.AbstractCommandHandler;

public class ReloadCommand extends AbstractCommand {

    public ReloadCommand(BrohoofBansPlugin plugin, Data data, Settings settings) {
        super(plugin, data, settings);
    }

    public boolean execute(CommandSender sender) {
        if (sender.hasPermission("brohoofbans.reload")) {
            sender.sendMessage(AbstractCommandHandler.NO_PERMISSION);
            return true;
        }
        data.forceConnectionRefresh();
        settings.reloadSettings();
        sender.sendMessage(ChatColor.YELLOW + "[BrohoofBans] Config reloaded.");
        return true;
    }
}
