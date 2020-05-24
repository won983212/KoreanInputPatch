package won983212.simpleui.indicators;

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
