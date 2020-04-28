package com.brohoof.brohoofbans;

import java.util.Optional;

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
    private Data d;

    public EventManager(API api, Data d,ExpireConverter c, Settings s) {
        this.api = api;
        this.d = d;
        this.c = c;
        this.s = s;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent pEvent) {
        Optional<Ban> ban = api.getBan(pEvent.getPlayer().getUniqueId());
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
    }

    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void updateInfo(PlayerLoginEvent pEvent) {
        d.updateBans(pEvent.getPlayer());
    }
}
