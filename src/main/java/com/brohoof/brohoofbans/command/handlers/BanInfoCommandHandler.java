package com.brohoof.brohoofbans.command.handlers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.ExpireConverter;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.BanInfoCommand;

public class BanInfoCommandHandler extends AbstractCommandHandler {

    private BanInfoCommand banInfoCommand;

    public BanInfoCommandHandler(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(api);
        banInfoCommand = new BanInfoCommand(plugin, api, settings);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("baninfo")) {
            // Format = /baninfo [-i] <playerName | UUID> args[0] is either -i or player.
            // args[1] is player if -1
            if (args.length < 1)
                return false;
            boolean wantsFullInfo = args[0].equalsIgnoreCase("-i");
            return banInfoCommand.execute(sender, this.getPlayer(wantsFullInfo ? args[1] : args[0]), wantsFullInfo);
        }
        return false;
    }
}
