package won983212.simpleui.component;

import java.util.ListIterator;

import net.minecraft.client.renderer.GlStateManager;

/**
 * SwitchPanel은 여러 개의 컴포넌트중 하나만 보여주는 패널입니다. 미리 컴포넌트들을 추가해두었다가 상황에 따라 다른 컴포넌트를 보여줄 수 있습니다.
 * UITab과 함깨 사용하면 유용합니다.
 */
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
		}
		return clicked != null;
	}
	
	@Override
	public void onStaticMouseDown(int mouseX, int mouseY, int mouseButton) {
		if (!components.isEmpty()) {
			UIComponent comp = components.get(selected);
			comp.onStaticMouseDown(mouseX, mouseY, mouseButton);
		}
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
