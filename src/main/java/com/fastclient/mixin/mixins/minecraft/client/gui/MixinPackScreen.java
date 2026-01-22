package com.fastclient.mixin.mixins.minecraft.client.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fastclient.gui.GuiResourcePackConvert;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Mixin(PackScreen.class)
public class MixinPackScreen extends Screen {

	protected MixinPackScreen(Text title) {
		super(title);
	}

	@Inject(method = "init", at = @At("HEAD"))
	private void onInit(CallbackInfo ci) {

		ButtonWidget.Builder builder = ButtonWidget
				.builder(Text.of("Convert"), button -> client.setScreen(new GuiResourcePackConvert(this))).size(98, 20);

		builder.position(width - 98 - 5, 5);
		this.addDrawableChild(builder.build());
	}
}
