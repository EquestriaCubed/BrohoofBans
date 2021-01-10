package com.brohoof.brohoofbans.command;

import org.bukkit.OfflinePlayer;
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

    public boolean execute(OfflinePlayer victim, String expires, String reason) {
        Ban ban = new Ban(victim.getUniqueId(), SweetieLib.CONSOLE_UUID, victim.getName(), SweetieLib.CONSOLE_NAME, "NULL", "127.0.0.1", expires, reason, false);
        saveBan(ban);
        return true;
    }

    public boolean execute(Player sender, OfflinePlayer victim, String expires, String reason) {
        if (!sender.hasPermission("brohoofbans.ban")) {
            sender.sendMessage(SweetieLib.NO_PERMISSION);
            return true;
        }
        Ban ban = new Ban(victim.getUniqueId(), sender.getUniqueId(), victim.getName(), sender.getName(), "NULL", getIP(sender.getAddress()), expires, reason, false);
        saveBan(ban);
        return true;
    }

    public boolean execute(Player sender, Player victim, String expires, String reason) {
        if (!sender.hasPermission("brohoofbans.ban")) {
            sender.sendMessage(SweetieLib.NO_PERMISSION);
            return true;
        }
        Ban ban = new Ban(victim.getUniqueId(), sender.getUniqueId(), victim.getName(), sender.getName(), getIP(victim.getAddress()), getIP(sender.getAddress()), expires, reason, false);
        victim.kickPlayer("You are banned for:\n" + reason);
        saveBan(ban);
        return true;
    }

    public boolean execute(Player victim, String expires, String reason) {
        Ban ban = new Ban(victim.getUniqueId(), SweetieLib.CONSOLE_UUID, victim.getName(), SweetieLib.CONSOLE_NAME, getIP(victim.getAddress()), "127.0.0.1", expires, reason, false);
        victim.kickPlayer("You are banned for:\n" + reason);
        saveBan(ban);
        return true;
    }

    private void saveBan(Ban ban) {
        plugin.getLogger().info(ban.toString());
        api.ban(ban).thenAccept((object) -> {
            broadcastBanMessage(ban);
        });

    }
}
