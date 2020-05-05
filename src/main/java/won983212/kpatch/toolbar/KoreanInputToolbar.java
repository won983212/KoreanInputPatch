package won983212.kpatch.toolbar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import won983212.kpatch.inputengines.KoreanInputContext;
import won983212.kpatch.wrapper.TextfieldWrapper;

public class KoreanInputToolbar extends TopViewToolbar {
	@Override
	public void renderToolbar(IToolbarContainer c) {
		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		TextfieldWrapper wrapper = (TextfieldWrapper) c;

		int x = wrapper.getX() - 1;
		int y = wrapper.getY() - fr.FONT_HEIGHT - 4;
		int height = fr.FONT_HEIGHT + 2;
		boolean outbounds = wrapper.getY() < fr.FONT_HEIGHT + 6;
		if (outbounds) {
			y = wrapper.getY() + wrapper.getHeight() + 2;
		}
		if (!wrapper.getEnableBackgroundDrawing()) {
			x -= 1;
			y += outbounds ? 2 : -2;
		}
		
		int len = wrapper.getText().length();
		int maxLen = wrapper.getMaxStringLength();
		String alertText = null;
		int alertBg = 0;
		if(maxLen == len) {
			alertBg = 0xffe53935;
			alertText = "꽉참";
		} else if(maxLen - 10 < len) {
			alertBg = 0xfffb8c00;
			alertText = maxLen - len + "자";
		}

		String idiText = (KoreanInputContext.isKorMode() ? "한" : "영");
		int textWidth = fr.getStringWidth(idiText);
		
		Gui.drawRect(x, y, x + 2, y + height, KoreanInputContext.isKorMode() ? 0xff1E88E5 : 0xffE53935);
		Gui.drawRect(x + 2, y, x + textWidth + 6, y + height, 0xffffffff);
		if(alertText != null) {
			final int alertX = x + textWidth + 7;
			Gui.drawRect(alertX, y, alertX + fr.getStringWidth(alertText) + 4, y + height, alertBg);
			fr.drawString(alertText, alertX + 2, y + 1, 0xffffffff);
		}
		fr.drawString(idiText, x + 4, y + 1, 0xff000000);
	}
}
