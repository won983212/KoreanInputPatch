package won983212.kpatch.ui.components;

import java.io.IOException;

public abstract class UIComponent<T> {
	protected int x;
	protected int y;
	protected int width;
	protected int height;
	
	public boolean isIn(int px, int py) {
		return px >= x && px <= x + width && py >= y && py <= y + height;
	}
	
	public T setLocation(int x, int y) {
		this.x = x;
		this.y = y;
		return (T) this;
	}
	
	public T setSize(int width, int height) {
		this.width = width;
		this.height = height;
		return (T) this;
	}
	
	public void onKeyTyped(char typedChar, int keyCode) {
	}

	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		return false;
	}

	public boolean onMouseReleased(int mouseX, int mouseY, int state) {
		return false;
	}

	public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
	}
	
	public abstract void draw(int mouseX, int mouseY, float partialTicks);
}
