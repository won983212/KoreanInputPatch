package won983212.simpleui.components;

import won983212.simpleui.Theme;
import won983212.simpleui.UITools;
import won983212.simpleui.components.panels.UIStyledComponent;

public class UICombobox extends UIStyledComponent<UICombobox> {
	private boolean isFocused = false;
	
	public UICombobox() {
		setMinimalSize(60, 14);
		setBackgroundColor(Theme.WHITE);
		setForegroundColor(Theme.BLACK);
		setBorder(Theme.GRAY);
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		return true;
	}
	
	@Override
	public void onLostFocus() {
		isFocused = false;
	}

	@Override
	public void onGotFocus() {
		isFocused = true;
	}
	
	@Override
	protected void renderComponent(int mouseX, int mouseY, float partialTicks) {
		float textX = x + 2;
		float textY = y + (height - fontRenderer.FONT_HEIGHT) / 2f;
		int color = foregroundColor;
		
		if(!isEnabled()) {
			color = Theme.LIGHT_GRAY;
		}
		
		// background
		UITools.drawArea(x, y, width, height, backgroundColor, borderShadow, borderColor, roundRadius);
		
		// selected label
		UITools.drawText(fontRenderer, "Selected", textX, textY, foregroundColor, textShadow);
		
		// arrow
		char arrow = isFocused ? '▲' : '▼';
		UITools.drawText(fontRenderer, String.valueOf(arrow), x + width - fontRenderer.getCharWidth(arrow) - 2, textY, foregroundColor, 0);
	}
}
