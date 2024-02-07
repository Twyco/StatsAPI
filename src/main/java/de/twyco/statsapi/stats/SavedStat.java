package de.twyco.statsapi.stats;

import org.jetbrains.annotations.NotNull;

public class SavedStat <V extends Number> extends Stat<V>{

    protected SavedStat(@NotNull String statName, @NotNull V value) {
        super(statName, value);
    }

    @Override
    public String getStatName() {
        return super.getStatName();
    }
}
