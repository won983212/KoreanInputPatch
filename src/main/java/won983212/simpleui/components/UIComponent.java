package won983212.simpleui.components;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;
import won983212.simpleui.UITools.UIContext;

public abstract class UIComponent<T> {
	protected final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;
	protected int backgroundColor = Theme.PRIMARY;
	protected int foregroundColor = Theme.WHITE;
	private UIContext ctx = new UIContext();
	
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
	
	public T setBackgroundColor(int color) {
		this.backgroundColor = color;
		return (T) this;
	}
	
	public T setForegroundColor(int color) {
		this.foregroundColor = color;
		return (T) this;
	}
	
	public T setShadow(int shadow) {
		ctx.useShadow(shadow);
		return (T) this;
	}
	
	public T setTextCenter(boolean horizontal, boolean vertical) {
		ctx.useTextCenter(horizontal, vertical);
		return (T) this;
	}
	
	public T setTextCenterArea(int width, int height) {
		ctx.useTextCenterArea(width, height);
		return (T) this;
	}
	
	protected void useContext() {
		UITools.useCustomContext(ctx);
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
	
	public abstract void draw(int mouseX, int mouseY, float partialTicks);
}
