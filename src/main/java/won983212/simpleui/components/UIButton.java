package won983212.simpleui.components;

import net.minecraft.client.Minecraft;
import won983212.simpleui.UITools;
import won983212.simpleui.Theme;

public class UIButton extends UIComponent<UIButton> {
	private String label;
	
	public UIButton(String label) {
		setText(label);
	}
	
	public UIButton setText(String label) {
		this.label = label;
		return this;
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		//Theme.adjustColor(Theme.PRIMARY, 40);
		UITools.drawArea(x, y, width, height, backgroundColor);
		UITools.useShadow(-1);
		UITools.useTextCenterArea(width, height);
		UITools.useTextCenter(true, true);
		UITools.drawText(Minecraft.getMinecraft().fontRenderer, label, x, y, foregroundColor);
	}
}
