package won983212.simpleui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;

// TODO Combobox, Keybox, Textfield
public abstract class UIComponent<T> {
	protected int x = 0;
	protected int y = 0;
	protected int width = 0;
	protected int height = 0;
	protected boolean isEnabled = true;
	
	public boolean isIn(int px, int py) {
		return px >= x && px <= x + width && py >= y && py <= y + height;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}
	
	public T setEnabled(boolean enable) {
		this.isEnabled = enable;
		return (T) this;
	}
	
	private boolean setLocation_(int x, int y) {
		if(this.x == x && this.y == y)
			return false;
			
		// check invalid parameter
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		
		// set location and update area for painting
		this.x = x;
		this.y = y;
		return true;
	}
	
	public T setLocation(int x, int y) {
		if(setLocation_(x, y))
			onChangedBounds();
		return (T) this;
	}
	
	private boolean setSize_(int width, int height) {
		if(this.width == width && this.height == height)
			return false;
		
		// check invalid parameter
		if (width < 0)
			width = 0;
		if (height < 0)
			height = 0;

		// set location and update area for painting & centering text
		this.width = width;
		this.height = height;
		return true;
	}
	
	public T setSize(int width, int height) {
		if(setSize_(width, height))
			onChangedBounds();
		return (T) this;
	}
	
	public T setBounds(int x, int y, int width, int height) {
		boolean b1 = setLocation_(x, y);
		boolean b2 = setSize_(width, height);
		if(b1 || b2)
			onChangedBounds();
		return (T) this;
	}
	
	protected void onChangedBounds() {
	}
	
	public abstract void draw(int mouseX, int mouseY, float partialTicks);
}
