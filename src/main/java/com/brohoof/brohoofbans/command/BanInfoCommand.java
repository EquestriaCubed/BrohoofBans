package com.brohoof.brohoofbans.command;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.sweetiebelle.lib.SweetieLib;

import com.brohoof.brohoofbans.API;
import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.ExpireConverter;
import com.brohoof.brohoofbans.Settings;

public class BanInfoCommand extends AbstractCommand {


    public BanInfoCommand(BrohoofBansPlugin plugin, API api, Settings settings) {
        super(plugin, api, settings);
    }

    public boolean execute(CommandSender sender, OfflinePlayer player, boolean wantsFullInfo) {
        CompletableFuture<Optional<Ban>> future = this.getBan(player);
        future.thenAccept((ban) -> {
            if (ban.isPresent()) {
                if (wantsFullInfo) {
                    if (sender.hasPermission("brohoofbans.baninfo.admin")) {
                        sendFullData(sender, ban.get());
                        return;
                    }
                    sender.sendMessage(SweetieLib.NO_PERMISSION);
                    return;
                }
                if (sender.hasPermission("brohoofbans.baninfo")) {
                    sendPartialData(sender, ban.get());
                    return;
                }
                sender.sendMessage(SweetieLib.NO_PERMISSION);
                return;
            } else
                sender.sendMessage(ChatColor.YELLOW + player.getName() + " is " + ChatColor.RED + "not" + ChatColor.YELLOW + " banned.");
        });
        return true;
    }

    private void sendFullBanData(CommandSender sender, Ban ban) {
        sender.sendMessage(ChatColor.YELLOW + ban.getVictimName() + " " + ChatColor.RED + "is " + ChatColor.YELLOW + "banned.");
        sender.sendMessage(ChatColor.YELLOW + "Victim UUID is " + ban.getVictim().toString() + ". Their name is " + ban.getVictimName());
        sender.sendMessage(ChatColor.YELLOW + "Issuer UUID is " + ban.getExecutor().toString() + ". Their name is " + ban.getExecutorName());
        sender.sendMessage(ChatColor.YELLOW + "The Issuer's IP is " + ban.getExecutorIP() + ". The Victim's IP is " + ban.getVictimIP());
        if (ban.getExpires().equals("NEVER"))
            sender.sendMessage(ChatColor.YELLOW + "The Victim is banned for " + ChatColor.WHITE + ban.getReason() + ChatColor.YELLOW + ". This ban does " + ChatColor.RED + "not " + ChatColor.YELLOW + "expire.");
        else
            sender.sendMessage(ChatColor.YELLOW + "The Victim is banned for " + ChatColor.WHITE + ban.getReason() + ChatColor.YELLOW + ". This ban expires at " + ChatColor.WHITE + ExpireConverter.getFriendlyTime(Long.parseLong(ban.getExpires())));
    }

    private void sendFullData(CommandSender sender, Ban ban) {
        if (ban.isSuspension())
            sendFullSuspensionData(sender, ban);
        else
            sendFullBanData(sender, ban);
    }

    private void sendFullSuspensionData(CommandSender sender, Ban ban) {
        sender.sendMessage(ChatColor.YELLOW + ban.getVictimName() + ChatColor.RED + "is " + ChatColor.YELLOW + "suspended.");
        sender.sendMessage(ChatColor.YELLOW + "Victim UUID is " + ban.getVictim().toString() + ". Their name is " + ban.getVictimName());
        sender.sendMessage(ChatColor.YELLOW + "Issuer UUID is " + ban.getExecutor().toString() + ". Their name is " + ban.getExecutorName());
        sender.sendMessage(ChatColor.YELLOW + "The Victim is suspended for " + ChatColor.WHITE + ban.getReason());
        sender.sendMessage(ChatColor.YELLOW + "The Issuer's IP is " + ban.getExecutorIP() + ". The Victim's IP is " + ban.getVictimIP());
    }

    private void sendPartialBanData(CommandSender sender, Ban ban) {
        sender.sendMessage(ChatColor.YELLOW + ban.getVictimName() + " " + ChatColor.RED + "is " + ChatColor.YELLOW + "banned.");
        sender.sendMessage(ChatColor.YELLOW + "Victim UUID is " + ban.getVictim().toString() + ". Their name is " + ban.getVictimName());
        if (ban.getExpires().equalsIgnoreCase("NEVER"))
            sender.sendMessage(ChatColor.YELLOW + "The Victim is banned for " + ChatColor.WHITE + ban.getReason() + ChatColor.YELLOW + ". This ban does " + ChatColor.RED + "not " + ChatColor.YELLOW + "expire.");
        else
            sender.sendMessage(ChatColor.YELLOW + "The Victim is banned for " + ChatColor.WHITE + ban.getReason() + ChatColor.YELLOW + ". This ban expires at " + ChatColor.WHITE + ExpireConverter.getFriendlyTime(Long.parseLong(ban.getExpires())));
    }

    private void sendPartialData(CommandSender sender, Ban ban) {
        if (ban.isSuspension())
            sendPartialSuspensionData(sender, ban);
        else
            sendPartialBanData(sender, ban);
    }

    private void sendPartialSuspensionData(CommandSender sender, Ban ban) {
        sender.sendMessage(ChatColor.YELLOW + ban.getVictimName() + " " + ChatColor.RED + "is " + ChatColor.YELLOW + "suspended.");
        sender.sendMessage(ChatColor.YELLOW + "Victim UUID is " + ban.getVictim().toString() + ". Their name is " + ban.getVictimName());
        sender.sendMessage(ChatColor.YELLOW + "The Victim is suspended for " + ChatColor.WHITE + ban.getReason());
    }
}
