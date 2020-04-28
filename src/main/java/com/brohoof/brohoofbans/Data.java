package com.brohoof.brohoofbans;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.sweetiebelle.lib.ConnectionManager;

class Data {

    private BrohoofBansPlugin p;
    private Settings s;
    private ConnectionManager connection;

    Data(BrohoofBansPlugin p, Settings s, ConnectionManager connectionManager) {
        this.s = s;
        this.p = p;
        this.connection = connectionManager;
        createTables();
    }

    /** Creates a new table in the database */
    private boolean createTable(String pQuery) {
        try {
            connection.executeUpdate(pQuery);
            return true;
        } catch (SQLException e) {
            error(e);
            return false;
        }
    }

    private void createTables() {
        // Generate the information about the various tables
        // Boolean 0 = false, 1 = true.
        String ban = "CREATE TABLE " + s.dbPrefix + "ban (banid INT NOT NULL AUTO_INCREMENT PRIMARY KEY, victimUUID VARCHAR(36), victimName VARCHAR(16), victimIP VARCHAR(39), executorUUID VARCHAR(36), executorName VARCHAR(16), executorIP VARCHAR(39), isSuspension TINYINT(1), expires VARCHAR(21), reason VARCHAR(2000));";
        // Generate the database tables
        if (!tableExists(s.dbPrefix + "ban"))
            createTable(ban);
    }

    /**
     * Method used to handle errors
     *
     * @param e
     *            Exception
     */
    private void error(Exception e) {
        if (s.stackTraces) {
            e.printStackTrace();
            return;
        }
        if (e instanceof SQLException) {
            p.getLogger().severe("SQLException: " + e.getMessage());
            return;
        }
        if (e instanceof IllegalArgumentException)
            // It was probably someone not putting in a valid UUID, so we can ignore.
            // p.getLogger().severe("IllegalArgumentException: " + e.getMessage());
            return;
        if (e instanceof ClassCastException)
            // It was probably someone trying to do something with an offline player.
            // p.getLogger().severe("ClassCastException: " + e.getMessage());
            return;
        p.getLogger().severe("Unhandled Exception " + e.getCause() + ": " + e.getMessage());
        e.printStackTrace();
    }

    Optional<Ban> getBan(String playername) {
        String executorIP;
        String executorName;
        UUID executorUUID;
        String expiresIn;
        String reason;
        String victimName;
        String victimIP;
        UUID victimUUID;
        boolean isSuspended;
        try {
            ResultSet rs = connection.executeQuery("SELECT * FROM " + s.dbPrefix + "ban WHERE victimName = \"" + playername + "\";");
            if (rs.next()) {
                executorIP = rs.getString("executorIP");
                executorName = rs.getString("executorName");
                executorUUID = UUID.fromString(rs.getString("executorUUID"));
                expiresIn = rs.getString("expires");
                reason = rs.getString("reason");
                victimUUID = UUID.fromString(rs.getString("victimUUID"));
                victimName = rs.getString("victimName");
                victimIP = rs.getString("victimIP");
                isSuspended = rs.getBoolean("isSuspension");
                rs.close();
                Optional.<Ban>of(new Ban(victimUUID, executorUUID, victimName, executorName, victimIP, executorIP, expiresIn, reason, isSuspended));
            }
            return Optional.empty();
        } catch (SQLException e) {
            error(e);
            return Optional.empty();
        }
    }

    Optional<Ban> getBan(UUID uuid) {
        String executorIP;
        String executorName;
        UUID executorUUID;
        String expiresIn;
        String reason;
        String victimName;
        String victimIP;
        UUID victimUUID;
        boolean isSuspended;
        try {
            ResultSet rs = connection.executeQuery("SELECT * FROM " + s.dbPrefix + "ban WHERE victimUUID = \"" + uuid.toString() + "\";");
            if (rs.next()) {
                executorIP = rs.getString("executorIP");
                executorName = rs.getString("executorName");
                executorUUID = UUID.fromString(rs.getString("executorUUID"));
                expiresIn = rs.getString("expires");
                reason = rs.getString("reason");
                victimUUID = UUID.fromString(rs.getString("victimUUID"));
                victimName = rs.getString("victimName");
                victimIP = rs.getString("victimIP");
                isSuspended = rs.getBoolean("isSuspension");
                rs.close();
                return Optional.of(new Ban(victimUUID, executorUUID, victimName, executorName, victimIP, executorIP, expiresIn, reason, isSuspended));
            }
            return Optional.empty();
        } catch (SQLException e) {
            error(e);
            return Optional.empty();
        }
    }

    void saveBan(Ban b) {
        try {
            PreparedStatement update = connection.getStatement(String.format("UPDATE %sban SET victimName = ?, victimIP = ?, executorUUID = ?, " + "executorName = ?, executorIP = ?, isSuspension = ?, expires = ?, reason = ? WHERE victimUUID = ?;", s.dbPrefix));
            update.setString(1, b.getVictimName());
            update.setString(2, b.getVictimIP());
            update.setString(3, b.getExecutor().toString());
            update.setString(4, b.getExecutorName());
            update.setString(5, b.getExecutorIP());
            update.setBoolean(6, b.isSuspension());
            update.setString(7, b.getExpires());
            update.setString(8, b.getReason());
            update.setString(9, b.getVictim().toString());
            update.executeUpdate();
            update.close();
        } catch (SQLException e) {
            error(e);
        }
    }

    void createBan(Ban b) {
        try {
            PreparedStatement update = connection.getStatement(String.format("INSERT INTO %sban (victimUUID, victimName, victimIP, executorUUID, executorName, executorIP, isSuspension, expires, reason) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);", s.dbPrefix));
            update.setString(1, b.getVictim().toString());
            update.setString(2, b.getVictimName());
            update.setString(3, b.getVictimIP());
            update.setString(4, b.getExecutor().toString());
            update.setString(5, b.getExecutorName());
            update.setString(6, b.getExecutorIP());
            update.setBoolean(7, b.isSuspension());
            update.setString(8, b.getExpires());
            update.setString(9, b.getReason());
            update.executeUpdate();
            update.close();
        } catch (SQLException e) {
            error(e);
        }
    }

    private boolean tableExists(String pTable) {
        try {
            return connection.executeQuery("SELECT * FROM " + pTable) != null;
        } catch (SQLException e) {
            return false;
        }
    }

    void unban(Ban b) {
        try {
            connection.executeUpdate("DELETE FROM " + s.dbPrefix + "ban WHERE victimUUID = \"" + b.getVictim().toString() + "\";");
        } catch (SQLException e) {
            error(e);
        }
    }

    void updateBans(Player victimOrExecutor) {
        try {
            connection.executeUpdate("UPDATE " + s.dbPrefix + "ban SET victimName = \"" + victimOrExecutor.getName() + "\" WHERE victimUUID = \"" + victimOrExecutor.getUniqueId().toString() + "\";");
            connection.executeUpdate("UPDATE " + s.dbPrefix + "ban SET executorName = \"" + victimOrExecutor.getName() + "\" WHERE executorUUID = \"" + victimOrExecutor.getUniqueId().toString() + "\";");
        } catch (SQLException e) {
            error(e);
        }
    }
}
