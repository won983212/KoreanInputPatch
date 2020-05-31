package won983212.simpleui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;

public abstract class UIControl<T> extends UIComponent<T> {
	protected final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
	protected int backgroundColor = Theme.PRIMARY;
	protected int foregroundColor = Theme.WHITE;
	protected int shadow = 0;
	protected int radius = 0;
	protected int textArrange = 0;

	public T setBackgroundColor(int color) {
		this.backgroundColor = color;
		return (T) this;
	}

	public T setForegroundColor(int color) {
		this.foregroundColor = color;
		return (T) this;
	}

	public T setShadow(int color) {
		this.shadow = color;
		return (T) this;
	}

	public T setRadius(int radius) {
		this.radius = radius;
		return (T) this;
	}

	public T setTextCenter(boolean horizontal, boolean vertical) {
		this.textArrange = 0;
		if (horizontal)
			this.textArrange |= UITools.CENTER_H;
		if (vertical)
			this.textArrange |= UITools.CENTER_V;
		return (T) this;
	}

	public void onKeyTyped(char typedChar, int keyCode) {
	}

	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		return false;
	}

	public void onMouseReleased(int mouseX, int mouseY, int state) {
	}

	public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
	}
}
