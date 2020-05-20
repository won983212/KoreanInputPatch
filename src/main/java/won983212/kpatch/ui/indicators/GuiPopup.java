package won983212.kpatch.ui.indicators;

import won983212.kpatch.KoreanInputPatch;
import won983212.kpatch.ui.Theme;
import won983212.kpatch.ui.UIUtils;

public abstract class GuiPopup {
	private boolean show = false;
	
	public boolean isShow() {
		return show;
	}
	
	public void setVisible(boolean visible) {
		this.show = visible;
	}
	
	public void draw(int x, int y, Object... args) {
		if(show) {
			renderPopup(x, y, args);
		}
	}
	
	protected abstract void renderPopup(int x, int y, Object[] args);
}
