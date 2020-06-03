package won983212.simpleui.components;

import won983212.simpleui.Theme;
import won983212.simpleui.UITools;
import won983212.simpleui.components.panels.UIStyledComponent;

public class UICombobox extends UIStyledComponent<UICombobox> {
	public UICombobox() {
		setMinimalSize(60, 14);
		setBackgroundColor(Theme.WHITE);
		setForegroundColor(Theme.BLACK);
		setBorder(Theme.GRAY);
	}
	
	@Override
	protected void renderComponent(int mouseX, int mouseY, float partialTicks) {
		float textX = x + 2;
		float textY = y + (height - fontRenderer.FONT_HEIGHT) / 2f;
		int color = foregroundColor;
		
		if(!isEnabled()) {
			color = Theme.LIGHT_GRAY;
		}
		
		UITools.drawArea(x, y, width, height, backgroundColor, borderShadow, borderColor, roundRadius);
		UITools.drawText(fontRenderer, "Selected", textX, textY, color, textShadow);
	}
}
