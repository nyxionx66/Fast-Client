package com.fastclient.management.mod.impl.hud;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.SimpleHUDMod;
import com.fastclient.skia.font.Icon;
import com.fastclient.utils.server.ServerUtils;

public class ServerIPDisplayMod extends SimpleHUDMod {

	public ServerIPDisplayMod() {
		super("mod.serveripdisplay.name", "mod.serveripdisplay.description", Icon.DNS);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {

		if (ServerUtils.isSingleplayer()) {
			return "Singleplayer";
		}

		return ServerUtils.getAddress();
	}

	@Override
	public String getIcon() {
		return Icon.DNS;
	}
}