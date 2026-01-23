package com.fastclient.mixin.mixins.minecraft.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.fastclient.management.mod.impl.player.ForceMainHandMod;
import com.fastclient.management.mod.impl.player.OldAnimationsMod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;

@Mixin(PlayerEntity.class)
public class MixinPlayerEntity {

	@Inject(method = "getAttackCooldownProgress", at = @At("HEAD"), cancellable = true)
	public void disableCooldown(CallbackInfoReturnable<Float> cir) {
		if (OldAnimationsMod.getInstance().isEnabled() && OldAnimationsMod.getInstance().isDisableAttackCooldown()) {
			cir.setReturnValue(1F);
		}
	}

	// getMainArm exists in PlayerEntity for MC 1.21-1.21.4, moved to LivingEntity in 1.21.11
	// Using require = 0 makes this optional - won't crash if method doesn't exist
	@Inject(method = "getMainArm", at = @At("HEAD"), cancellable = true, require = 0)
	private void injectGetMainArm(CallbackInfoReturnable<Arm> cir) {

		MinecraftClient client = MinecraftClient.getInstance();
		PlayerEntity player = client.player;
		PlayerEntity e = ((PlayerEntity) (Object) this);

		if (player != null && ForceMainHandMod.getInstance().isEnabled() && e.getId() != player.getId()) {
			cir.setReturnValue(ForceMainHandMod.getInstance().isRightHand() ? Arm.RIGHT : Arm.LEFT);
		}
	}
}
