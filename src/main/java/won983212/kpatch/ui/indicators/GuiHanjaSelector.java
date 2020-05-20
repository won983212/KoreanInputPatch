package won983212.kpatch.ui.indicators;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import won983212.kpatch.Hanja;
import won983212.kpatch.ui.Theme;
import won983212.kpatch.ui.UIUtils;
import won983212.kpatch.ui.animation.DecimalAnimation;

public class GuiHanjaSelector extends GuiPopup {
	private static final int titleHeight = 20;
	private static final int gap = 3;
	public static final int HEIGHT = 132;
	
	private int prevPage = 0;
	private DecimalAnimation pageAnimation = new DecimalAnimation(1000);
	
	protected void renderPopup(int x, int y, Object[] args) {
		final char key = (char) args[0];
		final int page = (int) args[1];
		final String pageText = (String) args[2];
		final Hanja[] hanjas = (Hanja[]) args[3];

		int width = 0;
		final FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

		for (int i = 0; i < 9; i++) {
			int idx = (page - 1) * 9 + i;
			if(idx >= hanjas.length)
				break;

			width = Math.max(width, x + 10 + gap * 2 + fr.getStringWidth(hanjas[idx].meaning));
			width = Math.max(50, width);
		}
		
		// background
		UIUtils.useShadow(Theme.BACKGROUND_SHADOW);
		UIUtils.drawArea(x, y, width, HEIGHT, Theme.BACKGROUND);
		
		// title background
		UIUtils.drawArea(x, y, width, titleHeight, Theme.PRIMARY);
		
		// title
		GlStateManager.scale(2, 2, 2);
		final int keyX = (x + gap + 1) / 2;
		final int keyY = (y - fr.FONT_HEIGHT + titleHeight / 2) / 2;
		fr.drawStringWithShadow(String.valueOf(key), keyX, keyY, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 0.5);
		
		// page indicator
		final int pageX = x + width - fr.getStringWidth(pageText) - gap;
		final int pageY = y - fr.FONT_HEIGHT + titleHeight - gap / 2;
		fr.drawStringWithShadow(pageText, pageX, pageY, 0xffffffff);
		
		double pageAni = pageAnimation.update();
		if(pageAnimation.isRunning()) {
			renderPage(fr, x, y, prevPage, hanjas);
			UIUtils.drawArea(x, y, width * pageAni, HEIGHT, Theme.BACKGROUND);
		}
		
		renderPage(fr, x, y, page, hanjas);
	}
	
	private void renderPage(FontRenderer fr, int x, int y, int page, Hanja[] hanjas) {
		for (int i = 0; i < 9; i++) {
			int idx = (page - 1) * 9 + i;
			if(idx >= hanjas.length)
				break;
			
			int py = y + titleHeight + gap + i * (fr.FONT_HEIGHT + gap) + 1;
			UIUtils.useShadow(0xff999999);
			UIUtils.drawText(fr, String.valueOf(i + 1), x + gap, py, 0xff000000);
			fr.drawString(String.valueOf(hanjas[idx].hanja), x + 9 + gap * 2, py, 0xff000000);
			fr.drawString(hanjas[idx].meaning, x + 23 + gap * 2, py, 0xff000000);
		}
	}

	public void animatePage(boolean next, int prevPage) {
		this.prevPage = prevPage;
		pageAnimation.play();
	}
}
