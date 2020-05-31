package won983212.simpleui.components.panels;

import java.util.Collection;
import java.util.ListIterator;

import won983212.simpleui.UIComponent;

public class UIMultiplexerPanel extends UIPanel {
	private int selectedPage = -1;

	@Override
	public UIMultiplexerPanel addAll(Collection<? extends UIComponent> components) {
		super.addAll(components);
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

	@Override
	protected void onChangedBounds() {
		getComponent(selectedPage).setBounds(x, y, width, height);
	}

	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		componentKeyType(getComponent(selectedPage), typedChar, keyCode);
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		return componentMouseClicked(getComponent(selectedPage), mouseX, mouseY, mouseButton);
	}

	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		getComponent(selectedPage).draw(mouseX, mouseY, partialTicks);
	}
}
