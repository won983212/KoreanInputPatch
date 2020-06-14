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
		if(rad > 0) {
			drawRect(left + rad, top, right - rad, bottom);
			drawRect(left, top + rad, left + rad, bottom - rad);
			drawRect(left + rad, top + rad, right, bottom - rad);
			drawArc(left + rad, top + rad, rad, 1);
			drawArc(left + rad, bottom - rad, rad, 2);
			drawArc(right - rad, bottom - rad, rad, 3);
			drawArc(right - rad, top + rad, rad, 4);
		} else {
			drawRect(left, top, right, bottom);
		}
	}

	public static void drawArcRect(double left, double top, double right, double bottom, int color) {
		drawArcRect(left, top, right, bottom, color, 0, 0, 0);
	}

	public static void drawArcRect(double left, double top, double right, double bottom, int color, int shadow) {
		drawArcRect(left, top, right, bottom, color, shadow, 0, 0);
	}

	public static void drawArcRect(double left, double top, double right, double bottom, int color, int shadow,
			int border) {
		drawArcRect(left, top, right, bottom, color, shadow, border, 0);
	}
	
	public static void drawArcRect(double left, double top, double right, double bottom, int color, int shadow,
			int border, int round) {
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
			drawRoundRect(left + 0.5, top + 0.5, right + 0.5, bottom + 0.5, round);
		}

		if(border != 0) {
			bindColor(border);
			drawRoundRect(left, top, right, bottom, round);
			bindColor(color);
			drawRoundRect(left + 0.5, top + 0.5, right - 0.5, bottom - 0.5, round);
		} else {
			bindColor(color);
			drawRoundRect(left, top, right, bottom, round);
		}

		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
	}

	public static void drawArea(double x, double y, double width, double height, int color) {
		drawArcRect(x, y, x + width, y + height, color);
	}

	public static void drawArea(double x, double y, double width, double height, int color, int shadow) {
		drawArcRect(x, y, x + width, y + height, color, shadow);
	}

	public static void drawArea(double x, double y, double width, double height, int color, int shadow, int border) {
		drawArcRect(x, y, x + width, y + height, color, shadow, border);
	}

	public static void drawArea(double x, double y, double width, double height, int color, int shadow, int border,
			int round) {
		drawArcRect(x, y, x + width, y + height, color, shadow, border, round);
	}

	public static FontRenderer getDefaultASCIIRenderer() {
		if (ascii_font_renderer == null) {
			ascii_font_renderer = new ZWSPFixedFontRenderer(Minecraft.getMinecraft(), false);
		}
		return ascii_font_renderer;
	}
	
	public static int drawText(FontRenderer fr, String text, float x, float y, int color, int shadow) {
		return drawText(fr, text, x, y, color, shadow, 0);
	}
	
	public static int drawText(FontRenderer fr, String text, float x, float y, int color, int shadow, int arrange) {
		return drawText(fr, text, x, y, color, shadow, arrange, Integer.MAX_VALUE);
	}
	
	public static int drawText(FontRenderer fr, String text, float x, float y, int color, int shadow, int arrange, int wrapWidth) {
		int len = fr.getStringWidth(text);
		if (arrange > 0) {
			if ((arrange & CENTER_H) > 0) {
				int w = wrapWidth;
				if(wrapWidth >= len) {
					w = fr.getStringWidth(text);
				}
				x = x - w / 2f;
			}
			if ((arrange & CENTER_V) > 0) {
				int h = fr.FONT_HEIGHT;
				if(wrapWidth < len) {
					h = fr.listFormattedStringToWidth(text, wrapWidth).size() * fr.FONT_HEIGHT;
				}
				y = y - h / 2f;
			}
		}

		float tx = x - ((int) x);
		float ty = y - ((int) y);
		
		if (shadow != 0) {
			if(wrapWidth < len) {
				GlStateManager.translate(tx + 0.5f, ty + 0.5f, 0);
				fr.drawSplitString(text, (int) x, (int) y, wrapWidth, shadow);
				GlStateManager.translate(-(tx + 0.5f), -(ty + 0.5f), 0);
			} else {
				fr.drawString(text, x + 0.5f, y + 0.5f, shadow, false);
			}
			len++;
		}
		
		if(wrapWidth < len) {
			GlStateManager.translate(tx, ty, 0);
			fr.drawSplitString(text, (int) x, (int) y, wrapWidth, color);
			GlStateManager.translate(-tx, -ty, 0);
		} else {
			fr.drawString(text, x, y, color, false);
		}
		
		return len;
	}
}
