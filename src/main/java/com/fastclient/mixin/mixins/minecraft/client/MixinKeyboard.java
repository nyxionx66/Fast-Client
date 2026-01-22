package com.fastclient.mixin.mixins.minecraft.client;

import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fastclient.Fast;
import com.fastclient.management.mod.settings.impl.KeybindSetting;

import net.minecraft.client.Keyboard;
import net.minecraft.client.util.InputUtil;

@Mixin(Keyboard.class)
public class MixinKeyboard {

	@Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;onKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;)V", shift = At.Shift.AFTER))
	public void onPressed(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {

		for (KeybindSetting s : Fast.getInstance().getModManager().getKeybindSettings()) {

			if (s.getKey().equals(InputUtil.fromKeyCode(key, scancode))) {

				if (action == GLFW.GLFW_PRESS) {
					s.setPressed();
				}

				s.setKeyDown(true);
			}
		}
	}

	@Inject(method = "onKey", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/option/KeyBinding;setKeyPressed(Lnet/minecraft/client/util/InputUtil$Key;Z)V", shift = At.Shift.AFTER, ordinal = 0))
	public void onReleased(long window, int key, int scancode, int action, int modifiers, CallbackInfo ci) {
		for (KeybindSetting s : Fast.getInstance().getModManager().getKeybindSettings()) {
			if (s.getKey().equals(InputUtil.fromKeyCode(key, scancode))) {
				s.setKeyDown(false);
			}
		}
	}
}
