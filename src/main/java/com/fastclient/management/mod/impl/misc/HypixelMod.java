package com.fastclient.management.mod.impl.misc;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.ClientTickEvent;
import com.fastclient.management.mod.Mod;
import com.fastclient.management.mod.ModCategory;
import com.fastclient.management.mod.settings.impl.BooleanSetting;
import com.fastclient.skia.font.Icon;
import com.fastclient.utils.TimerUtils;
import com.fastclient.utils.server.Server;
import com.fastclient.utils.server.ServerUtils;

public class HypixelMod extends Mod {

	private static HypixelMod instance;
	private BooleanSetting levelHeadSetting = new BooleanSetting("setting.levelhead", "setting.levelhead.description",
			Icon._123, this, false);
	private BooleanSetting autoTipSetting = new BooleanSetting("setting.autotip", "setting.autotip.description",
			Icon.MONEY, this, false);

	private TimerUtils tipTimer = new TimerUtils();

	public HypixelMod() {
		super("mod.hypixel.name", "mod.hypixel.description", Icon.CONSTRUCTION, ModCategory.MISC);

		instance = this;
	}

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
		if (autoTipSetting.isEnabled() && client.player != null && ServerUtils.isJoin(Server.HYPIXEL)) {
			if (tipTimer.delay(1200000)) {
				client.player.networkHandler.sendChatCommand("tip all");
				tipTimer.reset();
			}
		} else {
			tipTimer.reset();
		}
	};

	public static HypixelMod getInstance() {
		return instance;
	}

	public BooleanSetting getLevelHeadSetting() {
		return levelHeadSetting;
	}
}