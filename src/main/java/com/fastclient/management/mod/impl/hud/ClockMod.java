package com.fastclient.management.mod.impl.hud;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.SimpleHUDMod;
import com.fastclient.skia.font.Icon;

public class ClockMod extends SimpleHUDMod{

	private DateFormat df = new SimpleDateFormat("HH:mm a", Locale.US);
	
	public ClockMod() {
		super("mod.clock.name", "mod.clock.description", Icon.SCHEDULE);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};
	
	@Override
	public String getText() {
		return df.format(Calendar.getInstance().getTime());
	}

	@Override
	public String getIcon() {
		return Icon.SCHEDULE;
	}
}
