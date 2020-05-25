package won983212.simpleui.windows;

import net.minecraft.client.gui.GuiMainMenu;
import won983212.simpleui.Theme;
import won983212.simpleui.components.UIButton;
import won983212.simpleui.components.UILabel;
import won983212.simpleui.components.UIRectangle;
import won983212.simpleui.components.UISwitch;

public class UIScreenSettings extends UIScreen {
	public static final int WIDTH = 300;
	public static final int HEIGHT = 200;
	private GuiMainMenu parent;
	
	public UIScreenSettings(GuiMainMenu parent) {
		this.parent = parent;
	}
	
	@Override
	public void initGui() {
		final int sx = (width - WIDTH) / 2;
		final int sy = (height - HEIGHT) / 2;
		final int navW = 70;
		final int titleH = 16;
		
		// title bar
		add(new UIRectangle().setLocation(sx, sy).setSize(WIDTH, titleH));
		add(new UILabel("설정창").setLocation(sx + 6, sy).setTextCenterArea(0, titleH).setTextCenter(false, true).setShadow(-1));
		
		// navigation
		add(new UIRectangle().setLocation(sx, sy + titleH).setSize(navW, HEIGHT - titleH).setBackgroundColor(0xffcfcfcf));
		add(new UILabel("★ 일반").setLocation(sx, sy + titleH).setForegroundColor(Theme.DARK_GRAY).setShadow(Theme.LIGHT_GRAY));
		
		// content
		add(new UIRectangle().setLocation(sx + navW, sy + titleH).setSize(WIDTH - navW, HEIGHT - titleH).setBackgroundColor(Theme.BACKGROUND));
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
