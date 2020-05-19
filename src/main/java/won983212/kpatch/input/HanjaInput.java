package won983212.kpatch.input;

import org.lwjgl.input.Keyboard;

import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.kpatch.ui.Point2i;
import won983212.kpatch.ui.indicators.GuiColorSelector;
import won983212.kpatch.ui.indicators.GuiHanjaSelector;

public class HanjaInput extends InputProcessor {
	private GuiHanjaSelector indicator = new GuiHanjaSelector();
	
	public HanjaInput(IInputWrapper base) {
		super(base);
	}

	@Override
	public boolean handleKeyTyped(char c, int i) {
		if (i == 0) {
			return false;
		} else if (i == Keyboard.KEY_KANJI) {
			indicator.setVisible(!indicator.isShow());
			return true;
		} else if (indicator.isShow()) {
			if (c >= '0' && c <= '9') {
				write("§" + c);
				indicator.select(c);
				return true;
			}
			indicator.setVisible(false);
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
