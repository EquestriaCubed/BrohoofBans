package com.brohoof.brohoofbans.command.handlers;

import java.util.Optional;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.ExpireConverter;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.BanInfoCommand;

public class BanInfoCommandHandler extends AbstractCommandHandler {

    private BanInfoCommand banInfoCommand;

    public BanInfoCommandHandler(BrohoofBansPlugin plugin, API api, Settings settings, ExpireConverter converter) {
        super(api);
        banInfoCommand = new BanInfoCommand(plugin, api, converter, settings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("baninfo")) {
            // Format = /baninfo [-i] <playerName | UUID> args[0] is either -i or player. args[1] is player if -1
            if (args.length < 1)
                return false;
            if (args[0].equalsIgnoreCase("-i")) {
                OfflinePlayer player = getPlayer(args[1]);
                Optional<Ban> ban = BrohoofBansPlugin.getAPI().getBan(player.getUniqueId());
                if (ban.isPresent())
                    return banInfoCommand.execute(sender, ban.get(), true);
                return banInfoCommand.execute(sender, player.getName());
            }
            // Not a -i
            OfflinePlayer player = getPlayer(args[0]);
            Optional<Ban> ban = BrohoofBansPlugin.getAPI().getBan(player.getUniqueId());
            if (ban.isPresent())
                return banInfoCommand.execute(sender, ban.get(), false);
            return banInfoCommand.execute(sender, player.getName());
        }
        return false;
    }
}
