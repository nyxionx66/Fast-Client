package com.fastclient.management.mod.impl.hud;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.SimpleHUDMod;
import com.fastclient.skia.font.Icon;

public class CoordsMod extends SimpleHUDMod {

	public CoordsMod() {
		super("mod.coords.name", "mod.coords.description", Icon.PIN_DROP);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		return "X: " + (int) client.player.getX() + " Y: " + (int) client.player.getY() + " Z: "
				+ (int) client.player.getZ();
	}

	@Override
	public String getIcon() {
		return Icon.PIN_DROP;
	}
}