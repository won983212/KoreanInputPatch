package won983212.kpatch.screens;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiMainMenu;
import won983212.kpatch.Configs;
import won983212.simpleui.Arranges;
import won983212.simpleui.DirWeights;
import won983212.simpleui.Theme;
import won983212.simpleui.UIScreen;
import won983212.simpleui.components.UIButton;
import won983212.simpleui.components.UICombobox;
import won983212.simpleui.components.UIRectangle;
import won983212.simpleui.components.UITab;
import won983212.simpleui.components.panels.GridPanel;
import won983212.simpleui.components.panels.StackPanel;
import won983212.simpleui.components.panels.UIComponent;
import won983212.simpleui.events.IStateChangedEventListener;

public class UIScreenSettings extends UIScreen {
	private GuiMainMenu parent;
	
	public UIScreenSettings(GuiMainMenu parent) {
		super(300, 200);
		this.parent = parent;
	}
	
	static {
		/*ArrayList<Property> properties = new ArrayList<>();
		properties.add(new Property("한영 지시자 표시", Configs.IME_INDICATOR_VISIBLE_MODE, Property.SELECT,
				new String[] {"끄기", "채팅창에만 표시", "모든 필드에 표시"}));
		properties.add(new Property("한영 지시자 애니메이션", Configs.IME_INDICATOR_ANIMATE, Property.BOOLEAN));
		properties.add(new Property("UI 애니메이션", Configs.UI_ANIMATE, Property.BOOLEAN));
		properties.add(new Property("한영 전환키", Configs.KEY_KOR, Property.KEY));
		properties.add(new Property("한자키", Configs.KEY_HANJA, Property.KEY));
		properties.add(new Property("색 입력키", Configs.KEY_COLOR, Property.KEY));
		pages[0] = properties.toArray(new Property[properties.size()]);
		pageTabs[0] = "일반";*/
		
		//new String[] { "일반", "폰트", "입력", "고급" };
	}
	
	@Override
	protected void initComponents() {
		GridPanel panel = new GridPanel();
		panel.addColumns("80, *, auto");
		panel.addRows("*, 20");
		panel.add(GridPanel.setLayout(new UITab().setTabValues(new String[] {"일반", "입력", "폰트", "키", "고급"}), 0, 0, 1, 2));
		panel.add(GridPanel.setLayout(new UIRectangle().setBackgroundColor(Theme.BACKGROUND), 1, 0, 2, 2));
		
		StackPanel buttons = new StackPanel();
		buttons.add(new UIButton("초기화").setMinimalSize(28, 11).setMargin(new DirWeights(3)).setBackgroundColor(Theme.SECONDARY));
		buttons.add(new UIButton("저장").setMinimalSize(28, 11).setMargin(new DirWeights(3, 3, 0, 3)));
		panel.add(GridPanel.setLayout(buttons, 2, 1, 1, 1));
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
