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

public class IsBannedCommand extends AbstractCommand {

    public IsBannedCommand(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(plugin, api, settings);
    }

    public boolean execute(CommandSender sender, OfflinePlayer player) {
        CompletableFuture<Optional<Ban>> future = this.getBan(player);
        future.thenAccept((oBan) -> {
            if (!sender.hasPermission("brohoofbans.isbanned")) {
                sender.sendMessage(SweetieLib.NO_PERMISSION);
                return;
            }
            if (oBan.isPresent()) {
                Ban ban = oBan.get();
                if (ban.isSuspension()) {
                    sender.sendMessage(ChatColor.YELLOW + ban.getVictimName() + " " + ChatColor.RED + "is" + ChatColor.YELLOW + " suspended.");
                    return;
                }
                sender.sendMessage(ChatColor.YELLOW + ban.getVictimName() + " " + ChatColor.RED + "is" + ChatColor.YELLOW + " banned.");
                return;
            }
            sender.sendMessage(ChatColor.YELLOW + player.getName() + " is " + ChatColor.RED + "not" + ChatColor.YELLOW + " banned.");
        });
        return true;
    }
}
