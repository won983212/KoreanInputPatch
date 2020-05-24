package won983212.kpatch.ui.windows;

import java.io.IOException;
import java.util.ArrayList;

import net.minecraft.client.gui.GuiScreen;
import won983212.kpatch.ui.components.UIComponent;

public class UIScreen extends GuiScreen {
	private ArrayList<UIComponent> components = new ArrayList<>();
	private UIComponent clicked = null;
	
	protected void add(UIComponent comp) {
		components.add(comp);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(UIComponent comp : components) {
			comp.draw(mouseX, mouseY, partialTicks);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		for(UIComponent comp : components) {
			comp.onKeyTyped(typedChar, keyCode);
		}
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		for(UIComponent comp : components) {
			if(comp.isIn(mouseX, mouseY)) {
				if(comp.onMouseClicked(mouseX, mouseY, mouseButton)) {
					clicked = comp;
					break;
				}
			}
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		if(clicked != null) {
			clicked.onMouseReleased(mouseX, mouseY, state);
			clicked = null;
		}
	}

	@Override
	protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		if(clicked != null) {
			clicked.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}
	
}
