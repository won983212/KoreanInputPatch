package won983212.simpleui.windows;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiMainMenu;
import won983212.kpatch.Configs;
import won983212.simpleui.Theme;
import won983212.simpleui.components.UIButton;
import won983212.simpleui.components.UIComponent;
import won983212.simpleui.components.UILabel;
import won983212.simpleui.components.UIPageView;
import won983212.simpleui.components.UIRectangle;
import won983212.simpleui.components.UISettingList;
import won983212.simpleui.components.UISettingList.Property;
import won983212.simpleui.components.UISwitch;
import won983212.simpleui.components.UITab;
import won983212.simpleui.events.IStateChangedEventListener;

public class UIScreenSettings extends UIScreen {
	public static final int WIDTH = 300;
	public static final int HEIGHT = 200;
	private static SettingPage[] pages;
	private GuiMainMenu parent;
	private UIPageView pageView;
	
	static {
		ArrayList<Property> properties = new ArrayList<>();
		pages = new SettingPage[4];
		
		properties.add(new Property("한영 지시자 표시", Configs.IME_INDICATOR_VISIBLE_MODE, Property.SELECT));
		properties.add(new Property("한영 지시자 애니메이션", Configs.IME_INDICATOR_ANIMATE, Property.BOOLEAN));
		properties.add(new Property("UI 애니메이션", Configs.UI_ANIMATE, Property.BOOLEAN));
		properties.add(new Property("한영 전환키", Configs.KEY_KOR, Property.KEY));
		properties.add(new Property("한자키", Configs.KEY_HANJA, Property.KEY));
		properties.add(new Property("색 입력키", Configs.KEY_COLOR, Property.KEY));
		pages[0] = new SettingPage("일반", properties.toArray(new Property[properties.size()]));
	}
	
	public UIScreenSettings(GuiMainMenu parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		final int sx = (width - WIDTH) / 2;
		final int sy = (height - HEIGHT) / 2;
		final int navW = 70;

		// navigation
		add(new UIRectangle().setLocation(sx, sy).setSize(navW, HEIGHT).setBackgroundColor(0xffdfdfdf));
		add(new UITab().setLocation(sx, sy).setSize(navW, HEIGHT)
				.setSelectedEvent(new IStateChangedEventListener<Integer>() {
					@Override
					public Integer onChanged(UIComponent comp, Integer newState) {
						pageView.setPageIndex(newState);
						return newState;
					}
				}).setTabValues(new String[] { "일반", "폰트", "입력", "고급" }));

		// content
		add(new UIRectangle().setLocation(sx + navW, sy).setSize(WIDTH - navW, HEIGHT)
				.setBackgroundColor(Theme.BACKGROUND));
		add(pageView = new UIPageView().setLocation(sx + navW, sy).setSize(WIDTH - navW, HEIGHT)
				.setPages(createPages()));

		// footer
		add(new UIButton("초기화").setLocation(sx + WIDTH - 72, sy + HEIGHT - 17).setSize(34, 14));
		add(new UIButton("저장").setLocation(sx + WIDTH - 34, sy + HEIGHT - 17).setSize(30, 14));
	}

	private UIComponent[] createPages() {
		UIComponent[] ret = new UIComponent[4];
		ret[0] = new UISettingList(pages[0].properties);
		ret[1] = new UILabel("Font Setting").setForegroundColor(Theme.BLACK).setTextCenter(true, true);
		ret[2] = new UILabel("Input Setting").setForegroundColor(Theme.BLACK).setTextCenter(true, true);
		ret[3] = new UILabel("Advanced Setting").setForegroundColor(Theme.BLACK).setTextCenter(true, true);
		return ret;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(parent != null) {
			parent.drawScreen(0, 0, partialTicks);
		}
		
		drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private static class SettingPage {
		public final String title;
		public final Property[] properties;
		
		public SettingPage(String title, Property[] properties) {
			this.title = title;
			this.properties = properties;
		}
	}
}
