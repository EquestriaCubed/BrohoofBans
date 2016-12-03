package com.brohoof.brohoofbans;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.entity.Player;

public class Data {
    private Connection connection;
    private final BrohoofBansPlugin p;
    private final Settings s;

    Data(final BrohoofBansPlugin p, final Settings s) {
        this.s = s;
        this.p = p;
        createTables();
    }

    /** Creates a new table in the database */
    private boolean createTable(final String pQuery) {
        try {
            executeQuery(pQuery);
            return true;
        } catch (final SQLException e) {
            error(e);
            return false;
        }
    }

    private final void createTables() {
        // Generate the information about the various tables
        // Boolean 0 = false, 1 = true.
        final String ban = "CREATE TABLE " + s.dbPrefix + "ban (banid INT NOT NULL AUTO_INCREMENT PRIMARY KEY, victimUUID VARCHAR(36), victimName VARCHAR(16), victimIP VARCHAR(39), executorUUID VARCHAR(36), executorName VARCHAR(16), executorIP VARCHAR(39), isSuspension TINYINT(1), expires VARCHAR(21), reason VARCHAR(2000));";
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
    void error(final Exception e) {
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

    /**
     * Executes the given SQL statement, which may be an <code>INSERT</code>, <code>UPDATE</code>, or <code>DELETE</code> statement or an SQL statement that returns nothing, such as an SQL DDL statement.
     * <p>
     *
     * @param query
     *            an SQL Data Manipulation Language (DML) statement, such as INSERT, UPDATE or DELETE; or an SQL statement that returns nothing, such as a DDL statement.
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements or (2) 0 for SQL statements that return nothing
     * @throws SQLException
     *             if a database access error occurs, this method is called on a closed <code>Statement</code>, the given SQL statement produces a <code>ResultSet</code> object, the method is called on a <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    private int executeQuery(final String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase + "?autoReconnect=true&useSSL=false");
            connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
            p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
        }
        if (s.showQueries)
            p.getLogger().info("[QUERY] " + query);
        return connection.createStatement().executeUpdate(query);
    }

    void forceConnectionRefresh() {
        try {
            if (connection == null || connection.isClosed()) {
                final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase + "?autoReconnect=true&useSSL=false");
                connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
                p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
            } else {
                connection.close();
                final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase + "?autoReconnect=true&useSSL=false");
                connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
                p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
            }
        } catch (final SQLException e) {
            error(e);
        }
    }

    Ban getBan(final String playername) {
        String executorIP;
        String executorName;
        UUID executorUUID;
        String expiresIn;
        String reason;
        String victimName;
        String victimIP;
        UUID victimUUID;
        boolean isSuspended;
        if (!isBanned(playername))
            return null;
        try {
            final ResultSet rs = getResultSet("SELECT * FROM " + s.dbPrefix + "ban WHERE victimName = \"" + playername + "\";");
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
                return new Ban(victimUUID, executorUUID, victimName, executorName, victimIP, executorIP, expiresIn, reason, isSuspended);
            }
            return null;
        } catch (final SQLException e) {
            error(e);
            return null;
        }
    }

    Ban getBan(final UUID uuid) {
        String executorIP;
        String executorName;
        UUID executorUUID;
        String expiresIn;
        String reason;
        String victimName;
        String victimIP;
        UUID victimUUID;
        boolean isSuspended;
        if (!isBanned(uuid))
            return null;
        try {
            final ResultSet rs = getResultSet("SELECT * FROM " + s.dbPrefix + "ban WHERE victimUUID = \"" + uuid.toString() + "\";");
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
                return new Ban(victimUUID, executorUUID, victimName, executorName, victimIP, executorIP, expiresIn, reason, isSuspended);
            }
            return null;
        } catch (final SQLException e) {
            error(e);
            return null;
        }
    }

    /**
     * Private method for getting an SQL connection, then submitting a query. This method throws an SQL Exception to allow another method to handle it.
     *
     * @param query
     *            an SQL statement to be sent to the database, typically a static SQL <code>SELECT</code> statement
     * @return a <code>ResultSet</code> object that contains the data produced by the given query; never <code>null</code>
     * @throws SQLException
     *             if a database access error occurs, this method is called on a closed <code>Statement</code>, the given SQL statement produces a <code>ResultSet</code> object, the method is called on a <code>PreparedStatement</code> or <code>CallableStatement</code>
     */
    private ResultSet getResultSet(final String query) throws SQLException {
        if (connection == null || connection.isClosed()) {
            final String connect = new String("jdbc:mysql://" + s.dbHost + ":" + s.dbPort + "/" + s.dbDatabase + "?autoReconnect=true&useSSL=false");
            connection = DriverManager.getConnection(connect, s.dbUser, s.dbPass);
            p.getLogger().info("Connecting to " + s.dbUser + "@" + connect + "...");
        }
        if (s.showQueries)
            p.getLogger().info("[QUERY] " + query);
        return connection.createStatement().executeQuery(query);
    }

    public boolean isBanned(final String playername) {
        try {
            final ResultSet rs = getResultSet("SELECT * FROM " + s.dbPrefix + "ban WHERE victimName = \"" + playername + "\";");
            return rs.next();
        } catch (final SQLException e) {
            error(e);
            return false;
        }
    }

    public boolean isBanned(final UUID id) {
        try {
            final ResultSet rs = getResultSet("SELECT * FROM " + s.dbPrefix + "ban WHERE victimUUID = \"" + id.toString() + "\";");
            return rs.next();
        } catch (final SQLException e) {
            error(e);
            return false;
        }
    }

    boolean isSuspended(final UUID uuid) {
        try {
            final ResultSet rs = getResultSet("SELECT * FROM " + s.dbPrefix + "ban WHERE victimUUID = \"" + uuid.toString() + "\";");
            if (rs.next())
                return rs.getBoolean("isSuspended");
            return false;
        } catch (final SQLException e) {
            error(e);
            return false;
        }
    }

    void saveBan(final Ban b) {
        try {
            if (isBanned(b.getVictim())) {
                executeQuery("UPDATE " + s.dbPrefix + "ban SET victimName = \"" + b.getVictimName() + "\", victimIP = \"" + 
            b.getVictimIP() + "\", executorUUID = \"" + b.getExecutor().toString() + "\", executorName = \"" + 
                        b.getExecutorName() + "\", executorIP = \"" + b.getExecutorIP() + 
                        "\", isSuspension = \"" + (b.isSuspension() ? 1 : 0) + "\", expires = \"" 
                        + b.getExpires() + "\", reason = \"" + b.getReason() + "\" WHERE victimUUID = \"" + b.getVictim().toString() + "\";");
                return;
            }
            executeQuery("INSERT INTO " + s.dbPrefix + "ban (victimUUID, victimName, victimIP, executorUUID, executorName, executorIP, isSuspension, expires, reason) VALUES (\"" + b.getVictim().toString() + "\", \"" + b.getVictimName() + "\", \"" + b.getVictimIP() + "\", \"" + b.getExecutor().toString() + "\", \"" + b.getExecutorName() + "\", \"" + b.getExecutorIP() + "\", \"" + (b.isSuspension() ? 1 : 0) + "\", \"" + b.getExpires() + "\", \"" + b.getReason() + "\");");
            return;
        } catch (final SQLException e) {
            error(e);
        }
    }

    private boolean tableExists(final String pTable) {
        try {
            return getResultSet("SELECT * FROM " + pTable) != null;
        } catch (final SQLException e) {
            // Table 'mccreative.BrohoofBans_ban' doesn't exist
            if (e.getMessage().equalsIgnoreCase("Table '" + s.dbDatabase + "." + pTable + "' doesn't exist") || e.getMessage().equalsIgnoreCase("Table \"" + s.dbDatabase + "." + pTable + "\" doesn't exist"))
                return false;
            error(e);
        }
        return false;
    }

    void unban(final Ban b) {
        try {
            executeQuery("DELETE FROM " + s.dbPrefix + "ban WHERE victimUUID = \"" + b.getVictim().toString() + "\";");
        } catch (final SQLException e) {
            error(e);
        }
    }

    void updateBans(final Player victimOrExecutor) {
        try {
            executeQuery("UPDATE " + s.dbPrefix + "ban SET victimName = \"" + victimOrExecutor.getName() + "\" WHERE victimUUID = \"" + victimOrExecutor.getUniqueId().toString() + "\";");
            executeQuery("UPDATE " + s.dbPrefix + "ban SET executorName = \"" + victimOrExecutor.getName() + "\" WHERE executorUUID = \"" + victimOrExecutor.getUniqueId().toString() + "\";");
        } catch (final SQLException e) {
            error(e);
        }
    }
}
