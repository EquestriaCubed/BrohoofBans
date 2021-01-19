package com.brohoof.brohoofbans.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.sweetiebelle.lib.SweetieLib;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Settings;

public class BanCommand extends AbstractCommand {

    public BanCommand(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(plugin, api, settings);
    }

    public boolean execute(CommandSender sender, OfflinePlayer victim, long expires, String reason) {
        if (!sender.hasPermission("brohoofbans.ban")) {
            sender.sendMessage(SweetieLib.NO_PERMISSION);
            return true;
        }
        String victimAddress = "NULL";
        if (victim instanceof Player) {
            victimAddress = API.getAddress((Player) victim);
            ((Player) victim).kickPlayer("You are banned for:\n" + reason);
        }
        Ban ban;
        if (sender instanceof Player)
            ban = new Ban(victim.getUniqueId(), ((OfflinePlayer) sender).getUniqueId(), victim.getName(), sender.getName(), victimAddress, API.getAddress((Player) sender), expires, reason, false);
        else
            ban = new Ban(victim.getUniqueId(), SweetieLib.CONSOLE_UUID, victim.getName(), SweetieLib.CONSOLE_NAME, victimAddress, "127.0.0.1", expires, reason, false);
        plugin.getLogger().info(ban.toString());
        api.ban(ban).thenAccept((object) -> {
            broadcastBanMessage(ban);
        });
        return true;
    }
}