package com.fastclient.mixin.mixins.minecraft.client.render;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fastclient.management.mod.impl.player.FreelookMod;
import com.fastclient.mixin.interfaces.IMixinCameraEntity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;

@Mixin(Camera.class)
public abstract class MixinCamera {

    @Unique
    boolean firstTime = true;

    @Shadow
    protected abstract void setRotation(float yaw, float pitch);
    
    @Inject(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V", ordinal = 1, shift = At.Shift.AFTER))
    public void lockRotation(BlockView focusedBlock, Entity cameraEntity, boolean isThirdPerson, boolean isFrontFacing, float tickDelta, CallbackInfo ci) {
    	
    	MinecraftClient client = MinecraftClient.getInstance();
    	
        if (FreelookMod.getInstance().isEnabled() && FreelookMod.getInstance().isActive() && cameraEntity instanceof ClientPlayerEntity) {
        	IMixinCameraEntity cameraOverriddenEntity = (IMixinCameraEntity) cameraEntity;

            if (firstTime && MinecraftClient.getInstance().player != null) {
                cameraOverriddenEntity.setCameraPitch(client.player.getPitch());
                cameraOverriddenEntity.setCameraYaw(client.player.getYaw());
                firstTime = false;
            }
            this.setRotation(cameraOverriddenEntity.getCameraYaw(), cameraOverriddenEntity.getCameraPitch());

        }
        if (FreelookMod.getInstance().isEnabled() && !FreelookMod.getInstance().isActive() && cameraEntity instanceof ClientPlayerEntity) {
            firstTime = true;
        }
    }
}
