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
	public static final int TEXT_CENTER_HORIZONTAL = 1;
	public static final int TEXT_CENTER_VERTICAL = 2;
	
	private static final UIContext context = new UIContext();
	private static FontRenderer ascii_font_renderer = null;
	private static boolean isAutoReset = true;

	// ================================= Core =================================

	public static void useRound(int radius) {
		context.useRound(radius);
	}
	
	public static void useShadow(int shadow) {
		context.useShadow(shadow);
	}
	
	public static void useTextCenter(boolean horizontal, boolean vertical) {
		context.useTextCenter(horizontal, vertical);
	}
	
	public static void useTextCenterArea(int width, int height) {
		context.useTextCenterArea(width, height);
	}
	
	
	public static void useCustomContext(UIContext ctx) {
		context.copy(ctx);
	}
	
	// maintain UI context attributes until leaving context.
	public static void enterContext() {
		isAutoReset = false;
	}
	
	public static void leaveContext() {
		isAutoReset = true;
	}
	
	// ================================= Drawing Utils =================================
	
	private static void bindColor(int color) {
		float f3 = (float) (color >> 24 & 255) / 255.0F;
		float f = (float) (color >> 16 & 255) / 255.0F;
		float f1 = (float) (color >> 8 & 255) / 255.0F;
		float f2 = (float) (color & 255) / 255.0F;
		GlStateManager.color(f, f1, f2, f3);
	}
	
	private static void drawRectImpl(double left, double top, double right, double bottom) {
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
		drawRectImpl(left + rad, top, right - rad, bottom);
		drawRectImpl(left, top + rad, left + rad, bottom - rad);
		drawRectImpl(left + rad, top + rad, right, bottom - rad);
		drawArc(left + rad, top + rad, rad, 1);
		drawArc(left + rad, bottom - rad, rad, 2);
		drawArc(right - rad, bottom - rad, rad, 3);
		drawArc(right - rad, top + rad, rad, 4);
	}
	
	public static void drawRect(double left, double top, double right, double bottom, int color) {
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
		
		if(context.shadowColor != -1) {
			bindColor(context.shadowColor);
			if(context.rectRound > 0) {
				drawRoundRect(left + 1, top + 1, right + 1, bottom + 1, context.rectRound);
			} else {
				drawRectImpl(left + 1, top + 1, right + 1, bottom + 1);
			}
			if(isAutoReset) {
				context.shadowColor = -1;
			}
		}
		
		bindColor(color);
		if(context.rectRound > 0) {
			drawRoundRect(left, top, right, bottom, context.rectRound);
			if(isAutoReset) {
				context.rectRound = -1;
			}
		} else {
			drawRectImpl(left, top, right, bottom);
		}
		
		GlStateManager.enableTexture2D();
		GlStateManager.disableBlend();
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
		int aw = 0;
		int ah = 0;
		
		if(context.areaWidth + context.areaHeight > 0) {
			aw = context.areaWidth;
			ah = context.areaHeight;
			if(isAutoReset) {
				context.areaWidth = 0;
				context.areaHeight = 0;
			}
		}
		
		if(context.textFlag > 0) {
			if((context.textFlag & TEXT_CENTER_HORIZONTAL) > 0) {
				if(aw > 0) {
					x = x + (aw - fr.getStringWidth(text)) / 2;
				} else {
					x = x + fr.getStringWidth(text) / 2;
				}
			}
			
			if((context.textFlag & TEXT_CENTER_VERTICAL) > 0) {
				if(ah > 0) {
					y = y + (ah - fr.FONT_HEIGHT) / 2;
				} else {
					y = y + fr.FONT_HEIGHT / 2;
				}
			}
			
			if(isAutoReset) {
				context.textFlag = 0;
			}
		}
		
		if(context.shadowColor != -1) {
			fr.drawString(text, x + 0.5f, y + 0.5f, context.shadowColor, false);
			if(isAutoReset) {
				context.shadowColor = -1;
			}
		}
		
		fr.drawString(text, x, y, color, false);
	}
	
	public static class UIContext {
		public int shadowColor = -1;
		public int rectRound = -1;
		public int areaWidth = 0;
		public int areaHeight = 0;
		public int textFlag = 0;
		
		public void useRound(int radius) {
			rectRound = radius;
		}
		
		public void useShadow(int shadow) {
			if(shadow == -1) {
				shadowColor = (shadow & 16579836) >> 2 | shadow & -16777216;
			} else {
				shadowColor = shadow;
			}
		}

		public void useTextCenterArea(int width, int height) {
			areaWidth = width;
			areaHeight = height;
		}
		
		public void useTextCenter(boolean horizontal, boolean vertical) {
			int h = horizontal ? TEXT_CENTER_HORIZONTAL : 0;
			int v = vertical ? TEXT_CENTER_VERTICAL : 0;
			textFlag |= (h | v);
		}
		
		public void copy(UIContext ctx) {
			this.rectRound = ctx.rectRound;
			this.shadowColor = ctx.shadowColor;
			this.areaWidth = ctx.areaWidth;
			this.areaHeight = ctx.areaHeight;
			this.textFlag = ctx.textFlag;
		}
	}
}
