package com.brohoof.brohoofbans.command;

import java.net.InetSocketAddress;
import java.util.Objects;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.BrohoofBansPlugin;
import com.brohoof.brohoofbans.Data;
import com.brohoof.brohoofbans.Settings;

public abstract class AbstractCommand {

    protected Data data;
    protected BrohoofBansPlugin plugin;
    protected Settings settings;

    public AbstractCommand(BrohoofBansPlugin plugin, Data data, Settings settings) {
        this.plugin = plugin;
        this.data = data;
        this.settings = settings;
    }

    protected void broadcastBanMessage(Ban ban) {
        if (settings.broadcastMessage)
            if (ban.isSuspension())
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Suspended " + ban.getVictimName() + " for " + ban.getReason());
            else
                Bukkit.broadcastMessage(ChatColor.YELLOW + "Banned " + ban.getVictimName() + " for " + ban.getReason());
    }

    protected void broadcastKickMessage(Player target, String reason) {
        if (settings.broadcastMessage)
            Bukkit.broadcastMessage(ChatColor.YELLOW + "Kicked " + target.getName() + " for " + reason);
    }

    protected String getIP(InetSocketAddress address) {
        return Objects.requireNonNull(address.getAddress().getHostAddress());
    }
}
