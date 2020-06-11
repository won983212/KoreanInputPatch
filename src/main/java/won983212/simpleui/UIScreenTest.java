package won983212.simpleui;

import net.minecraft.client.gui.GuiMainMenu;
import won983212.simpleui.component.GridPanel;
import won983212.simpleui.component.ui.UIButton;
import won983212.simpleui.component.ui.UICombobox;
import won983212.simpleui.component.ui.UIKeyBox;
import won983212.simpleui.component.ui.UIRectangle;
import won983212.simpleui.component.ui.UISwitch;
import won983212.simpleui.component.ui.UITextField;

public class UIScreenTest extends UIScreen {
	private GuiMainMenu parent;

	public UIScreenTest(GuiMainMenu parent) {
		super(100, 100);
		this.parent = parent;
	}
	
	@Override
	protected void initComponents() {
		add(new UIRectangle().setBackgroundColor(0xff666666));
		add(new UIButton("Hello").setArrange(Arranges.CC));
		add(new UICombobox(0, new String[] {
			"Item1", "Item2", "Item3", "Item4"
		}).setArrange(Arranges.BC));
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
