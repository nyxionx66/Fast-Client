package com.fastclient.management.mod.impl.settings;

import org.lwjgl.glfw.GLFW;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.ClientTickEvent;
import com.fastclient.gui.modmenu.GuiModMenu;
import com.fastclient.libraries.material3.hct.Hct;
import com.fastclient.management.mod.Mod;
import com.fastclient.management.mod.ModCategory;
import com.fastclient.management.mod.settings.impl.BooleanSetting;
import com.fastclient.management.mod.settings.impl.HctColorSetting;
import com.fastclient.management.mod.settings.impl.KeybindSetting;
import com.fastclient.management.mod.settings.impl.NumberSetting;
import com.fastclient.skia.font.Icon;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.InputUtil;

public class ModMenuSettings extends Mod {

	private static ModMenuSettings instance;

	private KeybindSetting keybindSetting = new KeybindSetting("setting.keybind", "setting.keybind.description",
			Icon.KEYBOARD, this, InputUtil.fromKeyCode(GLFW.GLFW_KEY_RIGHT_SHIFT, 0));
	private BooleanSetting darkModeSetting = new BooleanSetting("setting.darkmode", "setting.darkmode.description",
			Icon.DARK_MODE, this, true);
	private HctColorSetting hctColorSetting = new HctColorSetting("setting.color", "setting.color.description",
			Icon.PALETTE, this, Hct.fromInt(0xFFE65100));
	private BooleanSetting blurSetting = new BooleanSetting("setting.blur", "setting.blur.description", Icon.LENS_BLUR,
			this, true);
	private NumberSetting blurIntensitySetting = new NumberSetting("setting.blurintensity",
			"setting.blurintensity.description", Icon.BLUR_LINEAR, this, 5, 1, 20, 1);

	private Screen modMenu;

	public ModMenuSettings() {
		super("mod.modmenu.name", "mod.modmenu.description", Icon.MENU, ModCategory.MISC);

		instance = this;
		this.setHidden(true);
		this.setEnabled(true);
	}

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {

		if (keybindSetting.isPressed()) {

			if (modMenu == null) {
				modMenu = new GuiModMenu().build();
			}

			client.setScreen(modMenu);
		}
	};

	@Override
	public void onDisable() {
		this.setEnabled(true);
	}

	public static ModMenuSettings getInstance() {
		return instance;
	}

	public BooleanSetting getDarkModeSetting() {
		return darkModeSetting;
	}

	public HctColorSetting getHctColorSetting() {
		return hctColorSetting;
	}

	public BooleanSetting getBlurSetting() {
		return blurSetting;
	}

	public NumberSetting getBlurIntensitySetting() {
		return blurIntensitySetting;
	}

	public Screen getModMenu() {
		return modMenu;
	}
}
