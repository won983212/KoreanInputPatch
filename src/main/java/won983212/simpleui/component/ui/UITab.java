package won983212.simpleui.component.ui;

import net.minecraft.client.gui.Gui;
import won983212.kpatch.Configs;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;
import won983212.simpleui.animation.DecimalAnimation;
import won983212.simpleui.animation.IntervalAnimation;
import won983212.simpleui.component.UIStyledComponent;
import won983212.simpleui.events.IStateChangedEventListener;

public class UITab extends UIStyledComponent<UITab> {
	private static final int ITEM_HEIGHT = 28;
	
	private int selected = 0;
	private int prevSelected = 0;
	private String[] values = null;
	private IntervalAnimation transitionAnimation = new IntervalAnimation(100);
	private IStateChangedEventListener<Integer> event;
	
	public UITab() {
		setBackgroundColor(0xffcfcfcf);
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
		
		// background
		UITools.drawArea(x, y, width, height, backgroundColor);

		int selectedY = y + selected * ITEM_HEIGHT;
		if (Configs.getBoolean(Configs.UI_ANIMATE)) {
			if(prevSelected != selected) {
				transitionAnimation.setRange(prevSelected * ITEM_HEIGHT, selected * ITEM_HEIGHT);
				transitionAnimation.play();
				prevSelected = selected;
			}
			selectedY = (int) (transitionAnimation.update() + y);
		}
		
		// selected indicator
		UITools.drawArea(x, selectedY, width, ITEM_HEIGHT, Theme.WHITE);
		UITools.drawArea(x, selectedY, 2, ITEM_HEIGHT, foregroundColor);
		
		for (int i = 0; i < values.length; i++) {
			int textColor = Theme.DARK_GRAY;
			String value = values[i];
			if(i == selected) {
				textColor = foregroundColor;
				value = "§l" + value;
			}
			UITools.drawText(fontRenderer, value, x + 12, y + ITEM_HEIGHT * (i + 0.5f), textColor, 0, UITools.CENTER_V);
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
