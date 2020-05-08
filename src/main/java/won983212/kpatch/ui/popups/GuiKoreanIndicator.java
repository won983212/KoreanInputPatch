package won983212.kpatch.ui.popups;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import won983212.kpatch.Configs;
import won983212.kpatch.input.Korean2Input;
import won983212.kpatch.ui.UIUtils;
import won983212.kpatch.ui.animation.ColorAnimation;
import won983212.kpatch.ui.animation.IntAnimation;
import won983212.kpatch.ui.animation.RatioAnimation;

//TODO Implement indicator Process: with animation
public class GuiKoreanIndicator {
	private RatioAnimation alertWidthAnimation = new RatioAnimation(100);
	private ColorAnimation modeColorAnimation = new ColorAnimation(0xff1E88E5, 0xffE53935, 100);
	private String alertText;
	private int alertBg;
	private boolean prevKrMode = Korean2Input.isKorMode();

	public GuiKoreanIndicator() {
		modeColorAnimation.setReverse(prevKrMode);
	}
	
	public void drawIndicator(int x, int y, int len, int maxLen) {
		drawIndicator(x, y, len, maxLen, "자");
	}

	public void drawIndicator(int x, int y, int len, int maxLen, String unit) {
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		final int height = fr.FONT_HEIGHT + 2;
		final boolean useAnimation = Configs.getBoolean(Configs.IME_INDICATOR_ANIMATE);

		if (!Configs.getBoolean(Configs.IME_INDICATOR_VISIBLE)) { // 0이면 무조건 렌더링 안함
			return;
		}

		boolean kr = Korean2Input.isKorMode();
		String idiText = (kr ? "한글" : "영문");
		int textWidth = fr.getStringWidth(idiText);
		
		if(useAnimation && kr != prevKrMode) {
			modeColorAnimation.setReverse(kr);
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

		// kor indicator bg
		int indicatorColor = useAnimation ? modeColorAnimation.update() : (kr ? 0xff1E88E5 : 0xffE53935);
		Gui.drawRect(x, y, x + textWidth + 4, y + height, indicatorColor);

		if (alertText != null) {
			final int alertX = x + textWidth + 6;
			final int alertWidth = fr.getStringWidth(alertText) + 8;

			// alert decorator
			Gui.drawRect(alertX, y, alertX + 2, y + height, alertBg);

			// alert bg
			double p = useAnimation ? alertWidthAnimation.update() : 1;
			UIUtils.drawRectDouble(alertX + 2, y, alertX + alertWidth * p, y + height, 0xffffffff);

			// alert text
			if (p > 0.9) {
				fr.drawString(alertText, alertX + 5, y + 1, 0xff000000);
			}
		}

		// kor indicator text
		fr.drawStringWithShadow(idiText, x + 2, y + 1, 0xffffffff);
	}
}
