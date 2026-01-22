import java.util.Arrays;
import java.util.List;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.RenderSkiaEvent;
import com.fastclient.management.mod.api.hud.HUDMod;
import com.fastclient.management.mod.settings.impl.ComboSetting;
import com.fastclient.skia.Skia;
import com.fastclient.skia.font.Fonts;
import com.fastclient.skia.font.Icon;

import io.github.humbleui.skija.FontMetrics;
import io.github.humbleui.types.Rect;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;

public class ArmorStatsMod extends HUDMod {

	protected ComboSetting orientationSetting = new ComboSetting("setting.orientation", "setting.orientation.description",
			Icon.VIEW_AGENDA, this, Arrays.asList("Vertical", "Horizontal"), "Vertical");

	public ArmorStatsMod() {
		super("mod.armorstats.name", "mod.armorstats.description", Icon.SHIELD);
	}

	public EventBus.EventListener<RenderSkiaEvent> onRenderSkia = event -> {
		if (client.player == null) return;
		this.draw();
	};

	protected void draw() {
		float fontSize = 9;
		float iconSize = 10.5F;
		float padding = 5;
		float itemSpacing = 4;
		
		boolean isVertical = "Vertical".equals(orientationSetting.getOption());
		
		// Get armor items
		ItemStack helmet = client.player.getEquippedStack(EquipmentSlot.HEAD);
		ItemStack chestplate = client.player.getEquippedStack(EquipmentSlot.CHEST);
		ItemStack leggings = client.player.getEquippedStack(EquipmentSlot.LEGS);
		ItemStack boots = client.player.getEquippedStack(EquipmentSlot.FEET);
		
		// Calculate total armor and toughness
		int totalArmor = 0;
		float totalToughness = 0;
		
		ItemStack[] armorPieces = {helmet, chestplate, leggings, boots};
		for (ItemStack stack : armorPieces) {
			if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem) {
				ArmorItem armorItem = (ArmorItem) stack.getItem();
				totalArmor += armorItem.getMaterial().value().getDefense(armorItem.getType());
				totalToughness += armorItem.getMaterial().value().toughness();
			}
		}
		
		String armorText = totalArmor + " Armor";
		String toughnessText = String.format("%.1f", totalToughness) + " Toughness";
		
		Rect armorBounds = Skia.getTextBounds(armorText, Fonts.getRegular(fontSize));
		Rect toughnessBounds = Skia.getTextBounds(toughnessText, Fonts.getRegular(fontSize));
		Rect iconBounds = Skia.getTextBounds(Icon.SHIELD, Fonts.getIcon(iconSize));
		FontMetrics metrics = Fonts.getRegular(fontSize).getMetrics();
		float textCenterY = (metrics.getAscent() - metrics.getDescent()) / 2 - metrics.getAscent();
		
		float width, height;
		
		if (isVertical) {
			// Vertical layout: stack armor and toughness
			float maxTextWidth = Math.max(armorBounds.getWidth(), toughnessBounds.getWidth());
			width = iconBounds.getWidth() + 4 + maxTextWidth + (padding * 2);
			height = (fontSize * 2) + itemSpacing + (padding * 2);
		} else {
			// Horizontal layout: side by side
			width = iconBounds.getWidth() + 4 + armorBounds.getWidth() + 10 + toughnessBounds.getWidth() + (padding * 2);
			height = fontSize + (padding * 2) - 1.5F;
		}
		
		this.begin();
		this.drawBackground(getX(), getY(), width, height);
		
		if (isVertical) {
			// Vertical layout
			// First line: armor
			this.drawText(Icon.SHIELD, getX() + padding, getY() + padding + (fontSize / 2) - (iconBounds.getHeight() / 2),
					Fonts.getIcon(iconSize));
			this.drawText(armorText, getX() + padding + iconBounds.getWidth() + 4,
					getY() + padding + (fontSize / 2) - textCenterY, Fonts.getRegular(fontSize));
			
			// Second line: toughness
			this.drawText(toughnessText, getX() + padding + iconBounds.getWidth() + 4,
					getY() + padding + fontSize + itemSpacing + (fontSize / 2) - textCenterY, Fonts.getRegular(fontSize));
		} else {
			// Horizontal layout
			this.drawText(Icon.SHIELD, getX() + padding, getY() + (height / 2) - (iconBounds.getHeight() / 2),
					Fonts.getIcon(iconSize));
			this.drawText(armorText, getX() + padding + iconBounds.getWidth() + 4,
					getY() + (height / 2) - textCenterY, Fonts.getRegular(fontSize));
			this.drawText(toughnessText, getX() + padding + iconBounds.getWidth() + 4 + armorBounds.getWidth() + 10,
					getY() + (height / 2) - textCenterY, Fonts.getRegular(fontSize));
		}
		
		this.finish();
		position.setSize(width, height);
	}

	@Override
	public float getRadius() {
		return 6;
	}
}
