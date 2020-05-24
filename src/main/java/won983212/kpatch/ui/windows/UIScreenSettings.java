package won983212.kpatch.ui.windows;

import net.minecraft.client.gui.GuiMainMenu;
import won983212.kpatch.ui.components.UIButton;

public class UIScreenSettings extends UIScreen {
	private GuiMainMenu parent;
	
	public UIScreenSettings(GuiMainMenu parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		add(new UIButton("Test Button").setLocation(10, 10).setSize(100, 20));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(parent != null) {
			parent.drawScreen(0, 0, partialTicks);
		}
		drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
