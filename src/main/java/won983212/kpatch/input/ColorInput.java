package won983212.kpatch.input;

import won983212.kpatch.Configs;
import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.kpatch.ui.Point2i;
import won983212.kpatch.ui.indicators.GuiColorSelector;

public class ColorInput extends InputProcessor {
	private GuiColorSelector indicator = new GuiColorSelector();

	public ColorInput(IInputWrapper base) {
		super(base);
	}

	@Override
	public boolean handleKeyTyped(char c, int i) {
		if (i == 0) {
			return false;
		} else if (i == Configs.getInt(Configs.KEY_COLOR)) {
			indicator.setVisible(!indicator.isShow());
			return true;
		} else if (indicator.isShow()) {
			if ("0123456789abcdeflmnokr".indexOf(c) != -1) {
				write("§" + c);
			}
			indicator.setVisible(false);
			return true;
		}
		return false;
	}

	@Override
	public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
		indicator.setVisible(false);
	}

	public void drawIndicator(Point2i p) {
		drawIndicator(p.x, p.y);
	}

	public void drawIndicator(int x, int y) {
		indicator.draw(x, y);
	}
}
