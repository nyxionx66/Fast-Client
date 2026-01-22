package com.fastclient.gui.modmenu.pages;

import com.fastclient.Fast;
import com.fastclient.gui.api.Gui;
import com.fastclient.gui.api.page.Page;
import com.fastclient.gui.api.page.impl.RightLeftTransition;
import com.fastclient.management.color.api.ColorPalette;
import com.fastclient.skia.Skia;
import com.fastclient.skia.font.Fonts;
import com.fastclient.skia.font.Icon;

public class HomePage extends Page {

	public HomePage(Gui parent) {
		super(parent, "text.home", Icon.HOME, new RightLeftTransition(true));
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.draw(mouseX, mouseY);

		ColorPalette palette = Fast.getInstance().getColorManager().getPalette();

		Skia.drawImage("home_logo.png", x + (width / 2) - 40, y + (height / 2) - 100, 80, 80);
		Skia.drawCenteredText("Fast Client", x + (width / 2), y + (height / 2) + 10,
				palette.getOnSurface(), Fonts.getMedium(32));
		Skia.drawCenteredText("Performance & Utility Client", x + (width / 2), y + (height / 2) + 40,
				palette.getOnSurfaceVariant(), Fonts.getRegular(14));

		Skia.drawCenteredText("Right-click modules to access settings", x + (width / 2), y + height - 60,
				palette.getOnSurfaceVariant(), Fonts.getRegular(12));
		Skia.drawCenteredText("Use the edit button to customize your HUD", x + (width / 2), y + height - 40,
				palette.getOnSurfaceVariant(), Fonts.getRegular(12));
	}
}
