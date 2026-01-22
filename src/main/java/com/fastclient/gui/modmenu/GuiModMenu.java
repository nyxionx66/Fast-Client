package com.fastclient.gui.modmenu;

import java.util.ArrayList;
import java.util.List;

import com.fastclient.gui.api.Gui;
import com.fastclient.gui.api.page.SimplePage;
import com.fastclient.gui.modmenu.component.NavigationRail;
import com.fastclient.gui.modmenu.pages.HomePage;
import com.fastclient.gui.modmenu.pages.ModsPage;
import com.fastclient.gui.modmenu.pages.MusicPage;
import com.fastclient.gui.modmenu.pages.ProfilePage;
import com.fastclient.gui.modmenu.pages.SettingsPage;

public class GuiModMenu extends Gui {

	private NavigationRail navigationRail;

	public GuiModMenu() {
		super(false);
	}

	@Override
	public void init() {
		components.clear();
		navigationRail = new NavigationRail(this, getX(), getY(), 90, getHeight());
		components.add(navigationRail);
		super.init();
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		// Update NavigationRail position and size dynamically for window resize
		navigationRail.setX(getX());
		navigationRail.setY(getY());
		navigationRail.setHeight(getHeight());
		
		// Also update current page size for responsive layout
		if (currentPage != null) {
			setPageSize(currentPage);
		}
		
		super.draw(mouseX, mouseY);
	}

	@Override
	public void setPageSize(SimplePage p) {
		p.setX(getX() + navigationRail.getWidth());
		p.setY(getY());
		p.setWidth(getWidth() - navigationRail.getWidth());
		p.setHeight(getHeight());
	}

	@Override
	public List<SimplePage> createPages() {

		List<SimplePage> pages = new ArrayList<>();

		pages.add(new HomePage(this));
		pages.add(new ModsPage(this));
		pages.add(new MusicPage(this));
		pages.add(new ProfilePage(this));
		pages.add(new SettingsPage(this));

		return pages;
	}

	@Override
	public float getX() {
		float x = (client.getWindow().getWidth() / 2) - (getWidth() / 2);
		return Math.max(0, x);
	}

	@Override
	public float getY() {
		float y = (client.getWindow().getHeight() / 2) - (getHeight() / 2);
		return Math.max(0, y);
	}

	@Override
	public float getWidth() {
		// Responsive: 85% of window width, clamped between 600 and 1200
		float windowWidth = client.getWindow().getWidth();
		float targetWidth = windowWidth * 0.85F;
		return Math.max(600, Math.min(1200, targetWidth));
	}

	@Override
	public float getHeight() {
		// Responsive: 80% of window height, clamped between 400 and 700
		float windowHeight = client.getWindow().getHeight();
		float targetHeight = windowHeight * 0.80F;
		return Math.max(400, Math.min(700, targetHeight));
	}
}
