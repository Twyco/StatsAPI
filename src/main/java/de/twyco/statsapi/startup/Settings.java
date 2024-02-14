package de.twyco.statsapi.startup;

import java.util.HashMap;

public abstract class Settings {

    private static boolean freezeSettings = false;

    private static String host;
    private static int port;
    private static String username;
    private static String password;

    private static int statsID;
    private static String statsName;

    private static HashMap<String, String> dummyEntry;
    private static String pointsCalculation;

    public static void freezeSettings() {
        freezeSettings = true;
    }

    public static void setHost(String host) {
        if (!freezeSettings) {
            Settings.host = host;
        }
    }

    public static void setPort(int port) {
        if (!freezeSettings) {
            Settings.port = port;
        }
    }

    public static void setUsername(String username) {
        if (!freezeSettings) {
            Settings.username = username;
        }
    }

    public static void setPassword(String password) {
        if (!freezeSettings) {
            Settings.password = password;
        }
    }

    public static void setStatsID(int statsID) {
        if (!freezeSettings) {
            Settings.statsID = statsID;
        }
    }

    public static void setStatsName(String statsName) {
        if (!freezeSettings) {
            Settings.statsName = statsName;
        }
    }

    public static void setDummyEntry(HashMap<String, String> dummyEntry) {
        if (!freezeSettings) {
            Settings.dummyEntry = dummyEntry;
        }
    }

    public static void setPointsCalculation(String pointsCalculation) {
        if (!freezeSettings) {
            Settings.pointsCalculation = pointsCalculation;
        }
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
