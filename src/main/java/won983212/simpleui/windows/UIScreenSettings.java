package won983212.simpleui.windows;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiMainMenu;
import won983212.kpatch.Configs;
import won983212.simpleui.Theme;
import won983212.simpleui.components.UIButton;
import won983212.simpleui.components.UIComponent;
import won983212.simpleui.components.UIPanel;
import won983212.simpleui.components.UIRectangle;
import won983212.simpleui.components.UISettingList;
import won983212.simpleui.components.UISettingList.Property;
import won983212.simpleui.components.UITab;
import won983212.simpleui.events.IStateChangedEventListener;

public class UIScreenSettings extends UIScreen {
	public static final int WIDTH = 300;
	public static final int HEIGHT = 200;
	private static Property[][] pages;
	private static String[] pageTabs;
	private GuiMainMenu parent;
	private UIPanel pageView;
	
	static {
		pages = new Property[4][];
		pageTabs = new String[pages.length];
		
		ArrayList<Property> properties = new ArrayList<>();
		properties.add(new Property("한영 지시자 표시", Configs.IME_INDICATOR_VISIBLE_MODE, Property.SELECT));
		properties.add(new Property("한영 지시자 애니메이션", Configs.IME_INDICATOR_ANIMATE, Property.BOOLEAN));
		properties.add(new Property("UI 애니메이션", Configs.UI_ANIMATE, Property.BOOLEAN));
		properties.add(new Property("한영 전환키", Configs.KEY_KOR, Property.KEY));
		properties.add(new Property("한자키", Configs.KEY_HANJA, Property.KEY));
		properties.add(new Property("색 입력키", Configs.KEY_COLOR, Property.KEY));
		pages[0] = properties.toArray(new Property[properties.size()]);
		pageTabs[0] = "일반";
		
		properties.clear();
		properties.add(new Property("한영 지시자 표시", Configs.IME_INDICATOR_VISIBLE_MODE, Property.SELECT));
		properties.add(new Property("한영 지시자 애니메이션", Configs.IME_INDICATOR_ANIMATE, Property.BOOLEAN));
		properties.add(new Property("UI 애니메이션", Configs.UI_ANIMATE, Property.BOOLEAN));
		pages[1] = properties.toArray(new Property[properties.size()]);
		pageTabs[1] = "폰트";
		
		pages[2] = properties.toArray(new Property[properties.size()]);
		pageTabs[2] = "입력";
		
		pages[3] = properties.toArray(new Property[properties.size()]);
		pageTabs[3] = "고급";
		
		//new String[] { "일반", "폰트", "입력", "고급" };
	}
	
	public UIScreenSettings(GuiMainMenu parent) {
		this.parent = parent;
	}

	private List<UIComponent> createPages() {
		ArrayList<UIComponent> ret = new ArrayList<>();
		for (Property[] p : pages) {
			ret.add(new UISettingList(p));
		}
		return ret;
	}
	
	@Override
	public void initGui() {
		final int sx = (width - WIDTH) / 2;
		final int sy = (height - HEIGHT) / 2;
		final int navW = 70;

		// navigation
		add(new UIRectangle().setBounds(sx, sy, navW, HEIGHT).setBackgroundColor(0xffdfdfdf));
		add(new UITab().setBounds(sx, sy, navW, HEIGHT)
			.setSelectedEvent(new IStateChangedEventListener<Integer>() {
				@Override
				public Integer onChanged(UIComponent comp, Integer newState) {
					pageView.selectPage(newState);
					return newState;
				}
			}).setTabValues(pageTabs));

		// content
		add(new UIRectangle().setBounds(sx + navW, sy, WIDTH - navW, HEIGHT)
			.setBackgroundColor(Theme.BACKGROUND));
		add(pageView = new UIPanel().addAll(createPages()).selectPage(0).setBounds(sx + navW, sy, WIDTH - navW, HEIGHT));

		// footer
		add(new UIButton("초기화").setBounds(sx + WIDTH - 72, sy + HEIGHT - 17, 34, 14));
		add(new UIButton("저장").setBounds(sx + WIDTH - 34, sy + HEIGHT - 17, 30, 14));
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
