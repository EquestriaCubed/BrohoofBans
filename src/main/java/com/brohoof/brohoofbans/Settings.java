package com.brohoof.brohoofbans;

import org.bukkit.configuration.file.FileConfiguration;

class Settings {
    String dbDatabase;
    String dbHost;
    String dbPass;
    String dbPort;
    String dbPrefix;
    String dbUser;
    private final BrohoofBansPlugin plugin;
    boolean stackTraces;
    String suspendReason;
    boolean showQueries;

    Settings(final BrohoofBansPlugin plugin) {
        this.plugin = plugin;
        readSettings(plugin.getConfig());
    }

    /**
     * Reads settings
     *
     * @param pConfig
     */
    private void readSettings(final FileConfiguration config) {
        stackTraces = config.getBoolean("general.printStackTraces");
        suspendReason = config.getString("general.suspendReason");
        showQueries = config.getBoolean("general.showQueries");
        dbHost = config.getString("database.host");
        dbPort = config.getString("database.port");
        dbUser = config.getString("database.username");
        dbPass = config.getString("database.password");
        dbDatabase = config.getString("database.database");
        dbPrefix = config.getString("database.prefix");
    }

    /**
     * Reloads settings
     */
    void reloadSettings() {
        plugin.reloadConfig();
        readSettings(plugin.getConfig());
    }
}
