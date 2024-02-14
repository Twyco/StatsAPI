package de.twyco.statsapi.stats;

import de.twyco.statsapi.misc.Database;
import org.json.*;

import java.util.*;

public class Statistic {

    private final UUID uuid;
    private int minigameID;
    private int seasonID;
    private Set<Stat> stats;

    protected Statistic(UUID uuid, int minigameID, int seasonID) {
        this.uuid = uuid;
        this.minigameID = minigameID;
        this.seasonID = seasonID;
        this.stats = new HashSet<>();
        //loadStats();
    }

   /* private void loadStats() {
        this.stats = new HashSet<>();
        JSONObject jsonObject = new JSONObject(structure);
        JSONArray savedStats = jsonObject.getJSONArray("saved");
        JSONArray displayedStatsStructure = jsonObject.getJSONArray("displayed");
        HashMap<String, Double> stats = this.database.getStats(this.uuid, this.seasonID, this.minigameID);
        for (Object obj : savedStats) {
            String statName = String.valueOf(obj);
            double value = stats.get(statName);
            this.savedStats.add(new SavedStat(statName, value));
        }
        for (int i = 0; i < displayedStatsStructure.length(); i++) {
            JSONObject item = displayedStatsStructure.getJSONObject(i);
            String displayStatsName = item.keySet().stream().toList().get(0);
            String itemStructure = item.getString(displayStatsName);
            this.displayedStats.add(new DisplayedStat(displayStatsName, itemStructure, this.savedStats));
        }

    }


    protected void reloadStats() {
        HashMap<String, Double> stats = this.database.getStats(this.uuid, this.seasonID, this.minigameID);
        for (SavedStat stat : this.savedStats) {
            String statName = stat.getStatName();
            double value = stats.get(statName);
            stat.setValue(value);
        }
    }

    protected void save() {
        for (SavedStat stat : this.savedStats) {
            String statName = stat.getStatName();
            double value = stat.getValue();
            database.saveStat(this.uuid, this.seasonID, this.minigameID, statName, value);
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
        loadStats();
    }

    protected void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
        reloadStats();
    }

    protected UUID getUUID() {
        return uuid;
    }*/
}
