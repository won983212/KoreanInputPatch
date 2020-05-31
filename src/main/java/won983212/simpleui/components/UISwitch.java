package won983212.simpleui.components;

import won983212.kpatch.Configs;
import won983212.simpleui.Theme;
import won983212.simpleui.UIComponent;
import won983212.simpleui.UIStyledComponent;
import won983212.simpleui.UITools;
import won983212.simpleui.animation.DecimalAnimation;
import won983212.simpleui.events.IStateChangedEventListener;

public class UISwitch extends UIComponent<UISwitch> {
	public static final int DEFAULT_WIDTH = 24;
	public static final int DEFAULT_HEIGHT = 9;
	private boolean isActive;
	private IStateChangedEventListener<Boolean> event;
	private DecimalAnimation barAnimation = new DecimalAnimation(100);
	
	public UISwitch() {
		setActive(false);
		width = DEFAULT_WIDTH;
		height = DEFAULT_HEIGHT;
	}
	
	public UISwitch setChangedEventListener(IStateChangedEventListener<Boolean> event) {
		this.event = event;
		return this;
	}
	
	public boolean isActive() {
		return isActive;
	}
	
	public UISwitch setActive(boolean active) {
		this.isActive = active;
		barAnimation.setReverse(!isActive);
		return this;
	}
	
	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		UITools.drawArea(x, y, width, height, isActive ? Theme.PRIMARY : Theme.LIGHT_GRAY, 0, 1);
		
		double animatedX = 0;
		if(Configs.getBoolean(Configs.UI_ANIMATE)) {
			animatedX = barAnimation.update() * width / 2;
		} else {
			animatedX = isActive ? width / 2 : 0;
		}
		
		final double indX = x + 0.5 + animatedX;
		UITools.drawArea(indX, y + 0.5, width / 2 - 1, height - 1, Theme.BACKGROUND, 0, 1);
	}
	
	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		isActive = !isActive;
		
		if(event != null) {
			isActive = event.onChanged(this, isActive);
		}
		
		if(Configs.getBoolean(Configs.UI_ANIMATE)) {
			barAnimation.setReverse(!isActive);
			barAnimation.play();
		}
		
		return true;
	}
}
