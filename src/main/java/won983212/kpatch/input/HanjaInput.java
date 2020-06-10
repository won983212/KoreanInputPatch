package won983212.kpatch.input;

import java.awt.Point;

import org.lwjgl.input.Keyboard;

import won983212.kpatch.Configs;
import won983212.kpatch.Hanja;
import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.kpatch.indicators.GuiHanjaSelector;

public class HanjaInput extends InputProcessor {
	private GuiHanjaSelector indicator = new GuiHanjaSelector();
	
	private boolean showIndicator = false;
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
		updateHanjaUI();
	}

	public void nextPage() {
		int max = 0;
		if (hanjaCache != null) {
			max = (int) Math.ceil(hanjaCache.length / 9.0);
		}
		if (page < max) {
			page++;
			updateHanjaUI();
		}
	}
	
	public void prevPage() {
		if(page > 1) {
			page--;
			updateHanjaUI();
		}
	}

	private void updateHanjaUI() {
		if(hanjaCache != null) {
			indicator.setHanjaData(key, page, hanjaCache);
		}
	}
	
	@Override
	public boolean handleKeyTyped(char c, int i) {
		if (i == 0) {
			return false;
		} else if (i == Configs.getInt(Configs.KEY_HANJA)) {
			if(!showIndicator) {
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
			showIndicator = !showIndicator;
			return true;
		} else if (showIndicator) {
			if (c >= '1' && c <= '9') {
				deleteChars(-1);
				write(hanjaCache[(page - 1) * 9 + c - '1'].hanja);
				input.cancelAllInputContext();
			} else if(i == Keyboard.KEY_LEFT) {
				prevPage();
				return true;
			} else if(i == Keyboard.KEY_RIGHT) {
				nextPage();
				return true;
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
	
	public int getIndicatorHeight() {
		return indicator.getHeight();
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
