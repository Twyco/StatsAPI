package de.twyco.statsapi.stats;

import java.util.UUID;

public class PlayerStatistic {

    private final Statistic playerStatistic;

    public PlayerStatistic(UUID uuid, Short minigameID, Short seasonID) {
        this.playerStatistic = new Statistic(uuid, minigameID, seasonID);
    }

    public PlayerStatistic(UUID uuid, Short minigameID) {
        //TODO Change (short) 0 to current seasonID
        this.playerStatistic = new Statistic(uuid, minigameID, (short) 0);
    }

    public Statistic getPlayerStatistic() {
        return this.playerStatistic;
    }

    public void setStatValue(SavedStat<Number> stat, Number newValue) {
        if (!playerStatistic.containsSavedStat(stat)) {
            throw new RuntimeException("Das angegebene Stat gehört nicht zu dem Spieler!");
        }
        stat.setValue(newValue);
    }

    public <V extends Number> void changeStatValue(SavedStat<V> stat, V difference) {
        if (!playerStatistic.containsSavedStat(stat)) {
            throw new RuntimeException("Das angegebene Stat gehört nicht zu dem Spieler!");
        }
        V currentValue = stat.getValue();
        double newValue = currentValue.doubleValue() + difference.doubleValue();
        stat.setValue((V) Double.valueOf(newValue));
    }

    public void saveStatistic() { //TODO void ins UML
        this.playerStatistic.save();
    }
}
