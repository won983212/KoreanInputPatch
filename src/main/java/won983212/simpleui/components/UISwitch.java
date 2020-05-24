package won983212.simpleui.components;

import won983212.simpleui.Theme;
import won983212.simpleui.UITools;

//TODO Implementation
public class UISwitch extends UIComponent<UISwitch> {
	
	public UISwitch() {
		width = 26;
		height = 9;
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		boolean on = false;
		UITools.drawArea(x, y, width, height, Theme.PRIMARY);
		UITools.drawArea(x + 0.5, y + 0.5, width - 1, height - 1, on ? Theme.SUCCESS : Theme.BACKGROUND);
		UITools.drawArea(x + 0.5, y + 0.5, width / 2 - 1, height - 1, backgroundColor);
	}
}
