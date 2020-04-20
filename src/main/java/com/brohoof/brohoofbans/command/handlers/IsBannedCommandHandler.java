package com.brohoof.brohoofbans.command.handlers;

import java.util.Optional;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Data;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.IsBannedCommand;

public class IsBannedCommandHandler extends AbstractCommandHandler {

    private IsBannedCommand isBannedCommand;

    public IsBannedCommandHandler(BrohoofBansPlugin plugin, Data data, Settings settings) {
        super(data);
        isBannedCommand = new IsBannedCommand(plugin, data, settings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("isbanned")) {
            // Format = /isbanned <playerName | UUID> args[0] is player.
            if (args.length < 1)
                return false;
            OfflinePlayer player = getPlayer(args[0]);
            Optional<Ban> ban = data.getBan(player.getUniqueId());
            if (ban.isPresent())
                return isBannedCommand.execute(sender, ban.get());
            return isBannedCommand.execute(sender, player.getName());
        }
        return false;
    }
}
