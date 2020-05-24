package won983212.simpleui.components;

import net.minecraft.client.Minecraft;
import won983212.simpleui.UITools;
import won983212.simpleui.animation.DecimalAnimation;
import won983212.simpleui.events.IActionEventListener;
import won983212.kpatch.Configs;
import won983212.simpleui.Theme;

public class UIButton extends UIComponent<UIButton> {
	private String label;
	private DecimalAnimation hoverAnimation = new DecimalAnimation(150);
	private IActionEventListener clickEvent = null;
	private boolean hover = false;
	private boolean isClicking = false;

	public UIButton(String label) {
		setText(label);
	}

	public UIButton setText(String label) {
		this.label = label;
		return this;
	}

	public UIButton addClickListener(IActionEventListener clickEvent) {
		this.clickEvent = clickEvent;
		return this;
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (clickEvent != null) {
			clickEvent.onClick(mouseX, mouseY, mouseButton);
		}
		isClicking = true;
		return true;
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		isClicking = false;
	}

	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		int color = backgroundColor;
		if (isIn(mouseX, mouseY)) {
			int adj;
			if (Configs.getBoolean(Configs.UI_ANIMATE)) {
				if (!hover) {
					hoverAnimation.setReverse(false);
					hoverAnimation.play();
					hover = true;
				}
				adj = (int) (hoverAnimation.update() * 30);
			} else {
				adj = 30;
			}
			color = Theme.adjustColor(backgroundColor, adj);
		} else {
			hover = false;
		}

		int px = x;
		int py = y;

		if (isClicking) {
			px++;
			py++;
		} else {
			UITools.useShadow(Theme.BACKGROUND_SHADOW);
		}

		UITools.drawArea(px, py, width, height, color);
		UITools.useShadow(-1);
		UITools.useTextCenterArea(width, height);
		UITools.useTextCenter(true, true);
		UITools.drawText(fontRenderer, label, px, py, foregroundColor);
	}
}
