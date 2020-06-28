package won983212.simpleui.component.ui;

import won983212.simpleui.UITools;
import won983212.simpleui.component.UIStyledComponent;

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
		String[] texts = text.split("\n");
		int stackedY = y;
		for(String str : texts) {
			UITools.drawText(fontRenderer, str, x, stackedY, foregroundColor, textShadow, textArrange, width);
			stackedY += fontRenderer.FONT_HEIGHT + 2;
		}
	}
}
