
package com.fastclient.libraries.material3.quantize;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Creates a dictionary with keys of colors, and values of count of the color
 */
public final class QuantizerMap implements Quantizer {
	Map<Integer, Integer> colorToCount;

	@Override
	public QuantizerResult quantize(int[] pixels, int colorCount) {
		final Map<Integer, Integer> pixelByCount = new LinkedHashMap<>();
		for (int pixel : pixels) {
			final Integer currentPixelCount = pixelByCount.get(pixel);
			final int newPixelCount = currentPixelCount == null ? 1 : currentPixelCount + 1;
			pixelByCount.put(pixel, newPixelCount);
		}
		colorToCount = pixelByCount;
		return new QuantizerResult(pixelByCount);
	}

	public Map<Integer, Integer> getColorToCount() {
		return colorToCount;
	}
}
