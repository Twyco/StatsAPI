package de.twyco.statsapi.stats;

import org.jetbrains.annotations.NotNull;

/**
 * Eine SavedStat ist eine Statistik, die von {@link Stat} erbt.
 * SavedStats sind die Statistiken, die Gespeichert werden.
 * @see StatisticManager
 * @see DisplayedStat
 * @see Stat
 * @author Twyco
 */
public class SavedStat extends Stat{

    protected SavedStat(@NotNull String statName, double value) {
        super(statName, value);
    }

    /**
     * Auslesen des Namens der Statistik.
     * @return Den Namen der Statistik.
     */
    @Override
    public String getStatName() {
        return super.getStatName();
    }


}
