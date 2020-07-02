package won983212.kpatch.screens;

import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import won983212.kpatch.Configs;
import won983212.kpatch.KoreanInputPatch;
import won983212.simpleui.Arranges;
import won983212.simpleui.DirWeights;
import won983212.simpleui.Theme;
import won983212.simpleui.UIScreen;
import won983212.simpleui.component.GridPanel;
import won983212.simpleui.component.GridPanel.LengthDefinition;
import won983212.simpleui.component.GridPanel.LengthType;
import won983212.simpleui.component.StackPanel;
import won983212.simpleui.component.SwitchPanel;
import won983212.simpleui.component.UIComponent;
import won983212.simpleui.component.ui.UIAlertPopup;
import won983212.simpleui.component.ui.UIButton;
import won983212.simpleui.component.ui.UILabel;
import won983212.simpleui.component.ui.UIRectangle;
import won983212.simpleui.component.ui.UITab;
import won983212.simpleui.events.IClickEventListener;
import won983212.simpleui.events.IStateChangedEventListener;

public class UIScreenSettings extends UIScreen implements IStateChangedEventListener<Integer>, IClickEventListener {
	private GuiScreen parent;
	private UITab sidebar;
	private SwitchPanel contentPanel = new SwitchPanel();
	private UIAlertPopup alert = new UIAlertPopup().setCallbackEvent(this, 2, 3);
	private ArrayList<SettingProperty> properties = new ArrayList<>();
	
	public UIScreenSettings(GuiScreen parent) {
		super(320, 200);
		this.parent = parent;
	}
	
	private void generatePages() {
		ArrayList<SettingProperty> properties = new ArrayList<>();

		// 일반
		properties.add(new SettingProperty("입력 키보드", Configs.INPUT_KEYBOARD_ARRAY, SettingProperty.SELECT,
				new String[] {"두벌식", "세벌식 390", "세벌식 최종"}));
		properties.add(new SettingProperty("채팅에서 자동으로 영타 변환", Configs.CHAT_CONVERT_KOR_MODE, SettingProperty.SELECT, 
				new String[] {"끄기", "한글 없는 문장일 때", "항상"}));
		contentPanel.add(generatePage(properties));
		
		// 폰트
		properties.add(new SettingProperty("폰트 사용", Configs.USE_FONT, SettingProperty.BOOLEAN));
		properties.add(new SettingProperty("폰트", Configs.FONT_FAMILY, SettingProperty.INPUT));
		properties.add(new SettingProperty("안티엘리어싱 사용", Configs.USE_FONT_ANTIALIASING, SettingProperty.BOOLEAN));
		contentPanel.add(generatePage(properties));
		
		// 키
		properties.add(new SettingProperty("한영 전환키", Configs.KEY_KOR, SettingProperty.KEY));
		properties.add(new SettingProperty("한자키", Configs.KEY_HANJA, SettingProperty.KEY));
		properties.add(new SettingProperty("색 입력키", Configs.KEY_COLOR, SettingProperty.KEY));
		contentPanel.add(generatePage(properties));
		
		// UI
		properties.add(new SettingProperty("한영 지시자 표시", Configs.IME_INDICATOR_VISIBLE_MODE, SettingProperty.SELECT,
				new String[] {"끄기", "채팅창에만 표시", "모든 필드에 표시"}));
		properties.add(new SettingProperty("한영 지시자 애니메이션", Configs.IME_INDICATOR_ANIMATE, SettingProperty.BOOLEAN));
		properties.add(new SettingProperty("UI 애니메이션", Configs.UI_ANIMATE, SettingProperty.BOOLEAN));
		contentPanel.add(generatePage(properties));
		
		//TODO MOD IMAGE ADD
		// 정보
		GridPanel infoPanel = new GridPanel();
		infoPanel.addColumns("30,140");
		infoPanel.addRows("15,15,auto,auto,auto");
		infoPanel.add(GridPanel.setLayout(new UIRectangle(), 0, 0, 1, 2));
		infoPanel.add(GridPanel.setLayout(new UILabel("룻트의 한글패치").setMargin(new DirWeights(2, 2, 5, 2)).setForegroundColor(Theme.BLACK), 1, 0, 1, 1));
		infoPanel.add(GridPanel.setLayout(new UILabel("v" + KoreanInputPatch.VERSION).setMargin(new DirWeights(2, 2, 5, 2)).setForegroundColor(Theme.GRAY), 1, 1, 1, 1));
		infoPanel.add(GridPanel.setLayout(new UILabel("제작자   룻트(won983212)").setMargin(new DirWeights(8, 2, 2, 2)).setForegroundColor(Theme.BLACK), 0, 2, 2, 1));
		infoPanel.add(GridPanel.setLayout(new UILabel("이메일   won983212@naver.com").setMargin(new DirWeights(2, 8, 2, 2)).setForegroundColor(Theme.BLACK), 0, 3, 2, 1));
		infoPanel.add(GridPanel.setLayout(new UIButton("Github로 이동.."), 0, 4, 2, 1));
		contentPanel.add(infoPanel.setArrange(Arranges.CC));
		
		sidebar.setTabValues(new String[] {"일반", "폰트", "키", "UI", "모드 정보"});
	}
	
	private GridPanel generatePage(ArrayList<SettingProperty> properties) {
		GridPanel panel = new GridPanel();
		panel.addColumns("*,auto");
		for (int i = 0; i < properties.size(); i++)
			panel.addRow(new LengthDefinition(LengthType.FIXED, 25));
		
		for (int i = 0; i < properties.size(); i++) {
			SettingProperty p = properties.get(i);
			UIComponent<? extends UIComponent> comp = p.getComponent();
			comp = (UIComponent) comp.setMargin(new DirWeights(10, 0, 8, 8)).setArrange(Arranges.CR);
			panel.add(GridPanel.setLayout(new UILabel(p.getLabel()).setArrange(Arranges.CL).setForegroundColor(Theme.BLACK)
					.setMargin(new DirWeights(10, 0, 8, 8)), 0, i, 1, 1));
			panel.add(GridPanel.setLayout(comp, 1, i, 1, 1));
		}
		
		this.properties.addAll(properties);
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
		buttons.add(new UIButton("초기화").setId(0).setClickListener(this).setMinimalSize(28, 11)
				.setMargin(new DirWeights(3)).setBackgroundColor(Theme.SECONDARY));
		buttons.add(new UIButton("저장").setId(1).setClickListener(this).setMinimalSize(28, 11)
				.setMargin(new DirWeights(3, 3, 0, 3)));
		panel.add(GridPanel.setLayout(buttons, 2, 1, 1, 1));
		add(panel);
		
		addPopup(alert);
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

	@Override
	public void onClick(UIComponent comp, int mouseX, int mouseY, int mouseButton) {
		switch(comp.getId()) {
		case 0:
			alert.showMessage("주의", "모든 데이터가 초기화됩니다. 계속합니까?");
			break;
		case 1:
			for(SettingProperty p : properties) p.save();
			Configs.save();
			break;
		case 3:
			Configs.setDefault();
			Minecraft.getMinecraft().displayGuiScreen(parent);
			break;
		}
	}
}
