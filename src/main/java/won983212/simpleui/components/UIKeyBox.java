package won983212.simpleui.components;

import org.lwjgl.input.Keyboard;

import won983212.kpatch.Configs;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;
import won983212.simpleui.animation.DecimalAnimation;
import won983212.simpleui.components.panels.UIStyledComponent;

public class UIKeyBox extends UIStyledComponent<UIKeyBox> {
	private static UIKeyBox editingBox = null;
	private DecimalAnimation hoverAnimation = new DecimalAnimation(150);
	private boolean hover = false;
	private int key;

	public UIKeyBox() {
		this(Keyboard.KEY_NONE);
	}
	
	public UIKeyBox(int initialKey) {
		this.key = initialKey;
		setMinimalSize(50, 18);
		setTextShadow(Theme.BACKGROUND_SHADOW);
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		editingBox = this;
		return true;
	}
	
	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		if(keyCode == 0)
			return;
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
		int color = backgroundColor;
		if (!isEnabled()) {
			color = Theme.LIGHT_GRAY;
		} else if (containsRelative(mouseX, mouseY)) {
			int adj;
			if (Configs.getBoolean(Configs.UI_ANIMATE)) {
				if (!hover) {
					hoverAnimation.setReverse(false);
					hoverAnimation.play();
					hover = true;
				}
				adj = (int) (hoverAnimation.update() * 30);
			} else {
				adj = 30;
			}
			color = Theme.adjColor(backgroundColor, adj);
		} else {
			hover = false;
		}
		
		String keyText = "미지정";
		if(editingBox == this) {
			keyText = "...";
		} else if(key != Keyboard.KEY_NONE) {
			keyText = Keyboard.getKeyName(key);
		}
		
		UITools.drawArea(x, y, width, height, color, borderShadow, borderColor, roundRadius);
		UITools.drawText(fontRenderer, keyText, x + width / 2, y + height / 2, foregroundColor, textShadow, UITools.CENTER_BOTH);
	}
	
	public static boolean isKeyBoxEditing() {
		return editingBox != null;
	}
}
