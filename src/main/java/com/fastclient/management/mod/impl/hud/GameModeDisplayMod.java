package com.fastclient.management.mod.impl.hud;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.SimpleHUDMod;
import com.fastclient.skia.font.Icon;

public class GameModeDisplayMod extends SimpleHUDMod {

	public GameModeDisplayMod() {
		super("mod.gamemodedisplay.name", "mod.gamemodedisplay.description", Icon.SPORTS_ESPORTS);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {

		String prefix = "Mode: ";

		if (client.player.isCreative()) {
			return prefix + "Creative";
		} else if (client.player.isSpectator()) {
			return prefix + "Spectator";
		} else {
			return prefix + "Survival";
		}
	}

	@Override
	public String getIcon() {
		return Icon.SPORTS_ESPORTS;
	}
}