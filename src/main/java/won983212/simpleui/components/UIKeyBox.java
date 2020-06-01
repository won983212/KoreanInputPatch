package won983212.simpleui.components;

import org.lwjgl.input.Keyboard;

import won983212.simpleui.UITools;
import won983212.simpleui.components.panels.UIStyledComponent;

//TODO 구현중
public class UIKeyBox extends UIStyledComponent<UIButton> {
	private static UIKeyBox editingBox = null;
	private int key;

	public UIKeyBox() {
		this(Keyboard.KEY_NONE);
	}
	
	public UIKeyBox(int initialKey) {
		this.key = initialKey;
		setMinimalSize(40, 16);
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		editingBox = this;
		return true;
	}
	
	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		if(editingBox == this) {
			editingBox = null;
			key = keyCode;
			if(key == Keyboard.KEY_ESCAPE) {
				key = Keyboard.KEY_NONE;
			}
		}
	}
	
	@Override
	protected void renderComponent(int mouseX, int mouseY, float partialTicks) {
		String keyText = "미지정";
		if(editingBox == this) {
			keyText = "...";
		} else if(key != Keyboard.KEY_NONE) {
			keyText = Keyboard.getKeyName(key);
		}
		
		UITools.drawArea(x, y, width, height, backgroundColor, shadow, roundRadius);
		UITools.drawText(fontRenderer, keyText, x + width / 2, y + height / 2, foregroundColor, shadow, UITools.CENTER_BOTH);
	}
	
	public static boolean isKeyBoxEditing() {
		return editingBox != null;
	}
}
