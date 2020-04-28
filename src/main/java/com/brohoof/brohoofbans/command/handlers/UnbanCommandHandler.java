package com.brohoof.brohoofbans.command.handlers;

import java.util.Optional;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.UnbanCommand;

public class UnbanCommandHandler extends AbstractCommandHandler {

    private UnbanCommand unbanCommand;

    public UnbanCommandHandler(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(api);
        unbanCommand = new UnbanCommand(plugin, api, settings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("unban")) {
            // Format = /unban <playerName | UUID> args[0] is player.
            if (args.length < 1)
                return false;
            OfflinePlayer player = getPlayer(args[0]);
            Optional<Ban> ban = api.getBan(player.getUniqueId());
            if (ban.isPresent())
                return unbanCommand.execute(sender, ban.get());
            return unbanCommand.execute(sender, player.getName());
        }
        return false;
    }
}
