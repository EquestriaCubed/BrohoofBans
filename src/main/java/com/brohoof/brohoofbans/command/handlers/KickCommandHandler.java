package com.brohoof.brohoofbans.command.handlers;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Data;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.KickCommand;

public class KickCommandHandler extends AbstractCommandHandler {

    private KickCommand kickCommand;

    public KickCommandHandler(BrohoofBansPlugin plugin, Data data, Settings settings) {
        super(data);
        kickCommand = new KickCommand(plugin, data, settings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kick")) {
            // Format = /kick playerName | UUID reason... args[0 OR 1] is the name or UUID, other args are reason
            if (args.length < 2)
                return false;
            OfflinePlayer target = getPlayer(args[0]);
            final String reason = getReason(args, false);
            if (target instanceof Player)
                return kickCommand.execute(sender, (Player) target, reason);
            return kickCommand.execute(sender);
        }
        return false;
    }
}
