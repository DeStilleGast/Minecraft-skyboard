package com.destillegast.skyboard.config;

/**
 * Created by DeStilleGast 21-10-2019
 */
public enum EnumHealthStyle {
    SHORT("Short"),
    LONG("Long"),
    SMART("Smart");

    private String displayString;

    EnumHealthStyle(String display) {
        this.displayString = display;
    }

    @Override
    public String toString() {
        return this.displayString;
    }
}
