package won983212.simpleui.components;

import won983212.kpatch.Configs;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;
import won983212.simpleui.components.panels.UIStyledComponent;

public class UIRectangle extends UIStyledComponent<UIRectangle> {
	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		UITools.drawArea(x, y, width, height, backgroundColor, shadow, radius);
	}
}
