package won983212.simpleui.windows;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import won983212.simpleui.components.UIComponent;
import won983212.simpleui.components.UIPanel;

public class UIScreen extends GuiScreen {
	private UIPanel rootPanel = new UIPanel();

	protected void add(UIComponent comp) {
		rootPanel.add(comp);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		rootPanel.draw(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		rootPanel.onKeyTyped(typedChar, keyCode);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		rootPanel.onMouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		rootPanel.onMouseReleased(mouseX, mouseY, state);
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		rootPanel.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
	}
}
