package won983212.kpatch.ui.indicators;

import won983212.kpatch.ui.Theme;
import won983212.kpatch.ui.UIUtils;

public class GuiHanjaSelector extends GuiPopup {
	public static final int HEIGHT = 360;
	
	public void select(char code) {
		setVisible(false);
	}
	
	protected void renderPopup(int x, int y) {
		// background
		UIUtils.useShadow(Theme.BACKGROUND_SHADOW);
		UIUtils.drawArea(x, y, 100, HEIGHT, Theme.BACKGROUND);
	}
}
