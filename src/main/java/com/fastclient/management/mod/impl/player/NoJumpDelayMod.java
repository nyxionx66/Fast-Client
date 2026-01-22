package com.fastclient.management.mod.impl.player;

import com.fastclient.management.mod.Mod;
import com.fastclient.management.mod.ModCategory;
import com.fastclient.skia.font.Icon;

public class NoJumpDelayMod extends Mod {

	private static NoJumpDelayMod instance;

	public NoJumpDelayMod() {
		super("mod.nojumpdelay.name", "mod.nojumpdelay.description", Icon.KEYBOARD_DOUBLE_ARROW_UP, ModCategory.PLAYER);

		instance = this;
	}

	public static NoJumpDelayMod getInstance() {
		return instance;
	}
}