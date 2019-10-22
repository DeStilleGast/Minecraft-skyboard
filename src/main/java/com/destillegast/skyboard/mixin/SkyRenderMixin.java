package com.destillegast.skyboard.mixin;

import com.destillegast.skyboard.SkyTablistRenderer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Created by DeStilleGast 20-10-2019
 */
@Mixin(WorldRenderer.class)
public class SkyRenderMixin {

    @Shadow
    @Final
    private MinecraftClient client;

    private SkyTablistRenderer skyRenderer = new SkyTablistRenderer();

    @Inject(method = "renderSky", at = @At("RETURN"))
    public void renderSky(float delta, CallbackInfo ci) {
        skyRenderer.render(client);
    }
}
