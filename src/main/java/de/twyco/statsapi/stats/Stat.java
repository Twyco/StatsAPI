package de.twyco.statsapi.stats;

import org.jetbrains.annotations.NotNull;

/**
 * Ein Stat ist eine Statistic, die einen Namen und Wert besitzt
 *
 * @author Twyco
 * @see StatisticManager
 */
public class Stat {

    private final String statName;
    private double value;

    protected Stat(@NotNull String statName, double value) {
        this.statName = statName;
        this.value = value;
    }

    /**
     * Auslesen des Namens der Statistik.
     *
     * @return der Name der Statistik.
     */
    public String getName() {
        return statName;
    }

    /**
     * Auslesen des Wertes der Statistik.
     *
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
