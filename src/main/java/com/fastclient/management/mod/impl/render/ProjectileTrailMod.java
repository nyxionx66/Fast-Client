package com.fastclient.management.mod.impl.render;

import java.util.Arrays;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.ClientTickEvent;
import com.fastclient.management.mod.Mod;
import com.fastclient.management.mod.ModCategory;
import com.fastclient.management.mod.settings.impl.ComboSetting;
import com.fastclient.skia.font.Icon;

import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;

public class ProjectileTrailMod extends Mod {

	private ComboSetting effectSetting = new ComboSetting("setting.effect", "setting.effect.description",
			Icon.EXPLOSION, this,
			Arrays.asList("setting.blacksmoke", "setting.fire", "setting.greenstar", "setting.hearts", "setting.magic",
					"setting.musicnote", "setting.slime", "setting.spark", "setting.swirl", "setting.whitesmoke"),
			"setting.hearts");

	public ProjectileTrailMod() {
		super("mod.projectiletrail.name", "mod.projectiletrail.description", Icon.TRAIL_LENGTH, ModCategory.RENDER);
	}

	public final EventBus.EventListener<ClientTickEvent> onClientTick = event -> {

		if (client.player != null && client.world != null) {

			Box box = new Box(client.player.getPos().add(-150.0, -150.0, -150.0),
					client.player.getPos().add(150.0, 150.0, 150.0));

			for (ProjectileEntity projectile : client.world.getEntitiesByClass(ProjectileEntity.class, box,
					entity -> true)) {

				if (!(projectile.getVelocity().lengthSquared() > 0.01) || projectile.isOnGround()
						|| client.world.getBlockState(projectile.getBlockPos()).isSolidBlock(client.world,
								projectile.getBlockPos()) && projectile.getOwner().getId() != client.player.getId()) {
					continue;
				}

				ParticleType<?> type = getCurrentType();

				if (type != null && type instanceof ParticleEffect) {
					client.world.addParticle((ParticleEffect) type, projectile.getX(), projectile.getY(),
							projectile.getZ(), 0.0, 0.0, 0.0);
				}
			}
		}
	};

	private ParticleType<?> getCurrentType() {

		String option = effectSetting.getOption();

		if (option.contains("blacksmoke")) {
			return ParticleTypes.SMOKE;
		} else if (option.contains("fire")) {
			return ParticleTypes.FLAME;
		} else if (option.contains("greenstar")) {
			return ParticleTypes.HAPPY_VILLAGER;
		} else if (option.contains("hearts")) {
			return ParticleTypes.HEART;
		} else if (option.contains("magic")) {
			return ParticleTypes.WITCH;
		} else if (option.contains("musicnote")) {
			return ParticleTypes.NOTE;
		} else if (option.contains("slime")) {
			return ParticleTypes.ITEM_SLIME;
		} else if (option.contains("spark")) {
			return ParticleTypes.FIREWORK;
		} else if (option.contains("whitesmoke")) {
			return ParticleTypes.WHITE_SMOKE;
		}

		return null;
	}
}