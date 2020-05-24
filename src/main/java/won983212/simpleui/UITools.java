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
	
	private static final UIContext context = new UIContext();
	private static FontRenderer ascii_font_renderer = null;
	private static boolean isAutoReset = true;

	// ================================= Core =================================
	
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
	
	public static void drawRect(double left, double top, double right, double bottom, int color) {
		ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
		int factor = sr.getScaleFactor();
		double revFactor = 1.0 / factor;

		GlStateManager.scale(revFactor, revFactor, revFactor);
		if(context.shadowColor != -1) {
			Gui.drawRect((int) ((left + 1) * factor), (int) ((top + 1) * factor), (int) ((right + 1) * factor), (int) ((bottom + 1) * factor), context.shadowColor);
			if(isAutoReset) {
				context.shadowColor = -1;
			}
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
		public int areaWidth = 0;
		public int areaHeight = 0;
		public int textFlag = 0;
		
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
			this.shadowColor = ctx.shadowColor;
			this.areaWidth = ctx.areaWidth;
			this.areaHeight = ctx.areaHeight;
			this.textFlag = ctx.textFlag;
		}
	}
}
