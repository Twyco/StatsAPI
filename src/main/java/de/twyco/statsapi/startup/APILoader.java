package de.twyco.statsapi.startup;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class APILoader {

    public static void main(String[] args) throws IOException {
        loadAPI("DataFolder");
        System.out.println(Settings.getHost());
        System.out.println(Settings.getPort());
        System.out.println(Settings.getUsername());
        System.out.println(Settings.getPassword());

        System.out.println(Settings.getStatsID());
        System.out.println(Settings.getStatsName());

        System.out.println("DummyEntry");
        for(String s : Settings.getDummyEntry().keySet()){
            System.out.println(" - " + s + " : " + Settings.getDummyEntry().get(s));
        }
        System.out.println(Settings.getPointsCalculation());
    }

    public static void loadAPI(String dataFolderPath) throws IOException {
        File settings = new File(dataFolderPath, "settings.yml");

        if (!settings.exists()) {
            copyDefault(settings);
        }

        loadSettings(settings);

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

        Settings.setStatsID(Integer.parseInt(String.valueOf(yml.get("statsID"))));
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
}
