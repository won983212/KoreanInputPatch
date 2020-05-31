package won983212.simpleui.components;

import won983212.kpatch.Configs;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;

public class UIRectangle extends UIControl<UIRectangle> {
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		UITools.drawArea(x, y, width, height, backgroundColor, shadow, radius);
	}
}
