package de.twyco.statsapi.startup;

import java.sql.*;
import java.util.UUID;

public abstract class API {

    //IMPORTANT WHEN A PLAYER REGISTER / JOINS FIRST TIME THE SERVER
    public static void registerNewUUID(UUID uuid) {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Settings";
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "INSERT INTO uuids (uuid) VALUES (?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, uuid.toString());
            statement.execute();
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
        }
    }

    public static void newSeason() {
        //TODO
    }


}
