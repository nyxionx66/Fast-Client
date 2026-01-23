package com.fastclient.management.color.api;

import java.awt.Color;

import com.fastclient.libraries.material3.Material3;
import com.fastclient.libraries.material3.dynamiccolor.DynamicScheme;
import com.fastclient.libraries.material3.dynamiccolor.MaterialDynamicColors;
import com.fastclient.libraries.material3.hct.Hct;
import com.fastclient.libraries.material3.utils.ColorUtils;

public class ColorPalette {

	private final DynamicScheme scheme;
	private final MaterialDynamicColors colors;
	private boolean dark;
	private Hct hct;

	private static final Color ORANGE_PRIMARY = new Color(0xFF, 0x84, 0x3F);           
	private static final Color YELLOW_ACCENT = new Color(0xFF, 0xC1, 0x1D);            
	private static final Color TEXT_ORANGE = new Color(0xED, 0x79, 0x3B);              
	private static final Color BACKGROUND_BLACK = new Color(0x00, 0x00, 0x00);         
	private static final Color HOVER_BUTTON = new Color(0x22, 0x15, 0x11);             
	
	private static final Color SURFACE_DARK = new Color(0x00, 0x00, 0x00);             
	private static final Color SURFACE_CONTAINER_DARK = new Color(0x0A, 0x08, 0x06);   
	private static final Color SURFACE_CONTAINER_HIGH = new Color(0x1A, 0x12, 0x0C);   
	private static final Color SURFACE_CONTAINER_HIGHEST = new Color(0x22, 0x15, 0x11);
	private static final Color SURFACE_CONTAINER_LOW = new Color(0x05, 0x04, 0x03);    
	private static final Color SURFACE_CONTAINER_LOWEST = new Color(0x00, 0x00, 0x00); 
	private static final Color OUTLINE_COLOR = new Color(0xFF, 0x84, 0x3F);            
	private static final Color OUTLINE_VARIANT = new Color(0x3A, 0x28, 0x1A);          
	private static final Color ON_SURFACE = new Color(0xFF, 0xFF, 0xFF);               
	private static final Color ON_SURFACE_VARIANT = new Color(0xED, 0x79, 0x3B);       
	
	private static final Color PRIMARY_CONTAINER = new Color(0x22, 0x15, 0x11);        
	private static final Color SECONDARY_CONTAINER = new Color(0x2A, 0x1C, 0x12);      
	private static final Color TERTIARY_CONTAINER = new Color(0x1A, 0x10, 0x0A);       
	
	private static final Color ON_PRIMARY_CONTAINER = new Color(0xFF, 0x84, 0x3F);     
	private static final Color ON_SECONDARY_CONTAINER = new Color(0xFF, 0xA0, 0x5A);  
	private static final Color ON_TERTIARY_CONTAINER = new Color(0xED, 0x79, 0x3B);    

	public ColorPalette(Hct hct, boolean dark, float contrast) {
		this.hct = hct;
		this.dark = dark;
		scheme = Material3.getDynamicScheme(hct, dark, contrast);
		colors = new MaterialDynamicColors();
	}

	public ColorPalette(Hct hct, boolean dark) {
		this(hct, dark, 0);
	}

	public Color getPrimary() {
		return ORANGE_PRIMARY;
	}

	public Color getSecondary() {
		return YELLOW_ACCENT;
	}

	public Color getTertiary() {
		return TEXT_ORANGE;
	}

	public Color getPrimaryContainer() {
		return PRIMARY_CONTAINER;
	}

	public Color getSecondaryContainer() {
		return SECONDARY_CONTAINER;
	}

	public Color getTertiaryContainer() {
		return TERTIARY_CONTAINER;
	}

	public Color getOnPrimary() {
		return BACKGROUND_BLACK;
	}

	public Color getOnSecondary() {
		return BACKGROUND_BLACK;
	}

	public Color getOnTertiary() {
		return BACKGROUND_BLACK;
	}

	public Color getOnPrimaryContainer() {
		return ON_PRIMARY_CONTAINER;
	}

	public Color getOnSecondaryContainer() {
		return ON_SECONDARY_CONTAINER;
	}

	public Color getOnTertiaryContainer() {
		return ON_TERTIARY_CONTAINER;
	}

	public Color getError() {
		return new Color(0xFF, 0x5A, 0x5A);
	}

	public Color getErrorContainer() {
		return new Color(0x4A, 0x1A, 0x1A);
	}

	public Color getOnError() {
		return BACKGROUND_BLACK;
	}

	public Color getOnErrorContainer() {
		return new Color(0xFF, 0xC0, 0xC0);
	}

	public Color getBackground() {
		return BACKGROUND_BLACK;
	}

	public Color getOnBackground() {
		return ON_SURFACE;
	}

	public Color getSurface() {
		return SURFACE_DARK;
	}

	public Color getOnSurface() {
		return ON_SURFACE;
	}

	public Color getSurfaceContainer() {
		return SURFACE_CONTAINER_DARK;
	}

	public Color getSurfaceVariant() {
		return HOVER_BUTTON;
	}

	public Color getOnSurfaceVariant() {
		return ON_SURFACE_VARIANT;
	}

	public Color getSurfaceContainerHigh() {
		return SURFACE_CONTAINER_HIGH;
	}

	public Color getSurfaceContainerHighest() {
		return SURFACE_CONTAINER_HIGHEST;
	}

	public Color getSurfaceContainerLow() {
		return SURFACE_CONTAINER_LOW;
	}

	public Color getSurfaceContainerLowest() {
		return SURFACE_CONTAINER_LOWEST;
	}

	public Color getSurfaceTint() {
		return ORANGE_PRIMARY;
	}

	public Color getOutline() {
		return OUTLINE_COLOR;
	}

	public Color getOutlineVariant() {
		return OUTLINE_VARIANT;
	}

	private Color argbToColor(int argb) {

		int red = ColorUtils.redFromArgb(argb);
		int green = ColorUtils.greenFromArgb(argb);
		int blue = ColorUtils.blueFromArgb(argb);

		return new Color(red, green, blue);
	}

	public boolean isDarkMode() {
		return dark;
	}

	public Hct getHct() {
		return hct;
	}
}