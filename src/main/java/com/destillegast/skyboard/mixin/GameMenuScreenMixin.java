package com.destillegast.skyboard.mixin;

import com.destillegast.skyboard.Loader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by DeStilleGast 22-10-2019
 */
@Mixin(GameMenuScreen.class)
public class GameMenuScreenMixin extends Screen {

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void initWidgets(CallbackInfo ci) {
        addButton(new ButtonWidget(10, this.height - 35, 150, 20, "Skyboard settings", buttonWidget ->
                MinecraftClient.getInstance().openScreen(Loader.openConfigScreen(MinecraftClient.getInstance().currentScreen))));
    }
}
