package com.fastclient.management.mod.impl.hud;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.SimpleHUDMod;
import com.fastclient.skia.font.Icon;

public class HealthDisplayMod extends SimpleHUDMod {

	public HealthDisplayMod() {
		super("mod.healthdisplay.name", "mod.healthdisplay.description", Icon.FAVORITE);
	}

	public EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		return (int) client.player.getHealth() + " Health";
	}

	@Override
	public String getIcon() {
		return Icon.FAVORITE;
	}
}