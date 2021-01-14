package com.brohoof.brohoofbans.command.handlers;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.ExpireConverter;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.BanCommand;

public class BanCommandHandler extends AbstractCommandHandler {

    private BanCommand banCommand;

    public BanCommandHandler(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(api);
        banCommand = new BanCommand(plugin, api, settings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ban")) {
            // Format = /ban [-t] [time] playerName | UUID reason... args[0 OR 1] is the
            // name or UUID, other args are reason
            if (args.length < 2)
                return false;
            Boolean isTemporary = false;
            String expires = "NEVER";
            if (args[0].equalsIgnoreCase("-t"))
                try {
                    expires = String.valueOf(ExpireConverter.getExpires(args[1]));
                    isTemporary = true;
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ChatColor.RED + "That is not a valid time format. " + ChatColor.WHITE + "1h-5m " + ChatColor.RED + "is a valid format.");
                    return true;
                }
            if (isTemporary && args.length <= 3)
                return false;
            // The player is online.
            OfflinePlayer victim = getPlayer(args[isTemporary ? 2 : 0]);
            String reason = getReason(args, isTemporary);
            return banCommand.execute(sender, victim, expires, reason);
        }
        return false;
    }
}
