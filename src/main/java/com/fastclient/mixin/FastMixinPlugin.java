package com.fastclient.mixin;

import java.util.List;
import java.util.Set;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensionpoints.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensionpoints.IMixinInfo;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

/**
 * Mixin plugin for version-conditional mixin loading.
 * Handles API differences between MC 1.21.x versions.
 */
public class FastMixinPlugin implements IMixinConfigPlugin {

	private static final String MC_VERSION;
	private static final boolean IS_1_21_11_OR_LATER;
	
	static {
		MC_VERSION = FabricLoader.getInstance()
				.getModContainer("minecraft")
				.map(mod -> mod.getMetadata().getVersion().getFriendlyString())
				.orElse("unknown");
		
		IS_1_21_11_OR_LATER = isVersionAtLeast("1.21.11");
		
		System.out.println("[FastClient] Detected Minecraft version: " + MC_VERSION);
		System.out.println("[FastClient] Using 1.21.11+ compatibility mode: " + IS_1_21_11_OR_LATER);
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

	@Override
	public void onLoad(String mixinPackage) {
	}

	@Override
	public String getRefMapperConfig() {
		return null;
	}

	@Override
	public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
		// BufferRendererAccessor was removed in 1.21.11 (BufferRenderer class refactored)
		if (mixinClassName.contains("BufferRendererAccessor")) {
			return !IS_1_21_11_OR_LATER;
		}
		
		return true;
	}

	@Override
	public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
	}

	@Override
	public List<String> getMixins() {
		return null;
	}

	@Override
	public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}

	@Override
	public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
	}
	
	public static boolean is1_21_11OrLater() {
		return IS_1_21_11_OR_LATER;
	}
}
