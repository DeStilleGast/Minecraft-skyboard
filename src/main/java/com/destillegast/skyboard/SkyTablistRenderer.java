package com.destillegast.skyboard;

import com.destillegast.skyboard.config.*;
import com.destillegast.skyboard.mixin.IPlayerListHud;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.SystemUtil;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by DeStilleGast 20-10-2019
 */
public class SkyTablistRenderer extends DrawableHelper {

    private MinecraftClient client;
    private PlayerScanner playerScanner = new PlayerScanner();

    private EnumSide lastSide = EnumSide.SOUTH;
    private long lastSwitchBoard = 0;
    private float fadingTick = 0;

    public void render(MinecraftClient client) {
        if (!ConfigHandler.getConfig().enabled) return;

        if (this.client == null) this.client = client;
        playerScanner.scan(client);

        IPlayerListHud iPlayerListHud = ((IPlayerListHud) client.inGameHud.getPlayerListWidget());
        ClientPlayNetworkHandler networkHandler = client.player.networkHandler;
        List<PlayerListEntry> playerListEntries = iPlayerListHud.getEntityOrdering().sortedCopy(networkHandler.getPlayerList());

        if (ConfigHandler.getConfig().specatorNames == EnumSpectatorNames.HIDDEN) {
            playerListEntries = playerListEntries.stream().filter(ple -> ple.getGameMode() != GameMode.SPECTATOR).collect(Collectors.toList());
        }

        float switchDelay = ConfigHandler.getConfig().skyboardFadeBackDelay;

        EnumSide side = ConfigHandler.getConfig().displaySide;
        if (side == EnumSide.PLAYERFACING) {
            if (client.cameraEntity != null) {
                switch (client.cameraEntity.getHorizontalFacing()) {
                    case WEST:
                        side = EnumSide.WEST;
                        break;
                    case EAST:
                        side = EnumSide.EAST;
                        break;
                    case NORTH:
                        side = EnumSide.NORTH;
                        break;
                    case SOUTH:
                    default:
                        side = EnumSide.SOUTH;
                        break;
                }
                float animationSpeed = ConfigHandler.getConfig().skyboardFadeAnimationSpeed;


                // why time based you ask, not everyone has the same FPS
                if (side != lastSide && (lastSwitchBoard + switchDelay) > SystemUtil.getMeasuringTimeMs()) {
                    side = lastSide;

                    if (fadingTick < 120) {
                        float timeLeft = (float) (lastSwitchBoard - SystemUtil.getMeasuringTimeMs()) / 2;

                        fadingTick = Math.min(-timeLeft * animationSpeed / 1000 * 120, 120);
                    }
                } else {
                    lastSide = side;

                    if (fadingTick > 0) {
                        float timeLeft = (switchDelay) - (float) (SystemUtil.getMeasuringTimeMs() - lastSwitchBoard) / 2;
                        fadingTick = Math.max(timeLeft * animationSpeed / 1000 * 120, 0);
                    } else {
                        lastSwitchBoard = SystemUtil.getMeasuringTimeMs();
                    }
                }
            } else {
                side = EnumSide.SOUTH;
            }
        }

//        if (playerListEntries.size() > 60) {
//
//            List sub = playerListEntries.subList(0, playerListEntries.size() / 2);
//            List two = new ArrayList(sub);
//            sub.clear(); // since sub is backed by one, this removes all sub-list items from one
//
////        playerListEntries = two;
//
//            GlStateManager.pushMatrix();
//
//            // SOUTH
//            GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 1.0F);         // (rotate it (default upside down))
//            GlStateManager.rotatef(45.0F, -1F, 0F, 0F);               // look at player (make it look bit nicer)
//            GlStateManager.translated(/*-125.5D*/ -512D / 2, -50, 100); // position in sky
//
//            GlStateManager.scalef(0.5F, 0.5F, 0F);
//
//            renderTabList(client, playerListEntries, client.world.getScoreboard(), client.world.getScoreboard().getObjectiveForSlot(0));
//            GlStateManager.popMatrix();
//
//            GlStateManager.pushMatrix();
//
//            GlStateManager.rotatef(180.0F, 1.0F, 0, 0);         // (rotate it (default upside down))
//            GlStateManager.rotatef(45.0F, 1F, 0F, 0F);               // look at player (make it look bit nicer)
//            GlStateManager.translated(/*-125.5D*/ -512D / 2, -50, 100); // position in sky
////        GlStateManager.rotatef(-180, 0, 1, 1);
//
//
//            GlStateManager.scalef(0.5F, 0.5F, 0F);
//
//            renderTabList(client, two, client.world.getScoreboard(), client.world.getScoreboard().getObjectiveForSlot(0));
//            GlStateManager.popMatrix();
//        } else {
        GlStateManager.pushMatrix();

        int rescalingY = Math.min(playerListEntries.size(), 20) * 2;
        int angle = ConfigHandler.getConfig().skyboardAngle;


        switch (side) {
            case SOUTH:
            default:
                GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 1.0F);          // (rotate it (default upside down))
                GlStateManager.rotatef(90 - angle, -1F, 0F, 0F);                // look at player (make it look bit nicer)
                break;
            case NORTH:
                GlStateManager.rotatef(180.0F, 1.0F, 0F, 0F);              // (rotate it (default upside down))
                GlStateManager.rotatef(angle, 1F, 0F, 0F);                 // look at player (make it look bit nicer)
                break;
            case EAST:
                GlStateManager.rotatef(180.0F, 1F, 0F, 0F);                // (rotate it (default upside down))
                GlStateManager.rotatef(90.0F, 0F, 1F, 0F);                 // look at player (make it look bit nicer)
                GlStateManager.rotatef(angle, 1F, 0F, 0F);
                break;
            case WEST:
                GlStateManager.rotatef(180.0F, 1F, 0F, 0F);                // (rotate it (default upside down))
                GlStateManager.rotatef(90.0F, 0F, -1F, 0F);                // look at player (make it look bit nicer)
                GlStateManager.rotatef(angle, 1F, 0F, 0F);
        }

        GlStateManager.translated(/*-125.5D*/ -512D / 2, -rescalingY + 50 - ConfigHandler.getConfig().skyboardHeightDrawing, ConfigHandler.getConfig().skyboardDistanceDrawing + (fadingTick * 5)); // position in sky

        GlStateManager.scalef(0.5F, 0.5F, 0F);

        renderTabList(client, playerListEntries, client.world.getScoreboard(), client.world.getScoreboard().getObjectiveForSlot(0));
        GlStateManager.popMatrix();
    }

    // Copied and edited from vanilla
    private void renderTabList(MinecraftClient client, List<PlayerListEntry> playerListEntries, Scoreboard scoreboard, ScoreboardObjective objective) {

        IPlayerListHud iPlayerListHud = ((IPlayerListHud) client.inGameHud.getPlayerListWidget());
        PlayerListHud playerListHud = client.inGameHud.getPlayerListWidget();

        boolean forceShowHealth = ConfigHandler.getConfig().forceShowHealth;


        int maxPlayernameLength = 0;
        int int_3 = 0;
        Iterator playerlistIterator = playerListEntries.iterator();

        int playernameLength;
        while (playerlistIterator.hasNext()) {
            PlayerListEntry playerListEntry = (PlayerListEntry) playerlistIterator.next();
            playernameLength = client.textRenderer.getStringWidth(playerListHud.getPlayerName(playerListEntry).asFormattedString());
            maxPlayernameLength = Math.max(maxPlayernameLength, playernameLength);
            if (objective != null && objective.getRenderType() != ScoreboardCriterion.RenderType.HEARTS) {
                playernameLength = client.textRenderer.getStringWidth(" " + scoreboard.getPlayerScore(playerListEntry.getProfile().getName(), objective).getScore());
                int_3 = Math.max(int_3, playernameLength);
            }
        }

        playerListEntries = playerListEntries.subList(0, Math.min(playerListEntries.size(), 80));
        int playerListSize = playerListEntries.size();
        int playerListSizeDraw = playerListSize;

        for (playernameLength = 1; playerListSizeDraw > 20; playerListSizeDraw = (playerListSize + playernameLength - 1) / playernameLength) {
            ++playernameLength;
        }

        boolean smartSizeSmall = ConfigHandler.getConfig().healthStyle == EnumHealthStyle.SMART && playerListSize > 40 || ConfigHandler.getConfig().healthStyle == EnumHealthStyle.SHORT;
        boolean shouldShowHeads = true;//client.isInSingleplayer() || client.getNetworkHandler().getConnection().isEncrypted();
        int extraWidth;
        if (forceShowHealth || objective != null) {
            if (forceShowHealth || objective.getRenderType() == ScoreboardCriterion.RenderType.HEARTS) {
                extraWidth = smartSizeSmall ? 50 : 90;
            } else {
                extraWidth = int_3;
            }
        } else {
            extraWidth = 0;
        }

//        fill(0, 0, 500, 5, 0xffffffff);

        // TODO: make it center on 0
        int scaledWidth = 512 * 2;

        int entryWidth = Math.min(playernameLength * ((shouldShowHeads ? 9 : 0) + maxPlayernameLength + extraWidth + 13), scaledWidth - 50) / playernameLength;
        int x = scaledWidth / 2 - (entryWidth * playernameLength + (playernameLength - 1) * 5) / 2;
        int headerMaxHeight = 10;
        int headerWidth = entryWidth * playernameLength + (playernameLength - 1) * 5;
        List<String> headerText = null;
        if (iPlayerListHud.getHeader() != null) {
            headerText = client.textRenderer.wrapStringToWidthAsList(iPlayerListHud.getHeader().asFormattedString(), scaledWidth - 50);

            String string_1;
            for (Iterator var18 = headerText.iterator(); var18.hasNext(); headerWidth = Math.max(headerWidth, client.textRenderer.getStringWidth(string_1))) {
                string_1 = (String) var18.next();
            }
        }

        List<String> footerList = null;
        String string_3;
        Iterator var36;
        if (iPlayerListHud.getFooter() != null) {
            footerList = client.textRenderer.wrapStringToWidthAsList(iPlayerListHud.getFooter().asFormattedString(), scaledWidth - 50);

            for (var36 = footerList.iterator(); var36.hasNext(); headerWidth = Math.max(headerWidth, client.textRenderer.getStringWidth(string_3))) {
                string_3 = (String) var36.next();
            }
        }

        int var10000;
        int var10001;
        int var10002;
        int var10004;
        int rowX;

        // HEADER
        if (headerText != null) {
            var10000 = scaledWidth / 2 - headerWidth / 2 - 1;
            var10001 = headerMaxHeight - 1;
            var10002 = scaledWidth / 2 + headerWidth / 2 + 1;
            var10004 = headerText.size();

            fill(var10000, var10001, var10002, headerMaxHeight + var10004 * 9, -2147483648);

            for (var36 = headerText.iterator(); var36.hasNext(); headerMaxHeight += 9) {
                string_3 = (String) var36.next();
                rowX = client.textRenderer.getStringWidth(string_3);
                client.textRenderer.drawWithShadow(string_3, (float) (scaledWidth / 2 - rowX / 2), (float) headerMaxHeight, -1);

            }

            ++headerMaxHeight;
        }

        // flikker fix
        GlStateManager.disableDepthTest();

        GlStateManager.pushMatrix();
        GlStateManager.translatef(0, 0, 1);
        fill(scaledWidth / 2 - headerWidth / 2 - 2, headerMaxHeight - 2, scaledWidth / 2 + headerWidth / 2 + 2, headerMaxHeight + playerListSizeDraw * 9 + 1, 0xbb000000);

        GlStateManager.popMatrix();

        int textBackgroundColor = client.options.getTextBackgroundColor(553648127);

        int rowY;
        for (int i = 0; i < playerListSize; ++i) {
            rowX = i / playerListSizeDraw;
            rowY = i % playerListSizeDraw;
            int entryX = x + rowX * entryWidth + rowX * 5;
            int entryY = headerMaxHeight + rowY * 9;

//            GlStateManager.translatef(0, 0, 1);
            fill(entryX, entryY, entryX + entryWidth, entryY + 8, /*textBackgroundColor*/ 0xaa000000);

            GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1F);
            GlStateManager.enableAlphaTest();
            GlStateManager.enableBlend();
            GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            if (i < playerListEntries.size()) {
                PlayerListEntry playerEntry = (PlayerListEntry) playerListEntries.get(i);
                GameProfile gameProfile_1 = playerEntry.getProfile();
                int maxUsageLength;
                if (shouldShowHeads) {
                    PlayerEntity playerEntity_1 = client.world.getPlayerByUuid(gameProfile_1.getId());
                    boolean dinnerboneCape = playerEntity_1 != null && playerEntity_1.isSkinOverlayVisible(PlayerModelPart.CAPE) && ("Dinnerbone".equals(gameProfile_1.getName()) || "Grumm".equals(gameProfile_1.getName()));
                    client.getTextureManager().bindTexture(playerEntry.getSkinTexture());
                    maxUsageLength = 8 + (dinnerboneCape ? 8 : 0);
                    int int_23 = 8 * (dinnerboneCape ? -1 : 1);
                    DrawableHelper.blit(entryX, entryY, 8, 8, 8.0F, (float) maxUsageLength, 8, int_23, 64, 64);
                    if (playerEntity_1 != null && playerEntity_1.isSkinOverlayVisible(PlayerModelPart.HAT)) {
                        int int_24 = 8 + (dinnerboneCape ? 8 : 0);
                        int int_25 = 8 * (dinnerboneCape ? -1 : 1);
                        DrawableHelper.blit(entryX, entryY, 8, 8, 40.0F, (float) int_24, 8, int_25, 64, 64);
                    }

                    entryX += 9;
                }

                Text entryName = playerListHud.getPlayerName(playerEntry);

                if (playerEntry.getGameMode() == GameMode.SPECTATOR) {
                    client.textRenderer.drawWithShadow(new LiteralText("").formatted(ConfigHandler.getConfig().specatorNames == EnumSpectatorNames.STRIKETHROUGH ? Formatting.STRIKETHROUGH : Formatting.ITALIC).append(entryName).asFormattedString(), (float) entryX, (float) entryY, -1862270977);
                } else {
                    client.textRenderer.drawWithShadow(entryName.asFormattedString(), (float) entryX, (float) entryY, -1);
                }

                int guessPlayerHealth = (int) playerScanner.getPlayerHealth(playerEntry.getProfile());

                if (objective != null && playerEntry.getGameMode() != GameMode.SPECTATOR || forceShowHealth && guessPlayerHealth > 0) {
                    int maxEntryLength = entryX + maxPlayernameLength + 1;
                    maxUsageLength = maxEntryLength + extraWidth;
                    if (maxUsageLength - maxEntryLength > 5) {
                        switch (ConfigHandler.getConfig().healthStyle) {
                            case LONG:
                                if (guessPlayerHealth == 0 || forceShowHealth)
                                    break; // If we don't break, we will crash, game doesn't handle null objective
                                iPlayerListHud.isRenderScoreboardObjective(objective, entryY, gameProfile_1.getName(), maxEntryLength, maxUsageLength, playerEntry);
                                break;
                            case SHORT:
                                renderScoreObjective(objective, entryY, gameProfile_1.getName(), maxEntryLength, maxUsageLength, playerEntry, guessPlayerHealth);
                                break;
                            case SMART:
                                if (smartSizeSmall) {
                                    renderScoreObjective(objective, entryY, gameProfile_1.getName(), maxEntryLength, maxUsageLength, playerEntry, guessPlayerHealth);
                                } else {
                                    if (guessPlayerHealth == 0 || forceShowHealth) break;
                                    iPlayerListHud.isRenderScoreboardObjective(objective, entryY, gameProfile_1.getName(), maxEntryLength, maxUsageLength, playerEntry);
                                }
                        }
                    }
                }
//                GlStateManager.translated(1, 1, 0.9);

                GlStateManager.pushMatrix();
//                GlStateManager.scalef(1.5F, 1.5F ,0);
                iPlayerListHud.isRenderLatencyIcon(entryWidth, entryX - (shouldShowHeads ? 9 : 0), entryY, playerEntry);
                GlStateManager.popMatrix();


            }
        }

        if (footerList != null) {
            headerMaxHeight += playerListSizeDraw * 9 + 1;
            var10000 = scaledWidth / 2 - headerWidth / 2 - 1;
            var10001 = headerMaxHeight - 1;
            var10002 = scaledWidth / 2 + headerWidth / 2 + 1;
            var10004 = footerList.size();

            fill(var10000, var10001, var10002, headerMaxHeight + var10004 * 9, -2147483648);

            for (Iterator footerLine = footerList.iterator(); footerLine.hasNext(); headerMaxHeight += 9) {
                String footerText = (String) footerLine.next();
                rowY = client.textRenderer.getStringWidth(footerText);
                client.textRenderer.drawWithShadow(footerText, (float) (scaledWidth / 2 - rowY / 2), (float) headerMaxHeight, -1);
            }
        }

        // flikker fix
        GlStateManager.enableDepthTest();
    }

    private void renderScoreObjective(ScoreboardObjective scoreboardObjective, int y, String gameprofileName, int left, int right, PlayerListEntry playerListEntry, int fallbackScore) {
        int playerScore = scoreboardObjective != null ? scoreboardObjective.getScoreboard().getPlayerScore(gameprofileName, scoreboardObjective).getScore() : fallbackScore;
        if (scoreboardObjective == null || scoreboardObjective.getRenderType() == ScoreboardCriterion.RenderType.HEARTS) {
            this.client.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
            long long_1 = SystemUtil.getMeasuringTimeMs();

            // helps with blinking
            if (playerScore < playerListEntry.method_2973()) {                              // getLastHealth
                playerListEntry.method_2978(long_1);                                        // setLastHealthTime
                playerListEntry.method_2975((long) (client.inGameHud.getTicks() + 20));     // setHealthBlinkTime
            } else if (playerScore > playerListEntry.method_2973()) {
                playerListEntry.method_2978(long_1);                                        // setLastHealthTime
                playerListEntry.method_2975((long) (client.inGameHud.getTicks() + 10));     // setHealthBlinkTime
            }

            playerListEntry.method_2972(playerScore);     // setLastHealth
            int maxHearths = MathHelper.ceil((float) Math.max(playerScore, playerListEntry.method_2960()) / 2.0F);
//            int playerHP = Math.max(MathHelper.ceil((float) (playerScore / 2)), Math.max(MathHelper.ceil((float) (playerListEntry.method_2960() / 2)), 10));
            boolean blink = playerListEntry.method_2961() > (long) client.inGameHud.getTicks() && (playerListEntry.method_2961() - (long) client.inGameHud.getTicks()) / 3L % 2L == 1L;

            if (maxHearths > 0) {
                int rightSide = left + (right - left);


                float colorDefiner = MathHelper.clamp((float) playerScore / 20.0F, 0.0F, 1.0F);
                int stringColor = (int) ((1.0F - colorDefiner) * 255.0F) << 16 | (int) (colorDefiner * 255.0F) << 8;
                String hpString;//+ (float) playerScore / 2.0F;
//                if (right - this.client.textRenderer.getStringWidth(hpString + "hp") >= left) {
//                    hpString = hpString + "hp";
//                }
                EnumHealthDisplay displayStyle = ConfigHandler.getConfig().healthDisplay;

                switch (displayStyle) {
                    case LONG:
                    case LONGHP:
                    default:
                        hpString = displayStyle.format(playerScore / 2D);
                        break;
                    case SHORT:
                    case SHORTHP:
                        hpString = displayStyle.format(playerScore);
                        break;
                }


                int offsetRight = client.textRenderer.getStringWidth(hpString) + 12;

                // heart background
                this.blit(rightSide - offsetRight, y, blink ? 25 : 16, 0, 9, 9);

                // red full heart
//                if (1 < playerScore) {
//                    this.blit(rightSide - offsetRight, y, 52, 0, 9, 9);
//                }
//
//                // red half hearts
//                if (1 == playerScore) {
//                    this.blit(rightSide - offsetRight, y, 61, 0, 9, 9);
//                }

                if (playerScore > 10) {
                    this.blit(rightSide - offsetRight, y, 52, 0, 9, 9);
                } else {
                    this.blit(rightSide - offsetRight, y, 61, 0, 9, 9);
                }


                this.client.textRenderer.drawWithShadow(hpString, (float) rightSide - this.client.textRenderer.getStringWidth(hpString), (float) y, stringColor);
//                }
            }
        } else {
            // currentHP is now score
            String string_3 = Formatting.YELLOW + "" + playerScore;
            this.client.textRenderer.drawWithShadow(string_3, (float) (right - this.client.textRenderer.getStringWidth(string_3)), (float) y, 16777215);
        }
    }

//    private Text getPlayerName(PlayerListEntry playerListEntry_1) {
//        return playerListEntry_1.getDisplayName() != null ? playerListEntry_1.getDisplayName() : Team.modifyText(playerListEntry_1.getScoreboardTeam(), new LiteralText(playerListEntry_1.getProfile().getName()));
//    }


//    protected void renderLatencyIcon(int int_1, int int_2, int int_3, PlayerListEntry playerListEntry_1) {
//        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.client.getTextureManager().bindTexture(GUI_ICONS_LOCATION);
////        int int_4 = false;
//        byte int_10;
//        if (playerListEntry_1.getLatency() < 0) {
//            int_10 = 5;
//        } else if (playerListEntry_1.getLatency() < 150) {
//            int_10 = 0;
//        } else if (playerListEntry_1.getLatency() < 300) {
//            int_10 = 1;
//        } else if (playerListEntry_1.getLatency() < 600) {
//            int_10 = 2;
//        } else if (playerListEntry_1.getLatency() < 1000) {
//            int_10 = 3;
//        } else {
//            int_10 = 4;
//        }
//
//        this.blitOffset += 100;
//        this.blit(int_2 + int_1 - 11 , int_3, 0, 176 + int_10 * 8, 10, 8);
//        this.blitOffset -= 100;
//    }

//    class EntryOrderComparator implements Comparator<PlayerListEntry> {
//        private EntryOrderComparator() {
//        }
//
//        public int compare(PlayerListEntry playerListEntry, PlayerListEntry playerListEntry1) {
//            Team team_1 = playerListEntry.getScoreboardTeam();
//            Team team_2 = playerListEntry1.getScoreboardTeam();
//            return ComparisonChain.start().compareTrueFirst(playerListEntry.getGameMode() != GameMode.SPECTATOR, playerListEntry1.getGameMode() != GameMode.SPECTATOR).compare(team_1 != null ? team_1.getName() : "", team_2 != null ? team_2.getName() : "").compare(playerListEntry.getProfile().getName(), playerListEntry1.getProfile().getName(), String::compareToIgnoreCase).result();
//        }
//    }
}
