package com.fastclient.mixin.mixins.minecraft.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.fastclient.management.mod.impl.player.ForceMainHandMod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;

/**
 * Mixin for getMainArm in PlayerEntity.
 * This is for MC 1.21 - 1.21.4 where getMainArm is in PlayerEntity.
 * For MC 1.21.11+, use MixinLivingEntityGetMainArm instead.
 */
@Mixin(PlayerEntity.class)
public class MixinPlayerEntityGetMainArm {

	@Inject(method = "getMainArm", at = @At("HEAD"), cancellable = true)
	private void injectGetMainArm(CallbackInfoReturnable<Arm> cir) {
		MinecraftClient client = MinecraftClient.getInstance();
		PlayerEntity player = client.player;
		PlayerEntity self = ((PlayerEntity) (Object) this);

		if (player != null && ForceMainHandMod.getInstance().isEnabled() && self.getId() != player.getId()) {
			cir.setReturnValue(ForceMainHandMod.getInstance().isRightHand() ? Arm.RIGHT : Arm.LEFT);
		}
	}
}
