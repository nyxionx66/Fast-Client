package com.fastclient.management.mod.impl.render;

import com.fastclient.Fast;
import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.Mod;
import com.fastclient.management.mod.ModCategory;
import com.fastclient.management.music.Music;
import com.fastclient.management.music.MusicManager;
import com.fastclient.management.music.MusicPlayer;
import com.fastclient.skia.Skia;
import com.fastclient.skia.font.Icon;
import com.fastclient.utils.ColorUtils;

public class MusicWaveformMod extends Mod {

	public MusicWaveformMod() {
		super("mod.musicwaveform.name", "mod.musicwaveform.description", Icon.AIRWAVE, ModCategory.RENDER);
	}

	public EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {

		MusicManager musicManager = Fast.getInstance().getMusicManager();
		Music m = musicManager.getCurrentMusic();

		int offsetX = 0;

		if (musicManager.isPlaying()) {

			for (int i = 0; i < MusicPlayer.SPECTRUM_BANDS; i++) {

				MusicPlayer.ANIMATIONS[i].onTick(MusicPlayer.VISUALIZER[i], 10);
				Skia.drawRect(offsetX, client.getWindow().getScaledHeight() + MusicPlayer.ANIMATIONS[i].getValue(), 10,
						client.getWindow().getScaledHeight(), ColorUtils.applyAlpha(m.getColor(), 80));

				offsetX += 10;
			}
		}
	};
}
