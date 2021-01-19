package com.brohoof.brohoofbans.command;

import java.util.UUID;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.sweetiebelle.lib.SweetieLib;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Settings;

public class SuspendCommand extends AbstractCommand {

    public SuspendCommand(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(plugin, api, settings);
    }

    public boolean execute(CommandSender sender, OfflinePlayer victim, String reason) {
        if (!sender.hasPermission("brohoofbans.suspend")) {
            sender.sendMessage(SweetieLib.NO_PERMISSION);
            return true;
        }
        UUID victimUUID = victim.getUniqueId();
        UUID executorUUID = (sender instanceof OfflinePlayer) ? ((OfflinePlayer) sender).getUniqueId() : SweetieLib.CONSOLE_UUID;
        String victimName = victim.getName();
        String executorName = (sender instanceof OfflinePlayer) ? ((OfflinePlayer) sender).getName() : SweetieLib.CONSOLE_NAME;
        String victimIP = (victim instanceof Player) ? API.getAddress((Player) victim) : "NULL";
        String executorIP = (sender instanceof Player) ? API.getAddress((Player) sender) : "127.0.0.1";
        Ban ban = new Ban(victimUUID, executorUUID, victimName, executorName, victimIP, executorIP, -1, reason, true);

        if (victim instanceof Player)
            ((Player) victim).kickPlayer(settings.suspendReason);
        plugin.getLogger().info(ban.toString());
        api.ban(ban).thenAccept((object) -> {
            broadcastBanMessage(ban);
        });
        return true;
    }
}
