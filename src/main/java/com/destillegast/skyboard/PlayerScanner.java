package com.destillegast.skyboard;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DeStilleGast 22-10-2019
 */
public class PlayerScanner {

    private List<PlayerEntity> knownPlayers = new ArrayList<>();

    public void scan(MinecraftClient client) {
        knownPlayers.clear();
        client.world.getEntities().forEach(entity -> {
            if (entity instanceof PlayerEntity) {
                knownPlayers.add((PlayerEntity) entity);
            }
        });
    }

    public float getPlayerHealth(GameProfile gp) {
        return knownPlayers.stream().filter(player -> player.getGameProfile().equals(gp)).map(LivingEntity::getHealth).findFirst().orElse(0F);
    }
}
