package com.destillegast.skyboard.config;

import java.text.DecimalFormat;

/**
 * Created by DeStilleGast 21-10-2019
 */
public enum EnumHealthDisplay {
    SHORT("20", "##"),
    SHORTHP("20HP", "##HP"),
    LONG("10.0", "##.#"),
    LONGHP("10.0HP", "##.#HP");

    private String displayString;
    private DecimalFormat formatter;

    EnumHealthDisplay(String displayString, String format) {
        this.displayString = displayString;
        this.formatter = new DecimalFormat(format);
    }

    @Override
    public String toString() {
        return displayString;
    }

    public String format(double input) {
        return formatter.format(input);
    }
}
