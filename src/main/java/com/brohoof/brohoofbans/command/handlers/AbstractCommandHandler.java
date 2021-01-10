package com.brohoof.brohoofbans.command.handlers;

import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;

import com.brohoof.brohoofbans.API;

public abstract class AbstractCommandHandler implements CommandExecutor {

    protected API api;

    public AbstractCommandHandler(API api) {
        this.api = api;
    }

    @SuppressWarnings("deprecation")
    protected OfflinePlayer getPlayer(String playerORuuid) {
        try {
            UUID uuid = UUID.fromString(playerORuuid);
            Player p = Bukkit.getPlayer(uuid);
            if (p == null)
                return Bukkit.getOfflinePlayer(uuid);
            return p;
        } catch (IllegalArgumentException e) {
            // Not a UUID
            for (Player p : Bukkit.getOnlinePlayers())
                if (p.getName().equalsIgnoreCase(playerORuuid))
                    return p;
            return Bukkit.getServer().getOfflinePlayer(playerORuuid);
        }
    }

    protected String getReason(String[] args, boolean hasFlag) {
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
