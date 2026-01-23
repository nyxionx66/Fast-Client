package com.fastclient.skia.font;

import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.Typeface;

public class Fonts {

	private static final String ICON_FILL = "MaterialSymbolsRounded_Fill.ttf";
	private static final String ICON = "MaterialSymbolsRounded.ttf";

	public static void loadAll() {
		FontHelper.preloadFonts(ICON_FILL, ICON);
	}

	public static Font getRegular(float size) {
		return new Font(Typeface.makeDefault(), size);
	}

	public static Font getMedium(float size) {
		// Default typeface doesn't have a specific "Medium" variant easily accessible this way,
		// but using the default is the best fallback for removing custom files.
		return new Font(Typeface.makeDefault(), size);
	}

	public static Font getIconFill(float size) {
		return FontHelper.load(ICON_FILL, size);
	}

	public static Font getIcon(float size) {
		return FontHelper.load(ICON, size);
	}
}
