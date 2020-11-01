package won983212.simpleui.component.ui;

import won983212.simpleui.UITools;
import won983212.simpleui.component.UIStyledComponent;

public class UIRectangle extends UIStyledComponent<UIRectangle> {
	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		UITools.drawArea(x, y, width, height, backgroundColor, borderShadow, borderColor, roundRadius);
	}
}
