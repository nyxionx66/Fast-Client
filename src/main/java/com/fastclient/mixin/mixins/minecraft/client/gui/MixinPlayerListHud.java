package com.fastclient.mixin.mixins.minecraft.client.gui;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fastclient.management.presence.PresenceManager;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Mixin to render FastClient icon next to usernames in the tab list.
 * Uses TAIL injection on render to draw icons after vanilla rendering completes.
 */
@Mixin(PlayerListHud.class)
public abstract class MixinPlayerListHud {

    @Shadow
    @Final
    private MinecraftClient client;

    @Shadow
    @Nullable
    private Text header;

    @Shadow
    @Nullable
    private Text footer;

    @Shadow
    protected abstract List<PlayerListEntry> collectPlayerEntries();

    @Shadow
    protected abstract Text getPlayerName(PlayerListEntry entry);

    @Unique
    private static final Identifier FASTCLIENT_ICON = Identifier.of("fastclient", "textures/gui/icon.png");

    @Unique
    private static final int ICON_SIZE = 8;

    @Unique
    private static final int ICON_PADDING = 2;

    /**
     * Render FastClient icons after the vanilla tab list rendering.
     * We recalculate positions to match where vanilla drew the names.
     */
    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;ILnet/minecraft/scoreboard/Scoreboard;Lnet/minecraft/scoreboard/ScoreboardObjective;)V", at = @At("TAIL"))
    private void onRenderTail(DrawContext context, int scaledWindowWidth, Scoreboard scoreboard, @Nullable ScoreboardObjective objective, CallbackInfo ci) {
        List<PlayerListEntry> entries = this.collectPlayerEntries();
        if (entries.isEmpty()) return;

        // Calculate layout parameters (matching vanilla logic)
        int maxNameWidth = 0;
        int maxScoreWidth = 0;

        for (PlayerListEntry entry : entries) {
            int nameWidth = this.client.textRenderer.getWidth(this.getPlayerName(entry));
            maxNameWidth = Math.max(maxNameWidth, nameWidth);
            
            if (objective != null) {
                // Score width calculation would go here if needed
            }
        }

        int playerCount = entries.size();
        int columns = playerCount > 80 ? 4 : (playerCount > 40 ? 3 : (playerCount > 20 ? 2 : 1));
        int rows = (int) Math.ceil((double) playerCount / columns);

        int columnWidth = Math.min(maxNameWidth + 13 + 4, 150); // 13 for head, 4 for padding
        int totalWidth = columnWidth * columns + (columns - 1) * 5;
        
        int startX = scaledWindowWidth / 2 - totalWidth / 2;
        int startY = 10;

        // Account for header
        if (this.header != null) {
            startY += 10 + this.client.textRenderer.fontHeight;
        }

        // Iterate through entries and draw icons for FastClient users
        for (int i = 0; i < entries.size() && i < columns * rows; i++) {
            PlayerListEntry entry = entries.get(i);
            UUID playerUuid = entry.getProfile().getId();

            if (PresenceManager.isFastClientUser(playerUuid)) {
                int column = i / rows;
                int row = i % rows;

                int x = startX + column * (columnWidth + 5);
                int y = startY + row * 9;

                // Draw icon before the player head (which is 8x8 at x+1)
                // Position: just before where the skin head renders
                drawFastClientIcon(context, x - ICON_SIZE - 1, y);
            }
        }
    }

    @Unique
    private void drawFastClientIcon(DrawContext context, int x, int y) {
        context.drawTexture(
            RenderLayer::getGuiTextured,
            FASTCLIENT_ICON,
            x, y,
            0, 0,
            ICON_SIZE, ICON_SIZE,
            ICON_SIZE, ICON_SIZE
        );
    }
}
