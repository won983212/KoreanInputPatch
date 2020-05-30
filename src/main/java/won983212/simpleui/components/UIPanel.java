package won983212.simpleui.components;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

public class UIPanel extends UIComponent<UIPanel> {
	private ArrayList<UIComponent> components = new ArrayList<>();
	private UIComponent clicked = null;
	private int selectedPage = -1;
	
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
	
	// select view page. page is index of showing UIComponent;
	// only showing selected page component.
	public UIPanel selectPage(int page) {
		selectedPage = page;
		onChangedBounds();
		return this;
	}
	
	@Override
	protected void onChangedBounds() {
		if(selectedPage != -1) {
			components.get(selectedPage).setBounds(x, y, width, height);
		}
	}
	
	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		if(selectedPage != -1) {
			componentKeyType(components.get(selectedPage), typedChar, keyCode);
		} else {
			for(UIComponent comp : components) {
				componentKeyType(comp, typedChar, keyCode);
			}
		}
	}
	
	private void componentKeyType(UIComponent comp, char typedChar, int keyCode) {
		if(comp.isEnabled()) {
			comp.onKeyTyped(typedChar, keyCode);
		}
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(selectedPage != -1) {
			return componentMouseClicked(components.get(selectedPage), mouseX, mouseY, mouseButton);
		} else {
			for(UIComponent comp : components) {
				if(componentMouseClicked(comp, mouseX, mouseY, mouseButton))
					return true;
			}
			return false;
		}
	}
	
	private boolean componentMouseClicked(UIComponent comp, int mouseX, int mouseY, int mouseButton) {
		if(comp.isEnabled() && comp.isIn(mouseX, mouseY)) {
			if(comp.onMouseClicked(mouseX, mouseY, mouseButton)) {
				clicked = comp;
				return true;
			}
		}
		return false;
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		if(clicked != null) {
			clicked.onMouseReleased(mouseX, mouseY, state);
			clicked = null;
		}
	}

	@Override
	public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if(clicked != null) {
			clicked.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		if(selectedPage != -1) {
			components.get(selectedPage).draw(mouseX, mouseY, partialTicks);
		} else {
			for(UIComponent comp : components) {
				comp.draw(mouseX, mouseY, partialTicks);
			}
		}
	}
}
