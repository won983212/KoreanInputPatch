package won983212.kpatch.ui.popups;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import won983212.kpatch.Configs;
import won983212.kpatch.input.Korean2Input;

//TODO Implement indicator Process: with animation
public class GuiKoreanIndicator {
	public static void drawIndicator(int x, int y, int len, int maxLen) {
		drawIndicator(x, y, len, maxLen, "자");
	}

	public static void drawIndicator(int x, int y, int len, int maxLen, String unit) {
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		final int height = fr.FONT_HEIGHT + 2;

		if (!Configs.getBoolean(Configs.IME_INDICATOR_VISIBLE)) { // 0이면 무조건 렌더링 안함
			return;
		}

		String idiText = (Korean2Input.isKorMode() ? "한글" : "영문");
		int textWidth = fr.getStringWidth(idiText);

		/*final String alertText = (maxLen == len) ? "꽉참" : (maxLen - len + unit);
		final int alertBg = (maxLen == len) ? 0xffe53935 : 0xfffb8c00;*/
		
		String alertText;
		int alertBg;
		
		if(maxLen == len) {
			alertText = "꽉참";
			alertBg = 0xffe53935;
		} else {
			alertText = maxLen - len + unit;
			alertBg = 0xfffb8c00;
		}

		// kor indicator bg
		Gui.drawRect(x, y, x + textWidth + 4, y + height, Korean2Input.isKorMode() ? 0xff1E88E5 : 0xffE53935);

		if (alertText != null) {
			final int alertX = x + textWidth + 6;
			final int alertWidth = fr.getStringWidth(alertText) + 8;

			// alert decorator
			Gui.drawRect(alertX, y, alertX + 2, y + height, alertBg);

			// alert bg
			Gui.drawRect(alertX + 2, y, alertX + alertWidth, y + height, 0xffffffff);

			// alert text
			fr.drawString(alertText, alertX + 5, y + 1, 0xff000000);
		}

		// kor indicator text
		fr.drawStringWithShadow(idiText, x + 2, y + 1, 0xffffffff);

		/*
		 * final String _alertText = alertText; final int _alertBg = alertBg;
		 * KoreanInputPatch.instance.getEventHandler().addTopRenderQueue(new Runnable()
		 * {
		 * 
		 * @Override public void run() { Gui.drawRect(x, y, x + textWidth + 4, y +
		 * height, Korean2Input.isKorMode() ? 0xff1E88E5 : 0xffE53935); if(_alertText !=
		 * null) { final int alertX = x + textWidth + 6; Gui.drawRect(alertX, y, alertX
		 * + 2, y + height, _alertBg); Gui.drawRect(alertX + 2, y, alertX +
		 * fr.getStringWidth(_alertText) + 4, y + height, _alertBg);
		 * fr.drawString(_alertText, alertX + 2, y + 1, 0xff000000); }
		 * fr.drawString(idiText, x + 2, y + 1, 0xffffffff); } });
		 */
	}
}