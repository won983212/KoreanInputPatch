package won983212.simpleui.components.panels;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

import won983212.simpleui.UIComponent;
import won983212.simpleui.UIStyledComponent;

public class UIPanel extends UIComponent<UIPanel> {
	private ArrayList<UIComponent> components = new ArrayList<>();
	private UIComponent clicked = null;

	public UIPanel addAll(Collection<? extends UIComponent> components) {
		this.components.addAll(components);
		return this;
	}

	public UIPanel add(UIComponent comp) {
		components.add(comp);
		return this;
	}

	public UIPanel clearComponents() {
		components.clear();
		return this;
	}

	public UIComponent getComponent(int index) {
		return components.get(index);
	}

	protected void componentKeyType(UIComponent comp, char typedChar, int keyCode) {
		if (comp.isEnabled()) {
			comp.onKeyTyped(typedChar, keyCode);
		}
	}

	protected boolean componentMouseClicked(UIComponent comp, int mouseX, int mouseY, int mouseButton) {
		if (comp.isEnabled() && comp.isIn(mouseX, mouseY)) {
			if (comp.onMouseClicked(mouseX, mouseY, mouseButton)) {
				clicked = comp;
				return true;
			}
		}
		return false;
	}

	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		for (UIComponent comp : components) {
			componentKeyType(comp, typedChar, keyCode);
		}
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		ListIterator<UIComponent> li = components.listIterator(components.size());
		while (li.hasPrevious()) {
			if (componentMouseClicked(li.previous(), mouseX, mouseY, mouseButton))
				return true;
		}
		return false;
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		if (clicked != null) {
			clicked.onMouseReleased(mouseX, mouseY, state);
			clicked = null;
		}
	}

	@Override
	public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (clicked != null) {
			clicked.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}

	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		for (UIComponent comp : components) {
			comp.draw(mouseX, mouseY, partialTicks);
		}
	}
}
