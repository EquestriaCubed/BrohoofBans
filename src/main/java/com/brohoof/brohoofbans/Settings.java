package com.brohoof.brohoofbans;

import org.bukkit.configuration.file.FileConfiguration;

public class Settings {

    public boolean broadcastMessage;
    public String suspendReason;
    public String dbPrefix;
    private BrohoofBansPlugin plugin;
    public boolean stackTraces;

    public Settings(BrohoofBansPlugin plugin) {
        this.plugin = plugin;
        readSettings(plugin.getConfig());
    }

    /**
     * Reads settings
     *
     * @param pConfig
     */
    private void readSettings(FileConfiguration config) {
        suspendReason = config.getString("general.suspendReason");
        broadcastMessage = config.getBoolean("general.broadcastMessage");
        stackTraces = config.getBoolean("general.printStackTraces");
        dbPrefix = config.getString("database.prefix");
        
    }

    /**
     * Reloads settings
     */
    public void reloadSettings() {
        plugin.reloadConfig();
        readSettings(plugin.getConfig());
    }
}
