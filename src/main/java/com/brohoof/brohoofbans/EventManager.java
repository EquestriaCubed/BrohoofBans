package com.brohoof.brohoofbans;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

class EventManager implements Listener {

    private API api;
    private ExpireConverter c;
    private Settings s;
    public EventManager(API api, ExpireConverter c, Settings s) {
        this.api = api;
        this.c = c;
        this.s = s;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent pEvent) {
        CompletableFuture<Optional<Ban>> future = api.getBan(pEvent.getPlayer().getUniqueId());
        future.thenAccept((ban) -> {
            if (ban.isPresent()) {
                Ban b = ban.get();
                if (!b.getExpires().equalsIgnoreCase("NEVER"))
                    if (Long.parseLong(b.getExpires()) - System.currentTimeMillis() <= 0) {
                        api.unban(b);
                        return;
                    }
                if (b.isSuspension()) {
                    pEvent.disallow(Result.KICK_BANNED, s.suspendReason);
                    return;
                }
                if (b.getExpires().equalsIgnoreCase("NEVER")) {
                    pEvent.disallow(Result.KICK_BANNED, "You are banned for:\n" + b.getReason());
                    return;
                }
                pEvent.disallow(Result.KICK_BANNED, "You are banned for:\n" + b.getReason() + ". \n" + ChatColor.RED + "Expires at " + ChatColor.WHITE + c.getFriendlyTime(Long.parseLong(b.getExpires())) + ".");
                return;
            }
        });

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void updateInfo(PlayerLoginEvent pEvent) {
        api.updateBan(pEvent.getPlayer());
    }
}
