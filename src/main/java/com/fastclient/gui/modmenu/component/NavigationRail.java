package com.fastclient.gui.modmenu.component;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.fastclient.Fast;
import com.fastclient.animation.Animation;
import com.fastclient.animation.Duration;
import com.fastclient.animation.SimpleAnimation;
import com.fastclient.animation.cubicbezier.impl.EaseStandard;
import com.fastclient.animation.other.DummyAnimation;
import com.fastclient.gui.api.Gui;
import com.fastclient.gui.api.page.SimplePage;
import com.fastclient.gui.edithud.GuiEditHUD;
import com.fastclient.management.color.api.ColorPalette;
import com.fastclient.management.mod.impl.settings.ModMenuSettings;
import com.fastclient.skia.Skia;
import com.fastclient.skia.font.Fonts;
import com.fastclient.skia.font.Icon;
import com.fastclient.ui.component.Component;
import com.fastclient.ui.component.handler.impl.ButtonHandler;
import com.fastclient.ui.component.impl.IconButton;
import com.fastclient.utils.ColorUtils;
import com.fastclient.utils.language.I18n;
import com.fastclient.utils.mouse.MouseUtils;

import io.github.humbleui.skija.FilterTileMode;
import io.github.humbleui.skija.Font;
import io.github.humbleui.skija.ImageFilter;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;

public class NavigationRail extends Component {

	private List<Navigation> navigations = new ArrayList<>();
	private Navigation currentNavigation;
	private IconButton editButton;
	private com.fastclient.utils.mouse.ScrollHelper scrollHelper = new com.fastclient.utils.mouse.ScrollHelper();

	private Gui parent;

	public NavigationRail(Gui parent, float x, float y, float width, float height) {
		super(x, y);
		this.parent = parent;
		this.width = width;
		this.height = height;

		for (SimplePage p : parent.getPages()) {

			Navigation n = new Navigation(p);

			if (p.getTitle().equals(parent.getCurrentPage().getTitle())) {
				currentNavigation = n;
				n.animation = new EaseStandard(Duration.MEDIUM_3, 0, 1);
			}

			navigations.add(n);
		}

		editButton = new IconButton(Icon.EDIT, x, y + 44, IconButton.Size.NORMAL, IconButton.Style.TERTIARY);
		editButton.setX(x + (width / 2) - (editButton.getWidth() / 2));
		editButton.setHandler(new ButtonHandler() {

			@Override
			public void onAction() {
				parent.close(new GuiEditHUD(ModMenuSettings.getInstance().getModMenu()).build());
			}
		});
	}

	@Override
	public void draw(double mouseX, double mouseY) {

		ColorPalette palette = Fast.getInstance().getColorManager().getPalette();

		editButton.setX(x + (width / 2) - (editButton.getWidth() / 2));
		editButton.setY(y + 44);

		Skia.drawRoundedRectVarying(x, y, width, height, 35, 0, 0, 35, palette.getSurface());

		Skia.drawRect(x + width - 1, y, 1, height, Color.WHITE);

		float btnX = editButton.getX();
		float btnY = editButton.getY();
		float btnW = editButton.getWidth();
		float btnH = editButton.getHeight();
		Skia.drawOutline(btnX - 2, btnY - 2, btnW + 4, btnH + 4, 18, 3, 
				ColorUtils.applyAlpha(palette.getPrimary(), 0.6F));

		editButton.draw(mouseX, mouseY);

		float navStartY = 120;
		float navItemHeight = 68;
		float availableNavHeight = height - navStartY - 10;
		float totalNavHeight = navigations.size() * navItemHeight;
		
		scrollHelper.onUpdate();
		scrollHelper.setMaxScroll(totalNavHeight, availableNavHeight);

		Skia.save();
		Skia.clip(x, y + navStartY, width, availableNavHeight, 0);
		Skia.translate(0, scrollHelper.getValue());

		float offsetY = navStartY;

		for (Navigation n : navigations) {

			SimplePage p = n.page;
			String title = p.getTitle();
			String icon = p.getIcon();
			Font font = currentNavigation.equals(n) ? Fonts.getIconFill(24) : Fonts.getIcon(24);
			Rect bounds = Skia.getTextBounds(icon, font);
			float iconWidth = bounds.getWidth();
			float iconHeight = bounds.getHeight();

			java.awt.Color c0 = java.awt.Color.WHITE;
			java.awt.Color c1 = java.awt.Color.WHITE;

			Animation animation = n.animation;
			float selWidth = 56;
			float selHeight = 32;
			float adjustedY = y + offsetY + scrollHelper.getValue();
			boolean isActive = currentNavigation.equals(n);

			Skia.drawText(icon, x + (width / 2) - (iconWidth / 2), y + (offsetY + (selHeight / 2)) - (iconHeight / 2),
					c0, font);
			Skia.drawCenteredText(I18n.get(title), x + (width / 2), y + offsetY + selHeight + 5, c1,
					Fonts.getMedium(12));

			if (isActive) {
				float barWidth = 32;
				float barHeight = 3;
				float barX = x + (width / 2) - (barWidth / 2);
				float barY = y + offsetY + selHeight + 18;
				Skia.drawRoundedRect(barX, barY, barWidth, barHeight, 1.5F, palette.getPrimary());
			}

			offsetY += navItemHeight;
		}
		
		Skia.restore();
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {

		float offsetY = 120;
		float selWidth = 56;
		float selHeight = 32;

		editButton.mousePressed(mouseX, mouseY, button);

		for (Navigation n : navigations) {
			float adjustedY = y + offsetY + scrollHelper.getValue();
			if (MouseUtils.isInside(mouseX, mouseY, x + (width / 2) - (selWidth / 2), adjustedY, selWidth, selHeight)
					&& button == GLFW.GLFW_MOUSE_BUTTON_LEFT && !currentNavigation.equals(n)) {
				n.pressed = true;
			}

			offsetY += 68;
		}
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {

		float offsetY = 120;
		float selWidth = 56;
		float selHeight = 32;

		editButton.mouseReleased(mouseX, mouseY, button);

		for (Navigation n : navigations) {
			float adjustedY = y + offsetY + scrollHelper.getValue();
			if (MouseUtils.isInside(mouseX, mouseY, x + (width / 2) - (selWidth / 2), adjustedY, selWidth, selHeight)
					&& button == GLFW.GLFW_MOUSE_BUTTON_LEFT && !currentNavigation.equals(n)) {
				currentNavigation.animation = new EaseStandard(Duration.MEDIUM_3, 1, 0);
				currentNavigation = n;
				parent.setCurrentPage(n.page);
				currentNavigation.animation = new EaseStandard(Duration.MEDIUM_3, 0, 1);
			}

			n.pressed = false;
			offsetY += 68;
		}
	}

	public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		if (MouseUtils.isInside(mouseX, mouseY, x, y, width, height)) {
			scrollHelper.onScroll(verticalAmount);
			return true;
		}
		return false;
	}

	private class Navigation {

		private SimpleAnimation focusAnimation = new SimpleAnimation();

		private Animation animation;
		private SimplePage page;
		private boolean pressed;

		private Navigation(SimplePage page) {
			this.page = page;
			this.animation = new DummyAnimation();
			this.pressed = false;
		}
	}
}
