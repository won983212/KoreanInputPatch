package won983212.simpleui.indicators;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import won983212.kpatch.KoreanInputPatch;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;

public class GuiColorSelector extends GuiIndicator {
	private static final String code = "0123456789abcdefklmnor";
	private static final int[] color = {
		0x000000, 0x0000aa, 0x00aa00, 0x00aaaa, 0xaa0000, 0xaa00aa, 0xffaa00, 0xaaaaaa,	// 0 1 2 3 4 5 6 7
		0x555555, 0x5555ff, 0x55ff55, 0x55ffff, 0xff5555, 0xff55ff,	0xffff55, 0xffffff, // 8 9 a b c d e f
		0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff						// k l m n o r
	};
	
	private static final int colorPanelSize = 14;
	private static final int margin = 5;
	private static final int columns = 6;
	private static final int rows = 4;
	private static final int gap = 3;
	private static final int borderColor = Theme.DARK_GRAY;
	
	public GuiColorSelector() {
		this.width = columns * (colorPanelSize + gap) - gap + margin * 2;
		this.height = rows * (colorPanelSize + gap) - gap + margin * 2;
	}
	
	public void draw(int x, int y) {
		KoreanInputPatch.instance.getEventHandler().addTopRenderQueue(() -> {
			FontRenderer fr = UITools.getDefaultASCIIRenderer();
			
			// background
			UITools.drawArea(x, y, width, height, Theme.BACKGROUND, Theme.BACKGROUND_SHADOW, 0);
			
			int k = 0;
			for (int j = 0; j < rows; j++) {
				for (int i = 0; i < columns; i++) {
					if(k > 15 && j != rows - 1) break;
					
					int px = x + margin + i * (colorPanelSize + gap);
					int py = y + margin + j * (colorPanelSize + gap);
					char c = code.charAt(k);
					
					// border
					UITools.drawArea(px, py, colorPanelSize, colorPanelSize, borderColor, 0, 0);
					
					// color panel
					UITools.drawArcRect(px + 0.5, py + 0.5, px + colorPanelSize - 0.5, py + colorPanelSize - 0.5, color[k] | 0xff000000, 0, 0);
					if(k > 15) {
						UITools.drawText(fr, "§" + c + "A", px + colorPanelSize / 2, py + colorPanelSize / 2 + 1, Theme.BLACK, 0, UITools.CENTER_BOTH);
					}
					
					// border label background
					UITools.drawArcRect(px + colorPanelSize - 4.5, py + colorPanelSize - 5, px + colorPanelSize, py + colorPanelSize, borderColor, 0, 0);
					
					// border label
					GlStateManager.scale(0.5, 0.5, 0.5);
					fr.drawString(String.valueOf(c), (px + colorPanelSize) * 2 - 7, (py + colorPanelSize) * 2 - 9, Theme.WHITE);
					GlStateManager.scale(2, 2, 2);
					
					k++;
				}
			}
		});
	}
}
