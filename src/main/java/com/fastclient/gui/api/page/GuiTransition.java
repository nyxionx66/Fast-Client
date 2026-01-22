package com.fastclient.gui.api.page;

import com.fastclient.animation.Animation;

public abstract class GuiTransition {

	private final boolean consecutive;

	public GuiTransition(boolean consecutive) {
		this.consecutive = consecutive;
	}

	public abstract float[] onTransition(Animation animation);

	public boolean isConsecutive() {
		return consecutive;
	}
}
