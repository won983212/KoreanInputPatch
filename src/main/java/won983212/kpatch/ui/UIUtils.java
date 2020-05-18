package won983212.kpatch.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;

public class UIUtils {
	public static void drawRectDouble(double left, double top, double right, double bottom, int color) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int factor = sr.getScaleFactor();
		double revFactor = 1.0 / factor;

		GlStateManager.scale(revFactor, revFactor, revFactor);
		Gui.drawRect((int) (left * factor), (int) (top * factor), (int) (right * factor), (int) (bottom * factor), color);
		GlStateManager.scale(factor, factor, factor);
	}
	
	public static void drawArea(int x, int y, int width, int height, int color) {
		Gui.drawRect(x, y, x + width, y + height, color);
	}
	
	public static void drawTextCustomShadow(FontRenderer fr, String text, int x, int y, int color, int shadowColor) {
		fr.drawString(text, x + 0.5f, y + 0.5f, shadowColor, false);
		fr.drawString(text, x, y, color);
	}
	
	public static void drawCentralText(FontRenderer fr, String text, int x, int y, int width, int height, int color) {
		x = x + (width - fr.getStringWidth(text)) / 2;
		y = y + (height - fr.FONT_HEIGHT) / 2;
		fr.drawString(text, x, y, color);
	}
}
