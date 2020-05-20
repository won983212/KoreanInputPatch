package won983212.kpatch.ui.indicators;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import won983212.kpatch.ui.Theme;
import won983212.kpatch.ui.UIUtils;

// TODO 한자 인디케이터 미완성
public class GuiHanjaSelector extends GuiPopup {
	private static final int titleHeight = 20;
	private static final int gap = 3;
	public static final int HEIGHT = 142;
	
	public void select(char code) {
		setVisible(false);
	}
	
	protected void renderPopup(int x, int y) {
		final int width = 65;
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;

		// background
		UIUtils.useShadow(Theme.BACKGROUND_SHADOW);
		UIUtils.drawArea(x, y, width, HEIGHT, Theme.BACKGROUND);
		
		UIUtils.drawArea(x, y, width, titleHeight, Theme.PRIMARY);
		
		GlStateManager.scale(2, 2, 2);
		fr.drawStringWithShadow("ㄷ", (x + gap + 1) / 2, (y - fr.FONT_HEIGHT + titleHeight / 2) / 2, 0xffffffff);
		GlStateManager.scale(0.5, 0.5, 0.5);

		final int pageX = x + width - fr.getStringWidth("1/3") - gap;
		final int pageY = y - fr.FONT_HEIGHT + titleHeight - gap / 2;
		fr.drawStringWithShadow("1/3", pageX, pageY, 0xffffffff);
		
		for (int i = 0; i < 10; i++) {
			int py = y + titleHeight + gap + i * (fr.FONT_HEIGHT + gap);
			fr.drawString(String.valueOf(i), x + gap, py, 0xff000000);
			fr.drawString("家", x + 8 + gap * 2, py, 0xff000000);
			fr.drawString("집지었던", x + 22 + gap * 2, py, 0xff000000);
		}
	}
}
