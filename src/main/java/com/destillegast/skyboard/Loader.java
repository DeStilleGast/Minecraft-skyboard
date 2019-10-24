package com.destillegast.skyboard;

import com.destillegast.skyboard.config.*;
import com.google.common.collect.Lists;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.MessageType;
import net.minecraft.text.LiteralText;

import java.io.IOException;

/**
 * Created by DeStilleGast 21-10-2019
 */
public class Loader implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
    }


    public static Screen openConfigScreen(Screen parentScreen) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setTitle("Skyboard config screen")
                .setSavingRunnable(Loader::saveConfig);


        ConfigCategory scrolling = builder.getOrCreateCategory("Skyboard");
        ConfigCategory skyboardDrawSettings = builder.getOrCreateCategory("Drawing settings");
        ConfigEntryBuilder entryBuilder = ConfigEntryBuilder.create();

        ConfigHandler.readConfig();
        Config c = ConfigHandler.getConfig();

        scrolling.addEntry(entryBuilder.startBooleanToggle("Enabled", c.enabled).setSaveConsumer(b -> c.enabled = b).build());

        scrolling.addEntry(entryBuilder.startSubCategory("Hearts in skyboard", Lists.newArrayList(
                entryBuilder.startEnumSelector("Heart render style", EnumHealthStyle.class, c.healthStyle).setSaveConsumer(e -> c.healthStyle = e).buildEntry(),
                entryBuilder.startEnumSelector("Heart display style (for short or smart)", EnumHealthDisplay.class, c.healthDisplay).setSaveConsumer(e -> c.healthDisplay = e).buildEntry(),
                entryBuilder.startBooleanToggle("Force show hearts (limited)", c.forceShowHealth).setTooltip("Forcing to show hearths on the scoreboard does", "not display the health of all players,", "the players need to be in range", "to retrieve the information,", "On some server this information is can be faked !", "", "This can also be seen as cheating !").setSaveConsumer(b -> c.forceShowHealth = b).buildEntry()
        )).setTooltip("This has only effect if there are hearts in the normal scoreboard").build());

        scrolling.addEntry(entryBuilder.startEnumSelector("Spectator names names", EnumSpectatorNames.class, c.specatorNames).setSaveConsumer(e -> c.specatorNames = e).build());

        skyboardDrawSettings.addEntry(entryBuilder.startEnumSelector("Display skyboard in this direction", EnumSide.class, c.displaySide).setSaveConsumer(e -> c.displaySide = e).build());
        skyboardDrawSettings.addEntry(entryBuilder.startIntSlider("Fade back delay", c.skyboardFadeBackDelay, 0, 8000).setSaveConsumer(i -> c.skyboardFadeBackDelay = i).setTextGetter(i -> i + " MS / " + i.toString().charAt(0) + " Sec").setTooltip("This has only effect if", "skyboard direction is set on \"" + EnumSide.PLAYERFACING + "\"").build());
        skyboardDrawSettings.addEntry(entryBuilder.startIntSlider("Animation speed", (int) (c.skyboardFadeAnimationSpeed * 10), 10, 100).setSaveConsumer(i -> c.skyboardFadeAnimationSpeed = i / 10F).setTextGetter(i -> "x" + i / 10F).build());
        skyboardDrawSettings.addEntry(entryBuilder.startIntSlider("Draw distance", c.skyboardDistanceDrawing, 50, 125).setSaveConsumer(d -> c.skyboardDistanceDrawing = d).build());
        skyboardDrawSettings.addEntry(entryBuilder.startIntSlider("Draw height", c.skyboardHeightDrawing, 0, 100).setSaveConsumer(d -> c.skyboardHeightDrawing = d).build());
        skyboardDrawSettings.addEntry(entryBuilder.startIntSlider("Draw angle", c.skyboardAngle, 0, 90).setSaveConsumer(d -> c.skyboardAngle = d).setTooltip("You might need to adjust height and distance after changing this value").setTextGetter(i -> i + " degree").build());

        return builder.setParentScreen(parentScreen).build();
    }

    private static void saveConfig() {
        try {
            ConfigHandler.saveConfig();
        } catch (IOException e) {
            MinecraftClient.getInstance().inGameHud.addChatMessage(MessageType.CHAT, new LiteralText("Skyboard settings failed to save"));
            e.printStackTrace();
        }
    }
}
