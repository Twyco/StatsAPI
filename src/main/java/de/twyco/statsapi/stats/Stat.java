package de.twyco.statsapi.stats;

import org.jetbrains.annotations.NotNull;

public abstract class Stat <V extends Number> {

    protected final String statName;
    protected V value;

    protected Stat(@NotNull String statName, @NotNull V value){
        this.statName = statName;
        this.value = value;
    }

    protected String getStatName() {
        return statName;
    }

    public V getValue() {
        return value;
    }

    protected void setValue(V value) {
        this.value = value;
    }
}
