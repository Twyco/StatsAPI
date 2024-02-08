package de.twyco.statsapi.stats;

import org.jetbrains.annotations.NotNull;

/**
 * Eine Stat ist eine Statistic, die einen Namen und Wert besitzt
 * @see SavedStat
 * @see DisplayedStat
 * @author Twyco
 */
public abstract class Stat {

    protected final String statName;
    protected double value;

    protected Stat(@NotNull String statName, double value){
        this.statName = statName;
        setValue(value);
    }

    protected String getStatName() {
        return statName;
    }

    /**
     * Auslesen des Wertes der Statistik.
     * @return den Wert der Statistik.
     */
    public double getValue() {
        return value;
    }

    protected void setValue(double value) {
        value *= 100;
        value = (int) value;
        value /= 100;
        this.value = value;
    }

}
