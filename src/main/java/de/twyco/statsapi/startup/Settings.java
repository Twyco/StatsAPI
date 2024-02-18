package de.twyco.statsapi.startup;

import java.util.HashMap;

public abstract class Settings {

    private static boolean writeAPI = false;

    private static String host;
    private static int port;
    private static String username;
    private static String password;

    private static int statsID;
    private static String statsName;

    private static HashMap<String, String> dummyEntry;
    private static String pointsCalculation;

    public static void setWriteAPI(boolean writeAPI) {
        Settings.writeAPI = writeAPI;
    }

    public static void setHost(String host) {
        Settings.host = host;
    }

    public static void setPort(int port) {
        Settings.port = port;
    }

    public static void setUsername(String username) {
        Settings.username = username;
    }

    public static void setPassword(String password) {
        Settings.password = password;
    }

    public static void setStatsID(int statsID) {
        Settings.statsID = statsID;
    }

    public static void setStatsName(String statsName) {
        Settings.statsName = statsName;
    }

    public static void setDummyEntry(HashMap<String, String> dummyEntry) {
        Settings.dummyEntry = dummyEntry;
    }

    public static void setPointsCalculation(String pointsCalculation) {
        Settings.pointsCalculation = pointsCalculation;
    }

    public static boolean isWriteAPI() {
        return writeAPI;
    }

    public static String getHost() {
        return host;
    }

    public static int getPort() {
        return port;
    }

    public static String getUsername() {
        return username;
    }

    public static String getPassword() {
        return password;
    }

    public static int getStatsID() {
        return statsID;
    }

    public static String getStatsName() {
        return statsName;
    }

    public static HashMap<String, String> getDummyEntry() {
        return dummyEntry;
    }

    public static String getPointsCalculation() {
        return pointsCalculation;
    }
}
