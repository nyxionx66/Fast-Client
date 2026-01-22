package com.fastclient.management.mod.impl.render;

import com.fastclient.management.mod.Mod;
import com.fastclient.management.mod.ModCategory;
import com.fastclient.management.mod.settings.impl.NumberSetting;
import com.fastclient.skia.font.Icon;

public class FullbrightMod extends Mod {

	private static FullbrightMod instance;
	private NumberSetting gammaSetting = new NumberSetting("setting.gamma", "setting.gamma.description", Icon.LIGHTBULB,
			this, 15, 1, 15, 1);

	public FullbrightMod() {
		super("mod.fullbright.name", "mod.fullbright.description", Icon.LIGHTBULB, ModCategory.RENDER);
		instance = this;
	}

	public static FullbrightMod getInstance() {
		return instance;
	}
	
	public float getGamma() {
		return gammaSetting.getValue();
	}
}
