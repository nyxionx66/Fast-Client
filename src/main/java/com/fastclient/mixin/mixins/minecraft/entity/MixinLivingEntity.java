package com.fastclient.mixin.mixins.minecraft.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fastclient.management.mod.impl.hud.JumpResetIndicatorMod;
import com.fastclient.management.mod.impl.player.NoJumpDelayMod;
import com.fastclient.mixin.interfaces.IMixinLivingEntity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Hand;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity implements IMixinLivingEntity {

	@Shadow
	private int jumpingCooldown;

	@Shadow
	public int handSwingTicks;

	@Shadow
	public boolean handSwinging;

	@Shadow
	public Hand preferredHand;

	@Shadow
	public abstract int getHandSwingDuration();

	@Inject(method = "tickMovement", at = @At("HEAD"))
	public void onNoJumpDelay(CallbackInfo ci) {
		if (NoJumpDelayMod.getInstance().isEnabled()) {
			jumpingCooldown = 0;
		}
	}

	@Inject(method = "jump", at = @At("HEAD"))
	private void onJump(CallbackInfo info) {

		JumpResetIndicatorMod mod = JumpResetIndicatorMod.getInstance();
		MinecraftClient client = MinecraftClient.getInstance();

		if ((LivingEntity) (Object) this == client.player) {
			mod.setJumpAge(client.player.age);
			mod.setLastTime(System.currentTimeMillis());
		}
	}

	@Inject(method = "onDamaged", at = @At("HEAD"))
	private void onDamage(CallbackInfo info) {

		JumpResetIndicatorMod mod = JumpResetIndicatorMod.getInstance();
		MinecraftClient client = MinecraftClient.getInstance();

		if ((LivingEntity) (Object) this == client.player) {
			mod.setHurtAge(client.player.age);
		}
	}

	@Override
	public void fakeSwingHand(Hand hand) {
		if (!this.handSwinging || this.handSwingTicks >= this.getHandSwingDuration() / 2 || this.handSwingTicks < 0) {
			this.handSwingTicks = -1;
			this.handSwinging = true;
			this.preferredHand = hand;
		}
	}
}
