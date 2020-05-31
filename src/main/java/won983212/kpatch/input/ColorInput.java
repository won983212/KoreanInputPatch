package won983212.kpatch.input;

import won983212.kpatch.Configs;
import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.simpleui.Point2i;
import won983212.simpleui.indicators.GuiColorSelector;

public class ColorInput extends InputProcessor {
	private GuiColorSelector indicator = new GuiColorSelector();
	private boolean showIndicator = false;

	public ColorInput(IInputWrapper base) {
		super(base);
	}

	@Override
	public boolean handleKeyTyped(char c, int i) {
		if (i == 0) {
			return false;
		} else if (i == Configs.getInt(Configs.KEY_COLOR)) {
			showIndicator = !showIndicator;
			return true;
		} else if (showIndicator) {
			if ("0123456789abcdeflmnokr".indexOf(c) != -1) {
				write("§" + c);
			}
			showIndicator = false;
			return true;
		}
		return false;
	}

	@Override
	public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		showIndicator = false;
	}

	public void drawIndicator(Point2i p) {
		drawIndicator(p.x, p.y);
	}

	public void drawIndicator(int x, int y) {
		if(showIndicator) {
			indicator.draw(x, y);
		}
	}
}
