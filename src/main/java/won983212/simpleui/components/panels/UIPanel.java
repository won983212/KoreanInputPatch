package won983212.simpleui.components.panels;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ListIterator;

import net.minecraft.client.renderer.GlStateManager;

//TODO Border 기능 추가, SettingPanel 디자인 개선
public class UIPanel extends UIComponent<UIPanel> {
	protected ArrayList<UIComponent> components = new ArrayList<>();
	protected UIComponent clicked = null;
	private UIComponent focusd = null;

	public UIPanel add(UIComponent comp) {
		comp.parent = this;
		components.add(comp);
		return this;
	}

	public UIPanel clearComponents() {
		components.clear();
		focusd = null;
		return this;
	}
	
	@Override
	public void requestLayout() {
		if (parent == null) {
			invalidateSize();
			layout();
		} else
			parent.requestLayout();
	}

	@Override
	public void arrange(Rectangle available) {
		super.arrange(available);
		layout();
	}

	@Override
	public Dimension measureMinSize() {
		Dimension dim = new Dimension();
		for (UIComponent obj : components) {
			Dimension clientDim = obj.getLayoutMinSize();
			dim.width = Math.max(dim.width, clientDim.width);
			dim.height = Math.max(dim.height, clientDim.height);
		}
		return dim;
	}

	/**
	 * 자식 컴포넌트들을 전부 arrange합니다.
	 */
	public void layout() {
		Rectangle available = getInnerBounds();
		for (UIComponent obj : components) {
			obj.arrange(available);
		}
	}
	
	@Override
	public boolean containsRelative(int x, int y) {
		for (UIComponent obj : components) {
			if (obj.isInteractive() && obj.containsRelative(x - obj.x, y - obj.y))
				return true;
		}
		return false;
	}

	@Override
	public void invalidateSize() {
		for (UIComponent obj : components)
			obj.invalidateSize();
		super.invalidateSize();
	}

	/**
	 * 컴포넌트 리스트상 <code>index</code> 위치에 있는 component를 반환합니다.
	 */
	public UIComponent getComponent(int index) {
		return components.get(index);
	}

	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		for (UIComponent comp : components) {
			if (comp instanceof UIPanel || focusd == comp)
				comp.onKeyTyped(typedChar, keyCode);
		}
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		ListIterator<UIComponent> li = components.listIterator(components.size());
		clicked = null;
		
		while (li.hasPrevious()) {
			UIComponent comp = li.previous();
			if (comp.isInteractive() && comp.containsRelative(mouseX - comp.x, mouseY - comp.y)) {
				if (comp.onMouseClicked(mouseX - comp.x, mouseY - comp.y, mouseButton)) {
					if(clicked == null) {
						setFocus(comp);
						clicked = comp;
					}
				}
			}
			comp.onStaticMouseDown(mouseX, mouseY, mouseButton);
		}
		
		if (clicked != null) {
			setFocus(null);
			return true;
		} else {
			return false;
		}
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

	public void setFocus(UIComponent obj) {
		if (focusd == obj)
			return;
		if (focusd != null)
			focusd.onLostFocus();
		focusd = obj;
		if (focusd != null)
			focusd.onGotFocus();
	}

	@Override
	public void onLostFocus() {
		super.onLostFocus();
		setFocus(null);
	}

	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		for (UIComponent comp : components) {
			GlStateManager.translate(x, y, 0);
			comp.draw(mouseX - comp.x, mouseY - comp.y, partialTicks);
			GlStateManager.translate(-x, -y, 0);
		}
	}
}
