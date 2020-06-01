package won983212.simpleui.windows;

import net.minecraft.client.gui.GuiMainMenu;
import won983212.simpleui.Arranges;
import won983212.simpleui.components.UIButton;
import won983212.simpleui.components.UIRectangle;
import won983212.simpleui.components.UISwitch;
import won983212.simpleui.components.panels.GridPanel;

public class UIScreenTest extends UIScreen {
	private GuiMainMenu parent;

	public UIScreenTest(GuiMainMenu parent) {
		super(100, 100);
		this.parent = parent;
	}
	
	@Override
	protected void initComponents() {
		GridPanel panel = new GridPanel();
		panel.addColumns("*1,*1");
		panel.addRows("auto,*2,*1");
		panel.add(new UIButton("1"));
		panel.add(GridPanel.setLayout(new UIButton("2"), 1, 1, 1, 1));
		panel.add(GridPanel.setLayout(new UIButton("3"), 0, 2, 2, 1));
		
		add(new UIRectangle().setBackgroundColor(0xffaaffff));
		add(new UIRectangle().setArrange(Arranges.BR));
		add(new UIButton("Hello").setArrange(Arranges.TL));
		add(new UISwitch().setArrange(Arranges.CR));
		add(panel);
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
