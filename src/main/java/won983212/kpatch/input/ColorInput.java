package won983212.kpatch.input;

import org.lwjgl.input.Keyboard;

import won983212.kpatch.ui.indicators.GuiColorSelector;

public class ColorInput extends InputProcessor {
	private GuiColorSelector indicator = new GuiColorSelector();
	
	public ColorInput(IInputWrapper base) {
		super(base);
	}

	@Override
	public boolean handleKeyTyped(char c, int i) {
		if(i == Keyboard.KEY_INSERT) {
			indicator.setVisible(!indicator.isShow());
			return true;
		} else if(indicator.isShow()) {
			if("0123456789abcdeflmnokr".indexOf(c) != -1) {
				write("§" + c);
				indicator.select(c);
				return true;
			}
			indicator.setVisible(false);
		}
		return false;
	}
	
	public void drawIndicator(int x, int y) {
		indicator.drawIndicator(x, y);
	}
}
