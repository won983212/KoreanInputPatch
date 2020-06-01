package won983212.simpleui.windows;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import won983212.simpleui.components.panels.UIComponent;
import won983212.simpleui.components.panels.UIPanel;
import won983212.simpleui.components.panels.UIStyledComponent;

/**
 * SimpleUI를 사용할 수 있는 <code>GuiScreen</code>입니다.
 * Rendering, Event Handling, Component Managing을 담당합니다.
 */
public class UIScreen extends GuiScreen {
	protected final Rectangle screenBounds;
	private UIPanel rootPanel = new UIPanel();

	/**
	 * x나 y가 -1일 경우에는 위치를 자동으로 가운데로 설정한다. width나 height또한 0으로 설정할 수 있으며, 0일 경우에는 맞춤 사이즈로 설정한다.
	 */
	public UIScreen(int x, int y, int width, int height) {
		screenBounds = new Rectangle(x, y, width, height);
		rootPanel.setRelativeBounds(x, y, width, height);
	}
	
	/**
	 * 위치를 자동으로 가운데로 설정한다. width나 height또한 0으로 설정할 수 있으며, 0일 경우에는 맞춤 사이즈로 설정한다.
	 */
	public UIScreen(int width, int height) {
		this(-1, -1, width, height);
	}
	
	@Override
	public void initGui() {
		rootPanel.clearComponents();
		initComponents();
		rootPanel.invalidateSize();
		if(screenBounds.width == 0 || screenBounds.height == 0) {
			Dimension dim = rootPanel.measureMinSize();
			screenBounds.setSize(dim.width, dim.height);
		}
		if(screenBounds.x == -1 || screenBounds.y == -1) {
			screenBounds.setLocation((width - screenBounds.width) / 2, (height - screenBounds.height) / 2);
		}
		rootPanel.setRelativeBounds(screenBounds.x, screenBounds.y, screenBounds.width, screenBounds.height);
		rootPanel.layout();
	}
	
	protected void initComponents() {
	}
	
	protected void add(UIComponent comp) {
		rootPanel.add(comp);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		rootPanel.draw(mouseX - screenBounds.x, mouseY - screenBounds.y, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		rootPanel.onKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		rootPanel.onMouseClicked(mouseX - screenBounds.x, mouseY - screenBounds.y, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		rootPanel.onMouseReleased(mouseX - screenBounds.x, mouseY - screenBounds.y, state);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		rootPanel.onMouseClickMove(mouseX - screenBounds.x, mouseY - screenBounds.y, clickedMouseButton, timeSinceLastClick);
	}
}
