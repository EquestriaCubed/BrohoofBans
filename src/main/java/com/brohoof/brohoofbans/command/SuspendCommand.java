package com.brohoof.brohoofbans.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Data;
import com.brohoof.brohoofbans.Settings;
import com.brohoof.brohoofbans.command.handlers.AbstractCommandHandler;

public class SuspendCommand extends AbstractCommand {

    public SuspendCommand(BrohoofBansPlugin plugin, Data data, Settings settings) {
        super(plugin, data, settings);
    }

    public boolean execute(OfflinePlayer victim, String reason) {
        Ban ban = new Ban(victim.getUniqueId(), AbstractCommandHandler.CONSOLE_UUID, victim.getName(), "CONSOLE", "NULL", "127.0.0.1", "NEVER", reason, true);
        saveBan(ban);
        return true;
    }

    public boolean execute(Player sender, OfflinePlayer victim, String reason) {
        if (!sender.hasPermission("brohoofbans.suspend")) {
            sender.sendMessage(AbstractCommandHandler.NO_PERMISSION);
            return true;
        }
        Ban ban = new Ban(victim.getUniqueId(), sender.getUniqueId(), victim.getName(), sender.getName(), "NULL", getIP(sender.getAddress()), "NEVER", reason, true);
        saveBan(ban);
        return true;
    }

    public boolean execute(Player sender, Player victim, String reason) {
        if (!sender.hasPermission("brohoofbans.suspend")) {
            sender.sendMessage(AbstractCommandHandler.NO_PERMISSION);
            return true;
        }
        Ban ban = new Ban(victim.getUniqueId(), sender.getUniqueId(), victim.getName(), sender.getName(), getIP(victim.getAddress()), getIP(sender.getAddress()), "NEVER", reason, true);
        victim.kickPlayer(settings.suspendReason);
        saveBan(ban);
        return true;
    }

    public boolean execute(Player victim, String reason) {
        Ban ban = new Ban(victim.getUniqueId(), AbstractCommandHandler.CONSOLE_UUID, victim.getName(), "CONSOLE", getIP(victim.getAddress()), "127.0.0.1", "NEVER", reason, true);
        victim.kickPlayer(settings.suspendReason);
        saveBan(ban);
        return true;
    }

    private void saveBan(Ban ban) {
        plugin.getLogger().info(ban.toString());
        data.saveBan(ban);
        broadcastBanMessage(ban);
    }
}
