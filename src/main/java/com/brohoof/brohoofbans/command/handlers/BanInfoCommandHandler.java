package com.brohoof.brohoofbans.command.handlers;

import java.util.Optional;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Data;
import com.brohoof.brohoofbans.ExpireConverter;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.BanInfoCommand;

public class BanInfoCommandHandler extends AbstractCommandHandler {

    private BanInfoCommand banInfoCommand;

    public BanInfoCommandHandler(BrohoofBansPlugin plugin, Data data, Settings settings, ExpireConverter converter) {
        super(data);
        banInfoCommand = new BanInfoCommand(plugin, data, converter, settings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("baninfo")) {
            // Format = /baninfo [-i] <playerName | UUID> args[0] is either -i or player. args[1] is player if -1
            if (args.length < 1)
                return false;
            if (args[0].equalsIgnoreCase("-i")) {
                OfflinePlayer player = this.getPlayer(args[1]);
                Optional<Ban> ban = data.getBan(player.getUniqueId());
                if (ban.isPresent())
                    return banInfoCommand.execute(sender, ban.get(), true);
                return banInfoCommand.execute(sender, player.getName());
            }
            // Not a -i
            OfflinePlayer player = this.getPlayer(args[0]);
            Optional<Ban> ban = data.getBan(player.getUniqueId());
            if (ban.isPresent())
                return banInfoCommand.execute(sender, ban.get(), false);
            return banInfoCommand.execute(sender, player.getName());
        }
        return false;
    }
}
