package com.fastclient.mixin.mixins.minecraft.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.fastclient.management.mod.impl.hud.JumpResetIndicatorMod;
import com.fastclient.management.mod.impl.player.ForceMainHandMod;
import com.fastclient.management.mod.impl.player.NoJumpDelayMod;
import com.fastclient.mixin.interfaces.IMixinLivingEntity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;
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

	// getMainArm is in LivingEntity for MC 1.21.11+, in PlayerEntity for earlier versions
	// Using require = 0 makes this optional - won't crash if method doesn't exist here
	@Inject(method = "getMainArm", at = @At("HEAD"), cancellable = true, require = 0)
	private void injectGetMainArm(CallbackInfoReturnable<Arm> cir) {
		LivingEntity self = (LivingEntity) (Object) this;
		
		// Only apply to PlayerEntity instances that aren't the local player
		if (self instanceof PlayerEntity) {
			MinecraftClient client = MinecraftClient.getInstance();
			if (client.player != null && ForceMainHandMod.getInstance().isEnabled() 
					&& self.getId() != client.player.getId()) {
				cir.setReturnValue(ForceMainHandMod.getInstance().isRightHand() ? Arm.RIGHT : Arm.LEFT);
			}
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
