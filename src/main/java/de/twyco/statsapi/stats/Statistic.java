package de.twyco.statsapi.stats;

import de.twyco.statsapi.misc.SQLDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Statistic {

    private final UUID uuid;
    private int statID;
    private int seasonID;
    private List<Stat> stats;

    public Statistic(UUID uuid, int statID, int seasonID) { //TODO Change back to protected
        this.uuid = uuid;
        this.statID = statID;
        this.seasonID = seasonID;
        loadStats();
    }

    private void loadStats() {
        this.stats = new ArrayList<>();
        HashMap<String, Double> stats;
        try {
            stats = SQLDatabase.getStats(this.uuid, this.seasonID, this.statID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (String key : stats.keySet()) {
            Stat stat = new Stat(key, stats.get(key));
            this.stats.add(stat);
        }
    }

    public List<Stat> getStats() {
        return stats;
    }


    /*protected void reloadStats() {
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
    }*/

    protected int getSeasonID() {
        return this.seasonID;
    }

    protected int getStatID() {
        return this.statID;
    }
    protected void setStatID(int statID) {
        this.statID = statID;
        loadStats();
    }

    protected void setSeasonID(int seasonID) {
        this.seasonID = seasonID;
        //reloadStats();
    }

    protected UUID getUUID() {
        return uuid;
    }
}
