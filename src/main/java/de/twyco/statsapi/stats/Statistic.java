package de.twyco.statsapi.stats;

import de.twyco.statsapi.misc.Data;
import de.twyco.statsapi.misc.SQLDatabase;
import de.twyco.statsapi.startup.Settings;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unused")
public class Statistic {

    private UUID uuid;
    private int statID;
    private int seasonID;
    private final List<Stat> stats;

    public Statistic(UUID uuid, int statID, int seasonID) {
        if (Settings.isWriteAPI()) {
            throw new IllegalArgumentException("Please use the Statistic(UUID uuid) Constructor, if you are using a write API.");
        }
        this.uuid = uuid;
        this.statID = statID;
        this.seasonID = seasonID;
        this.stats = new ArrayList<>();
        loadStats();
    }

    public Statistic(UUID uuid) {
        if (!Settings.isWriteAPI()) {
            throw new IllegalArgumentException("Please use the Statistic(UUID uuid, int statID, int seasonID) Constructor, if you are using a read only API.");
        }
        this.uuid = uuid;
        this.statID = Settings.getStatsID();
        this.seasonID = Data.getCurrentSeason();
        this.stats = new ArrayList<>();
        loadStats();
    }

    private void loadStats() {
        this.stats.clear();
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

    public Stat getStat(String statName) {
        for (Stat stat : stats) {
            if (stat.getName().equals(statName)) {
                return stat;
            }
        }
        throw new IllegalArgumentException("Can't find the stat (" + statName + ") for " + statID);
    }

    public void setStat(String statName, double value) {
        if (!Settings.isWriteAPI()) {
            return;
        }
        for (Stat stat : stats) {
            if (stat.getName().equals(statName)) {
                stat.setValue(value);
                return;
            }
        }
        System.err.println("Can't find the stat (" + statName + ") for " + statID);
    }

    public void updateStat(String statName, double differenz) {
        if (!Settings.isWriteAPI()) {
            return;
        }
        for (Stat stat : stats) {
            if (stat.getName().equals(statName)) {
                stat.setValue(stat.getValue() + differenz);
                return;
            }
        }
        System.err.println("Can't find the stat (" + statName + ") for " + statID);
    }

    public void updateStat(String statName, double differenz, double lowerLimit) {
        if (!Settings.isWriteAPI()) {
            return;
        }
        for (Stat stat : stats) {
            if (stat.getName().equals(statName)) {
                stat.setValue(Math.max(stat.getValue() + differenz, lowerLimit));
                return;
            }
        }
        System.err.println("Can't find the stat (" + statName + ") for " + statID);
    }

    public void save() {
        if (!Settings.isWriteAPI()) {
            return;
        }
        try {
            SQLDatabase.saveStats(this.stats, this.uuid, this.seasonID, this.statID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getSeasonID() {
        return this.seasonID;
    }

    public void setSeasonID(int seasonID) {
        if (Settings.isWriteAPI()) {
            return;
        }
        this.seasonID = seasonID;
        loadStats();
    }

    public int getStatID() {
        return this.statID;
    }

    public void setStatID(int statID) {
        if (Settings.isWriteAPI()) {
            return;
        }
        this.statID = statID;
        loadStats();
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
        loadStats();
    }
}
