package com.fastclient.management.mod.settings.impl;

import com.fastclient.Fast;
import com.fastclient.libraries.material3.hct.Hct;
import com.fastclient.management.mod.Mod;
import com.fastclient.management.mod.settings.Setting;

public class HctColorSetting extends Setting {

	private Hct hct;
	private Hct defaultHct;

	public HctColorSetting(String name, String description, String icon, Mod parent, Hct hct) {
		super(name, description, icon, parent);
		this.hct = hct;
		this.defaultHct = hct;

		Fast.getInstance().getModManager().addSetting(this);
	}

	@Override
	public void reset() {
		this.hct = defaultHct;
	}

	public Hct getHct() {
		return hct;
	}

	public void setHct(Hct hct) {
		this.hct = hct;
	}

	public Hct getDefaultHct() {
		return defaultHct;
	}
}