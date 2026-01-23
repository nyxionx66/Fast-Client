package com.fastclient;

import com.fastclient.animation.Delta;
import com.fastclient.event.EventBus;
import com.fastclient.event.server.PacketHandler;
import com.fastclient.libraries.browser.JCefBrowser;
import com.fastclient.management.color.ColorManager;
import com.fastclient.management.config.ConfigManager;
import com.fastclient.management.hypixel.HypixelManager;
import com.fastclient.management.mod.ModManager;
import com.fastclient.management.music.MusicManager;
import com.fastclient.management.presence.PresenceManager;
import com.fastclient.management.profile.ProfileManager;
import com.fastclient.management.user.UserManager;
import com.fastclient.management.websocket.WebSocketManager;
import com.fastclient.skia.font.Fonts;
import com.fastclient.utils.file.FileLocation;
import com.fastclient.utils.language.I18n;
import com.fastclient.utils.language.Language;

public class Fast {

	private final static Fast instance = new Fast();

	private final String name = "Fast";
	private final String version = "8.0";

	private long launchTime;

	private ModManager modManager;
	private ColorManager colorManager;
	private MusicManager musicManager;
	private ConfigManager configManager;
	private ProfileManager profileManager;
	private WebSocketManager webSocketManager;
	private UserManager userManager;
	private HypixelManager hypixelManager;

	public void start() {

		JCefBrowser.download();
		Fonts.loadAll();
		FileLocation.init();
		I18n.setLanguage(Language.ENGLISH);

		launchTime = System.currentTimeMillis();

		modManager = new ModManager();
		modManager.init();
		colorManager = new ColorManager();
		musicManager = new MusicManager();
		configManager = new ConfigManager();
		profileManager = new ProfileManager();
		webSocketManager = new WebSocketManager();
		userManager = new UserManager();
		hypixelManager = new HypixelManager();

		EventBus.getInstance().register(new FastHandler());
		EventBus.getInstance().register(new PacketHandler());
		EventBus.getInstance().register(new Delta());
		
		// Initialize FastClient user presence detection
		PresenceManager.init();
	}

	public static Fast getInstance() {
		return instance;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public long getLaunchTime() {
		return launchTime;
	}

	public ModManager getModManager() {
		return modManager;
	}

	public ColorManager getColorManager() {
		return colorManager;
	}

	public MusicManager getMusicManager() {
		return musicManager;
	}

	public ConfigManager getConfigManager() {
		return configManager;
	}

	public ProfileManager getProfileManager() {
		return profileManager;
	}

	public WebSocketManager getWebSocketManager() {
		return webSocketManager;
	}

	public UserManager getUserManager() {
		return userManager;
	}

	public HypixelManager getHypixelManager() {
		return hypixelManager;
	}
}
