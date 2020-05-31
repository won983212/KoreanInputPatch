package won983212.simpleui.components;

import won983212.simpleui.UITools;

public class UILabel extends UIControl<UILabel> {
	private String text;
	
	public UILabel(String text) {
		setText(text);
	}
	
	public UILabel setText(String text) {
		this.text = text;
		return this;
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		UITools.drawText(fontRenderer, text, x, y, foregroundColor, shadow, textArrange);
	}
}
