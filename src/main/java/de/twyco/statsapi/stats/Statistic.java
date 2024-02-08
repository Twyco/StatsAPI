package de.twyco.statsapi.stats;

import org.json.*;

import java.util.*;

public class Statistic {

    private final UUID uuid;
    private int minigameID;
    private int seasonID;
    private final Set<SavedStat> savedStats;
    private final Set<DisplayedStat> displayedStats;

    protected Statistic(UUID uuid, int minigameID, int seasonID) {
        this.uuid = uuid;
        this.minigameID = minigameID;
        this.seasonID = seasonID;
        this.savedStats = new HashSet<>();
        this.displayedStats = new HashSet<>();
        loadStats();
    }

    private void loadStats() {
        //TODO get json structure form Database
        String json = "{\"saved\":[\"kills\",\"deaths\",\"games\",\"games_won\"],\"displayed\":[{\"Deine Kills\":\"%kills%\"},{\"Deine Tode\":\"%deaths%\"},{\"Deine K/D\":\"%kills%/%deaths%\"},{\"Winrate\":\"(%games_won%/%games%)*100\"}]}";
        JSONObject jsonObject = new JSONObject(json);
        JSONArray savedStats = jsonObject.getJSONArray("saved");
        JSONArray displayedStatsStructure = jsonObject.getJSONArray("displayed");
        Random random = new Random();
        for (Object obj : savedStats) {
            String statName = String.valueOf(obj);
            double value = (int) (random.nextDouble() * 10); //TODO Get Value from Database with seasonID, minigameID, and statName
            System.out.println(statName + " " + value);
            this.savedStats.add(new SavedStat(statName, value));
        }
        for (int i = 0; i < displayedStatsStructure.length(); i++) {
            JSONObject item = displayedStatsStructure.getJSONObject(i);
            String displayStatsName = item.keySet().stream().toList().get(0);
            String structure = item.getString(displayStatsName);
            this.displayedStats.add(new DisplayedStat(displayStatsName, structure, this.savedStats));
        }

    }

    protected void reloadStats() {
        for (SavedStat stat : this.savedStats) {
            String statName = stat.getStatName();
            double value = 0; //TODO Get Value from Database with seasonID, minigameID, and statName
            stat.setValue(value);
        }
    }

    protected void save() {
        for (SavedStat stat : this.savedStats) {
            String statName = stat.getStatName();
            double value = stat.getValue();
            //TODO Save Value into Database with seasonID, minigameID, and statName
        }
    }

    protected int getSeasonID() {
        return this.seasonID;
    }

    protected int getMinigameID() {
        return this.minigameID;
    }

    protected Set<DisplayedStat> getDisplayedStats() {
        return this.displayedStats;
    }

    protected Set<SavedStat> getSavedStats() {
        return this.savedStats;
    }

    protected SavedStat getSavedStat(String statName) {
        for (SavedStat savedStat : this.savedStats) {
            if (savedStat.getStatName().equals(statName)) {
                return savedStat;
            }
        }
        throw new RuntimeException("Es existiert keine SavedStat, mit dem Namen: " + statName);
    }

    protected boolean containsSavedStat(String savedStatName) {
        for (SavedStat stat : this.savedStats) {
            if (stat.getStatName().equals(savedStatName)) {
                return true;
            }
        }
        return false;
    }

    protected boolean containsSavedStat(SavedStat stat) {
        return this.savedStats.contains(stat);
    }

    protected DisplayedStat getDisplayedStat(String statName) {
        for (DisplayedStat displayedStat : this.displayedStats) {
            if (displayedStat.getStatName().equals(statName)) {
                return displayedStat;
            }
        }
        throw new RuntimeException("Es existiert keine DisplayedStat, mit dem Namen: " + statName);
    }

    protected boolean containsDisplayedStat(String displayedStatName) {
        for (DisplayedStat stat : this.displayedStats) {
            if (stat.getDisplayName().equals(displayedStatName)) {
                return true;
            }
        }
        return false;
    }

    protected boolean containsDisplayedStat(DisplayedStat displayedStat) {
        return this.displayedStats.contains(displayedStat);
    }

    protected void setMinigameID(int minigameID) {
        this.minigameID = minigameID;
        //TODO Methode zum neuladen eines anderen minigames
    }

    protected void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
        reloadStats();
    }

    protected UUID getUUID() {
        return uuid;
    }
}
