package won983212.simpleui.windows;

import net.minecraft.client.gui.GuiMainMenu;
import won983212.simpleui.components.UIButton;
import won983212.simpleui.components.UILabel;
import won983212.simpleui.components.UIRectangle;
import won983212.simpleui.components.UISwitch;

public class UIScreenSettings extends UIScreen {
	private GuiMainMenu parent;
	
	public UIScreenSettings(GuiMainMenu parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		add(new UIButton("확인").setLocation(10, 10).setSize(30, 14));
		add(new UISwitch().setLocation(10, 50));
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
