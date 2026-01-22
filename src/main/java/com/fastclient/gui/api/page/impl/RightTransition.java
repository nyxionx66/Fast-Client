package com.fastclient.gui.api.page.impl;

import com.fastclient.animation.Animation;
import com.fastclient.gui.api.page.GuiTransition;

public class RightTransition extends GuiTransition {

	public RightTransition(boolean consecutive) {
		super(consecutive);
	}

	@Override
	public float[] onTransition(Animation animation) {

		float progress = animation.getValue();
		float x = 0;

		if (animation.getEnd() == 1) {
			x = 1 - progress;
		} else {
			x = 1 + -progress;
		}

		return new float[] { x, 0 };
	}
}
