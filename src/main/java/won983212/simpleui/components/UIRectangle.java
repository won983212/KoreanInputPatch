package won983212.simpleui.components;

import won983212.kpatch.Configs;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;

public class UIRectangle extends UIComponent<UIRectangle> {
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		useContext();
		UITools.drawArea(x, y, width, height, backgroundColor);
	}
}
