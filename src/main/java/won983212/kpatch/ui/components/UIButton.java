package won983212.kpatch.ui.components;

import net.minecraft.client.Minecraft;
import won983212.kpatch.ui.SimpleUI;
import won983212.kpatch.ui.Theme;

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
		SimpleUI.drawArea(x, y, width, height, Theme.PRIMARY);
		SimpleUI.useShadow(-1);
		SimpleUI.useTextCenterArea(width, height);
		SimpleUI.useTextCenter(true, true);
		SimpleUI.drawText(Minecraft.getMinecraft().fontRenderer, label, x, y, Theme.WHITE);
	}
}
