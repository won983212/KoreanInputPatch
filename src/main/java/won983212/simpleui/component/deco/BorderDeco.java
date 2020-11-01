package won983212.simpleui.component.deco;

import won983212.simpleui.Theme;
import won983212.simpleui.UITools;
import won983212.simpleui.component.UIComponent;
import won983212.simpleui.component.UIDecorator;

public class BorderDeco extends UIDecorator {
	public BorderDeco(UIComponent child) {
		setChild(child);
		setForegroundColor(Theme.LIGHT_GRAY);
	}

	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		super.renderComponent(mouseX, mouseY, partialTicks);
		UITools.drawArea(x, y, 0.5, height, foregroundColor);
		UITools.drawArea(x, y + height - 0.5, width, 0.5, foregroundColor);
		UITools.drawArea(x + width - 0.5, y, 0.5, height, foregroundColor);
		UITools.drawArea(x, y, width, 0.5, foregroundColor);
	}
}
