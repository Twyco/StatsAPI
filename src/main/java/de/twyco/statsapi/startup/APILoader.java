package de.twyco.statsapi.startup;

import de.twyco.statsapi.misc.Data;
import de.twyco.statsapi.stats.Stat;
import de.twyco.statsapi.stats.Statistic;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class APILoader {

    public static void main(String[] args) throws IOException {
        loadAPI("DataFolder");
        //TESTS
        Statistic statistic = new Statistic(UUID.fromString("a6555008-9a7e-4668-a505-05517b2b89bc"), 1, 0);
        for(Stat stat : statistic.getStats()){
            System.out.println(stat.getName() + ": " + stat.getValue());
        }
    }

    public static void loadAPI(String dataFolderPath) throws IOException {
        File settings = new File(dataFolderPath, "settings.yml");

        if (!settings.exists()) {
            copyDefault(settings);
        }

        loadSettings(settings);
        if (!testDatabaseConnection()) {
            return;
        }
        checkDatabases();
        if(Settings.isWriteAPI()){
            loadAPIasWrite();
        }else {
            loadAPIasRead();
        }
    }

    private static void loadAPIasWrite() {
        loadSeasons();
    }

    private static void loadAPIasRead() {
        setCurrentSeason();
        setMinigameIDsOForEachSeason();
    }

    private static void copyDefault(File settings) throws IOException {
        File defaultSettings = new File("src/main/resources/settings.yml");
        Path sourcePath = Paths.get(defaultSettings.toURI());
        Path destinationPath = Paths.get(settings.toURI());

        try {
            Files.createDirectories(destinationPath.getParent());
            Files.copy(sourcePath, destinationPath);
        } catch (IOException e) {
            throw new IOException("Error while creating new settings file: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static void loadSettings(File settings) throws FileNotFoundException {
        Map<String, Object> yml;
        try {
            Yaml yaml = new Yaml();
            FileInputStream inputStream = new FileInputStream(settings);
            yml = yaml.load(inputStream);
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException("Could not find file:" + e.getMessage());
        }
        if (!setAllSettings(yml)) {
            //TODO add missing Settings
        }
        Map<String, Object> database = (Map<String, Object>) yml.get("database");
        Settings.setHost(String.valueOf(database.get("host")));
        Settings.setPort(Integer.parseInt(String.valueOf(database.get("port"))));
        Settings.setUsername(String.valueOf(database.get("username")));
        Settings.setPassword(String.valueOf(database.get("password")));

        int statsID = Integer.parseInt(String.valueOf(yml.get("statsID")));
        boolean writeAPI = statsID != -1;

        Settings.setWriteAPI(writeAPI);
        if (!writeAPI) {
            return;
        }

        Settings.setStatsID(statsID);
        Settings.setStatsName(String.valueOf(yml.get("statsName")));

        Settings.setDummyEntry((HashMap<String, String>) yml.get("dummyEntry"));
        Settings.setPointsCalculation(String.valueOf(yml.get("pointsCalculation")));
    }

    @SuppressWarnings("unchecked")
    private static boolean setAllSettings(Map<String, Object> settings) {
        //Database Settings:
        if (!settings.containsKey("database")) {
            return false;
        }
        Map<String, Object> database = (Map<String, Object>) settings.get("database");
        if (!database.containsKey("host") || !database.containsKey("port") || !database.containsKey("username") || !database.containsKey("password")) {
            return false;
        }

        //Statistic Settings:
        if (!settings.containsKey("statsID")) {
            return false;
        }
        if (Integer.parseInt(settings.get("statsID").toString()) == -1) { //Wenn API nur werte liest restliche Settings egal
            return true;
        }
        return settings.containsKey("statsName") && settings.containsKey("pointsCalculation") && settings.containsKey("dummyEntry");
    }

    private static boolean testDatabaseConnection() {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort();
        String user = Settings.getUsername();
        String password = Settings.getPassword();

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            connection.close();
            return true;
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
            return false;
        }
    }

    private static void checkDatabases() {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort();
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url + "/Stats_Settings", user, password);
            connection.close();
        } catch (SQLException e) {
            createDatabase("Stats_Settings");
            createSettingsTables();
            createDatabase("Stats_Season_0");
        }
    }

    private static void createDatabase(String databaseName) {
        System.err.println("Creating Database: " + databaseName);
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort();
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            String sql = "CREATE DATABASE " + databaseName;
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
        }
    }

    private static void createSettingsTables() {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Settings";
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE seasons (seasonID INT AUTO_INCREMENT PRIMARY KEY, start DATE, end DATE);";
            statement.execute(sql);
            sql = "CREATE TABLE stats (statID INT AUTO_INCREMENT PRIMARY KEY, statName INT);";
            statement.execute(sql);
            sql = "CREATE TABLE existingStatsPerSeason (seasonID INT, statsID INT, PRIMARY KEY (seasonID, statsID), FOREIGN KEY (seasonID) REFERENCES seasons(seasonID), FOREIGN KEY (statsID) REFERENCES stats(statID));";
            statement.execute(sql);
            sql = "CREATE TABLE uuids (uuid varchar(255), PRIMARY KEY (uuid));"; //TODO Change 255
            statement.execute(sql);

            sql = "INSERT INTO seasons (start, end) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            java.sql.Date startDate = new java.sql.Date(new java.util.Date().getTime());
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, null);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
        }
    }

    private static void loadSeasons() {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Settings";
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM seasons";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int seasonID = resultSet.getInt("seasonID");
                java.util.Date start = resultSet.getDate("start");
                java.util.Date end = resultSet.getDate("end");
                if (end == null) {
                    Data.setCurrentSeason(seasonID);
                    Data.setSeasonStartDate(seasonID, start);
                } else {
                    Data.setSeasonStartDate(seasonID, start);
                    Data.setSeasonEndDate(seasonID, end);
                }
            }
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
        }
    }

    private static void setCurrentSeason() {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Settings";
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM seasons";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int seasonID = resultSet.getInt("seasonID");
                java.util.Date start = resultSet.getDate("start");
                java.util.Date end = resultSet.getDate("end");
                if (end == null) {
                    Data.setCurrentSeason(seasonID);
                    Data.setSeasonStartDate(seasonID, start);
                }
            }
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
        }
    }

    private static void setMinigameIDsOForEachSeason() {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Settings";
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            String sql = "SELECT * FROM existingStatsPerSeason";
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int seasonID = resultSet.getInt("seasonID");
                int start = resultSet.getInt("statsID");
                Data.addSeasonStatsIDs(seasonID, start);
            }
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
        }
    }
}
