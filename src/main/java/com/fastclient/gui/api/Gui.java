package com.fastclient.gui.api;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.fastclient.Fast;
import com.fastclient.animation.Animation;
import com.fastclient.animation.Duration;
import com.fastclient.animation.cubicbezier.impl.EaseEmphasizedDecelerate;
import com.fastclient.gui.api.page.GuiTransition;
import com.fastclient.gui.api.page.SimplePage;
import com.fastclient.management.color.api.ColorPalette;
import com.fastclient.management.config.ConfigType;
import com.fastclient.management.mod.impl.settings.ModMenuSettings;
import com.fastclient.shader.impl.KawaseBlur;
import com.fastclient.skia.Skia;
import com.fastclient.ui.component.Component;
import com.fastclient.utils.Multithreading;

import io.github.humbleui.skija.SurfaceOrigin;
import net.minecraft.client.gui.screen.Screen;

public abstract class Gui extends SimpleGui {

	protected List<Component> components = new ArrayList<>();
	protected List<SimplePage> pages;

	protected SimplePage currentPage;
	protected SimplePage lastPage;

	private Animation inOutAnimation;
	private boolean closable;
	private Screen nextScreen;

	public Gui(boolean mcScale) {
		super(mcScale);

		this.pages = createPages();

		if (!pages.isEmpty()) {
			this.currentPage = pages.getFirst();
		}
	}

	@Override
	public void init() {
		setPageSize(currentPage);
		inOutAnimation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 0, 1);
		closable = true;
		currentPage.init();
	}

	public void setPageSize(SimplePage p) {
		p.setX(getX());
		p.setY(getY());
		p.setWidth(getWidth());
		p.setHeight(getHeight());
	}

	@Override
	public void draw(double mouseX, double mouseY) {

		ColorPalette palette = Fast.getInstance().getColorManager().getPalette();

		if (ModMenuSettings.getInstance().getBlurSetting().isEnabled()) {
			Skia.drawImage(KawaseBlur.GUI_BLUR.getTexture(), 0, 0, client.getWindow().getWidth(),
					client.getWindow().getHeight(), inOutAnimation.getValue(), SurfaceOrigin.BOTTOM_LEFT);
		}

		Skia.save();
		Skia.setAlpha((int) (inOutAnimation.getValue() * 255));
		Skia.scale(getX(), getY(), getWidth(), getHeight(), 2 - inOutAnimation.getValue());

		Skia.save();
		Skia.clip(getX(), getY(), getWidth(), getHeight(), 35);
		Skia.drawRoundedRect(getX(), getY(), getWidth(), getHeight(), 35, palette.getSurfaceContainer());

		if (currentPage != null && lastPage == null) {
			currentPage.draw(mouseX, mouseY);
		}

		if (lastPage != null) {

			GuiTransition transition = lastPage.getTransition();

			if (currentPage.getTransition().isConsecutive()) {

				Skia.save();

				if (transition != null) {
					float[] result = transition.onTransition(lastPage.getAnimation());
					Skia.translate(result[0] * getWidth(), result[1] * getHeight());
				}

				lastPage.draw(mouseX, mouseY);
				Skia.restore();
			}

			Skia.save();
			transition = currentPage.getTransition();

			if (transition != null) {
				float[] result = transition.onTransition(currentPage.getAnimation());
				Skia.translate(result[0] * getWidth(), result[1] * getHeight());
			}

			currentPage.draw(mouseX, mouseY);
			Skia.restore();

			if (lastPage.getAnimation().isFinished()) {
				lastPage = null;
			}
		}

		Skia.restore();

		for (Component c : components) {
			c.draw(mouseX, mouseY);
		}

		Skia.restore();

		if (inOutAnimation.getEnd() == 0 && inOutAnimation.isFinished()) {
			client.setScreen(nextScreen);
			nextScreen = null;
		}
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {

		if (currentPage != null) {
			currentPage.mousePressed(mouseX, mouseY, button);
		}

		for (Component c : components) {
			c.mousePressed(mouseX, mouseY, button);
		}
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {

		if (currentPage != null) {
			currentPage.mouseReleased(mouseX, mouseY, button);
		}

		for (Component c : components) {
			c.mouseReleased(mouseX, mouseY, button);
		}
	}

	@Override
	public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
		// Check if NavigationRail should handle the scroll first (exclusive handling)
		boolean scrollHandled = false;
		for (Component c : components) {
			if (c instanceof com.fastclient.gui.modmenu.component.NavigationRail) {
				scrollHandled = ((com.fastclient.gui.modmenu.component.NavigationRail) c).mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
				if (scrollHandled) {
					break;
				}
			}
		}
		
		// Only scroll the page if the sidebar didn't handle the scroll
		if (!scrollHandled && currentPage != null) {
			currentPage.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
		}
	}

	@Override
	public void charTyped(char chr, int modifiers) {

		if (currentPage != null) {
			currentPage.charTyped(chr, modifiers);
		}

		for (Component c : components) {
			c.charTyped(chr, modifiers);
		}
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {

		if (keyCode == GLFW.GLFW_KEY_ESCAPE && inOutAnimation.getEnd() == 1 && closable) {
			close();
		}

		if (currentPage != null) {
			currentPage.keyPressed(keyCode, scanCode, modifiers);
		}

		for (Component c : components) {
			c.keyPressed(keyCode, scanCode, modifiers);
		}
	}

	public void close(Screen nextScreen) {
		if (inOutAnimation.getEnd() == 1) {
			this.nextScreen = nextScreen;
			inOutAnimation = new EaseEmphasizedDecelerate(Duration.EXTRA_LONG_1, 1, 0);
			Multithreading.runAsync(() -> {
				Fast.getInstance().getConfigManager().save(ConfigType.MOD);
			});
		}
	}

	public void close() {
		close(null);
	}

	public SimplePage getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(SimplePage page) {

		if (currentPage != null) {
			lastPage = currentPage;
			currentPage.onClosed();
		}

		this.currentPage = page;
		currentPage.setAnimation(new EaseEmphasizedDecelerate(Duration.MEDIUM_1, 0, 1));
		lastPage.setAnimation(new EaseEmphasizedDecelerate(Duration.MEDIUM_1, 1, 0));

		if (currentPage != null) {
			setPageSize(currentPage);
			currentPage.init();
		}
	}

	public void setCurrentPage(Class<? extends SimplePage> clazz) {

		SimplePage page = getPage(clazz);

		if (page != null) {
			setCurrentPage(page);
		}
	}

	public SimplePage getPage(Class<? extends SimplePage> clazz) {

		SimplePage page = null;

		for (SimplePage p : pages) {
			if (p.getClass().equals(clazz)) {
				page = p;
				break;
			}
		}

		return page;
	}

	public List<SimplePage> getPages() {
		return pages;
	}

	public boolean isClosable() {
		return closable;
	}

	public void setClosable(boolean closable) {
		this.closable = closable;
	}

	public abstract List<SimplePage> createPages();

	public abstract float getX();

	public abstract float getY();

	public abstract float getWidth();

	public abstract float getHeight();
}
