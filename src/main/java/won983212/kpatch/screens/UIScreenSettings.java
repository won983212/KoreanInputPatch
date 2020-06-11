package won983212.kpatch.screens;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiMainMenu;
import won983212.kpatch.Configs;
import won983212.simpleui.Arranges;
import won983212.simpleui.DirWeights;
import won983212.simpleui.HorizontalArrange;
import won983212.simpleui.Theme;
import won983212.simpleui.UIScreen;
import won983212.simpleui.component.GridPanel;
import won983212.simpleui.component.StackPanel;
import won983212.simpleui.component.SwitchPanel;
import won983212.simpleui.component.UIComponent;
import won983212.simpleui.component.GridPanel.LengthDefinition;
import won983212.simpleui.component.GridPanel.LengthType;
import won983212.simpleui.component.deco.BorderDeco;
import won983212.simpleui.component.ui.UIButton;
import won983212.simpleui.component.ui.UICombobox;
import won983212.simpleui.component.ui.UIKeyBox;
import won983212.simpleui.component.ui.UILabel;
import won983212.simpleui.component.ui.UIRectangle;
import won983212.simpleui.component.ui.UISwitch;
import won983212.simpleui.component.ui.UITab;
import won983212.simpleui.component.ui.UITextField;
import won983212.simpleui.events.IStateChangedEventListener;

public class UIScreenSettings extends UIScreen implements IStateChangedEventListener<Integer> {
	private GuiMainMenu parent;
	private UITab sidebar;
	private SwitchPanel contentPanel;
	
	public UIScreenSettings(GuiMainMenu parent) {
		super(300, 200);
		this.parent = parent;
	}
	
	private void generatePages() {
		ArrayList<Property> properties = new ArrayList<>();
		
		properties.add(new Property("한영 지시자 표시", Configs.IME_INDICATOR_VISIBLE_MODE, Property.SELECT,
				new String[] {"끄기", "채팅창에만 표시", "모든 필드에 표시"}));
		properties.add(new Property("한영 지시자 애니메이션", Configs.IME_INDICATOR_ANIMATE, Property.BOOLEAN));
		properties.add(new Property("UI 애니메이션", Configs.UI_ANIMATE, Property.BOOLEAN));
		properties.add(new Property("한영 전환키", Configs.KEY_KOR, Property.KEY));
		properties.add(new Property("한자키", Configs.KEY_HANJA, Property.KEY));
		properties.add(new Property("색 입력키", Configs.KEY_COLOR, Property.KEY));
		contentPanel.add(generatePage(properties));
		
		properties.add(new Property("한영 전환키", Configs.KEY_KOR, Property.KEY));
		properties.add(new Property("한자키", Configs.KEY_HANJA, Property.KEY));
		properties.add(new Property("색 입력키", Configs.KEY_COLOR, Property.KEY));
		contentPanel.add(generatePage(properties));
		
		//sidebar.setTabValues(new String[] {"일반", "입력", "폰트", "키", "고급"});
		sidebar.setTabValues(new String[] {"일반", "키"});
	}
	
	private GridPanel generatePage(ArrayList<Property> properties) {
		GridPanel panel = new GridPanel();
		panel.addColumns("*,auto");
		for (int i = 0; i < properties.size(); i++)
			panel.addRow(new LengthDefinition(LengthType.FIXED, 25));
		
		for (int i = 0; i < properties.size(); i++) {
			Property p = properties.get(i);
			UIComponent<? extends UIComponent> comp = null;

			switch (p.type) {
			case Property.BOOLEAN:
				comp = new UISwitch(Configs.getBoolean(p.confId));
				break;
			case Property.INPUT:
				comp = new UITextField(Configs.get(p.confId));
				break;
			case Property.KEY:
				comp = new BorderDeco(new UIKeyBox(Configs.getInt(p.confId)).setMinimalSize(60, 16))
						.setForegroundColor(Theme.adjColor(Theme.PRIMARY, -20));
				break;
			case Property.SELECT:
				comp = new UICombobox(Configs.getInt(p.confId), p.selectLabels).setMinimalSize(80, 14);
				break;
			}
			
			comp = (UIComponent) comp.setMargin(new DirWeights(10, 0, 8, 8)).setArrange(Arranges.CR);
			panel.add(GridPanel.setLayout(new UILabel(p.label).setArrange(Arranges.CL).setForegroundColor(Theme.BLACK)
					.setMargin(new DirWeights(10, 0, 8, 8)), 0, i, 1, 1));
			panel.add(GridPanel.setLayout(comp, 1, i, 1, 1));
		}
		
		properties.clear();
		return panel;
	}
	
	@Override
	protected void initComponents() {
		GridPanel panel = new GridPanel();
		panel.addColumns("60, *, auto");
		panel.addRows("*, 20");
		panel.add(GridPanel.setLayout((sidebar = new UITab()).setSelectedEvent(this), 0, 0, 1, 2));
		panel.add(GridPanel.setLayout(new UIRectangle().setBackgroundColor(Theme.BACKGROUND), 1, 0, 2, 2));
		panel.add(GridPanel.setLayout(contentPanel = new SwitchPanel(), 1, 0, 2, 2));
		generatePages();
		
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

	@Override
	public Integer onChanged(UIComponent comp, Integer newState) {
		if(contentPanel != null) {
			contentPanel.setPage(newState);
		}
		return newState;
	}
	
	private static class Property {
		private static final int SELECT = 1;
		private static final int BOOLEAN = 2;
		private static final int KEY = 3;
		private static final int INPUT = 4;
		
		private String label;
		private String confId;
		private int type;
		private String[] selectLabels = null;
		
		private Property(String label, String configId, int type) {
			this(label, configId, type, null);
		}
		
		private Property(String label, String configId, int type, String[] selectLabels) {
			this.label = label;
			this.confId = configId;
			this.type = type;
			this.selectLabels = selectLabels;
		}
	}
}
