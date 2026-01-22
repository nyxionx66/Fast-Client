package com.fastclient.management.mod.impl.hud;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.SimpleHUDMod;
import com.fastclient.skia.font.Icon;

public class DayCounterMod extends SimpleHUDMod {

	public DayCounterMod() {
		super("mod.daycounter.name", "mod.daycounter.description", Icon.TODAY);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		long time = client.world.getTime() / 24000L;
		return time + " Day" + (time > 1L ? "s" : "");
	}

	@Override
	public String getIcon() {
		return Icon.TODAY;
	}
}