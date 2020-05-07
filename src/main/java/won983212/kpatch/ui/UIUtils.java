package won983212.kpatch.ui;

import net.minecraft.client.Minecraft;
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
}
