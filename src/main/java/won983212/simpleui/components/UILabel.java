package won983212.simpleui.components;

import won983212.simpleui.UITools;
import won983212.simpleui.components.panels.UIStyledComponent;

public class UILabel extends UIStyledComponent<UILabel> {
	private String text;
	
	public UILabel(String text) {
		setText(text);
	}
	
	public UILabel setText(String text) {
		this.text = text;
		setMinimalSize(fontRenderer.getStringWidth(text), fontRenderer.FONT_HEIGHT);
		return this;
	}
	
	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		UITools.drawText(fontRenderer, text, x, y, foregroundColor, textShadow, textArrange);
	}
}
