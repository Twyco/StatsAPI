package de.twyco.statsapi.stats;

import org.jetbrains.annotations.NotNull;

public class DisplayedStat <V extends Number> extends Stat<V>{

    protected DisplayedStat(@NotNull String statName, @NotNull V value) {
        super(statName, value);
    }

    public String getDisplayName(){
        return getStatName();
    }

}
