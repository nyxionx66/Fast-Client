package com.fastclient.mixin.interfaces;

public interface IMixinCameraEntity {
	float getCameraPitch();
	float getCameraYaw();

	void setCameraPitch(float pitch);
	void setCameraYaw(float yaw);
}
