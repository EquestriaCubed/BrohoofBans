package com.brohoof.brohoofbans.command.handlers;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.brohoof.brohoofbans.Ban;
import com.brohoof.brohoofbans.Data;

public abstract class AbstractCommandHandler implements CommandExecutor {

    public final static UUID CONSOLE_UUID = UUID.fromString("3c879ef9-95c2-44d1-98f9-2824610477c8");
    public final static String NO_PERMISSION = ChatColor.RED + "You do not have permission.";
    protected Data data;

    public AbstractCommandHandler(Data data) {
        this.data = data;
    }

    protected Optional<Ban> getBan(final String uuidorName) {
        return data.getBan(this.getPlayer(uuidorName).getUniqueId());
    }

    protected String getIP(final InetSocketAddress address) {
        return address.getAddress().getHostAddress();
    }

    @SuppressWarnings("deprecation")
    protected OfflinePlayer getPlayer(final String playerORuuid) {
        try {
            UUID uuid = UUID.fromString(playerORuuid);
            final Player p = Bukkit.getPlayer(uuid);
            if (p == null)
                return Bukkit.getOfflinePlayer(uuid);
            return p;
        } catch (final IllegalArgumentException e) {
            // Not a UUID
            for (final Player p : Bukkit.getOnlinePlayers())
                if (p.getName().equalsIgnoreCase(playerORuuid))
                    return p;
            return Bukkit.getServer().getOfflinePlayer(playerORuuid);
        }
    }

    protected String getReason(final String[] args, final boolean hasFlag) {
        String reason = "";
        int j = 1;
        if (hasFlag)
            j = 3;
        for (int i = j; i < args.length; i++) {
            if (i != 1)
                reason += " ";
            reason += args[i];
        }
        if (reason.startsWith(" "))
            return reason.substring(1, reason.length());
        return reason;
    }
}
