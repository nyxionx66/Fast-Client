package com.fastclient.event.client;

import com.fastclient.event.Event;

public class PlayerDirectionChangeEvent extends Event {

	private final float prevPitch, prevYaw, pitch, yaw;

	public PlayerDirectionChangeEvent(float prevPitch, float prevYaw, float pitch, float yaw) {
		this.prevPitch = prevPitch;
		this.prevYaw = prevYaw;
		this.pitch = pitch;
		this.yaw = yaw;
	}

	public float getPrevPitch() {
		return prevPitch;
	}

	public float getPrevYaw() {
		return prevYaw;
	}

	public float getPitch() {
		return pitch;
	}

	public float getYaw() {
		return yaw;
	}
}
