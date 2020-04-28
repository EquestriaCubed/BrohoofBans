package com.brohoof.brohoofbans.command.handlers;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.SuspendCommand;

public class SuspendCommandHandler extends AbstractCommandHandler {

    private SuspendCommand suspendCommand;

    public SuspendCommandHandler(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(api);
        suspendCommand = new SuspendCommand(plugin, api, settings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("suspend")) {
            // Format = /suspend playerName | UUID reason... args[0] is the name or UUID, other args are reason
            if (args.length < 2)
                return false;
            // The player is online.
            OfflinePlayer victim = getPlayer(args[0]);
            String reason = getReason(args, false);
            if (victim instanceof Player) {
                if (sender instanceof Player)
                    // Sender is a Player
                    return suspendCommand.execute((Player) sender, (Player) victim, reason);
                // It's console
                return suspendCommand.execute((Player) victim, reason);
            }
            // Offline player
            if (sender instanceof Player)
                // Sender is a Player
                return suspendCommand.execute((Player) sender, victim, reason);
            // It's console
            return suspendCommand.execute(victim, reason);
        }
        return false;
    }
}
