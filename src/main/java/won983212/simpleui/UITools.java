package won983212.simpleui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import won983212.font.ZWSPFixedFontRenderer;

public class UITools {
	public static final int CENTER_H = 1;
	public static final int CENTER_V = 2;
	public static final int CENTER_BOTH = CENTER_H | CENTER_V;
	private static FontRenderer ascii_font_renderer = null;

	private static void bindColor(int color) {
		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		GlStateManager.color(f, f1, f2, f3);
	}

	private static void drawRect(double left, double top, double right, double bottom) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos(left, bottom, 0.0D).endVertex();
		bufferbuilder.pos(right, bottom, 0.0D).endVertex();
		bufferbuilder.pos(right, top, 0.0D).endVertex();
		bufferbuilder.pos(left, top, 0.0D).endVertex();
		tessellator.draw();
	}

	private static void drawArc(double x, double y, double radius, int seperatedIndex) {
		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION);
		bufferbuilder.pos(x, y, 0.0D).endVertex();
		for (int t = 0; t <= radius * 2; t++) {
			double theta = Math.PI / (2 * radius * 2) * t + (seperatedIndex % 4) * Math.PI / 2;
			bufferbuilder.pos(x + radius * Math.cos(theta), y - radius * Math.sin(theta), 0.0D).endVertex();
		}
		tessellator.draw();
	}

	private static void drawRoundRect(double left, double top, double right, double bottom, double rad) {
		drawRect(left + rad, top, right - rad, bottom);
		drawRect(left, top + rad, left + rad, bottom - rad);
		drawRect(left + rad, top + rad, right, bottom - rad);
		drawArc(left + rad, top + rad, rad, 1);
		drawArc(left + rad, bottom - rad, rad, 2);
		drawArc(right - rad, bottom - rad, rad, 3);
		drawArc(right - rad, top + rad, rad, 4);
	}

	public static void drawArcRect(double left, double top, double right, double bottom, int color, int shadow,
			int round) {
		if (left > right) {
			double i = left;
			left = right;
			right = i;
		}

		if (top > bottom) {
			double j = top;
			top = bottom;
			bottom = j;
		}

		GlStateManager.enableBlend();
		GlStateManager.disableTexture2D();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA,
				GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE,
				GlStateManager.DestFactor.ZERO);

		if (shadow != 0) {
			bindColor(shadow);
			if (round > 0) {
				drawRoundRect(left + 0.5, top + 0.5, right + 0.5, bottom + 0.5, round);
			} else {
				drawRect(left + 0.5, top + 0.5, right + 0.5, bottom + 0.5);
			}
		}

		bindColor(color);
		if (round > 0) {
			drawRoundRect(left, top, right, bottom, round);
		} else {
			drawRect(left, top, right, bottom);
		}

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawArea(double x, double y, double width, double height, int color, int shadow, int round) {
		drawArcRect(x, y, x + width, y + height, color, shadow, round);
	}

	public static FontRenderer getDefaultASCIIRenderer() {
		if (ascii_font_renderer == null) {
			ascii_font_renderer = new ZWSPFixedFontRenderer(Minecraft.getMinecraft(), false);
		}
		return ascii_font_renderer;
	}

	public static void drawText(FontRenderer fr, String text, float x, float y, int color, int shadow, int arrange) {
		if (arrange > 0) {
			if ((arrange & CENTER_H) > 0) {
				x = x - fr.getStringWidth(text) / 2;
			}
			if ((arrange & CENTER_V) > 0) {
				y = y - fr.FONT_HEIGHT / 2;
			}
		}

		if (shadow != 0) {
			fr.drawString(text, x + 0.5f, y + 0.5f, shadow, false);
		}

		fr.drawString(text, x, y, color, false);
	}
}
