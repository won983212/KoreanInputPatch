package com.debug;

import java.awt.event.KeyEvent;

public class TextInterface  {
	private String text = "";
	private int anchorCursor = 0;
	private int movingCursor = 0;

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public int getAnchorCursor() {
		return anchorCursor;
	}

	public int getMovingCursor() {
		return movingCursor;
	}

	public void setAnchorCursor(int cursor) {
		this.anchorCursor = cursor;
	}

	public void setMovingCursor(int cursor) {
		this.movingCursor = cursor;
	}

	private void setCursor(int cursor) {
		anchorCursor = cursor;
		movingCursor = cursor;
	}
	
	private boolean isAllowed(char c) {
		return "`1234567890-=\\~!@#$%^&*()_+|qwertyuiop[]asdfghjkl;'zxcvbnm,./QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>? ".indexOf(c) != -1;
	}

	public void keyTyped(KeyEvent e) {
		int i = e.getKeyCode();
		int minCur = Math.min(anchorCursor, movingCursor);
		int maxCur = Math.max(anchorCursor, movingCursor);
		if (i == KeyEvent.VK_LEFT) {
			if (e.isShiftDown()) {
				if (movingCursor > 0)
					setMovingCursor(movingCursor - 1);
			} else if (anchorCursor != movingCursor) {
				setCursor(minCur);
			} else if (anchorCursor > 0) {
				setCursor(anchorCursor - 1);
			}
		} else if (i == KeyEvent.VK_RIGHT) {
			if (e.isShiftDown()) {
				if (movingCursor < text.length())
					setMovingCursor(movingCursor + 1);
			} else if (anchorCursor != movingCursor) {
				setCursor(maxCur);
			} else if (anchorCursor < text.length()) {
				setCursor(anchorCursor + 1);
			}
		} else if (i == KeyEvent.VK_BACK_SPACE) {
			String s1 = text.substring(0, minCur);
			String s2 = text.substring(maxCur);
			if (anchorCursor != movingCursor) {
				text = s1 + s2;
				setCursor(minCur);
			} else if (s1.length() > 0) {
				text = s1.substring(0, s1.length() - 1) + s2;
				setCursor(minCur - 1);
			}
		} else if (i == KeyEvent.VK_ENTER) {
			String s1 = text.substring(0, minCur);
			String s2 = text.substring(maxCur);
			text = s1 + '\n' + s2;
			setCursor(s1.length()+1);
		} else {
			char c = e.getKeyChar();
			if (isAllowed(c)) {
				String s1 = text.substring(0, minCur);
				String s2 = text.substring(maxCur);
				text = s1 + c + s2;
				setCursor((s1+c).length());
			}
		}
	}
}