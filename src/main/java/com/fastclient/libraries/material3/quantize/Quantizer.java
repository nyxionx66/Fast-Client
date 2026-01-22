
package com.fastclient.libraries.material3.quantize;

interface Quantizer {
	public QuantizerResult quantize(int[] pixels, int maxColors);
}
