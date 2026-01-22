package com.fastclient.mixin.mixins.minecraft.client.util;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fastclient.skia.context.SkiaContext;

import net.minecraft.client.util.Window;

@Mixin(Window.class)
public class MixinWindow {

	@Inject(method = "onFramebufferSizeChanged", at = @At("RETURN"))
	private void onFramebufferSizeChanged(long window, int width, int height, CallbackInfo ci) {
		SkiaContext.createSurface(width, height);
	}
}
