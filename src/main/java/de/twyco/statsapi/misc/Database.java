package de.twyco.statsapi.misc;

import de.twyco.statsapi.startup.APILoader;
import de.twyco.statsapi.stats.Stat;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

public class Database {
    //TODO Change to Safe in MongoDB

    private final File database;

    public Database(File database) {
        this.database = database;
    }

    @NotNull
    public HashMap<String, Double> getStats(UUID uuid, int seasonID, int minigameID) {
        String json = readFile();
        assert json != null;
        JSONObject jsonObject = new JSONObject(json);
        JSONArray seasonArray = new JSONArray(jsonObject.getJSONArray("Seasons"));

        JSONArray minigamesArray = getJSONArray_ByKeyFromArray(seasonArray, String.valueOf(seasonID));
        JSONArray playerArray = getJSONArray_ByKeyFromArray(minigamesArray, String.valueOf(minigameID));
        JSONObject statsObject = getJSONObject_ByKeyFromArray(playerArray, uuid.toString()).getJSONObject(uuid.toString());

        HashMap<String, Double> stats = new HashMap<>();
        for (String key : statsObject.keySet()) {
            double val = statsObject.getDouble(key);
            stats.put(key, val);
        }

        return stats;
    }

    @NotNull
    public Set<Integer> getAllMinigameIDs(int seasonID) {
        String json = readFile();
        assert json != null;
        JSONObject jsonObject = new JSONObject(json);
        JSONArray seasonArray = new JSONArray(jsonObject.getJSONArray("Seasons"));
        JSONArray minigamesArray = getJSONArray_ByKeyFromArray(seasonArray, String.valueOf(seasonID));

        return IDSet(minigamesArray);
    }

    @NotNull
    public Set<Integer> getAllSeasonIDs() {
        String json = readFile();
        assert json != null;
        JSONObject jsonObject = new JSONObject(json);
        JSONArray seasonArray = new JSONArray(jsonObject.getJSONArray("Seasons"));

        return IDSet(seasonArray);
    }

    public void saveStat(UUID uuid, int seasonID, int minigameID, String statName, double value) {
        String json = readFile();
        assert json != null;
        JSONObject jsonObject = new JSONObject(json);
        JSONArray seasonArray = new JSONArray(jsonObject.getJSONArray("Seasons"));

        JSONArray minigamesArray = getJSONArray_ByKeyFromArray(seasonArray, String.valueOf(seasonID));
        JSONArray playerArray = getJSONArray_ByKeyFromArray(minigamesArray, String.valueOf(minigameID));
        JSONObject statsObject = getJSONObject_ByKeyFromArray(playerArray, uuid.toString()).getJSONObject(uuid.toString());

        if (!statsObject.has(statName)) {
            System.err.println("Dieses Minigame besitzt diese Statistik nicht!");
            return;
        }
        statsObject.put(statName, value);
        writeFile(jsonObject.toString());
    }

    public void saveStats(UUID uuid, int seasonID, int minigameID, HashMap<String, Double> statsMap) {
        for (String key : statsMap.keySet()) {
            saveStat(uuid, seasonID, minigameID, key, statsMap.get(key));
        }
    }

    public String getStructure(int minigameID) {
        String json = readFile();
        assert json != null;
        JSONObject jsonObject = new JSONObject(json);
        JSONObject minigames = jsonObject.getJSONObject("Minigames");
        return minigames.getString(String.valueOf(minigameID));
    }

    @NotNull
    private Set<Integer> IDSet(JSONArray jsonArray) {
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            for (String key : jsonObject.keySet()) {
                set.add(Integer.parseInt(key));
            }
        }

        return set;
    }

    private JSONArray getJSONArray_ByKeyFromArray(JSONArray jsonArray, String key) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String k = jsonObject.keySet().stream().toList().get(0);
            if (k.equals(key)) {
                return new JSONArray(jsonObject.getJSONArray(key));
            }
        }
        return new JSONArray();
    }

    private JSONObject getJSONObject_ByKeyFromArray(JSONArray jsonArray, String key) {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            String k = jsonObject.keySet().stream().toList().get(0);
            if (k.equals(key)) {
                return jsonObject;
            }
        }
        return new JSONObject();
    }

    private String readFile() {
        try {
            final FileReader fileReader = new FileReader(database);
            final BufferedReader bufferedReader = new BufferedReader(fileReader);

            StringBuilder jsonBuilder = new StringBuilder();
            String buffer;
            while ((buffer = bufferedReader.readLine()) != null) {
                jsonBuilder.append(buffer);
            }
            bufferedReader.close();
            fileReader.close();
            return jsonBuilder.toString();
        } catch (IOException e) {
            System.err.println("Konnte Datei nicht lesen!");
            return null;
        }
    }

    private void writeFile(String content) {
        try {
            final FileWriter fileWriter = new FileWriter(database);
            fileWriter.write(content);
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Konnte nicht in die Datei schreiben!");
        }
    }
}