package com.fastclient.mixin.mixins.minecraft.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.fastclient.management.mod.impl.player.ForceMainHandMod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Arm;

/**
 * Mixin for getMainArm in LivingEntity.
 * This is for MC 1.21.11+ where getMainArm was moved to LivingEntity.
 * For MC 1.21 - 1.21.4, use MixinPlayerEntityGetMainArm instead.
 */
@Mixin(LivingEntity.class)
public class MixinLivingEntityGetMainArm {

	@Inject(method = "getMainArm", at = @At("HEAD"), cancellable = true)
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
}
