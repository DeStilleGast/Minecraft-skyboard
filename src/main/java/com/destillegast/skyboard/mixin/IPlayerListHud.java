package com.destillegast.skyboard.mixin;

import com.google.common.collect.Ordering;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Created by DeStilleGast 20-10-2019
 */
@Mixin(PlayerListHud.class)
public interface IPlayerListHud {

    @Accessor("ENTRY_ORDERING")
    @Final
    public Ordering<PlayerListEntry> getEntityOrdering();

    @Accessor("header")
    public Text getHeader();

    @Accessor("footer")
    public Text getFooter();


    // might be a hacky way to access these methodes (I don't know all the mixin methodes)
    @Invoker
    public void isRenderLatencyIcon(int width, int x, int y, PlayerListEntry playerListEntry);

    @Invoker
    public void isRenderScoreboardObjective(ScoreboardObjective scoreboardObjective, int y, String gameProfileName, int lengthA, int lengthB, PlayerListEntry playerListEntry);



}
