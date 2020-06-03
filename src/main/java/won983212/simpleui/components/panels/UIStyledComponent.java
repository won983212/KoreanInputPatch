package won983212.simpleui.components.panels;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import won983212.kpatch.Configs;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;

/**
 * UIComponent에 스타일링이 포함된 클래스 
 */
public abstract class UIStyledComponent<T> extends UIComponent<T> {
	protected final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
	protected int backgroundColor = Theme.PRIMARY;
	protected int foregroundColor = Theme.WHITE;
	protected int borderColor = 0;
	protected int borderShadow = 0;
	protected int roundRadius = 0;
	protected int textShadow = 0;
	protected int textArrange = 0;
	
	public T setBackgroundColor(int color) {
		this.backgroundColor = color;
		return (T) this;
	}

	public T setForegroundColor(int color) {
		this.foregroundColor = color;
		return (T) this;
	}

	public T setBorderShadow(int color) {
		this.borderShadow = color;
		return (T) this;
	}

	public T setBorder(int borderColor) {
		this.borderColor = borderColor;
		return (T) this;
	}

	public T setTextShadow(int color) {
		this.textShadow = color;
		return (T) this;
	}

	public T setRadius(int radius) {
		this.roundRadius = radius;
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
}
