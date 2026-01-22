package com.fastclient.management.mod.impl.settings;

import java.util.Arrays;

import com.fastclient.Fast;
import com.fastclient.event.EventBus;
import com.fastclient.event.client.ClientTickEvent;
import com.fastclient.management.mod.Mod;
import com.fastclient.management.mod.ModCategory;
import com.fastclient.management.mod.settings.impl.BooleanSetting;
import com.fastclient.management.mod.settings.impl.ComboSetting;
import com.fastclient.management.mod.settings.impl.NumberSetting;
import com.fastclient.skia.font.Icon;

public class HUDModSettings extends Mod {

	private static HUDModSettings instance;

	private BooleanSetting blurSetting = new BooleanSetting("setting.blur", "setting.blur.description", Icon.LENS_BLUR,
			this, true);
	private ComboSetting designSetting = new ComboSetting("setting.design", "setting.design.description", Icon.PALETTE,
			this, Arrays.asList("design.simple", "design.classic", "design.clear", "design.materialyou"),
			"design.simple");
	private NumberSetting blurIntensitySetting = new NumberSetting("setting.blurintensity",
			"setting.blurintensity.description", Icon.BLUR_LINEAR, this, 5, 1, 20, 1);

	public HUDModSettings() {
		super("mod.hudsettings.name", "mod.hudsettings.description", Icon.BROWSE_ACTIVITY, ModCategory.MISC);
		this.setHidden(true);
		this.setEnabled(true);

		instance = this;
	}

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {
		if (!designSetting.getOption().equals(Fast.getInstance().getModManager().getCurrentDesign().getName())) {
			Fast.getInstance().getModManager().setCurrentDesign(designSetting.getOption());
		}
	};

	@Override
	public void onDisable() {
		this.setEnabled(true);
	}

	public static HUDModSettings getInstance() {
		return instance;
	}

	public BooleanSetting getBlurSetting() {
		return blurSetting;
	}

	public NumberSetting getBlurIntensitySetting() {
		return blurIntensitySetting;
	}
}
