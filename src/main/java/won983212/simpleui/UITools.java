package won983212.simpleui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import won983212.font.ZWSPFixedFontRenderer;

public class UITools {
	public static final int TEXT_CENTER_HORIZONTAL = 1;
	public static final int TEXT_CENTER_VERTICAL = 2;
	
	private static FontRenderer ascii_font_renderer = null; 
	private static int shadowColor = -1;
	private static long areaInfo = 0;
	private static int textFlag = 0;
	
	// ================================= Core Utils =================================
	
	public static void useShadow(int shadow) {
		if(shadow == -1) {
			shadowColor = (shadow & 16579836) >> 2 | shadow & -16777216;
		} else {
			shadowColor = shadow;
		}
	}
	
	public static void useTextCenterArea(int width, int height) {
		areaInfo = ((long) width << 32) + height;
	}
	
	public static void useTextCenter(boolean horizontal, boolean vertical) {
		int h = horizontal ? TEXT_CENTER_HORIZONTAL : 0;
		int v = vertical ? TEXT_CENTER_VERTICAL : 0;
		textFlag |= (h | v);
	}
	
	// ================================= Drawing Utils =================================
	
	public static void drawRect(double left, double top, double right, double bottom, int color) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int factor = sr.getScaleFactor();
		double revFactor = 1.0 / factor;

		GlStateManager.scale(revFactor, revFactor, revFactor);
		if(shadowColor != -1) {
			Gui.drawRect((int) ((left + 1) * factor), (int) ((top + 1) * factor), (int) ((right + 1) * factor), (int) ((bottom + 1) * factor), shadowColor);
			shadowColor = -1;
		}
		Gui.drawRect((int) (left * factor), (int) (top * factor), (int) (right * factor), (int) (bottom * factor), color);
		GlStateManager.scale(factor, factor, factor);
	}
	
	public static void drawArea(double x, double y, double width, double height, int color) {
		drawRect(x, y, x + width, y + height, color);
	}
	
	// ================================= Text Utils =================================
	
	public static FontRenderer getDefaultASCIIRenderer() {
		if(ascii_font_renderer == null) {
			ascii_font_renderer = new ZWSPFixedFontRenderer(Minecraft.getMinecraft(), false);
		}
		return ascii_font_renderer;
	}
	
	public static void drawText(FontRenderer fr, String text, float x, float y, int color) {
		int areaWidth = 0;
		int areaHeight = 0;
		
		if(areaInfo > 0) {
			areaWidth = (int) ((areaInfo >> 32) & 0x7fffffff);
			areaHeight = (int) (areaInfo & 0x7fffffff);
			areaInfo = 0;
		}
		
		if(textFlag > 0) {
			if((textFlag & TEXT_CENTER_HORIZONTAL) > 0) {
				if(areaWidth > 0) {
					x = x + (areaWidth - fr.getStringWidth(text)) / 2;
				} else {
					x = x + fr.getStringWidth(text) / 2;
				}
			}
			
			if((textFlag & TEXT_CENTER_VERTICAL) > 0) {
				if(areaHeight > 0) {
					y = y + (areaHeight - fr.FONT_HEIGHT) / 2;
				} else {
					y = y + fr.FONT_HEIGHT / 2;
				}
			}
			
			textFlag = 0;
		}
		
		if(shadowColor != -1) {
			fr.drawString(text, x + 0.5f, y + 0.5f, shadowColor, false);
			shadowColor = -1;
		}
		
		fr.drawString(text, x, y, color, false);
	}
}
