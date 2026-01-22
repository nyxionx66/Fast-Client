package com.fastclient.gui.modmenu.pages;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.fastclient.Fast;
import com.fastclient.animation.SimpleAnimation;
import com.fastclient.gui.api.Gui;
import com.fastclient.gui.api.page.Page;
import com.fastclient.gui.api.page.impl.RightLeftTransition;
import com.fastclient.gui.modmenu.component.MusicControlBar;
import com.fastclient.management.color.api.ColorPalette;
import com.fastclient.management.music.Music;
import com.fastclient.management.music.MusicManager;
import com.fastclient.skia.Skia;
import com.fastclient.skia.font.Fonts;
import com.fastclient.skia.font.Icon;
import com.fastclient.utils.ColorUtils;
import com.fastclient.utils.SearchUtils;
import com.fastclient.utils.mouse.MouseUtils;

import io.github.humbleui.skija.ClipMode;
import io.github.humbleui.skija.FilterTileMode;
import io.github.humbleui.skija.Image;
import io.github.humbleui.skija.ImageFilter;
import io.github.humbleui.skija.Paint;
import io.github.humbleui.skija.Path;
import io.github.humbleui.types.RRect;
import io.github.humbleui.types.Rect;

public class MusicPage extends Page {

	private SimpleAnimation controlBarAnimation = new SimpleAnimation();
	private MusicControlBar controlBar;
	private com.fastclient.ui.component.impl.IconButton refreshButton;
	private List<Item> items = new ArrayList<>();

	public MusicPage(Gui parent) {
		super(parent, "text.music", Icon.MUSIC_NOTE, new RightLeftTransition(true));
	}

	@Override
	public void init() {
		super.init();
		
		items.clear();
		
		for (Music m : Fast.getInstance().getMusicManager().getMusics()) {
			items.add(new Item(m));
		}

		controlBar = new MusicControlBar(x + 22, y + height - 60 - 18, width - 44);

		refreshButton = new com.fastclient.ui.component.impl.IconButton(Icon.AUTORENEW, 0, 0, 
				com.fastclient.ui.component.impl.IconButton.Size.SMALL, com.fastclient.ui.component.impl.IconButton.Style.SURFACE);
		
		refreshButton.setHandler(new com.fastclient.ui.component.handler.impl.ButtonHandler() {
			@Override
			public void onAction() {
				try {
					Fast.getInstance().getMusicManager().load();
					init();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		for (Item i : items) {
			i.xAnimation.setFirstTick(true);
			i.yAnimation.setFirstTick(true);
		}
	}

	@Override
	public void draw(double mouseX, double mouseY) {

		super.draw(mouseX, mouseY);

		MusicManager musicManager = Fast.getInstance().getMusicManager();
		ColorPalette palette = Fast.getInstance().getColorManager().getPalette();

		if (items.isEmpty()) {
			Skia.drawFullCenteredText(Icon.MUSIC_OFF, x + (width / 2), y + (height / 2) - 30,
					palette.getOnSurfaceVariant(), Fonts.getIcon(72));
			Skia.drawCenteredText("No music files found", x + (width / 2), y + (height / 2) + 30,
					palette.getOnSurfaceVariant(), Fonts.getRegular(16));
			Skia.drawCenteredText("Add .mp3 files to the music folder", x + (width / 2), y + (height / 2) + 50,
					palette.getOnSurfaceVariant(), Fonts.getRegular(12));
			return;
		}

		int index = 0;
		float offsetX = 28;
		float offsetY = 96;

		controlBarAnimation.onTick(MouseUtils.isInside(mouseX, mouseY, controlBar.getX(), controlBar.getY(),
				controlBar.getWidth(), controlBar.getHeight()) ? 1 : 0, 12);

		mouseY = mouseY - scrollHelper.getValue();

		Skia.save();
		Skia.translate(0, scrollHelper.getValue());

		for (Item i : items) {

			Music m = i.music;
			SimpleAnimation xAnimation = i.xAnimation;
			SimpleAnimation yAnimation = i.yAnimation;
			SimpleAnimation focusAnimation = i.focusAnimation;

			if (!searchBar.getText().isEmpty()
					&& !SearchUtils.isSimilar(m.getTitle() + " " + m.getArtist(), searchBar.getText())) {
				continue;
			}

			float itemX = x + offsetX;
			float itemY = y + offsetY;

			xAnimation.onTick(itemX, 14);
			yAnimation.onTick(itemY, 14);
			focusAnimation.onTick(MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 174, 174) ? 1 : 0, 10);

			itemX = xAnimation.getValue();
			itemY = yAnimation.getValue();

			if (m.getAlbum() != null) {
				drawRoundedImage(m.getAlbum(), itemX, itemY, 174, 174, 26,
						(Math.abs(focusAnimation.getValue()) + 0.001F) * 6);
			} else {
				Skia.drawRoundedRect(itemX, itemY, 174, 174, 26, palette.getSurfaceContainerHigh());
			}

			String limitedTitle = Skia.getLimitText(m.getTitle(), Fonts.getRegular(15), 174);
			String limitedArtist = Skia.getLimitText(m.getArtist(), Fonts.getRegular(12), 174);

			Skia.drawText(limitedTitle, itemX, itemY + 174 + 6, palette.getOnSurface(), Fonts.getRegular(15));
			Skia.drawText(limitedArtist, itemX, itemY + 174 + 6 + 15, palette.getOnSurfaceVariant(),
					Fonts.getRegular(12));

			String icon = musicManager.getCurrentMusic() != null && musicManager.getCurrentMusic().equals(m)
					&& musicManager.isPlaying() ? Icon.PAUSE : Icon.PLAY_ARROW;

			Skia.save();
			Skia.translate(0, 15 - (focusAnimation.getValue() * 15));
			Skia.drawFullCenteredText(icon, itemX + (174 / 2), itemY + (174 / 2),
					ColorUtils.applyAlpha(Color.WHITE, focusAnimation.getValue()), Fonts.getIconFill(64));
			Skia.restore();

			offsetX += 174 + 32;
			index++;

			if (index % 4 == 0) {
				offsetX = 28;
				offsetY += 206 + 23;
			}
		}

		scrollHelper.setMaxScroll(206, 23, index, 4, height - 96);
		Skia.restore();

		mouseY = mouseY + scrollHelper.getValue();

		Skia.save();
		Skia.translate(0, 100 - (controlBarAnimation.getValue() * 100));
		controlBar.setX(x + 22);
		controlBar.setY(y + height - 60 - 18);
		controlBar.setWidth(width - 44);
		controlBar.draw(mouseX, mouseY);
		Skia.restore();

		refreshButton.setX(searchBar.getX() - refreshButton.getWidth() - 8);
		refreshButton.setY(searchBar.getY());
		refreshButton.draw(mouseX, mouseY);
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {
		super.mousePressed(mouseX, mouseY, button);

		controlBar.mousePressed(mouseX, mouseY, button);

		if (MouseUtils.isInside(mouseX, mouseY, controlBar.getX(), controlBar.getY(), controlBar.getWidth(),
				controlBar.getHeight())) {
			return;
		}

		refreshButton.mousePressed(mouseX, mouseY, button);
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		super.mouseReleased(mouseX, mouseY, button);

		MusicManager musicManager = Fast.getInstance().getMusicManager();

		controlBar.mouseReleased(mouseX, mouseY, button);

		if (MouseUtils.isInside(mouseX, mouseY, controlBar.getX(), controlBar.getY(), controlBar.getWidth(),
				controlBar.getHeight())) {
			return;
		}

		refreshButton.mouseReleased(mouseX, mouseY, button);

		mouseY = mouseY - scrollHelper.getValue();

		for (Item i : items) {

			Music m = i.music;
			float itemX = i.xAnimation.getValue();
			float itemY = i.yAnimation.getValue();

			if (!searchBar.getText().isEmpty()
					&& !SearchUtils.isSimilar(m.getTitle() + " " + m.getArtist(), searchBar.getText())) {
				continue;
			}

			if (MouseUtils.isInside(mouseX, mouseY, itemX, itemY, 174, 174) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {

				if (musicManager.getCurrentMusic() != m) {
					musicManager.stop();
					musicManager.setCurrentMusic(m);
					musicManager.play();
				} else {
					musicManager.switchPlayBack();
				}
			}
		}
	}
	
	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		super.keyPressed(keyCode, scanCode, modifiers);
		controlBar.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public void charTyped(char chr, int modifiers) {
		super.charTyped(chr, modifiers);
		controlBar.charTyped(chr, modifiers);
	}

	private void drawRoundedImage(File file, float x, float y, float width, float height, float cornerRadius,
			float blurRadius) {

		Path path = new Path();
		path.addRRect(RRect.makeXYWH(x, y, width, height, cornerRadius));

		Paint blurPaint = new Paint();
		blurPaint.setImageFilter(ImageFilter.makeBlur(blurRadius, blurRadius, FilterTileMode.CLAMP));

		Skia.save();

		Skia.getCanvas().clipPath(path, ClipMode.INTERSECT, true);

		Skia.drawImage(file, x, y, width, height);

		if (Skia.getImageHelper().load(file)) {
			Image image = Skia.getImageHelper().get(file.getName());
			if (image != null) {
				Skia.getCanvas().drawImageRect(image, Rect.makeWH(image.getWidth(), image.getHeight()),
						Rect.makeXYWH(x, y, width, height), blurPaint, true);
			}
		}

		Skia.restore();
	}

	private class Item {

		private Music music;
		private SimpleAnimation xAnimation = new SimpleAnimation();
		private SimpleAnimation yAnimation = new SimpleAnimation();
		private SimpleAnimation focusAnimation = new SimpleAnimation();

		private Item(Music music) {
			this.music = music;
		}
	}
}
