package com.brohoof.brohoofbans;

import java.util.UUID;

class Ban {
    private final String executorIP;
    private final String executorName;
    private final UUID executorUUID;
    private final String expiresIn;
    private final boolean isSuspension;
    private final String reason;
    private final String victimIP;
    private final String victimName;
    private final UUID victimUUID;

    /**
     *
     * @param victimUUID
     * @param executorUUID
     * @param victimName
     * @param executorName
     * @param victimIP
     * @param executorIP
     * @param expiresIn
     * @param reason
     * @param isSuspension
     */
    Ban(final UUID victimUUID, final UUID executorUUID, final String victimName, final String executorName, final String victimIP, final String executorIP, final String expiresIn, final String reason, final boolean isSuspension) {
        this.victimUUID = victimUUID;
        this.executorUUID = executorUUID;
        this.victimName = victimName;
        this.executorName = executorName;
        this.victimIP = victimIP;
        this.executorIP = executorIP;
        this.expiresIn = expiresIn;
        this.reason = reason;
        this.isSuspension = isSuspension;
    }

    UUID getExecutor() {
        return executorUUID;
    }

    String getExecutorIP() {
        return executorIP;
    }

    String getExecutorName() {
        return executorName;
    }

    String getExpires() {
        return expiresIn;
    }

    String getReason() {
        return reason;
    }

    UUID getVictim() {
        return victimUUID;
    }

    String getVictimIP() {
        return victimIP;
    }

    String getVictimName() {
        return victimName;
    }

    boolean isSuspension() {
        return isSuspension;
    }

    @Override
    public String toString() {
        return "BAN[VICTIM: {UUID = " + victimUUID.toString() + ",NAME = " + victimName + "} EXECUTOR: {UUID = " + executorUUID.toString() + ",NAME = " + executorName + "} REASON: {" + reason + "} EXPIRES: {" + expiresIn + "} SUSPENDED: {" + isSuspension + "}]";
    }
}
