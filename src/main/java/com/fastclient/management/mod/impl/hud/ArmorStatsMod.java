package com.fastclient.management.mod.impl.hud;

import java.util.Arrays;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderGameOverlayEvent;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.HUDMod;
import com.fastclient.management.mod.settings.impl.BooleanSetting;
import com.fastclient.management.mod.settings.impl.ComboSetting;
import com.fastclient.skia.font.Icon;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;


public class ArmorStatsMod extends HUDMod {

	private ComboSetting orientationSetting;
	private BooleanSetting showDurabilitySetting;
	private BooleanSetting showEmptySlotsSetting;

	public ArmorStatsMod() {
		super("mod.armorstats.name", "mod.armorstats.description", Icon.SHIELD);
		
		orientationSetting = new ComboSetting("setting.orientation", "setting.orientation.description",
				Icon.VIEW_AGENDA, this, Arrays.asList("option.vertical", "option.horizontal"), "option.vertical");
		showDurabilitySetting = new BooleanSetting("setting.showdurability", "setting.showdurability.description",
				Icon.SHOW_CHART, this, true);
		showEmptySlotsSetting = new BooleanSetting("setting.showemptyslots", "setting.showemptyslots.description",
				Icon.VISIBILITY, this, false);
	}

	// Store current dimensions and item data for cross-event rendering
	private float currentWidth = 0;
	private float currentHeight = 0;
	private ItemStack[] currentArmorPieces = null;
	private float[] itemPositionsX = new float[4];
	private float[] itemPositionsY = new float[4];
	private boolean[] itemVisible = new boolean[4];
	
	// Default placeholder items for empty armor slots (real Minecraft items)
	private static final ItemStack[] EMPTY_SLOT_ITEMS = {
		new ItemStack(Items.LEATHER_HELMET),
		new ItemStack(Items.LEATHER_CHESTPLATE),
		new ItemStack(Items.LEATHER_LEGGINGS),
		new ItemStack(Items.LEATHER_BOOTS)
	};

	public EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		if (client.player == null) return;
		this.draw();
	};
	
	// Render the actual Minecraft item icons using DrawContext
	public EventBus.EventListener<RenderGameOverlayEvent> onRenderGameOverlay = event -> {
		if (client.player == null || currentArmorPieces == null) return;
		
		DrawContext context = event.getContext();
		float scale = position.getScale();
		boolean showDurability = showDurabilitySetting.isEnabled();
		
		for (int i = 0; i < currentArmorPieces.length; i++) {
			if (!itemVisible[i]) continue;
			
			ItemStack stack = currentArmorPieces[i];
			ItemStack displayStack = stack.isEmpty() ? EMPTY_SLOT_ITEMS[i] : stack;
			
			// Calculate position with HUD scale applied
			float scaledX = position.getX() + (itemPositionsX[i] - position.getX()) * scale;
			float scaledY = position.getY() + (itemPositionsY[i] - position.getY()) * scale;
			
			// Draw the actual Minecraft item
			context.getMatrices().push();
			context.getMatrices().scale(scale, scale, 1.0f);
			context.drawItem(displayStack, (int)(scaledX / scale), (int)(scaledY / scale));
			
			// Draw durability bar under the item (like in inventory)
			if (showDurability && !stack.isEmpty() && stack.getMaxDamage() > 0 && stack.getDamage() > 0) {
				float durabilityPercent = 1.0f - ((float) stack.getDamage() / stack.getMaxDamage());
				int barX = (int)(scaledX / scale) + 2;
				int barY = (int)(scaledY / scale) + 14; // Moved down to be visible
				int barWidth = 13;
				int filledWidth = Math.round(barWidth * durabilityPercent);
				
				// Background bar (black)
				context.fill(barX, barY, barX + barWidth, barY + 2, 0xFF000000);
				// Durability bar (colored based on durability)
				int barColor = getDurabilityBarColor(durabilityPercent);
				context.fill(barX, barY, barX + filledWidth, barY + 1, barColor);
			}
			
			context.getMatrices().pop();
		}
	};

	protected void draw() {
		float padding = 5;
		float itemSpacing = 4;
		float itemSize = 16;
		
		boolean isVertical = "option.vertical".equals(orientationSetting.getOption());
		boolean showEmpty = showEmptySlotsSetting.isEnabled();
		
		currentArmorPieces = new ItemStack[] {
			client.player.getEquippedStack(EquipmentSlot.HEAD),
			client.player.getEquippedStack(EquipmentSlot.CHEST),
			client.player.getEquippedStack(EquipmentSlot.LEGS),
			client.player.getEquippedStack(EquipmentSlot.FEET)
		};
		
		// Reset visibility
		for (int i = 0; i < 4; i++) {
			itemVisible[i] = false;
		}
		
		int visibleCount = 0;
		for (ItemStack stack : currentArmorPieces) {
			if (!stack.isEmpty() || showEmpty) visibleCount++;
		}
		
		if (visibleCount == 0) {
			position.setSize(0, 0);
			return;
		}
		
		float width, height;
		
		if (isVertical) {
			width = itemSize + (padding * 2);
			height = (visibleCount * itemSize) + ((visibleCount - 1) * itemSpacing) + (padding * 2);
		} else {
			width = (visibleCount * itemSize) + ((visibleCount - 1) * itemSpacing) + (padding * 2);
			height = itemSize + (padding * 2);
		}
		
		this.begin();
		this.drawBackground(getX(), getY(), width, height);
		
		float offsetX = 0;
		float offsetY = 0;
		
		for (int i = 0; i < currentArmorPieces.length; i++) {
			ItemStack stack = currentArmorPieces[i];
			if (stack.isEmpty() && !showEmpty) continue;
			
			float itemX, itemY;
			
			if (isVertical) {
				itemX = getX() + padding;
				itemY = getY() + padding + offsetY;
				
				// Store position for item rendering in the other event
				itemPositionsX[i] = itemX;
				itemPositionsY[i] = itemY;
				itemVisible[i] = true;
				
				offsetY += itemSize + itemSpacing;
			} else {
				itemX = getX() + padding + offsetX;
				itemY = getY() + padding;
				
				// Store position for item rendering in the other event
				itemPositionsX[i] = itemX;
				itemPositionsY[i] = itemY;
				itemVisible[i] = true;
				
				offsetX += itemSize + itemSpacing;
			}
		}
		
		this.finish();
		position.setSize(width, height);
		currentWidth = width;
		currentHeight = height;
	}
	
	private String getArmorText(ItemStack stack, boolean showDurability) {
		if (stack.isEmpty()) {
			return "Empty";
		}
		
		String name = stack.getName().getString();
		name = name.replace(" Helmet", "").replace(" Chestplate", "").replace(" Leggings", "").replace(" Boots", "");
		
		if (showDurability && stack.getMaxDamage() > 0) {
			int durability = stack.getMaxDamage() - stack.getDamage();
			return name + " " + durability;
		}
		
		return name;
	}
	
	// Returns Minecraft-style durability bar color (green -> yellow -> red)
	private int getDurabilityBarColor(float percent) {
		int r, g;
		if (percent > 0.5f) {
			// Green to yellow (0.5 - 1.0)
			float t = (percent - 0.5f) * 2;
			r = (int)(255 * (1 - t));
			g = 255;
		} else {
			// Yellow to red (0.0 - 0.5)
			float t = percent * 2;
			r = 255;
			g = (int)(255 * t);
		}
		return 0xFF000000 | (r << 16) | (g << 8);
	}

	@Override
	public float getRadius() {
		return 6;
	}
}
