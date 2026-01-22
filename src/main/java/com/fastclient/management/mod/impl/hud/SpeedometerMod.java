package com.fastclient.management.mod.impl.hud;

import java.text.DecimalFormat;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.SimpleHUDMod;
import com.fastclient.skia.font.Icon;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

public class SpeedometerMod extends SimpleHUDMod {

	private DecimalFormat speedFormat = new DecimalFormat("0.00");

	public SpeedometerMod() {
		super("mod.speedometer.name", "mod.speedometer.description", Icon.SPEED);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};
	
	@Override
	public String getText() {

		String suffix = " m/s";
		
		if (client.player != null) {
			
			Entity entity = client.player.getVehicle() == null ? client.player : client.player.getVehicle();
			Vec3d vec = entity.getMovement();
			
			if (entity.isOnGround() && vec.y < 0) {
				vec = new Vec3d(vec.x, 0, vec.z);
			}
			
			return speedFormat.format(vec.length() * 20) + suffix;
		}

		return speedFormat.format(0) + suffix;
	}

	@Override
	public String getIcon() {
		return Icon.SPEED;
	}
}
