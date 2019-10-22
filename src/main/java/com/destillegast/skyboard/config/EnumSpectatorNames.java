package com.destillegast.skyboard.config;

import net.minecraft.util.Formatting;

/**
 * Created by DeStilleGast 22-10-2019
 */
public enum EnumSpectatorNames {
    NORMAL(Formatting.ITALIC + "Vanilla"),
    STRIKETHROUGH(Formatting.STRIKETHROUGH + "Strikethrough"),
    HIDDEN("Hidden");

    private String displayString;

    EnumSpectatorNames(String displayString) {
        this.displayString = displayString;
    }

    @Override
    public String toString() {
        return this.displayString;
    }
}
