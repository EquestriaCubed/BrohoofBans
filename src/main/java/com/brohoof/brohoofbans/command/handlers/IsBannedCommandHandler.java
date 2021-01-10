package com.brohoof.brohoofbans.command.handlers;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.IsBannedCommand;

public class IsBannedCommandHandler extends AbstractCommandHandler {

    private IsBannedCommand isBannedCommand;

    public IsBannedCommandHandler(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(api);
        isBannedCommand = new IsBannedCommand(plugin, api, settings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("isbanned")) {
            // Format = /isbanned <playerName | UUID> args[0] is player.
            if (args.length < 1)
                return false;
            OfflinePlayer player = getPlayer(args[0]);
            return isBannedCommand.execute(sender, player);
        }
        return false;
    }
}
