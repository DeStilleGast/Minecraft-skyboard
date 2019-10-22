package com.destillegast.skyboard.config;

import com.google.gson.annotations.Expose;

/**
 * Created by DeStilleGast 21-10-2019
 */
public class Config {

    @Expose
    public boolean enabled = true;

    @Expose
    public EnumHealthStyle healthStyle = EnumHealthStyle.SMART;

    @Expose
    public EnumHealthDisplay healthDisplay = EnumHealthDisplay.SHORTHP;

    @Expose
    public EnumSide displaySide = EnumSide.PLAYERFACING;

    @Expose
    public EnumSpectatorNames specatorNames = EnumSpectatorNames.STRIKETHROUGH;

    @Expose
    public int skyboardDistanceDrawing = 100;

    @Expose
    public int skyboardHeightDrawing = 50;

    @Expose
    public int skyboardAngle = 45;

    @Expose
    public int skyboardFadeBackDelay = 2000;

    @Expose
    public float skyboardFadeAnimationSpeed = 2F;

    @Expose
    public boolean forceShowHealth = false;
}
