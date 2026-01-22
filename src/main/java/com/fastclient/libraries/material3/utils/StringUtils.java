
package com.fastclient.libraries.material3.utils;

/** Utility methods for string representations of colors. */
final class StringUtils {
	private StringUtils() {
	}

	/**
	 * Hex string representing color, ex. #ff0000 for red.
	 *
	 * @param argb ARGB representation of a color.
	 */
	public static String hexFromArgb(int argb) {
		int red = ColorUtils.redFromArgb(argb);
		int blue = ColorUtils.blueFromArgb(argb);
		int green = ColorUtils.greenFromArgb(argb);
		return String.format("#%02x%02x%02x", red, green, blue);
	}
}
