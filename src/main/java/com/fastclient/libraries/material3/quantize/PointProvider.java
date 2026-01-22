
package com.fastclient.libraries.material3.quantize;

/** An interface to allow use of different color spaces by quantizers. */
public interface PointProvider {
	/** The four components in the color space of an sRGB color. */
	public double[] fromInt(int argb);

	/** The ARGB (i.e. hex code) representation of this color. */
	public int toInt(double[] point);

	/**
	 * Squared distance between two colors. Distance is defined by scientific color
	 * spaces and referred to as delta E.
	 */
	public double distance(double[] a, double[] b);
}
