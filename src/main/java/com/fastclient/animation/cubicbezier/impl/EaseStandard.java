package com.fastclient.animation.cubicbezier.impl;

import com.fastclient.animation.cubicbezier.CubicBezier;

public class EaseStandard extends CubicBezier {

	public EaseStandard(float duration, float start, float end) {
		super(0.2F, 0.0F, 0F, 1F, duration, start, end);
	}
}
