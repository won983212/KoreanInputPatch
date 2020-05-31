package won983212.simpleui.components;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

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
	
	/**
	 * 단독으로 표시할 컴포넌트의 index를 설정합니다. 설정시 해당 component만 표시되며, 이벤트도 그 component만 받습니다.
	 */
	public UIPanel selectPage(int page) {
		selectedPage = page;
		onChangedBounds();
		return this;
	}
	
	/**
	 * Component의 사이즈나 위치가 변경되었을 때 호출됩니다.
	 */
	@Override
	protected void onChangedBounds() {
		if(selectedPage != -1) {
			components.get(selectedPage).setBounds(x, y, width, height);
		}
	}
	
	/**
	 * 키 입력 이벤트입니다. 활성화(Enabled)되어있는 모든 component에서 받을 수 있습니다.
	 */
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

	/**
	 * 마우스 클릭 이벤트입니다. 가장 위에 있는(index가 가장 높은) 컴포넌트만 우선적으로 이벤트를 받습니다.
	 */
	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if(selectedPage != -1) {
			return componentMouseClicked(components.get(selectedPage), mouseX, mouseY, mouseButton);
		} else {
			ListIterator<UIComponent> li = components.listIterator(components.size());
			while (li.hasPrevious()) {
				if (componentMouseClicked(li.previous(), mouseX, mouseY, mouseButton))
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

	/**
	 * 마우스 릴리즈 이벤트입니다. 가장 최근에 클릭된 component만 이 이벤트를 받습니다.
	 */
	@Override
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		if(clicked != null) {
			clicked.onMouseReleased(mouseX, mouseY, state);
			clicked = null;
		}
	}

	/**
	 * 마우스 드래그 이벤트입니다. 가장 최근에 클릭된 component만 이 이벤트를 받습니다.
	 */
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
