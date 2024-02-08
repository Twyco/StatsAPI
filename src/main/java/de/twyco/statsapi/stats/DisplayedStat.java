package de.twyco.statsapi.stats;

import de.twyco.statsapi.misc.StringCalculator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class DisplayedStat extends Stat {

    private final String structure;
    private final Set<SavedStat> savedStats;

    protected DisplayedStat(@NotNull String displayName, @NotNull String structure, Set<SavedStat> savedStats) {
        super(displayName, 0);
        this.structure = structure;
        this.savedStats = savedStats;
    }

    public String getDisplayName() {
        return super.getStatName();
    }

    @Override
    public double getValue() {
        super.setValue(decodeStatValue(structure));
        return super.value;
    }

    private double decodeStatValue(String value) {
        String replacedPlaceHolder = replacePlaceHolder(value);
        return StringCalculator.evaluateExpression(replacedPlaceHolder);
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
