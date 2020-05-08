package won983212.kpatch.ui.popups;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import won983212.kpatch.Configs;
import won983212.kpatch.KoreanInputPatch;
import won983212.kpatch.input.Korean2Input;
import won983212.kpatch.ui.UIUtils;
import won983212.kpatch.ui.animation.RatioAnimation;

//TODO Implement indicator Process: with animation
public class GuiKoreanIndicator {
	private static final int ENG_COLOR = 0xffE53935;
	private static final int KOR_COLOR = 0xff308FBF;

	private RatioAnimation alertWidthAnimation = new RatioAnimation(200);
	private RatioAnimation modeColorAnimation = new RatioAnimation(200);
	private String alertText;
	private int alertBg;
	private boolean prevKrMode = Korean2Input.isKorMode();

	public void drawIndicator(int x, int y, int len, int maxLen) {
		drawIndicator(x, y, len, maxLen, "자");
	}

	public void drawIndicator(int x, int y, int len, int maxLen, String unit) {
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		final int height = fr.FONT_HEIGHT + 2;
		final int conf = Configs.getInt(Configs.IME_INDICATOR_VISIBLE_MODE);
		final boolean useAnimation = Configs.getBoolean(Configs.IME_INDICATOR_ANIMATE);
		
		if(conf == 0) { // 0이면 무조건 렌더링 안함
			return;
		} else if(conf == 1 && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) { // 1이면 채팅 ui에서만 렌더링
			return;
		} // 2이상은 무조건 렌더링

		boolean kr = Korean2Input.isKorMode();
		String idiText = (kr ? "한글" : "영문");
		int textWidth = fr.getStringWidth(idiText);

		if (useAnimation && kr != prevKrMode) {
			modeColorAnimation.play();
			prevKrMode = kr;
		}

		if (maxLen == len) {
			alertText = "꽉참";
			alertBg = 0xffe53935;
		} else if (len > maxLen * 0.1) {
			if (useAnimation && alertText == null)
				alertWidthAnimation.play();
			alertText = maxLen - len + unit;
			alertBg = 0xfffb8c00;
		} else {
			alertText = null;
			alertBg = -1;
		}

		KoreanInputPatch.instance.getEventHandler().addTopRenderQueue(() -> {
			// Draw at top
			GlStateManager.translate(0, 0, 300);
			GlStateManager.disableLighting();
			
			// kor indicator bg
			Gui.drawRect(x, y, x + textWidth + 8, y + height, 0xffF2F2E6);
	
			// kor indicator badge
			if (modeColorAnimation.isRunning()) {
				Gui.drawRect(x, y, x + 2, y + height, !kr ? KOR_COLOR : ENG_COLOR);
				Gui.drawRect(x, y, x + 2, (int) (y + height * modeColorAnimation.update()), kr ? KOR_COLOR : ENG_COLOR);
			} else {
				Gui.drawRect(x, y, x + 2, y + height, kr ? KOR_COLOR : ENG_COLOR);
			}
	
			if (alertText != null) {
				final int alertX = x + textWidth + 10;
				final int alertWidth = 26;
	
				// alert bg
				double p = useAnimation ? alertWidthAnimation.update() : 1;
				UIUtils.drawRectDouble(alertX, y, alertX + alertWidth * p, y + height, alertBg);
	
				// alert text
				if (p > 0.9) {
					fr.drawStringWithShadow(alertText, alertX + (alertWidth - fr.getStringWidth(alertText)) / 2, y + 1, 0xffffffff);
				}
			}
	
			// kor indicator text
			int textX = x + 2 + (textWidth + 6 - fr.getStringWidth(idiText)) / 2;
			fr.drawString(idiText, textX + 0.5f, y + 1.5f, 0xffaaaaaa, false);
			fr.drawString(idiText, textX, y + 1, 0xff000000);
			
			GlStateManager.translate(0, 0, -300);
		});
	}
}
