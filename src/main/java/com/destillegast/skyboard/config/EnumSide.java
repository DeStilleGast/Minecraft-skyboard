package com.destillegast.skyboard.config;

/**
 * Created by DeStilleGast 21-10-2019
 */
public enum EnumSide {
    NORTH("North"),
    EAST("East"),
    SOUTH("South"),
    WEST("West"),
    PLAYERFACING("Player direction");

    private String displayString;

    EnumSide(String displayString) {
        this.displayString = displayString;
    }

    @Override
    public String toString() {
        return this.displayString;
    }
}
