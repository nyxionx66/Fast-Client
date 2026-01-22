package com.fastclient.management.mod.impl.hud;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.SimpleHUDMod;
import com.fastclient.skia.font.Icon;

public class MemoryUsageMod extends SimpleHUDMod {

	public MemoryUsageMod() {
		super("mod.memoryusage.name", "mod.memoryusage.description", Icon.MEMORY_ALT);
	}
	
	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		this.draw();
	};

	@Override
	public String getText() {
		return "Mem: " + getUsingMemory() + "%";
	}

	@Override
	public String getIcon() {
		return Icon.MEMORY_ALT;
	}

	private long getUsingMemory() {
		Runtime runtime = Runtime.getRuntime();
		return (runtime.totalMemory() - runtime.freeMemory()) * 100L / runtime.maxMemory();
	}
}
