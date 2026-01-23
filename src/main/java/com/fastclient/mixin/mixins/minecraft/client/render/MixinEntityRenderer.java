package com.fastclient.mixin.mixins.minecraft.client.render;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fastclient.Fast;
import com.fastclient.management.mod.impl.misc.HypixelMod;
import com.fastclient.management.presence.PresenceManager;
import com.fastclient.utils.server.Server;
import com.fastclient.utils.server.ServerUtils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.state.EntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

@Mixin(EntityRenderer.class)
public abstract class MixinEntityRenderer<T extends Entity, S extends EntityRenderState> {

	@Shadow
	@Final
	private TextRenderer textRenderer;

	@Unique
	private static final Identifier FASTCLIENT_ICON = Identifier.of("fastclient", "textures/gui/icon.png");

	@Unique
	private static final int ICON_SIZE = 8;

	/**
	 * TAIL injection to render level head text and FastClient icon after vanilla nameplate rendering completes.
	 * This is safer than INVOKE targeting TextRenderer.draw calls which may change
	 * between patch releases (shadow, background, outline variants).
	 * Using TAIL ensures the injection fires regardless of internal method structure.
	 */
	@Inject(method = "renderLabelIfPresent(Lnet/minecraft/client/render/entity/state/EntityRenderState;Lnet/minecraft/text/Text;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("TAIL"))
	private void onRenderLevelHead(S inState, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers,
			int light, CallbackInfo ci) {
		renderFastClientFeatures(inState, text, matrices, vertexConsumers, light);
	}

	@Unique
	private void renderFastClientFeatures(S inState, Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		MinecraftClient client = MinecraftClient.getInstance();

		if (inState instanceof PlayerEntityRenderState state) {
			AbstractClientPlayerEntity player = (AbstractClientPlayerEntity) client.world.getEntityById(state.id);
			
			if (player == null) return;
			
			// Render FastClient icon for detected users
			if (PresenceManager.isFastClientUser(player.getUuid())) {
				renderFastClientIcon(text, matrices, vertexConsumers, light);
			}
			
			// Render Level Head on Hypixel
			if (ServerUtils.isJoin(Server.HYPIXEL)) {
				if (text.getString().contains(player.getName().getString())) {
					if (HypixelMod.getInstance().isEnabled()
							&& HypixelMod.getInstance().getLevelHeadSetting().isEnabled()) {
						renderLevelHead(player, text, matrices, vertexConsumers, light, client);
					}
				}
			}
		}
	}

	@Unique
	private void renderFastClientIcon(Text text, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		// Calculate position before the name
		float textWidth = textRenderer.getWidth(text);
		float iconX = -textWidth / 2F - ICON_SIZE - 2; // 2px padding before name
		float iconY = -1; // Slightly above baseline to align with text
		
		matrices.push();
		
		// Scale down the icon for nametag rendering (nametags are rendered at a smaller scale)
		float iconScale = 0.5F;
		matrices.translate(iconX, iconY, 0);
		matrices.scale(iconScale, iconScale, 1);
		
		Matrix4f matrix = matrices.peek().getPositionMatrix();
		
		// Render the icon as a textured quad
		RenderLayer renderLayer = RenderLayer.getTextBackgroundSeeThrough();
		VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getGuiTextured(FASTCLIENT_ICON));
		
		// Draw textured quad for the icon
		int size = (int)(ICON_SIZE / iconScale);
		vertexConsumer.vertex(matrix, 0, size, 0).color(255, 255, 255, 255).texture(0, 1).overlay(OverlayTexture.DEFAULT_UV).light(light);
		vertexConsumer.vertex(matrix, size, size, 0).color(255, 255, 255, 255).texture(1, 1).overlay(OverlayTexture.DEFAULT_UV).light(light);
		vertexConsumer.vertex(matrix, size, 0, 0).color(255, 255, 255, 255).texture(1, 0).overlay(OverlayTexture.DEFAULT_UV).light(light);
		vertexConsumer.vertex(matrix, 0, 0, 0).color(255, 255, 255, 255).texture(0, 0).overlay(OverlayTexture.DEFAULT_UV).light(light);
		
		matrices.pop();
	}

	@Unique
	private void renderLevelHead(AbstractClientPlayerEntity player, Text text, MatrixStack matrices, 
			VertexConsumerProvider vertexConsumers, int light, MinecraftClient client) {
		String levelText = Formatting.AQUA.toString() + "Level: " + Formatting.YELLOW.toString()
				+ Fast.getInstance().getHypixelManager()
						.getByUuid(player.getUuid().toString().replace("-", "")).getNetworkLevel();

		float x = -textRenderer.getWidth(levelText) / 2F;
		float y = text.getString().contains("deadmau5") ? -20 : -10;
		int color = (int) (client.options.getTextBackgroundOpacity(0.25F) * 255.0F) << 24;

		Matrix4f matrix4f = matrices.peek().getPositionMatrix();

		textRenderer.draw(levelText, x, y, Colors.WHITE, false, matrix4f, vertexConsumers,
				TextRenderer.TextLayerType.NORMAL, color, light);
	}
}
