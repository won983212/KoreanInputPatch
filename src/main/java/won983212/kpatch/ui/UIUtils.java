package won983212.kpatch.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import won983212.font.ZWSPFixedFontRenderer;

public class UIUtils {
	private static FontRenderer ascii_font_renderer = null; 
	private static int shadowColor = -1;
	
	// ================================= Core Utils =================================
	
	public static void useShadow(int shadow) {
		shadowColor = shadow;
	}
	
	// ================================= Drawing Utils =================================
	
	public static void drawRectDouble(double left, double top, double right, double bottom, int color) {
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
		drawRectDouble(x, y, x + width, y + height, color);
	}
	
	// ================================= Text Utils =================================
	
	public static FontRenderer getDefaultASCIIRenderer() {
		if(ascii_font_renderer == null) {
			ascii_font_renderer = new ZWSPFixedFontRenderer(Minecraft.getMinecraft());
		}
		return ascii_font_renderer;
	}
	
	public static void drawText(FontRenderer fr, String text, float x, float y, int color) {
		if(shadowColor != -1) {
			fr.drawString(text, x + 0.5f, y + 0.5f, shadowColor, false);
			shadowColor = -1;
		}
		fr.drawString(text, x, y, color, false);
	}
	
	public static void drawAreaCenteredText(FontRenderer fr, String text, float x, float y, int width, int height, int color) {
		x = x + (width - fr.getStringWidth(text)) / 2;
		y = y + (height - fr.FONT_HEIGHT) / 2;
		drawText(fr, text, x, y, color);
	}
	
	public static void drawCenteredText(FontRenderer fr, String text, float x, float y, int color) {
		x = x - fr.getStringWidth(text) / 2;
		y = y - fr.FONT_HEIGHT / 2;
		drawText(fr, text, x, y, color);
	}
}
