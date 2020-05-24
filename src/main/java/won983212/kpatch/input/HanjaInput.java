package won983212.kpatch.input;

import org.lwjgl.input.Keyboard;

import won983212.kpatch.Configs;
import won983212.kpatch.Hanja;
import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.simpleui.Point2i;
import won983212.simpleui.indicators.GuiHanjaSelector;

public class HanjaInput extends InputProcessor {
	private GuiHanjaSelector indicator = new GuiHanjaSelector();
	
	private int page = 1;
	private char key = 0;
	private char prevKey = 0;
	private Hanja[] hanjaCache = null;
	
	public HanjaInput(IInputWrapper base) {
		super(base);
	}
	
	public void setHanjaKey(char c) {
		page = 1;
		key = c;
		if(prevKey != key) {
			hanjaCache = Hanja.getHanjas(key);
			prevKey = key;
		}
	}
	
	public void nextPage() {
		if(page < getMaxPage()) {
			page++;
		}
	}
	
	public void prevPage() {
		if(page > 1) {
			page--;
		}
	}
	
	private int getMaxPage() {
		if(hanjaCache != null) {
			return (int) Math.ceil(hanjaCache.length / 9.0);
		}
		return 0;
	}

	@Override
	public boolean handleKeyTyped(char c, int i) {
		if (i == 0) {
			return false;
		} else if (i == Configs.getInt(Configs.KEY_HANJA)) {
			if(!indicator.isShow()) {
				if(getEndCursor() - getStartCursor() > 1)
					return true;
				
				int cur = getEndCursor();
				String text = input.getText();
				if(cur != 0) {
					char pc = text.charAt(cur - 1);
					if((pc >= '가' && pc <= '힣') || "ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎㄲㄸㅃㅆ".indexOf(pc) != -1) {
						setHanjaKey(pc);
					} else {
						return true;
					}
				} else {
					return true;
				}
				
				if(hanjaCache == null)
					return true;
			}
			indicator.setVisible(!indicator.isShow());
			return true;
		} else if (indicator.isShow()) {
			if (c >= '1' && c <= '9') {
				input.cancelAllInputContext();
				backspace();
				write(hanjaCache[(page - 1) * 9 + c - '1'].hanja);
			} else if(i == Keyboard.KEY_LEFT) {
				prevPage();
				return true;
			} else if(i == Keyboard.KEY_RIGHT) {
				nextPage();
				return true;
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
		indicator.draw(x, y, key, page, page + "/" + getMaxPage(), hanjaCache);
	}
}
