package won983212.simpleui.components;

import net.minecraft.client.Minecraft;
import won983212.simpleui.UITools;
import won983212.simpleui.animation.DecimalAnimation;
import won983212.simpleui.components.panels.UIStyledComponent;
import won983212.simpleui.events.IClickEventListener;
import won983212.kpatch.Configs;
import won983212.simpleui.DirWeights;
import won983212.simpleui.Theme;

public class UIButton extends UIStyledComponent<UIButton> {
	private String label;
	private DecimalAnimation hoverAnimation = new DecimalAnimation(150);
	private IClickEventListener clickEvent = null;
	private boolean hover = false;
	private boolean isClicking = false;

	public UIButton(String label) {
		setText(label);
		setPadding(new DirWeights(2));
	}

	public UIButton setText(String label) {
		this.label = label;
		setMinimalSize(fontRenderer.getStringWidth(label), fontRenderer.FONT_HEIGHT);
		return this;
	}

	public UIButton setClickListener(IClickEventListener clickEvent) {
		this.clickEvent = clickEvent;
		return this;
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		if (clickEvent != null) {
			clickEvent.onClick(this, mouseX, mouseY, mouseButton);
		}
		isClicking = true;
		return true;
	}

	@Override
	public void onMouseReleased(int mouseX, int mouseY, int state) {
		isClicking = false;
	}

	@Override
	public void renderComponent(int mouseX, int mouseY, float partialTicks) {
		int color = backgroundColor;
		if (!isEnabled()) {
			color = Theme.LIGHT_GRAY;
		} else if (containsRelative(mouseX, mouseY)) {
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
			color = Theme.adjColor(backgroundColor, adj);
		} else {
			hover = false;
		}

		float px = x;
		float py = y;

		if (isClicking) {
			px += 0.5;
			py += 0.5;
			UITools.drawArea(px, py, width, height, color, 0, 0);
		} else {
			UITools.drawArea(px, py, width, height, color, Theme.BACKGROUND_SHADOW, 0);
		}
		
		UITools.drawText(fontRenderer, label, px + width / 2, py + height / 2, foregroundColor, Theme.BACKGROUND_SHADOW, UITools.CENTER_BOTH);
	}
}
