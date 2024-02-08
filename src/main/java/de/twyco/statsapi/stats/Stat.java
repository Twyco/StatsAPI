package de.twyco.statsapi.stats;

import org.jetbrains.annotations.NotNull;

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
