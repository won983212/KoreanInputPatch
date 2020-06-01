package won983212.simpleui.indicators;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import won983212.kpatch.Hanja;
import won983212.kpatch.KoreanInputPatch;
import won983212.simpleui.UITools;
import won983212.simpleui.Theme;

public class GuiHanjaSelector extends GuiIndicator {
	private static final int TITLE_HEIGHT = 20;
	private static final int GAP = 3;
	
	private char key;
	private int page;
	private String pageText;
	private Hanja[] hanjas;
	
	public void setHanjaData(char key, int page, Hanja[] hanjas) {
		this.key = key;
		this.page = page;
		this.pageText = page + "/" + (int) Math.ceil(hanjas.length / 9.0);
		this.hanjas = hanjas;
		this.width = 0;

		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		int pageContentLength = 0;
		
		for (int i = 0; i < 9; i++) {
			int idx = (page - 1) * 9 + i;
			if(idx >= hanjas.length)
				break;

			pageContentLength++;
			width = Math.max(width, 30 + GAP * 2 + fr.getStringWidth(hanjas[idx].meaning));
			width = Math.max(50, width);
		}
		
		this.height = TITLE_HEIGHT + GAP + pageContentLength * (fr.FONT_HEIGHT + GAP) + 1;
	}
	
	public void draw(int x, int y) {
		KoreanInputPatch.instance.getEventHandler().addTopRenderQueue(() -> {
			FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
			
			// background
			UITools.drawArea(x, y, width, height, Theme.BACKGROUND, Theme.BACKGROUND_SHADOW, 0);
			
			// title background
			UITools.drawArea(x, y, width, TITLE_HEIGHT, Theme.PRIMARY, 0, 0);
			
			// title
			GlStateManager.scale(2, 2, 2);
			final int keyX = (x + GAP + 1) / 2;
			final int keyY = (y - fr.FONT_HEIGHT + TITLE_HEIGHT / 2) / 2;
			fr.drawStringWithShadow(String.valueOf(key), keyX, keyY, Theme.WHITE);
			GlStateManager.scale(0.5, 0.5, 0.5);
			
			// page indicator
			final int pageX = x + width - fr.getStringWidth(pageText) - GAP;
			final int pageY = y - fr.FONT_HEIGHT + TITLE_HEIGHT - GAP / 2;
			fr.drawStringWithShadow(pageText, pageX, pageY, Theme.WHITE);
			
			// page
			for (int i = 0; i < 9; i++) {
				int idx = (page - 1) * 9 + i;
				if(idx >= hanjas.length)
					break;
				
				int py = y + TITLE_HEIGHT + GAP + i * (fr.FONT_HEIGHT + GAP) + 1;
				UITools.drawText(fr, String.valueOf(i + 1), x + GAP, py, Theme.BLACK, Theme.GRAY, 0);
				fr.drawString(String.valueOf(hanjas[idx].hanja), x + 9 + GAP * 2, py, Theme.BLACK);
				fr.drawString(hanjas[idx].meaning, x + 23 + GAP * 2, py, Theme.BLACK);
			}
		});
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
