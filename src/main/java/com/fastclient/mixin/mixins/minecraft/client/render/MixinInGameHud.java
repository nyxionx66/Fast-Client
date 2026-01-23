package com.fastclient.mixin.mixins.minecraft.client.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderGameOverlayEvent;
import com.fastclient.management.mod.impl.player.OldAnimationsMod;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(InGameHud.class)
public class MixinInGameHud {

	@Inject(method = "drawHeart(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/gui/hud/InGameHud$HeartType;IIZZZ)V", at = @At("HEAD"), cancellable = true)
	private void onDrawHeart(DrawContext context, InGameHud.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half, CallbackInfo ci) {
		OldAnimationsMod mod = OldAnimationsMod.getInstance();
		// Disable heart flash animation if enabled
		boolean actualBlinking = mod.isEnabled() && mod.isDisableHeartFlash() ? false : blinking;
		context.drawGuiTexture(RenderLayer::getGuiTextured, type.getTexture(hardcore, half, actualBlinking), x, y, 9, 9);
		ci.cancel();
	}
    
	@Inject(method = "renderMainHud(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V", at = @At("TAIL"))
	private void onRenderMainHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
		EventBus.getInstance().post(new RenderGameOverlayEvent(context));
	}
}
