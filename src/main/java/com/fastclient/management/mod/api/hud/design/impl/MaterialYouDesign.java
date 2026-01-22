package com.fastclient.management.mod.api.hud.design.impl;

import java.awt.Color;

import com.fastclient.Fast;
import com.fastclient.management.color.api.ColorPalette;
import com.fastclient.management.mod.api.hud.design.HUDDesign;
import com.fastclient.skia.Skia;
import com.fastclient.utils.ColorUtils;

import io.github.humbleui.skija.Font;

public class MaterialYouDesign extends HUDDesign {

	// FastClient orange theme text color
	private static final Color TEXT_ORANGE = new Color(0xED, 0x79, 0x3B);
	private static final Color ON_TEXT_BLACK = new Color(0x00, 0x00, 0x00);

	public MaterialYouDesign() {
		super("design.materialyou");
	}

	@Override
	public void drawBackground(float x, float y, float width, float height, float radius) {

		ColorPalette palette = Fast.getInstance().getColorManager().getPalette();

		Skia.drawRoundedBlur(x, y, width, height, radius);
		Skia.drawShadow(x, y, width, height, radius);
		// Use FastClient orange gradient theme
		Skia.drawGradientRoundedRect(x, y, width, height, radius,
				ColorUtils.applyAlpha(palette.getPrimaryContainer(), 200),
				ColorUtils.applyAlpha(palette.getTertiaryContainer(), 200));
	}

	@Override
	public void drawText(String text, float x, float y, Font font) {
		Skia.drawText(text, x, y, getTextColor(), font);
	}

	@Override
	public Color getTextColor() {
		// Use orange text for FastClient theme
		return TEXT_ORANGE;
	}

	@Override
	public Color getOnTextColor() {
		return ON_TEXT_BLACK;
	}
}
