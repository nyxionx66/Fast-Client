package com.fastclient.utils.mouse;

public class MouseUtils {

	public static boolean isInside(double mouseX, double mouseY, double x, double y, double width, double height) {
		return (mouseX > x && mouseX < (x + width)) && (mouseY > y && mouseY < (y + height));
	}
}