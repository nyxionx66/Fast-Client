package com.fastclient.management.mod.impl.hud;

import java.text.DecimalFormat;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.SimpleHUDMod;
import com.fastclient.skia.font.Icon;

public class PitchDisplayMod extends SimpleHUDMod {

	private DecimalFormat df = new DecimalFormat("0.##");

	public PitchDisplayMod() {
		super("mod.pitchdisplay.name", "mod.pitchdisplay.description", Icon.ARROW_UPWARD);
	}

	public EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		return "Pitch: " + df.format(client.player.getPitch());
	}

	@Override
	public String getIcon() {
		return Icon.ARROW_UPWARD;
	}
}