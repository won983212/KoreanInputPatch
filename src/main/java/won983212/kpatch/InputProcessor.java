package won983212.kpatch;

public abstract class InputProcessor {
	protected IInputWrapper input;

	protected InputProcessor(IInputWrapper input) {
		this.input = input;
	}
	
	public abstract boolean handleKeyTyped(char c, int i);
	
	public void draw(int parentX, int parentY, int parentWidth, int parentHeight) {
	}
	
	public void draw(int x, int y) {
	}
	
	public void onMouseClick(int mouseX, int mouseY, int mouseButton) {
	}

	public int getStartCursor() {
		return Math.min(input.getAnchorCursor(), input.getMovingCursor());
	}

	public int getEndCursor() {
		return Math.max(input.getAnchorCursor(), input.getMovingCursor());
	}

	public boolean isSelectionEnabled() {
		return input.getAnchorCursor() != input.getMovingCursor();
	}

	public void setCursor(int cursor) {
		input.setAnchorCursor(cursor);
		input.setMovingCursor(cursor);
	}

	protected void write(char c) {
		int start = getStartCursor();
		String in = input.getText();
		String s1 = in.substring(0, start);
		String s2 = in.substring(getEndCursor());
		input.setText(s1 + c + s2);
		setCursor(start + 1);
	}

	protected void write(String text) {
		int start = getStartCursor();
		String in = input.getText();
		String s1 = in.substring(0, start);
		String s2 = in.substring(getEndCursor());
		input.setText(s1 + text + s2);
		setCursor(start + text.length());
	}
	
	/**
	 * amount만큼 문자를 삭제합니다. amount가 음수면 왼쪽으로, 양수면 오른쪽으로 삭제합니다.
	 */
	protected boolean deleteChars(int amount) {
		int start = getStartCursor();
		String in = input.getText();
		String s1 = in.substring(0, start);
		String s2 = in.substring(getEndCursor());
		if (input.getMovingCursor() != input.getAnchorCursor()) {
			input.setText(s1 + s2);
			setCursor(start);
		} else if (amount < 0 && start > 0) {
			input.setText(s1.substring(0, s1.length() - 1) + s2);
			setCursor(start - 1);
		} else if (amount > 0 && s2.length() > 0) {
			input.setText(s1 + s2.substring(1));
			setCursor(start);
		} else {
			return false;
		}
		return true;
	}
	
	public static boolean processKeyInput(char charIn, int keyIn, InputProcessor... processors) {
		for(InputProcessor p : processors) {
			if(p.handleKeyTyped(charIn, keyIn))
				return true;
		}
		return false;
	}
	
	public static void processMouseClick(int mouseX, int mouseY, int mouseButton, InputProcessor... processors) {
		for(InputProcessor p : processors) {
			p.onMouseClick(mouseX, mouseY, mouseButton);
		}
	}
	
	public static void processRenderOnIndicator(int parentX, int parentY, int parentWidth, int parentHeight, InputProcessor... processors) {
		for(InputProcessor p : processors) {
			p.draw(parentX, parentY, parentWidth, parentHeight);
		}
	}
	
	public static void processRenderAt(int x, int y, InputProcessor... processors) {
		for(InputProcessor p : processors) {
			p.draw(x, y);
		}
	}
}
