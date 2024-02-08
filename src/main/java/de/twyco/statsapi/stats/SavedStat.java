package de.twyco.statsapi.stats;

import org.jetbrains.annotations.NotNull;

public class SavedStat extends Stat{

    protected SavedStat(@NotNull String statName, double value) {
        super(statName, value);
    }

    @Override
    public String getStatName() {
        return super.getStatName();
    }
}
