package won983212.simpleui.indicators;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.renderer.GlStateManager;
import won983212.kpatch.Configs;
import won983212.kpatch.KoreanInputPatch;
import won983212.kpatch.input.KoreanInput;
import won983212.simpleui.UITools;
import won983212.simpleui.Theme;
import won983212.simpleui.animation.AnimationBase;
import won983212.simpleui.animation.ColorAnimation;
import won983212.simpleui.animation.DecimalAnimation;

public class GuiKoreanIndicator {
	public static final int HEIGHT = 11;
	private DecimalAnimation alertBoomingAnimation = new DecimalAnimation(150);
	private DecimalAnimation alertWidthAnimation = new DecimalAnimation(200);
	private DecimalAnimation modeChangeAnimation = new DecimalAnimation(200);
	private ColorAnimation modeBgColorAnimation = new ColorAnimation(400, Theme.BACKGROUND, 0);
	private String alertText;
	private int alertBg;
	private boolean prevKrMode = KoreanInput.isKorMode();
	private int prevLen = 0;

	public GuiKoreanIndicator() {
		alertBoomingAnimation.setCompileType(AnimationBase.COMPILE_MOUNTAIN);
		modeBgColorAnimation.setCompileType(AnimationBase.COMPILE_MOUNTAIN);
	}
	
	public void drawIndicator(int x, int y, int len, int maxLen) {
		drawIndicator(x, y, len, maxLen, "자");
	}

	public void drawIndicator(int x, int y, int len, int maxLen, String unit) {
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		final int conf = Configs.getInt(Configs.IME_INDICATOR_VISIBLE_MODE);
		final boolean useAnimation = Configs.getBoolean(Configs.IME_INDICATOR_ANIMATE);

		if (conf == 0) { // 0이면 무조건 렌더링 안함
			return;
		} else if (conf == 1 && !(Minecraft.getMinecraft().currentScreen instanceof GuiChat)) { // 1이면 채팅 ui에서만 렌더링
			return;
		} // 2이상은 무조건 렌더링

		boolean kr = KoreanInput.isKorMode();
		String idiText = (kr ? "한글" : "영문");
		int textWidth = fr.getStringWidth(idiText);

		if (useAnimation && kr != prevKrMode) {
			modeChangeAnimation.play();
			modeBgColorAnimation.play();
			modeBgColorAnimation.setEndColor(kr ? Theme.adjColor(Theme.PRIMARY, 70) : Theme.adjColor(Theme.SECONDARY, 70));
			prevKrMode = kr;
		}

		if (maxLen == len) {
			alertText = "꽉참";
			alertBg = Theme.DANGER;
		} else if (len > maxLen * 0.7) {
			if (useAnimation) {
				if (alertText == null) {
					alertWidthAnimation.play();
				}
				if (maxLen - len < 7 && prevLen != len) {
					alertBoomingAnimation.play();
					prevLen = len;
				}
			}
			alertText = maxLen - len + unit;
			alertBg = Theme.WARN;
		} else {
			alertText = null;
			alertBg = -1;
		}

		KoreanInputPatch.instance.getEventHandler().addTopRenderQueue(() -> {
			// kor indicator bg
			if(modeBgColorAnimation.isRunning()) {
				UITools.drawArea(x, y, textWidth + 8, HEIGHT, modeBgColorAnimation.update());
			} else {
				UITools.drawArea(x, y, textWidth + 8, HEIGHT, Theme.BACKGROUND);
			}
	
			// kor indicator badge
			if (modeChangeAnimation.isRunning()) {
				UITools.drawArea(x, y, 2, HEIGHT, !kr ? Theme.PRIMARY : Theme.SECONDARY);
				UITools.drawArea(x, y, 2, (int) (HEIGHT * modeChangeAnimation.update()), kr ? Theme.PRIMARY : Theme.SECONDARY);
			} else {
				UITools.drawArea(x, y, 2, HEIGHT, kr ? Theme.PRIMARY : Theme.SECONDARY);
			}
	
			if (alertText != null) {
				final int alertX = x + textWidth + 10;
				final int alertWidth = 26;
	
				// alert bg
				double p = useAnimation ? alertWidthAnimation.update() : 1;
				UITools.drawRect(alertX, y, alertX + alertWidth * p, y + HEIGHT, alertBg);
	
				// alert text
				if (p > 0.9) {
					double scale = useAnimation ? 1 + alertBoomingAnimation.update() * 0.3 : 1;
					int alertTextX = (int) ((alertX + (alertWidth - fr.getStringWidth(alertText) * scale) / 2) / scale);
					int alertTextY = (int) ((y + (HEIGHT - fr.FONT_HEIGHT * scale) / 2) / scale);

					if (useAnimation) {
						GlStateManager.pushMatrix();
						GlStateManager.scale(scale, scale, scale);
					}
					
					fr.drawStringWithShadow(alertText, alertTextX, alertTextY, Theme.WHITE);
					
					if (useAnimation) {
						GlStateManager.popMatrix();
					}
				}
			}
	
			// kor indicator text
			int textX = x + 2 + (textWidth + 6 - fr.getStringWidth(idiText)) / 2;
			UITools.useShadow(Theme.LIGHT_GRAY);
			UITools.drawText(fr, idiText, textX, y + 1, Theme.BLACK);
		});
	}
}
