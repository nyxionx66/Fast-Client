package com.fastclient.gui.modmenu.pages.profile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFW;

import com.fastclient.Fast;
import com.fastclient.gui.api.Gui;
import com.fastclient.gui.api.page.Page;
import com.fastclient.gui.api.page.SimplePage;
import com.fastclient.gui.api.page.impl.RightTransition;
import com.fastclient.management.color.api.ColorPalette;
import com.fastclient.management.config.ConfigType;
import com.fastclient.management.profile.ProfileIcon;
import com.fastclient.skia.Skia;
import com.fastclient.skia.font.Fonts;
import com.fastclient.skia.font.Icon;
import com.fastclient.ui.component.Component;
import com.fastclient.ui.component.handler.impl.ButtonHandler;
import com.fastclient.ui.component.impl.Button;
import com.fastclient.ui.component.impl.text.TextField;
import com.fastclient.utils.Multithreading;
import com.fastclient.utils.file.dialog.FastFileDialog;
import com.fastclient.utils.language.I18n;
import com.fastclient.utils.mouse.MouseUtils;

import it.unimi.dsi.fastutil.objects.ObjectObjectImmutablePair;
import net.minecraft.client.MinecraftClient;

public class ProfileAddPage extends SimplePage {

	private List<Component> components = new ArrayList<>();
	private Class<? extends Page> prevPage;
	private TextField nameField, addressField;
	private Button addButton;
	private Object currentIcon;

	public ProfileAddPage(Gui parent, Class<? extends Page> prevPage) {
		super(parent, "text.profile", Icon.DESCRIPTION, new RightTransition(true));
		this.prevPage = prevPage;
		addButton = new Button(I18n.get("text.add"), 0, 0, Button.Style.TONAL);
	}

	@Override
	public void init() {

		MinecraftClient client = MinecraftClient.getInstance();

		components.clear();
		super.init();
		parent.setClosable(false);

		float offset = 26;

		nameField = new TextField(x + offset + 24, y + offset + 260, 280, "");
		addressField = new TextField(x + offset + 24 + 280 + 24, y + offset + 260, 280, "");
		addButton = new Button("text.add", x + (width - offset) - addButton.getWidth() - 16,
				y + (height - offset) - addButton.getHeight() - 16, Button.Style.TONAL);

		addButton.setHandler(new ButtonHandler() {
			@Override
			public void onAction() {
				if (!nameField.getText().isEmpty()) {
					Fast.getInstance().getProfileManager().save(nameField.getText(), client.getGameProfile().getName(),
							addressField.getText(), currentIcon, ConfigType.MOD);
					Fast.getInstance().getProfileManager().readProfiles();
					parent.setClosable(true);
					parent.setCurrentPage(prevPage);
				}
			}
		});

		components.add(nameField);
		components.add(addressField);
		components.add(addButton);
	}

	@Override
	public void draw(double mouseX, double mouseY) {
		super.draw(mouseX, mouseY);

		ColorPalette palette = Fast.getInstance().getColorManager().getPalette();
		float offset = 26;
		float itemX = x + offset;
		float itemY = y + offset;

		Skia.drawRoundedRect(itemX, itemY, width - offset * 2, height - offset * 2, 24, palette.getSurface());

		Skia.drawText(I18n.get("text.createprofile"), itemX + 24, itemY + 24, palette.getOnSurface(),
				Fonts.getMedium(28));
		Skia.drawText(I18n.get("text.icon"), itemX + 24, itemY + 70, palette.getOnSurfaceVariant(),
				Fonts.getMedium(26));

		for (ProfileIcon icon : ProfileIcon.values()) {

			if (currentIcon == null) {
				currentIcon = ProfileIcon.COMMAND;
			}

			float iconSize = 72;
			float iconX = x + offset + 24 + (icon.ordinal() * iconSize) + (icon.ordinal() * 12);
			float iconY = y + offset + 106;

			if ((currentIcon instanceof File && icon.equals(ProfileIcon.CUSTOM)) || currentIcon.equals(icon)) {

				float borderSize = 4;

				Skia.drawRoundedRect(iconX - borderSize, iconY - borderSize, iconSize + borderSize * 2,
						iconSize + borderSize * 2, 12 + (borderSize / 2), palette.getTertiaryContainer());
			}

			if (icon.equals(ProfileIcon.CUSTOM)) {
				if(currentIcon instanceof File) {
					Skia.drawRoundedImage(((File)currentIcon), iconX, iconY, iconSize, iconSize, 12);
				} else {
					Skia.drawRoundedRect(iconX, iconY, iconSize, iconSize, 12, palette.getSurfaceContainer());
					Skia.drawFullCenteredText(Icon.ADD, iconX + (iconSize / 2), iconY + (iconSize / 2),
							palette.getOnSurface(), Fonts.getIcon(26));
				}
			} else {
				Skia.drawRoundedImage(icon.getIconPath(), iconX, iconY, iconSize, iconSize, 12);
			}
		}
		
		Skia.drawText(I18n.get("text.name"), itemX + 24, itemY + 228, palette.getOnSurface(), Fonts.getRegular(26));
		Skia.drawText(I18n.get("text.serveraddress"), itemX + 24 + 280 + 24, itemY + 228, palette.getOnSurface(), Fonts.getRegular(26));

		for (Component component : components) {
			component.draw(mouseX, mouseY);
		}
	}

	@Override
	public void mousePressed(double mouseX, double mouseY, int button) {
		super.mousePressed(mouseX, mouseY, button);

		for (Component component : components) {
			component.mousePressed(mouseX, mouseY, button);
		}
	}

	@Override
	public void mouseReleased(double mouseX, double mouseY, int button) {
		super.mouseReleased(mouseX, mouseY, button);

		float offset = 26;
		
		for (ProfileIcon icon : ProfileIcon.values()) {
			
			float iconSize = 72;
			float iconX = x + offset + 20 + (icon.ordinal() * iconSize) + (icon.ordinal() * 12);
			float iconY = y + offset + 106;
			
			if(MouseUtils.isInside(mouseX, mouseY, iconX, iconY, iconSize, iconSize) && button == GLFW.GLFW_MOUSE_BUTTON_LEFT) {
				
				if(icon.equals(ProfileIcon.CUSTOM)) {
					
					Multithreading.runAsync(() -> {
						
						ObjectObjectImmutablePair<Boolean, File> chooseFile = FastFileDialog.chooseFile("Select icon", "png");
						
						if(chooseFile.left()) {
							currentIcon = chooseFile.right();
						}
					});
				} else {
					currentIcon = icon;
				}
			}
		}
		
		for (Component component : components) {
			component.mouseReleased(mouseX, mouseY, button);
		}
	}

	@Override
	public void charTyped(char chr, int modifiers) {
		super.charTyped(chr, modifiers);

		for (Component component : components) {
			component.charTyped(chr, modifiers);
		}
	}

	@Override
	public void keyPressed(int keyCode, int scanCode, int modifiers) {
		super.keyPressed(keyCode, scanCode, modifiers);

		for (Component component : components) {
			component.keyPressed(keyCode, scanCode, modifiers);
		}

		if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
			parent.setClosable(true);
			parent.setCurrentPage(prevPage);
		}
	}

	@Override
	public void onClosed() {
		if (!parent.isClosable()) {
			parent.setClosable(true);
			parent.getPage(prevPage).onClosed();
		}
	}
}
