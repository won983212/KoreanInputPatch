package won983212.kpatch.ui.indicators;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import won983212.kpatch.ui.Theme;
import won983212.kpatch.ui.UIUtils;

public class GuiColorSelector {
	private boolean show = false;
	
	public boolean isShow() {
		return show;
	}
	
	public void setVisible(boolean visible) {
		this.show = visible;
	}
	
	public void select(char code) {
		setVisible(false);
	}
	
	public void drawIndicator(int x, int y) {
		if(!show) return;

		FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
		final int colorPanelSize = fr.FONT_HEIGHT + 2;
		final int margin = 2;
		final int columns = 6;
		final int rows = 4;
		final int gap = 3;
		
		final String code = "0123456789abcdefklmnor";
		final int[] color = {
			0, 0x0000aa, 0x00aa00, 0x00aaaa, 0xaa0000, 0xaa00aa, 0xffaa00,
			0xaaaaaa, 0x555555, 0x5555ff, 0x55ff55, 0x55ffff, 0xff5555, 0xff55ff,
			0xffff55, 0xffffff
		};
		
		final int width = columns * (colorPanelSize + gap) - gap + margin * 2;
		final int height = rows * (colorPanelSize + gap) - gap + margin * 2;
		UIUtils.drawArea(x, y, width, height, Theme.BACKGROUND);
		
		int k = 0;
		for (int j = 0; j < rows - 1; j++) {
			for (int i = 0; i < columns; i++) {
				if(k >= color.length)
					break;
				int px = x + margin + i * (colorPanelSize + gap);
				int py = y + margin + j * (colorPanelSize + gap);
				UIUtils.drawRectDouble(px - 0.5, py - 0.5, px + colorPanelSize + 0.5, py + colorPanelSize + 0.5, 0xff666666);
				UIUtils.drawArea(px, py, colorPanelSize, colorPanelSize, color[k] | 0xff000000);
				UIUtils.drawCentralText(fr, String.valueOf(code.charAt(k)), px, py, colorPanelSize, colorPanelSize, k > 9 ? 0xff000000 : 0xffffffff);
				k++;
			}
		}
		
		k = 16;
		for (int i = 0; i < columns && k < code.length(); i++) {
			int px = x + margin + i * (colorPanelSize + gap);
			int py = y + margin + (rows - 1) * (colorPanelSize + gap);
			char c = code.charAt(k);

			UIUtils.drawRectDouble(px - 0.5, py - 0.5, px + colorPanelSize + 0.5, py + colorPanelSize + 0.5, 0xff666666);
			UIUtils.drawArea(px, py, colorPanelSize, colorPanelSize, 0xffdddddd);
			UIUtils.drawCentralText(fr, "§" + c + c, px, py, colorPanelSize, colorPanelSize, k > 9 ? 0xff000000 : 0xffffffff);
			k++;
		}
	}
}
