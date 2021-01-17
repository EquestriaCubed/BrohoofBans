package com.brohoof.brohoofbans;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.bukkit.entity.Player;

public class API {

    private Data data;

    API(Data d) {
        this.data = d;
    }

    public CompletableFuture<Optional<Ban>> getBan(String playerName) {
        return Scheduler.makeFuture(() -> {
            return data.getBan(playerName);
        });
    }

    public CompletableFuture<Optional<Ban>> getBan(UUID playerUUID) {
        return Scheduler.makeFuture(() -> {
            return data.getBan(playerUUID);
        });
    }

    public CompletableFuture<Boolean> isBanned(String playerName) {
        return Scheduler.makeFuture(() -> {
            return data.getBan(playerName).isPresent();
        });
    }
    /**
     * This method returns an {@link #isBanned(String)} without being wrapped in a
     * {@link CompletableFuture}.
     * <p>
     * This will run on the same thread the moment this function is called.
     * </p>
     * 
     * @deprecated this method is dangerous and bypasses sanity checks. You should
     *             only use this if you know what you're doing.
     * @param uuid the player's UUID
     * @return if the UUID is banned
     * 
     */
    @Deprecated
    public boolean isBannedDangerous(String playerName) {
        return data.getBan(playerName).isPresent();
    }

    public CompletableFuture<Boolean> isBanned(UUID playerUUID) {
        return Scheduler.makeFuture(() -> {
            return data.getBan(playerUUID).isPresent();
        });

    }

    /**
     * This method returns an {@link #isBanned(UUID)} without being wrapped in a
     * {@link CompletableFuture}.
     * <p>
     * This will run on the same thread the moment this function is called.
     * </p>
     * 
     * @deprecated this method is dangerous and bypasses sanity checks. You should
     *             only use this if you know what you're doing.
     * @param uuid the player's UUID
     * @return if the UUID is banned
     * 
     */
    @Deprecated
    public boolean isBannedDangerous(UUID playerUUID) {
        return data.getBan(playerUUID).isPresent();
    }

    public CompletableFuture<Void> ban(Ban ban) {
        return Scheduler.makeFuture(() -> {
            if (data.getBan(ban.getExecutor()).isPresent())
                data.saveBan(ban);
            else
                data.createBan(ban);
        });
    }

    public CompletableFuture<Void> unban(Ban ban) {
        return Scheduler.makeFuture(() -> {
            data.unban(ban);
        });
    }

    public CompletableFuture<Void> updateBan(Player victimOrExecutor) {
        return Scheduler.makeFuture(() -> {
            data.updateBans(victimOrExecutor);
        });
    }
}
