package won983212.simpleui.component;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.ListIterator;

import net.minecraft.client.renderer.GlStateManager;

public class UIDecorator extends UIStyledComponent<UIDecorator> {
	private UIComponent child = null;
	private boolean clicked = false;

	public UIDecorator setChild(UIComponent comp) {
		comp.parent = this;
		this.child = comp;
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
		if(child != null) {
			return child.getLayoutMinSize();
		}
		return getLayoutMinSize();
	}

	public void layout() {
		if (child != null) {
			child.arrange(getInnerBounds());
		}
	}

	@Override
	public boolean containsRelative(int x, int y) {
		if(child != null) {
			if (child.isInteractive() && child.containsRelative(x - child.x, y - child.y))
				return true;
		}
		return false;
	}

	@Override
	public void invalidateSize() {
		if(child != null)
			child.invalidateSize();
		super.invalidateSize();
	}

	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		if(child != null)
			child.onKeyTyped(typedChar, keyCode);
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (child != null) {
			if (child.isInteractive() && child.containsRelative(mouseX - child.x, mouseY - child.y)) {
				if (child.onMouseClicked(mouseX - child.x, mouseY - child.y, mouseButton)) {
					clicked = true;
				}
			}
			child.onStaticMouseDown(mouseX, mouseY, mouseButton);
		}

		return clicked;
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		if (clicked) {
			if(child != null)
				child.onMouseReleased(mouseX, mouseY, state);
			clicked = false;
		}
	}

	@Override
	public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
		if (clicked) {
			if(child != null)
				child.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
		}
	}

	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		if (child != null) {
			GlStateManager.translate(x, y, 0);
			child.draw(mouseX - child.x, mouseY - child.y, partialTicks);
			GlStateManager.translate(-x, -y, 0);
		}
	}
}
