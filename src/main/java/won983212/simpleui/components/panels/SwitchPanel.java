package won983212.simpleui.components.panels;

import java.util.ListIterator;

import net.minecraft.client.renderer.GlStateManager;

public class SwitchPanel extends UIPanel {
	private int selected = 0;
	
	public SwitchPanel setPage(int page) {
		this.selected = page;
		return this;
	}
	
	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		if(!components.isEmpty()) {
			components.get(selected).onKeyTyped(typedChar, keyCode);
		}
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		clicked = null;
		if(!components.isEmpty()) {
			UIComponent comp = components.get(selected);
			if (comp.isInteractive() && comp.containsRelative(mouseX - comp.x, mouseY - comp.y)) {
				comp.onMouseClicked(mouseX - comp.x, mouseY - comp.y, mouseButton);
				clicked = comp;
			}
			comp.onStaticMouseDown(mouseX, mouseY, mouseButton);
		}
		return clicked != null;
	}

	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		if (!components.isEmpty()) {
			UIComponent comp = components.get(selected);
			GlStateManager.translate(x, y, 0);
			comp.draw(mouseX - comp.x, mouseY - comp.y, partialTicks);
			GlStateManager.translate(-x, -y, 0);
		}
	}
}