package com.fastclient.libraries.material3;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import com.fastclient.libraries.material3.dynamiccolor.DynamicScheme;
import com.fastclient.libraries.material3.hct.Hct;
import com.fastclient.libraries.material3.quantize.QuantizerCelebi;
import com.fastclient.libraries.material3.scheme.SchemeVibrant;
import com.fastclient.libraries.material3.score.Score;
import com.fastclient.utils.ImageUtils;

/**
 * Material3 color system utility class.
 * 
 * FastClient Launcher Color Scheme:
 * - Primary Orange: #ff843f (RGB: 255, 132, 63)
 * - Yellow Accent: #ffc11d (RGB: 255, 193, 29)
 * - Text Orange: #ed793b (RGB: 237, 121, 59)
 * - Background Black: #000000
 * - Hover Button: #221511 (RGB: 34, 21, 17)
 */
public class Material3 {

	// FastClient theme base color - Orange #ff843f
	// HCT values: Hue ~25°, Chroma ~85, Tone ~70
	private static final double FASTCLIENT_HUE = 25.0;
	private static final double FASTCLIENT_CHROMA = 85.0;
	private static final double FASTCLIENT_TONE = 70.0;
	
	// ARGB value of #ff843f
	public static final int FASTCLIENT_ORANGE = 0xFFFF843F;
	// ARGB value of #ffc11d
	public static final int FASTCLIENT_YELLOW = 0xFFFFC11D;
	// ARGB value of #ed793b
	public static final int FASTCLIENT_TEXT_ORANGE = 0xFFED793B;
	// ARGB value of #221511
	public static final int FASTCLIENT_HOVER = 0xFF221511;

	/**
	 * Returns the FastClient default HCT color (orange theme).
	 */
	public static Hct getFastClientHct() {
		return Hct.from(FASTCLIENT_HUE, FASTCLIENT_CHROMA, FASTCLIENT_TONE);
	}

	public static DynamicScheme getDynamicScheme(Hct hct, boolean dark, double contrast) {
		// Always use FastClient orange theme
		return new SchemeVibrant(getFastClientHct(), dark, contrast);
	}

	public static Hct getImageHct(BufferedImage image) {
		// Return FastClient theme color instead of extracting from image
		if (image == null) {
			return getFastClientHct();
		}

		// Original image extraction (kept for reference but returns FastClient theme)
		// Map<Integer, Integer> quantizerResult = QuantizerCelebi.quantize(ImageUtils.imageToPixels(image), 128);
		// List<Integer> colors = Score.score(quantizerResult);
		// return Hct.fromInt(colors.get(0));
		
		return getFastClientHct();
	}

	public static Hct getImageHct(File imageFile) {
		// Return FastClient theme color
		return getFastClientHct();
	}
	
	/**
	 * Get the original image-based HCT if needed for other purposes.
	 */
	public static Hct getOriginalImageHct(BufferedImage image) {
		if (image == null) {
			return Hct.from(0, 0, 0);
		}

		Map<Integer, Integer> quantizerResult = QuantizerCelebi.quantize(ImageUtils.imageToPixels(image), 128);
		List<Integer> colors = Score.score(quantizerResult);

		return Hct.fromInt(colors.get(0));
	}
	
	/**
	 * Get the original image-based HCT from file if needed.
	 */
	public static Hct getOriginalImageHct(File imageFile) {
		try {
			return getOriginalImageHct(ImageIO.read(imageFile));
		} catch (IOException e) {
		}
		return Hct.from(0, 0, 0);
	}
}
