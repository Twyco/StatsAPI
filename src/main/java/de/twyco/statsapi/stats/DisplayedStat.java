package de.twyco.statsapi.stats;

import de.twyco.statsapi.calculator.Calculator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;


/**
 * Eine DisplayedStat ist eine Statistik, die von {@link Stat} erbt.
 * DisplayedStats sind die Statistiken, die einen Displaynamen, Struktur (Berechnung des Wertes) und SavedStats besitzt.
 * Eine DisplayedStat berechnet durch die Struktur, mit den SavedStats seinen Wert.
 *
 * @see StatisticManager
 * @see SavedStat
 * @see Stat
 * @author Twyco
 */
public class DisplayedStat extends Stat {

    private final String structure;
    private final Set<SavedStat> savedStats;

    protected DisplayedStat(@NotNull String displayName, @NotNull String structure, Set<SavedStat> savedStats) {
        super(displayName, 0);
        this.structure = structure;
        this.savedStats = savedStats;
    }

    /**
     * Auslesen des Displaynamens / Ausgabename der Statistik.
     * @return Der Displayname / Ausgabename der Statistik.
     */
    public String getDisplayName() {
        return super.getStatName();
    }

    /**
     * Auslesen des berechneten Wertes der Statistik.
     * @return Der Wert der Statistik.
     */
    @Override
    public double getValue() {
        super.setValue(decodeStatValue(structure));
        return super.value;
    }

    private double decodeStatValue(String value) {
        String replacedPlaceHolder = replacePlaceHolder(value);
        return Calculator.calculateStringTerm(replacedPlaceHolder);
    }

    private String replacePlaceHolder(String s) {
        String output = s;
        for (SavedStat stat : savedStats) {
            if (output.contains("%" + stat.getStatName() + "%")) {
                output = output.replace("%" + stat.getStatName() + "%", String.valueOf(stat.getValue()));
            }
        }
        if (output.contains("%")) {
            throw new RuntimeException("Ungültiges Display Pattern: " + s);
        }
        return output;
    }
}
