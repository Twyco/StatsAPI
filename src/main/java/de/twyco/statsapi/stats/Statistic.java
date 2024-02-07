package de.twyco.statsapi.stats;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Statistic {

    private final UUID uuid;
    private short minigameID;
    private short seasonID;
    private final Set<SavedStat<Number>> savedStats;
    private final Set<DisplayedStat<Number>> displayedStats;

    protected Statistic(UUID uuid, Short minigameID, Short seasonID) {
        this.uuid = uuid;
        this.minigameID = minigameID;
        this.seasonID = seasonID;
        this.savedStats = new HashSet<>();
        this.displayedStats = new HashSet<>();
    }

    private void loadStats() { //TODO change to void in UML
        //TODO Set savedStats and displayedStats

    }

    private void reloadStats() { //TODO change to void in UML
        //TODO Set savedStats and displayedStats

    }

    protected void save() { //TODO change to void in UML
        //TODO Save all SavedStats

    }

    public short getSeasonID() {
        return this.seasonID;
    }

    public short getMinigameID() {
        return this.minigameID;
    }

    public Set<DisplayedStat<Number>> getDisplayedStats() {
        return this.displayedStats;
    }

    public Set<SavedStat<Number>> getSavedStats() {
        return this.savedStats;
    }

    public SavedStat<Number> getSavedStat(String statName) {
        for (SavedStat<Number> savedStat : this.savedStats) {
            if (savedStat.getStatName().equals(statName)) {
                return savedStat;
            }
        }
        throw new RuntimeException("Es existiert keine SavedStat, mit dem Namen: " + statName);
    }

    public <V extends Number> boolean containsSavedStat(SavedStat<V> savedStat){
        return this.savedStats.contains(savedStat);
    }

    public DisplayedStat<Number> getDisplayedStat(String statName) {
        for (DisplayedStat<Number> displayedStat : this.displayedStats) {
            if (displayedStat.getStatName().equals(statName)) {
                return displayedStat;
            }
        }
        throw new RuntimeException("Es existiert keine DisplayedStat, mit dem Namen: " + statName);
    }

    public <V extends Number>  boolean containsDisplayedStat(DisplayedStat<V> displayedStat){
        return this.displayedStats.contains(displayedStat);
    }

    public void setMinigameID(short minigameID) {
        this.minigameID = minigameID;
        reloadStats();
    }

    public void setSeasonID(short seasonID) {
        this.seasonID = seasonID;
        reloadStats();
    }

    public UUID getUuid() { //TODO ins UML
        return uuid;
    }
}
