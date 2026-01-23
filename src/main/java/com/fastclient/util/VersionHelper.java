package com.fastclient.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

/**
 * Utility class to detect Minecraft version for compatibility handling
 */
public class VersionHelper {

	private static final String MC_VERSION;
	private static final boolean IS_1_21_11_OR_LATER;
	
	static {
		MC_VERSION = FabricLoader.getInstance()
				.getModContainer("minecraft")
				.map(mod -> mod.getMetadata().getVersion().getFriendlyString())
				.orElse("unknown");
		
		IS_1_21_11_OR_LATER = isVersionAtLeast("1.21.11");
	}
	
	public static String getMinecraftVersion() {
		return MC_VERSION;
	}
	
	/**
	 * Check if running on MC 1.21.11 or later where some APIs changed
	 */
	public static boolean is1_21_11OrLater() {
		return IS_1_21_11_OR_LATER;
	}
	
	private static boolean isVersionAtLeast(String targetVersion) {
		try {
			Version current = Version.parse(MC_VERSION);
			Version target = Version.parse(targetVersion);
			return current.compareTo(target) >= 0;
		} catch (VersionParsingException e) {
			return false;
		}
	}
}
