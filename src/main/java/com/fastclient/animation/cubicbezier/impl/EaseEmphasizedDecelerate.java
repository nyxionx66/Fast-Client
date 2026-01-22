package com.fastclient.animation.cubicbezier.impl;

import com.fastclient.animation.cubicbezier.CubicBezier;

public class EaseEmphasizedDecelerate extends CubicBezier {

	public EaseEmphasizedDecelerate(float duration, float start, float end) {
		super(0.05F, 0.7F, 0.1F, 1F, duration, start, end);
	}
}
