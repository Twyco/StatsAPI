package de.twyco.statsapi.misc;

import de.twyco.statsapi.startup.Settings;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.HashMap;
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
            while (resultSet.next()) {
                for (int i = 2; i <= columnCount; i++) {
                    String statName = resultSetMetaData.getColumnName(i);
                    double value = resultSet.getDouble(i);
                    stats.put(statName, value);
                }
            }
            return stats;
        } catch (SQLException e) {
            throw new SQLException("There is no Database for Season: " + seasonID + "; " + e.getMessage());
        }
    }

}
