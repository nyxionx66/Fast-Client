
package com.fastclient.libraries.material3.quantize;

import java.util.Map;

/** Represents result of a quantizer run */
public final class QuantizerResult {
	public final Map<Integer, Integer> colorToCount;

	QuantizerResult(Map<Integer, Integer> colorToCount) {
		this.colorToCount = colorToCount;
	}
}
