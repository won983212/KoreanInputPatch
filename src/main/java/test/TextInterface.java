package test;

import java.awt.event.KeyEvent;

import won983212.kpatch.inputengine.IInputWrapper;

public class TextInterface implements IInputWrapper {
	private String text = "";
	private int anchorCursor = 0;
	private int movingCursor = 0;

	@Override
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public int getAnchorCursor() {
		return anchorCursor;
	}

	@Override
	public int getMovingCursor() {
		return movingCursor;
	}

	@Override
	public void setAnchorCursor(int cursor) {
		this.anchorCursor = cursor;
	}

	@Override
	public void setMovingCursor(int cursor) {
		this.movingCursor = cursor;
	}

	private void setCursor(int cursor) {
		anchorCursor = cursor;
		movingCursor = cursor;
	}

	public void keyTyped(KeyEvent e) {
		int i = e.getKeyCode();
		if (i == KeyEvent.VK_LEFT) {
			if (e.isShiftDown()) {
				if (movingCursor > 0)
					setMovingCursor(movingCursor - 1);
			} else if (anchorCursor != movingCursor) {
				setCursor(Math.min(anchorCursor, movingCursor));
			} else if (anchorCursor > 0) {
				setCursor(anchorCursor - 1);
			}
		} else if (i == KeyEvent.VK_RIGHT) {
			if (e.isShiftDown()) {
				if (movingCursor < text.length())
					setMovingCursor(movingCursor + 1);
			} else if (anchorCursor != movingCursor) {
				setCursor(Math.max(anchorCursor, movingCursor));
			} else if (anchorCursor < text.length()) {
				setCursor(anchorCursor + 1);
			}
		} else if (i == KeyEvent.VK_BACK_SPACE) {
			int minCur = Math.min(anchorCursor, movingCursor);
			String s1 = text.substring(0, minCur);
			String s2 = text.substring(Math.max(anchorCursor, movingCursor));
			if (anchorCursor != movingCursor) {
				text = s1 + s2;
				setCursor(minCur);
			} else if (s1.length() > 0) {
				text = s1.substring(0, s1.length() - 1) + s2;
				setCursor(minCur - 1);
			}
		} else if (i == KeyEvent.VK_ENTER) {
			text = "";
			setCursor(0);
		}
	}

	@Override
	public boolean isFocused() {
		return true;
	}
}