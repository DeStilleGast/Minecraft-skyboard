package com.destillegast.skyboard;

import com.destillegast.skyboard.config.*;
import com.google.common.collect.Lists;
import io.github.prospector.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;

import java.io.IOException;
import java.util.function.Function;

/**
 * Created by DeStilleGast 22-10-2019
 */
public class ModMenuLoader implements ModMenuApi {

    @Override
    public String getModId() {
        return "skyboard";
    }

    @Override
    public Function<Screen, ? extends Screen> getConfigScreenFactory() {
        return Loader::openConfigScreen;
    }
}
