package com.fastclient.gui.modmenu.pages;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.fastclient.Fast;
import com.fastclient.animation.SimpleAnimation;
import com.fastclient.gui.api.Gui;
import com.fastclient.gui.api.page.Page;
import com.fastclient.gui.api.page.impl.LeftRightTransition;
import com.fastclient.gui.api.page.impl.RightLeftTransition;
import com.fastclient.gui.modmenu.pages.profile.ProfileAddPage;
import com.fastclient.management.color.api.ColorPalette;
import com.fastclient.management.profile.Profile;
import com.fastclient.management.profile.ProfileIcon;
import com.fastclient.skia.Skia;
import com.fastclient.skia.font.Fonts;
import com.fastclient.skia.font.Icon;
import com.fastclient.ui.component.handler.impl.ButtonHandler;
import com.fastclient.ui.component.impl.IconButton;
import com.fastclient.utils.SearchUtils;
import com.fastclient.utils.mouse.MouseUtils;

public class ProfilePage extends Page {

	private final List<Item> items = new ArrayList<>();
	private IconButton addButton;

	public ProfilePage(Gui parent) {
		super(parent, "text.profile", Icon.DESCRIPTION, new RightLeftTransition(true));
		addButton = new IconButton(Icon.ADD, 0, 0, IconButton.Size.LARGE, IconButton.Style.SECONDARY);
	}

	@Override
	public void init() {
		super.init();
		
		items.clear();
		
		for (Profile p : Fast.getInstance().getProfileManager().getProfiles()) {
			items.add(new Item(p));
		}

		for (Item i : items) {
			i.xAnimation.setFirstTick(true);
			i.yAnimation.setFirstTick(true);
		}

		addButton = new IconButton(Icon.ADD, x + width - addButton.getWidth() - 20,
				y + height - addButton.getHeight() - 20, IconButton.Size.LARGE, IconButton.Style.SECONDARY);
		addButton.setHandler(new ButtonHandler() {
			@Override
			public void onAction() {
				parent.setCurrentPage(new ProfileAddPage(parent, ProfilePage.this.getClass()));
				ProfilePage.this.setTransition(new LeftRightTransition(true));
			}
		});
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.draw(mouseX, mouseY);

		ColorPalette palette = Fast.getInstance().getColorManager().getPalette();

		addButton.setX(x + width - addButton.getWidth() - 20);
		addButton.setY(y + height - addButton.getHeight() - 20);
		addButton.draw(mouseX, mouseY);

		if (items.isEmpty()) {
			Skia.drawFullCenteredText(Icon.DESCRIPTION, x + (width / 2), y + (height / 2) - 30,
					palette.getOnSurfaceVariant(), Fonts.getIcon(72));
			Skia.drawCenteredText("No profiles saved", x + (width / 2), y + (height / 2) + 30,
					palette.getOnSurfaceVariant(), Fonts.getRegular(16));
			Skia.drawCenteredText("Click + to create a new profile", x + (width / 2), y + (height / 2) + 50,
					palette.getOnSurfaceVariant(), Fonts.getRegular(12));
			return;
		}

		double adjustedMouseY = mouseY - scrollHelper.getValue();

		int index = 0;
		float offsetX = 26;
		float offsetY = 0;
		float cardWidth = 260;
		float cardHeight = 96;
		float cardGap = 20;

		Skia.save();
		Skia.translate(0, scrollHelper.getValue());

		for (Item i : items) {

			Profile p = i.profile;
			SimpleAnimation xAnimation = i.xAnimation;
			SimpleAnimation yAnimation = i.yAnimation;
			Object icon = p.getIcon();

			float itemX = x + offsetX;
			float itemY = y + 96 + offsetY;

			if (!searchBar.getText().isEmpty()
					&& !SearchUtils.isSimilar(p.getName() + " " + p.getAuthor(), searchBar.getText())) {
				continue;
			}

			xAnimation.onTick(itemX, 14);
			yAnimation.onTick(itemY, 14);

			itemX = xAnimation.getValue();
			itemY = yAnimation.getValue();

			boolean isHovered = MouseUtils.isInside(mouseX, adjustedMouseY, itemX, itemY, cardWidth, cardHeight);

			Skia.drawRoundedRect(itemX, itemY, cardWidth, cardHeight, 16, 
					isHovered ? palette.getSurfaceContainerHigh() : palette.getSurface());
			
			float iconSize = 72;
			float iconPadding = 12;
			if (icon instanceof ProfileIcon) {
				Skia.drawRoundedImage(((ProfileIcon) icon).getIconPath(), itemX + iconPadding, itemY + iconPadding, iconSize, iconSize, 14);
			} else if (icon instanceof File) {
				Skia.drawRoundedImage(((File) icon), itemX + iconPadding, itemY + iconPadding, iconSize, iconSize, 14);
			} else {
				Skia.drawRoundedRect(itemX + iconPadding, itemY + iconPadding, iconSize, iconSize, 14, palette.getSurfaceContainerHigh());
				Skia.drawFullCenteredText(Icon.PERSON, itemX + iconPadding + iconSize/2, itemY + iconPadding + iconSize/2, 
						palette.getOnSurfaceVariant(), Fonts.getIconFill(32));
			}
			
			float textX = itemX + iconPadding + iconSize + 12;
			String profileName = Skia.getLimitText(p.getName() != null ? p.getName() : "Unnamed", Fonts.getMedium(18), cardWidth - iconSize - iconPadding * 2 - 20);
			Skia.drawText(profileName, textX, itemY + 28, palette.getOnSurface(), Fonts.getMedium(18));
			
			String author = p.getAuthor() != null ? p.getAuthor() : "";
			String author = p.getAuthor() != null ? p.getAuthor() : "";
			if (!author.isEmpty()) {
				String limitedAuthor = Skia.getLimitText(author, Fonts.getRegular(13), cardWidth - iconSize - iconPadding * 2 - 20);
				Skia.drawText(limitedAuthor, textX, itemY + 48, palette.getOnSurfaceVariant(), Fonts.getRegular(13));
			}
			
			if (isHovered) {
				Skia.drawText("Click to load", textX, itemY + cardHeight - 16, palette.getPrimary(), Fonts.getRegular(11));
			}

			index++;
			offsetX += cardGap + cardWidth;

			int cardsPerRow = Math.max(1, (int)((width - 52) / (cardWidth + cardGap)));
			if (index % cardsPerRow == 0) {
				offsetX = 26;
				offsetY += cardGap + cardHeight;
			}
		}
		
		int cardsPerRow = Math.max(1, (int)((width - 52) / (cardWidth + cardGap)));
		scrollHelper.setMaxScroll(cardHeight, cardGap, index, cardsPerRow, height - 96);

		Skia.restore();
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {
		super.mousePressed(mouseX, mouseY, button);
		addButton.mousePressed(mouseX, mouseY, button);
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		super.mouseReleased(mouseX, mouseY, button);
		addButton.mouseReleased(mouseX, mouseY, button);
		
		for (Item i : items) {
			
			float itemX = i.xAnimation.getValue();
			float itemY = i.yAnimation.getValue();
			
			if(MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 245, 88) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				Fast.getInstance().getProfileManager().load(i.profile);
			}
		}
	}

	@Override
	public void onClosed() {
		this.setTransition(new RightLeftTransition(true));
	}

	private class Item {

		private final Profile profile;
		private final SimpleAnimation xAnimation = new SimpleAnimation();
		private final SimpleAnimation yAnimation = new SimpleAnimation();

		private Item(Profile profile) {
			this.profile = profile;
		}
	}
}
