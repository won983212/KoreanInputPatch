package won983212.simpleui.windows;

import net.minecraft.client.gui.GuiMainMenu;
import won983212.simpleui.Arranges;
import won983212.simpleui.Theme;
import won983212.simpleui.components.UIButton;
import won983212.simpleui.components.UICombobox;
import won983212.simpleui.components.UIKeyBox;
import won983212.simpleui.components.UIRectangle;
import won983212.simpleui.components.UISwitch;
import won983212.simpleui.components.UITextField;
import won983212.simpleui.components.panels.GridPanel;

public class UIScreenTest extends UIScreen {
	private GuiMainMenu parent;

	public UIScreenTest(GuiMainMenu parent) {
		super(100, 100);
		this.parent = parent;
	}
	
	@Override
	protected void initComponents() {
		add(new UIRectangle().setBackgroundColor(0xff666666));
		add(new UITextField().setArrange(Arranges.CC));
		add(new UICombobox().setArrange(Arranges.BC));
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
