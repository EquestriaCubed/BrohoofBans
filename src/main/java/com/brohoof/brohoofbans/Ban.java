package com.brohoof.brohoofbans;

import java.util.UUID;

public class Ban {

    private String executorIP;
    private String executorName;
    private UUID executorUUID;
    private String expiresIn;
    private boolean isSuspension;
    private String reason;
    private String victimIP;
    private String victimName;
    private UUID victimUUID;

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
    public Ban(UUID victimUUID, UUID executorUUID, String victimName, String executorName, String victimIP, String executorIP, String expiresIn, String reason, boolean isSuspension) {
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

    public UUID getExecutor() {
        return executorUUID;
    }

    public String getExecutorIP() {
        return executorIP;
    }

    public String getExecutorName() {
        return executorName;
    }

    public String getExpires() {
        return expiresIn;
    }

    public String getReason() {
        return reason;
    }

    public UUID getVictim() {
        return victimUUID;
    }

    public String getVictimIP() {
        return victimIP;
    }

    public String getVictimName() {
        return victimName;
    }

    public boolean isSuspension() {
        return isSuspension;
    }

    @Override
    public String toString() {
        return "BAN[VICTIM: {UUID = " + victimUUID.toString() + ",NAME = " + victimName + "} EXECUTOR: {UUID = " + executorUUID.toString() + ",NAME = " + executorName + "} REASON: {" + reason + "} EXPIRES: {" + expiresIn + "} SUSPENDED: {" + isSuspension + "}]";
    }
}
