package com.fastclient.management.mod.impl.player;

import com.fastclient.management.mod.Mod;
import com.fastclient.management.mod.ModCategory;
import com.fastclient.skia.font.Icon;

public class HitDelayFixMod extends Mod {

	private static HitDelayFixMod instance;

	public HitDelayFixMod() {
		super("mod.hitdelayfix.name", "mod.hitdelayfix.description", Icon.TIMER_OFF, ModCategory.PLAYER);

		instance = this;
	}

	public static HitDelayFixMod getInstance() {
		return instance;
	}
}
