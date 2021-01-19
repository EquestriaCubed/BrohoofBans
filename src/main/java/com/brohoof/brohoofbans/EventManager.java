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
    private Settings s;

    public EventManager(API api, Settings s) {
        this.api = api;
        this.s = s;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent pEvent) {
        CompletableFuture<Optional<Ban>> future = api.getBan(pEvent.getPlayer().getUniqueId());
        future.thenAccept((ban) -> {
            if (ban.isPresent()) {
                Ban b = ban.get();
                BrohoofBansPlugin.getStaticLogger().info(b.toString());
                if (b.getExpires() != -1L)
                    if (b.getExpires() - System.currentTimeMillis() <= 0) {
                        api.unban(b);
                        return;
                    }
                if (b.isSuspension()) {
                    pEvent.disallow(Result.KICK_BANNED, s.suspendReason);
                    return;
                }
                if (b.getExpires() != -1L)
                    pEvent.disallow(Result.KICK_BANNED, "You are banned for:\n" + b.getReason() + ". \n" + ChatColor.RED + "Expires at " + ChatColor.WHITE + ExpireConverter.getFriendlyTime(b.getExpires() - System.currentTimeMillis()) + ".");
                else
                    pEvent.disallow(Result.KICK_BANNED, "You are banned for:\n" + b.getReason());
                return;
            }
        });

    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void updateInfo(PlayerLoginEvent pEvent) {
        api.updateBan(pEvent.getPlayer(), pEvent.getAddress().toString().split("/")[1]).thenAccept((object) -> {
            BrohoofBansPlugin.getStaticLogger().info("Updated player info.");
        });
    }
}
