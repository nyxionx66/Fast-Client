package com.fastclient.gui.modmenu.pages;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.fastclient.Fast;
import com.fastclient.animation.SimpleAnimation;
import com.fastclient.gui.api.Gui;
import com.fastclient.gui.api.page.Page;
import com.fastclient.gui.api.page.impl.LeftRightTransition;
import com.fastclient.gui.api.page.impl.RightLeftTransition;
import com.fastclient.management.color.api.ColorPalette;
import com.fastclient.management.mod.Mod;
import com.fastclient.skia.Skia;
import com.fastclient.skia.font.Fonts;
import com.fastclient.skia.font.Icon;
import com.fastclient.ui.component.api.PressAnimation;
import com.fastclient.utils.ColorUtils;
import com.fastclient.utils.SearchUtils;
import com.fastclient.utils.language.I18n;
import com.fastclient.utils.mouse.MouseUtils;

public class ModsPage extends Page {

	private List<Item> items = new ArrayList<>();

	public ModsPage(Gui parent) {
		super(parent, "text.mods", Icon.INVENTORY_2, new RightLeftTransition(true));
	}

	@Override
	public void init() {
		super.init();

		items.clear();
		
		for (Mod m : Fast.getInstance().getModManager().getMods()) {

			Item i = new Item(m);

			if (m.isEnabled()) {
				i.pressAnimation.setPressed();
			}

			items.add(i);
		}
		
		for (Item i : items) {
			i.xAnimation.setFirstTick(true);
			i.yAnimation.setFirstTick(true);
		}
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.draw(mouseX, mouseY);

		ColorPalette palette = Fast.getInstance().getColorManager().getPalette();

		float cardWidth = 200;
		float cardHeight = 151;
		float gap = 20;
		float padding = 26;
		float availableWidth = width - padding * 2;
		int columns = Math.max(1, (int) ((availableWidth + gap) / (cardWidth + gap)));
		float totalCardsWidth = columns * cardWidth + (columns - 1) * gap;
		float startX = x + padding + (availableWidth - totalCardsWidth) / 2;

		int index = 0;
		int col = 0;
		float offsetY = 0;

		mouseY = mouseY - scrollHelper.getValue();

		Skia.save();
		Skia.translate(0, scrollHelper.getValue());

		for (Item i : items) {

			Mod m = i.mod;
			SimpleAnimation focusAnimation = i.focusAnimation;
			SimpleAnimation xAnimation = i.xAnimation;
			SimpleAnimation yAnimation = i.yAnimation;

			if (m.isHidden()) {
				continue;
			}

			if (!searchBar.getText().isEmpty() && !SearchUtils.isSimilar(I18n.get(m.getName()), searchBar.getText())) {
				continue;
			}

			float itemX = startX + col * (cardWidth + gap);
			float itemY = y + 96 + offsetY;

			focusAnimation.onTick(
					MouseUtils.isInside(mouseX, mouseY, itemX, itemY, cardWidth, cardHeight) ? i.pressed ? 0.12F : 0.08F : 0,
					8);
			xAnimation.onTick(itemX, 14);
			yAnimation.onTick(itemY, 14);

			itemX = xAnimation.getValue();
			itemY = yAnimation.getValue();

			float iconHeight = cardHeight - 35;

			Skia.drawRoundedRectVarying(itemX, itemY, cardWidth, iconHeight, 26, 26, 0, 0, palette.getSurface());
			Skia.drawRoundedRectVarying(itemX, itemY + iconHeight, cardWidth, 35, 0, 0, 26, 26, palette.getSurfaceContainerLow());

			Skia.drawOutline(itemX, itemY, cardWidth, cardHeight, 26, 1, new java.awt.Color(255, 255, 255, 51));

			if (focusAnimation.getValue() > 0) {
				Skia.drawOutline(itemX, itemY, cardWidth, cardHeight, 26, 2,
						ColorUtils.applyAlpha(palette.getPrimary(), focusAnimation.getValue()));
			}

			Skia.drawRoundedRectVarying(itemX, itemY + iconHeight, cardWidth, 35, 0, 0, 26, 26,
					ColorUtils.applyAlpha(palette.getSurfaceContainerLowest(), focusAnimation.getValue()));

			Skia.save();
			Skia.clip(itemX, itemY + iconHeight, cardWidth, 35, 0, 0, 26, 26);
			i.pressAnimation.draw(itemX, itemY + iconHeight, cardWidth, 35, palette.getPrimaryContainer(), 1);
			Skia.restore();

			Skia.drawFullCenteredText(I18n.get(m.getName()), itemX + (cardWidth / 2), itemY + iconHeight + (35 / 2),
					java.awt.Color.WHITE, Fonts.getRegular(14));
			Skia.drawFullCenteredText(m.getIcon(), itemX + (cardWidth / 2), itemY + (iconHeight / 2), java.awt.Color.WHITE,
					Fonts.getIcon(56));

			index++;
			col++;

			if (col >= columns) {
				col = 0;
				offsetY += gap + cardHeight;
			}
		}

		int rows = (int) Math.ceil((double) index / columns);
		scrollHelper.setMaxScroll(cardHeight, gap, index, columns, height - 96);
		Skia.restore();
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {
		super.mousePressed(mouseX, mouseY, button);

		for (Item i : items) {

			float itemX = i.xAnimation.getValue();
			float itemY = i.yAnimation.getValue();

			if (i.mod.isHidden()) {
				continue;
			}

			if (!searchBar.getText().isEmpty()
					&& !SearchUtils.isSimilar(I18n.get(i.mod.getName()), searchBar.getText())) {
				continue;
			}

			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {

				if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 200, 151)) {
					i.pressed = true;
				}
			}
		}
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {

		mouseY = mouseY - scrollHelper.getValue();

		for (Item i : items) {

			Mod m = i.mod;
			float itemX = i.xAnimation.getValue();
			float itemY = i.yAnimation.getValue();

			if (i.mod.isHidden()) {
				continue;
			}

			if (!searchBar.getText().isEmpty()
					&& !SearchUtils.isSimilar(I18n.get(i.mod.getName()), searchBar.getText())) {
				continue;
			}

			if (button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {

				if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 200, 151)) {
					m.toggle();

					if (m.isEnabled()) {
						i.pressAnimation.onPressed(mouseX, mouseY, itemX, itemY + 116);
					} else {
						i.pressAnimation.onReleased(mouseX, mouseY, itemX, itemY + 116);
					}
				}
			} else if (button == GLFW.GLFW_MOUSE_BUTTON_RIGHT) {

				if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 244, 151)
						&& !Fast.getInstance().getModManager().getSettingsByMod(m).isEmpty()) {
					parent.setCurrentPage(new SettingsImplPage(parent, this.getClass(), m));
					this.setTransition(new LeftRightTransition(true));
				}
			}

			i.pressed = false;
		}
	}

	@Override
	public void onClosed() {
		this.setTransition(new RightLeftTransition(true));
	}

	private class Item {

		private Mod mod;
		private SimpleAnimation focusAnimation = new SimpleAnimation();
		private SimpleAnimation xAnimation = new SimpleAnimation();
		private SimpleAnimation yAnimation = new SimpleAnimation();
		private PressAnimation pressAnimation = new PressAnimation();
		private boolean pressed;

		private Item(Mod mod) {
			this.mod = mod;
			this.pressed = false;
		}
	}
}
