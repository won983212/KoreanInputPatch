package won983212.simpleui.components;

import net.minecraft.client.gui.Gui;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;
import won983212.simpleui.components.panels.UIStyledComponent;
import won983212.simpleui.events.IStateChangedEventListener;

public class UITab extends UIStyledComponent<UITab> {
	private static final int ITEM_HEIGHT = 28;
	
	private int selected = 0;
	private String[] values = null;
	private IStateChangedEventListener<Integer> event;
	
	public UITab() {
		setBackgroundColor(0xffdedede);
		setForegroundColor(Theme.PRIMARY);
	}
	
	public UITab setSelectedEvent(IStateChangedEventListener<Integer> event) {
		this.event = event;
		return this;
	}
	
	public UITab setSelectedIndex(int idx) {
		if(idx < 0) {
			idx = 0;
		} else if(idx >= values.length) {
			idx = values.length - 1;
		}
		
		this.selected = idx;
		
		if(event != null) {
			this.selected = event.onChanged(this, selected);
		}
		
		return this;
	}
	
	public UITab setTabValues(String[] values) {
		this.values = values;
		return this;
	}
	
	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		if(values == null) 
			return;
		
		UITools.drawArea(x, y, width, height, backgroundColor);
		for (int i = 0; i < values.length; i++) {
			int textColor = Theme.DARK_GRAY;
			String value = values[i];
			if(i == selected) {
				UITools.drawArea(x, y + i * ITEM_HEIGHT, width, ITEM_HEIGHT, Theme.WHITE);
				UITools.drawArea(x, y + i * ITEM_HEIGHT, 2, ITEM_HEIGHT, foregroundColor);
				textColor = foregroundColor;
				value = "§l" + value;
			}
			UITools.drawText(fontRenderer, value, x + 12, y + ITEM_HEIGHT * (i + 0.5f), textColor, Theme.LIGHT_GRAY, UITools.CENTER_V);
		}
	}
	
	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (values != null) {
			int cy = mouseY - y;
			if (cy < values.length * ITEM_HEIGHT) {
				setSelectedIndex(cy / ITEM_HEIGHT); 
			}
		}
		return true;
	}
}
