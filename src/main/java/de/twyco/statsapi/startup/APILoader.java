package de.twyco.statsapi.startup;

import de.twyco.statsapi.misc.Data;
import de.twyco.statsapi.stats.Stat;
import de.twyco.statsapi.stats.Statistic;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public abstract class APILoader {

    /* TODO TESTEN:
     *  für Read API:
     *   setStatID()
     *   setSeasonID()
     *  für Write API:
     *   setStatID() -> shouldn't work
     *   setSeasonID() -> shouldn't work
     */


    public static void main(String[] args) throws IOException {
        loadAPI("DataFolder");
        //TESTS
        Statistic statistic = new Statistic(UUID.fromString("a6555008-9a7e-4668-a505-05517b2b89bc"));
        for (Stat stat : statistic.getStats()) {
            System.out.println(stat.getName() + " | " + stat.getValue());
        }
        statistic.setStat("kills", 11);
        statistic.updateStat("deaths", 3);
        statistic.setStat("gamesPlayed", 787);
        statistic.updateStat("gamesWon", 3, -1000);
        statistic.save();
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
        setCurrentSeason();
        loadSeasons();
        if (Settings.isWriteAPI()) {
            loadAPIasWrite();
        } else {
            loadAPIasRead();
        }
    }

    private static void loadAPIasWrite() {
        if (!statExists(Settings.getStatsID())) {
            createStat(Settings.getStatsID(), Settings.getStatsName());
        } else {
            //TODO Override dummyEntry, statName and Point Calulcation in settings.yml
            // reload Settings
        }
        if (!statExistsInCurrentSeason(Settings.getStatsID())) {
            List<String> stats = new ArrayList<>(Settings.getDummyEntry().keySet());
            createStatTableInCurrentSeason(Settings.getStatsID(), stats);
        }
    }

    private static boolean statExists(int statID) {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Settings";
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "SELECT * FROM stats WHERE statID = ?;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, statID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
        }
        return false;
    }

    private static void createStat(int statsID, String statName) {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Settings";
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "INSERT INTO stats (statID, statName) VALUES (?, ?)"; //TODO ADD point calculation and stat structure
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, statsID);
            statement.setString(2, statName);
            statement.execute();
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
        }
    }

    private static boolean statExistsInCurrentSeason(int statID) {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Settings";
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            String sql = "SELECT * FROM existingStatsPerSeason WHERE seasonID = ? AND statID = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, Data.getCurrentSeason());
            statement.setInt(2, statID);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
        }
        return false;
    }

    private static void createStatTableInCurrentSeason(int statID, @NotNull List<String> stats) {
        String url = "jdbc:mysql://" + Settings.getHost() + ":" + Settings.getPort() + "/Stats_Season_" + Data.getCurrentSeason();
        String user = Settings.getUsername();
        String password = Settings.getPassword();
        StringBuilder columns = new StringBuilder();
        for (String str : stats) {
            columns.append(str).append(" DOUBLE DEFAULT 0, ");
        }
        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE id_" + statID + " (uuid varchar(255) PRIMARY KEY, " + columns + "FOREIGN KEY (uuid) REFERENCES Stats_Settings.uuids(uuid));"; //TODO Change 255
            statement.execute(sql);

            sql = "INSERT INTO Stats_Settings.existingStatsPerSeason (seasonID, statID) VALUES (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, Data.getCurrentSeason());
            preparedStatement.setInt(2, statID);
            preparedStatement.execute();
        } catch (SQLException e) {
            System.err.println("Can't connect to MySQL: " + e.getMessage());
        }
    }

    private static void loadAPIasRead() {
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
            sql = "CREATE TABLE stats (statID INT AUTO_INCREMENT PRIMARY KEY, statName varchar(255));"; //TODO Change 255; ADD Point calculation and structure
            statement.execute(sql);
            sql = "CREATE TABLE existingStatsPerSeason (seasonID INT, statID INT, PRIMARY KEY (seasonID, statID), FOREIGN KEY (seasonID) REFERENCES seasons(seasonID), FOREIGN KEY (statID) REFERENCES stats(statID));";
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
