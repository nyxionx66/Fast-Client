package com.fastclient.mixin.mixins.minecraft.entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.PlayerDirectionChangeEvent;
import com.fastclient.management.mod.impl.player.FreelookMod;
import com.fastclient.mixin.interfaces.IMixinCameraEntity;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

@Mixin(Entity.class)
public abstract class MixinEntity implements IMixinCameraEntity {

	@Unique
	private float cameraPitch;

	@Unique
	private float cameraYaw;

	@Shadow
	public abstract float getPitch();

	@Shadow
	public abstract float getYaw();

	@Inject(method = "changeLookDirection", at = @At("HEAD"))
	private void onPlayerDirectionChange(double cursorDeltaX, double cursorDeltaY, CallbackInfo ci) {

		float prevPitch = getPitch();
		float prevYaw = getYaw();
		float pitch = prevPitch + (float) (cursorDeltaY * .15);
		float yaw = prevYaw + (float) (cursorDeltaX * .15);
		pitch = MathHelper.clamp(pitch, -90.0F, 90.0F);

		EventBus.getInstance().post(new PlayerDirectionChangeEvent(prevPitch, prevYaw, pitch, yaw));
	}

	@Inject(method = "changeLookDirection", at = @At("HEAD"), cancellable = true)
	public void changeCameraLookDirection(double xDelta, double yDelta, CallbackInfo ci) {
		if (FreelookMod.getInstance().isEnabled() && FreelookMod.getInstance().isActive()
				&& (Entity) (Object) this instanceof ClientPlayerEntity) {
			double pitchDelta = (yDelta * 0.15);
			double yawDelta = (xDelta * 0.15);

			this.cameraPitch = MathHelper.clamp(this.cameraPitch + (float) pitchDelta, -90.0f, 90.0f);
			this.cameraYaw += (float) yawDelta;

			ci.cancel();

		}
	}

	@Override
	@Unique
	public float getCameraPitch() {
		return this.cameraPitch;
	}

	@Override
	@Unique
	public float getCameraYaw() {
		return this.cameraYaw;
	}

	@Override
	@Unique
	public void setCameraPitch(float pitch) {
		this.cameraPitch = pitch;
	}

	@Override
	@Unique
	public void setCameraYaw(float yaw) {
		this.cameraYaw = yaw;
	}
}
