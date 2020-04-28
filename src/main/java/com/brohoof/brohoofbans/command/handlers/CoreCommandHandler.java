package com.brohoof.brohoofbans.command.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.ReloadCommand;

public class CoreCommandHandler extends AbstractCommandHandler {

    private ReloadCommand reloadCommand;

    public CoreCommandHandler(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(api);
        reloadCommand = new ReloadCommand(plugin, api, settings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("brohoofbans")) {
            if (args.length == 0)
                return false;
            if (args[0].equalsIgnoreCase("reload"))
                return reloadCommand.execute(sender);
            return false;
        }
        return false;
    }
}
