package won983212.simpleui.components;

import java.io.IOException;
import java.util.ArrayList;

public class UIPanel extends UIComponent<UIPanel> {
	private ArrayList<UIComponent> components = new ArrayList<>();
	private UIComponent clicked = null;
	
	public void add(UIComponent comp) {
		components.add(comp);
	}

	public void onKeyTyped(char typedChar, int keyCode) {
		for(UIComponent comp : components) {
			if(comp.isEnabled()) {
				comp.onKeyTyped(typedChar, keyCode);
			}
		}
	}

	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		for(UIComponent comp : components) {
			if(comp.isEnabled() && comp.isIn(mouseX, mouseY)) {
				if(comp.onMouseClicked(mouseX, mouseY, mouseButton)) {
					clicked = comp;
					return true;
				}
			}
		}
		return false;
	}

	public void onMouseReleased(int mouseX, int mouseY, int state) {
		if(clicked != null) {
			clicked.onMouseReleased(mouseX, mouseY, state);
			clicked = null;
		}
	}

	public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if(clicked != null) {
			clicked.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		for(UIComponent comp : components) {
			comp.draw(mouseX, mouseY, partialTicks);
		}
	}
}
