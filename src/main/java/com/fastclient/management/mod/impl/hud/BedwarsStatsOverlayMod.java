package com.fastclient.management.mod.impl.hud;

import java.io.File;

import com.fastclient.Fast;
import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.hypixel.api.HypixelUser;
import com.fastclient.management.mod.api.hud.HUDMod;
import com.fastclient.management.mod.settings.impl.NumberSetting;
import com.fastclient.skia.Skia;
import com.fastclient.skia.font.Fonts;
import com.fastclient.skia.font.Icon;
import com.fastclient.utils.SkinUtils;
import com.fastclient.utils.server.Server;
import com.fastclient.utils.server.ServerUtils;

import net.minecraft.client.network.PlayerListEntry;

public class BedwarsStatsOverlayMod extends HUDMod {

	private NumberSetting maxSetting = new NumberSetting("setting.max", "setting.max.description", Icon.MAXIMIZE, this,
			16, 1, 30, 1);

	private int index;

	public BedwarsStatsOverlayMod() {
		super("mod.bedwarsstatsoverlay.name", "mod.bedwarsstatsoverlay.description", Icon.SINGLE_BED);
	}

	public final EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {

		int prevIndex = 0;
		int offsetY = 33;

		this.drawBackground(getX(), getY(), 294, (index * 15) + 34);

		this.drawText("Bedwars Stats", getX() + 5.5F, getY() + 5.5F, Fonts.getRegular(10F));
		Skia.drawRect(getX(), getY() + 18, 292, 1, this.getDesign().getTextColor());

		Skia.drawFullCenteredText("Name", getX() + 45, getY() + 27, this.getDesign().getTextColor(), Fonts.getMedium(9F));
		Skia.drawFullCenteredText("Level", getX() + 120, getY() + 27, this.getDesign().getTextColor(), Fonts.getMedium(9F));
		Skia.drawFullCenteredText("WLR", getX() + 170, getY() + 27, this.getDesign().getTextColor(), Fonts.getMedium(9F));
		Skia.drawFullCenteredText("FKDR", getX() + 220, getY() + 27, this.getDesign().getTextColor(), Fonts.getMedium(9F));
		Skia.drawFullCenteredText("BBLR", getX() + 270, getY() + 27, this.getDesign().getTextColor(), Fonts.getMedium(9F));

		if (ServerUtils.isJoin(Server.HYPIXEL)) {

			for (PlayerListEntry player : client.getNetworkHandler().getPlayerList()) {

				if (player.getProfile() == null) {
					continue;
				}

				String name = player.getProfile().getName();
				String uuid = player.getProfile().getId().toString().replace("-", "");
				HypixelUser hypixelUser = Fast.getInstance().getHypixelManager().getByUuid(uuid);

				if (hypixelUser != null) {

					if (player.getSkinTextures() != null) {

						File file = SkinUtils.getSkin(player.getSkinTextures().texture());

						if (file.exists()) {
							Skia.drawPlayerHead(file, getX() + 5.5F, getY() + offsetY, 12, 12, 2.5F);
						}
					}

					Skia.drawHeightCenteredText(name, getX() + 21, getY() + offsetY + 6F,
							this.getDesign().getTextColor(), Fonts.getRegular(9));

					Skia.drawFullCenteredText(hypixelUser.getBedwarsLevel(), getX() + 120, getY() + offsetY + 6F,
							this.getDesign().getTextColor(), Fonts.getRegular(9));
					Skia.drawFullCenteredText(hypixelUser.getWinLoseRatio(), getX() + 170, getY() + offsetY + 6F,
							this.getDesign().getTextColor(), Fonts.getRegular(9));
					Skia.drawFullCenteredText(hypixelUser.getFinalKillDeathRatio(), getX() + 220, getY() + offsetY + 6F,
							this.getDesign().getTextColor(), Fonts.getRegular(9));
					Skia.drawFullCenteredText(hypixelUser.getBedsBrokeLostRatio(), getX() + 270, getY() + offsetY + 6F,
							this.getDesign().getTextColor(), Fonts.getRegular(9));

					if (prevIndex > maxSetting.getValue()) {
						prevIndex++;
						index = prevIndex;
						break;
					}

					prevIndex++;
					offsetY += 15;
				}

				index = prevIndex;
			}
		}

		position.setSize(294, (index * 15) + 34);
	};

	@Override
	public float getRadius() {
		return 6;
	}
}
