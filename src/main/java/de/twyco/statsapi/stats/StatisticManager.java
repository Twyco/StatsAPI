package de.twyco.statsapi.stats;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class StatisticManager {

    private final Statistic statistic;

    public StatisticManager(UUID uuid, Short minigameID, Short seasonID) {
        this.statistic = new Statistic(uuid, minigameID, seasonID);
    }

    public void reloadStats(){
        this.statistic.reloadStats();
    }

    public void setStatValue(String statName, double newValue) {
        if (!statistic.containsSavedStat(statName)) {
            System.err.println("Das angegebene Stat gehört nicht zu dem Spieler!");
        }
        this.statistic.getSavedStat(statName).setValue(newValue);
    }

    public void setStatValue(SavedStat stat, double newValue) {
        if (!statistic.containsSavedStat(stat)) {
            System.err.println("Das angegebene Stat gehört nicht zu dem Spieler!");
        }
        stat.setValue(newValue);
    }

    public void changeStatValue(String statName, double difference) {
        if (!statistic.containsSavedStat(statName)) {
            System.err.println("Das angegebene Stat gehört nicht zu dem Spieler!");
        }
        double currentValue = this.statistic.getSavedStat(statName).getValue();
        double newValue = currentValue + difference;
        this.statistic.getSavedStat(statName).setValue(newValue);
    }

    public void changeStatValue(SavedStat stat, double difference) {
        if (!statistic.containsSavedStat(stat)) {
            System.err.println("Das angegebene Stat gehört nicht zu dem Spieler!");
        }
        double currentValue = stat.getValue();
        double newValue = currentValue + difference;
        stat.setValue(newValue);
    }

    public void saveStatistic() {
        this.statistic.save();
    }

    public void setMinigameID(int minigameID) {
        this.statistic.setMinigameID(minigameID);
    }

    public int getMinigameID(int minigameID) {
        return this.statistic.getMinigameID();
    }

    public void setSeasonID(int seasonID) {
        this.statistic.setSeasonID(seasonID);
    }

    public int getSeasonID(int seasonID) {
        return this.statistic.getSeasonID();
    }

    public Set<String> getSavedStatsNames() {
        List<SavedStat> savedStatsList = this.statistic.getSavedStats().stream().toList();
        Set<String> savedStatsNames = new HashSet<>();
        for (SavedStat stat : savedStatsList) {
            savedStatsNames.add(stat.getStatName());
        }
        return savedStatsNames;
    }

    public Set<SavedStat> getSavedStats() {
        return this.statistic.getSavedStats();
    }

    public SavedStat getSavedStat(String savedStatName) {
        return this.statistic.getSavedStat(savedStatName);
    }

    public boolean containSavedStat(String savedStatName) {
        return this.statistic.containsSavedStat(savedStatName);
    }

    public boolean containSavedStat(SavedStat savedStat) {
        return this.statistic.containsSavedStat(savedStat);
    }

    public Set<String> getDisplayedStatsNames() {
        List<DisplayedStat> displayedStatsList = this.statistic.getDisplayedStats().stream().toList();
        Set<String> displayedStatsNames = new HashSet<>();
        for (DisplayedStat stat : displayedStatsList) {
            displayedStatsNames.add(stat.getStatName());
        }
        return displayedStatsNames;
    }

    public Set<DisplayedStat> getDisplayedStats() {
        return this.statistic.getDisplayedStats();
    }

    public DisplayedStat getDisplayedStat(String displayedStatName) {
        return this.statistic.getDisplayedStat(displayedStatName);
    }

    public boolean containDisplayedStat(String displayedStatName) {
        return this.statistic.containsDisplayedStat(displayedStatName);
    }

    public boolean containDisplayedStat(DisplayedStat displayedStat) {
        return this.statistic.containsDisplayedStat(displayedStat);
    }

    public UUID getUUID() {
        return this.statistic.getUUID();
    }

}
