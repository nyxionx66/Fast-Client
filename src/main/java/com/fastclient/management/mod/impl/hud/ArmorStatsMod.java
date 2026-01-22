package com.fastclient.management.mod.impl.hud;

import java.util.Arrays;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.HUDMod;
import com.fastclient.management.mod.settings.impl.BooleanSetting;
import com.fastclient.management.mod.settings.impl.ComboSetting;
import com.fastclient.skia.Skia;
import com.fastclient.skia.font.Fonts;
import com.fastclient.skia.font.Icon;

import io.github.humbleui.skija.FontMetrics;
import io.github.humbleui.types.Rect;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;

import java.awt.Color;


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

	public EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		if (client.player == null) return;
		this.draw();
	};

	protected void draw() {
		float fontSize = 9;
		float padding = 5;
		float itemSpacing = 3;
		float itemSize = 16;
		
		boolean isVertical = "option.vertical".equals(orientationSetting.getOption());
		boolean showDurability = showDurabilitySetting.isEnabled();
		boolean showEmpty = showEmptySlotsSetting.isEnabled();
		
		ItemStack[] armorPieces = {
			client.player.getEquippedStack(EquipmentSlot.HEAD),
			client.player.getEquippedStack(EquipmentSlot.CHEST),
			client.player.getEquippedStack(EquipmentSlot.LEGS),
			client.player.getEquippedStack(EquipmentSlot.FEET)
		};
		
		String[] slotIcons = {"armor_helmet.png", "armor_chestplate.png", "armor_leggings.png", "armor_boots.png"};
		
		int visibleCount = 0;
		for (ItemStack stack : armorPieces) {
			if (!stack.isEmpty() || showEmpty) visibleCount++;
		}
		
		if (visibleCount == 0) {
			position.setSize(0, 0);
			return;
		}
		
		FontMetrics metrics = Fonts.getRegular(fontSize).getMetrics();
		float textCenterY = (metrics.getAscent() - metrics.getDescent()) / 2 - metrics.getAscent();
		
		float maxTextWidth = 0;
		for (int i = 0; i < armorPieces.length; i++) {
			ItemStack stack = armorPieces[i];
			if (stack.isEmpty() && !showEmpty) continue;
			
			String text = getArmorText(stack, showDurability);
			Rect bounds = Skia.getTextBounds(text, Fonts.getRegular(fontSize));
			maxTextWidth = Math.max(maxTextWidth, bounds.getWidth());
		}
		
		float width, height;
		
		if (isVertical) {
			width = itemSize + 4 + maxTextWidth + (padding * 2);
			height = (visibleCount * itemSize) + ((visibleCount - 1) * itemSpacing) + (padding * 2);
		} else {
			float totalWidth = 0;
			for (int i = 0; i < armorPieces.length; i++) {
				ItemStack stack = armorPieces[i];
				if (stack.isEmpty() && !showEmpty) continue;
				
				String text = getArmorText(stack, showDurability);
				Rect bounds = Skia.getTextBounds(text, Fonts.getRegular(fontSize));
				totalWidth += itemSize + 4 + bounds.getWidth();
			}
			totalWidth += (visibleCount - 1) * 10; 
			width = totalWidth + (padding * 2);
			height = itemSize + (padding * 2);
		}
		
		this.begin();
		this.drawBackground(getX(), getY(), width, height);
		
		float offsetX = 0;
		float offsetY = 0;
		
		for (int i = 0; i < armorPieces.length; i++) {
			ItemStack stack = armorPieces[i];
			if (stack.isEmpty() && !showEmpty) continue;
			
			String text = getArmorText(stack, showDurability);
			String iconPath = slotIcons[i];
			Rect textBounds = Skia.getTextBounds(text, Fonts.getRegular(fontSize));
			
			float itemX, itemY;
			float iconSize = 16;
			
			if (isVertical) {
				itemX = getX() + padding;
				itemY = getY() + padding + offsetY;
				
				Skia.drawImage(iconPath, itemX, itemY, iconSize, iconSize);
				
				this.drawText(text, itemX + itemSize + 4, itemY + (itemSize / 2) - textCenterY, Fonts.getRegular(fontSize));
				
				if (showDurability && !stack.isEmpty() && stack.getMaxDamage() > 0) {
					float durabilityPercent = 1.0f - ((float) stack.getDamage() / stack.getMaxDamage());
					float barWidth = textBounds.getWidth();
					float barHeight = 2;
					float barX = itemX + itemSize + 4;
					float barY = itemY + itemSize - 3;
					
					Skia.drawRoundedRect(barX, barY, barWidth, barHeight, 1, new Color(255, 255, 255, 68));
					Color barColor = getDurabilityColor(durabilityPercent);
					Skia.drawRoundedRect(barX, barY, barWidth * durabilityPercent, barHeight, 1, barColor);
				}
				
				offsetY += itemSize + itemSpacing;
			} else {
				itemX = getX() + padding + offsetX;
				itemY = getY() + padding;
				
				Skia.drawImage(iconPath, itemX, itemY, iconSize, iconSize);
				
				this.drawText(text, itemX + itemSize + 4, itemY + (itemSize / 2) - textCenterY, Fonts.getRegular(fontSize));
				
				offsetX += itemSize + 4 + textBounds.getWidth() + 10;
			}
		}
		
		this.finish();
		position.setSize(width, height);
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
	
	private Color getDurabilityColor(float percent) {
		if (percent > 0.5f) {
			return new Color(85, 255, 85);
		} else if (percent > 0.25f) {
			return new Color(255, 255, 85); 
		} else {
			return new Color(255, 85, 85);
		}
	}

	@Override
	public float getRadius() {
		return 6;
	}
}
