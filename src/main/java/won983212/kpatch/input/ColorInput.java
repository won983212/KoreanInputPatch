package won983212.kpatch.input;

import java.awt.Point;

import won983212.kpatch.Configs;
import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.simpleui.indicators.GuiColorSelector;

//TODO Book, sign에 아직 color, hanja적용 안했음
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
				input.cancelAllInputContext();
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

	public void draw(int parentX, int parentY, int parentWidth, int parentHeight) {
		if(showIndicator) {
			indicator.draw(parentX, parentY, parentWidth, parentHeight);
		}
	}
	
	public void draw(int x, int y) {
		if(showIndicator) {
			indicator.draw(x, y);
		}
	}
}
