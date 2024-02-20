package de.twyco.statsapi.misc;

import de.twyco.statsapi.startup.Settings;
import de.twyco.statsapi.stats.Stat;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class SQLDatabase {

    @NotNull
    public static HashMap<String, Double> getStats(UUID statOwner, int seasonID, int statID) throws SQLException {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Season_" + seasonID;
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String tableName = "id_" + statID;
            String sql = "SELECT * FROM " + tableName + " WHERE uuid = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, String.valueOf(statOwner));

            ResultSet resultSet = statement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();

            HashMap<String, Double> stats = new HashMap<>();
            if (!resultSet.next()) {
                for (int i = 2; i <= columnCount; i++) {
                    String statName = resultSetMetaData.getColumnName(i);
                    stats.put(statName, 0D);
                }
                insertUUID(statOwner, seasonID, statID);
                return stats;
            }
            do {
                for (int i = 2; i <= columnCount; i++) {
                    String statName = resultSetMetaData.getColumnName(i);
                    double value = resultSet.getDouble(i);
                    stats.put(statName, value);
                }
            } while (resultSet.next());
            return stats;
        } catch (SQLException e) {
            throw new SQLException("There is no Database for Season: " + seasonID + "; " + e.getMessage());
        }
    }

    @SuppressWarnings("all")
    public static void saveStats(List<Stat> stats, UUID statOwner, int seasonID, int statID) throws SQLException {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Season_" + seasonID;
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String tableName = "id_" + statID;
            for (Stat stat : stats) {
                String columnName = stat.getName();
                String sql = "UPDATE " + tableName + " SET " + columnName + " = ? WHERE uuid = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setDouble(1, stat.getValue());
                statement.setString(2, statOwner.toString());
                statement.execute();
            }
        } catch (SQLException e) {
            throw new SQLException("There is no Database for Season: " + seasonID + "; " + e.getMessage());
        }
    }

    private static void insertUUID(UUID statOwner, int seasonID, int statID) throws SQLException {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Season_" + seasonID;
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String tableName = "id_" + statID;
            String sql = "INSERT INTO " + tableName + "(uuid) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, statOwner.toString());
            statement.execute();
        } catch (SQLException e) {
            throw new SQLException("There is no Database for Season: " + seasonID + "; " + e.getMessage());
        }
    }

}
