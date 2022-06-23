package com.brohoof.brohoofbans.command;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import net.shonx.lib.SweetieLib;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Settings;

public class UnbanCommand extends AbstractCommand {

    public UnbanCommand(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(plugin, api, settings);
    }

    public boolean execute(CommandSender sender, OfflinePlayer player) {
        CompletableFuture<Optional<Ban>> future = this.getBan(player);
        future.thenAccept((ban) -> {
            if (!sender.hasPermission("brohoofbans.unban")) {
                sender.sendMessage(SweetieLib.NO_PERMISSION);
                return;
            }
            if (ban.isPresent()) {
                api.unban(ban.get()).thenAccept((object) -> {
                    sender.sendMessage(ChatColor.YELLOW + ban.get().getVictimName() + " unbanned.");
                });
                return;
            }
            sender.sendMessage(ChatColor.RED + player.getName() + " is not banned.");
        });
        return true;
    }
}
