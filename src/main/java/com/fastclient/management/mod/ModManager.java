package com.fastclient.management.mod;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fastclient.management.mod.api.hud.HUDMod;
import com.fastclient.management.mod.api.hud.design.HUDDesign;
import com.fastclient.management.mod.api.hud.design.impl.ClassicDesign;
import com.fastclient.management.mod.api.hud.design.impl.ClearDesign;
import com.fastclient.management.mod.api.hud.design.impl.MaterialYouDesign;
import com.fastclient.management.mod.api.hud.design.impl.SimpleDesign;
import com.fastclient.management.mod.impl.hud.BedwarsStatsOverlayMod;
import com.fastclient.management.mod.impl.hud.BossBarMod;
import com.fastclient.management.mod.impl.hud.ClockMod;
import com.fastclient.management.mod.impl.hud.ComboCounterMod;
import com.fastclient.management.mod.impl.hud.CoordsMod;
import com.fastclient.management.mod.impl.hud.DayCounterMod;
import com.fastclient.management.mod.impl.hud.FPSDisplayMod;
import com.fastclient.management.mod.impl.hud.GameModeDisplayMod;
import com.fastclient.management.mod.impl.hud.HealthDisplayMod;
import com.fastclient.management.mod.impl.hud.JumpResetIndicatorMod;
import com.fastclient.management.mod.impl.hud.KeystrokesMod;
import com.fastclient.management.mod.impl.hud.MemoryUsageMod;
import com.fastclient.management.mod.impl.hud.MouseStrokesMod;
import com.fastclient.management.mod.impl.hud.MusicInfoMod;
import com.fastclient.management.mod.impl.hud.NameDisplayMod;
import com.fastclient.management.mod.impl.hud.PingDisplayMod;
import com.fastclient.management.mod.impl.hud.PitchDisplayMod;
import com.fastclient.management.mod.impl.hud.PlayTimeDisplayMod;
import com.fastclient.management.mod.impl.hud.PlayerCounterMod;
import com.fastclient.management.mod.impl.hud.ProtocolVersionMod;
import com.fastclient.management.mod.impl.hud.ReachDisplayMod;
import com.fastclient.management.mod.impl.hud.ServerIPDisplayMod;
import com.fastclient.management.mod.impl.hud.SpeedometerMod;
import com.fastclient.management.mod.impl.hud.StopwatchMod;
import com.fastclient.management.mod.impl.hud.WeatherDisplayMod;
import com.fastclient.management.mod.impl.hud.WebBrowserMod;
import com.fastclient.management.mod.impl.hud.YawDisplayMod;
import com.fastclient.management.mod.impl.misc.DiscordRPCMod;
import com.fastclient.management.mod.impl.misc.HypixelMod;
import com.fastclient.management.mod.impl.misc.TimeChangerMod;
import com.fastclient.management.mod.impl.misc.WeatherChangerMod;
import com.fastclient.management.mod.impl.player.AutoGGMod;
import com.fastclient.management.mod.impl.player.ForceMainHandMod;
import com.fastclient.management.mod.impl.player.FreelookMod;
import com.fastclient.management.mod.impl.player.HitDelayFixMod;
import com.fastclient.management.mod.impl.player.NoJumpDelayMod;
import com.fastclient.management.mod.impl.player.OldAnimationsMod;
import com.fastclient.management.mod.impl.player.SnapTapMod;
import com.fastclient.management.mod.impl.player.TaplookMod;
import com.fastclient.management.mod.impl.player.ZoomMod;
import com.fastclient.management.mod.impl.render.BloodParticleMod;
import com.fastclient.management.mod.impl.render.CustomHandMod;
import com.fastclient.management.mod.impl.render.FullbrightMod;
import com.fastclient.management.mod.impl.render.MusicWaveformMod;
import com.fastclient.management.mod.impl.render.OverlayEditorMod;
import com.fastclient.management.mod.impl.render.ParticlesMod;
import com.fastclient.management.mod.impl.render.ProjectileTrailMod;
import com.fastclient.management.mod.impl.settings.HUDModSettings;
import com.fastclient.management.mod.impl.settings.ModMenuSettings;
import com.fastclient.management.mod.impl.settings.SystemSettings;
import com.fastclient.management.mod.settings.Setting;
import com.fastclient.management.mod.settings.impl.KeybindSetting;

public class ModManager {

	private List<Mod> mods = new ArrayList<>();
	private List<Setting> settings = new ArrayList<>();
	private List<HUDDesign> designs = new ArrayList<>();

	private HUDDesign currentDesign;

	public void init() {
		initMods();
		initDesigns();
	}

	private void initMods() {

		// HUD
		mods.add(new BedwarsStatsOverlayMod());
		mods.add(new BossBarMod());
		mods.add(new ClockMod());
		mods.add(new ComboCounterMod());
		mods.add(new CoordsMod());
		mods.add(new DayCounterMod());
		mods.add(new FPSDisplayMod());
		mods.add(new GameModeDisplayMod());
		mods.add(new HealthDisplayMod());
		mods.add(new JumpResetIndicatorMod());
		mods.add(new KeystrokesMod());
		mods.add(new MemoryUsageMod());
		mods.add(new MouseStrokesMod());
		mods.add(new MusicInfoMod());
		mods.add(new NameDisplayMod());
		mods.add(new PingDisplayMod());
		mods.add(new PitchDisplayMod());
		mods.add(new PlayerCounterMod());
		mods.add(new PlayTimeDisplayMod());
		mods.add(new ProtocolVersionMod());
		mods.add(new ReachDisplayMod());
		mods.add(new ServerIPDisplayMod());
		mods.add(new SpeedometerMod());
		mods.add(new StopwatchMod());
		mods.add(new WebBrowserMod());
		mods.add(new WeatherDisplayMod());
		mods.add(new YawDisplayMod());

		// Player
		mods.add(new AutoGGMod());
		mods.add(new ForceMainHandMod());
		mods.add(new FreelookMod());
		mods.add(new HitDelayFixMod());
		mods.add(new NoJumpDelayMod());
		mods.add(new OldAnimationsMod());
		mods.add(new SnapTapMod());
		mods.add(new TaplookMod());
		mods.add(new ZoomMod());

		// Render
		mods.add(new BloodParticleMod());
		mods.add(new CustomHandMod());
		mods.add(new FullbrightMod());
		mods.add(new MusicWaveformMod());
		mods.add(new OverlayEditorMod());
		mods.add(new ParticlesMod());
		mods.add(new ProjectileTrailMod());

		// Misc
		mods.add(new DiscordRPCMod());
		mods.add(new HypixelMod());
		mods.add(new TimeChangerMod());
		mods.add(new WeatherChangerMod());
		
		// Settings
		mods.add(new HUDModSettings());
		mods.add(new ModMenuSettings());
		mods.add(new SystemSettings());

		sortMods();
	}

	private void initDesigns() {
		designs.add(new ClassicDesign());
		designs.add(new ClearDesign());
		designs.add(new MaterialYouDesign());
		designs.add(new SimpleDesign());
		setCurrentDesign("design.simple");
	}

	public List<Mod> getMods() {
		return mods;
	}

	public List<Setting> getSettings() {
		return settings;
	}

	public List<HUDMod> getHUDMods() {
		return mods.stream().filter(m -> m instanceof HUDMod).map(m -> (HUDMod) m).collect(Collectors.toList());
	}

	public List<KeybindSetting> getKeybindSettings() {
		return settings.stream().filter(s -> s instanceof KeybindSetting).map(s -> (KeybindSetting) s)
				.collect(Collectors.toList());
	}

	public List<Setting> getSettingsByMod(Mod m) {
		return settings.stream().filter(s -> s.getParent().equals(m)).collect(Collectors.toList());
	}

	public void addSetting(Setting setting) {
		settings.add(setting);
	}

	public HUDDesign getCurrentDesign() {
		return currentDesign;
	}

	public void setCurrentDesign(String name) {
		this.currentDesign = getDesignByName(name);
	}

	public HUDDesign getDesignByName(String name) {
		return designs.stream().filter(design -> design.getName().equals(name)).findFirst()
				.orElseGet(() -> getDesignByName("design.simple"));
	}

	private void sortMods() {
		mods.sort((mod1, mod2) -> mod1.getName().compareTo(mod2.getName()));
	}
}
