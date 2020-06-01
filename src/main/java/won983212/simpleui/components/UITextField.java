package won983212.simpleui.components;

import java.awt.Point;

import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.kpatch.input.ColorInput;
import won983212.kpatch.input.HanjaInput;
import won983212.kpatch.input.KoreanInput;
import won983212.kpatch.input.SelectionCursorInput;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;
import won983212.simpleui.components.panels.UIComponent;
import won983212.simpleui.components.panels.UIStyledComponent;
import won983212.simpleui.indicators.GuiColorSelector;
import won983212.simpleui.indicators.GuiIndicator;
import won983212.simpleui.indicators.GuiKoreanIndicator;

//TODO LineOffset 구현
public class UITextField extends UIStyledComponent<UITextField> implements IInputWrapper {
	private String text = "";
	private int maxTextLength = 256;
	private boolean isFocused = false;
	
	private SelectionCursorInput selection = new SelectionCursorInput(this);
	private KoreanInput krIn = new KoreanInput(this);
	private ColorInput colorIn = new ColorInput(this);
	private HanjaInput hanjaIn = new HanjaInput(this);
	
	public UITextField() {
		setMinimalSize(80, 14);
		setBackgroundColor(Theme.WHITE);
		setForegroundColor(Theme.BLACK);
	}
	
	public UITextField setMaxTextLength(int len) {
		this.maxTextLength = len;
		return this;
	}
	
	public int getMaxTextLength() {
		return maxTextLength;
	}
	
	@Override
	public void onKeyTyped(char typedChar, int keyCode) {
		InputProcessor.processKeyInput(typedChar, keyCode, colorIn, hanjaIn, krIn, selection);
	}

	@Override
	public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
		InputProcessor.processMouseClick(mouseX, mouseY, mouseButton, colorIn, hanjaIn, krIn, selection);
		return true;
	}

	@Override
	public void onLostFocus() {
		isFocused = false;
	}

	@Override
	public void onGotFocus() {
		isFocused = true;
	}

	@Override
	protected void renderComponent(int mouseX, int mouseY, float partialTicks) {
		int textX = x + 1;
		int textY = y + (height - fontRenderer.FONT_HEIGHT) / 2;
		
		UITools.drawArea(x, y, width, height, backgroundColor, shadow, roundRadius);
		UITools.drawText(fontRenderer, text, textX, textY, foregroundColor, shadow, 0);
		
		if (isFocused) {
			Point p = calculateActualLocation();
			krIn.setLength(text.length(), maxTextLength);
			krIn.draw(p.x, p.y, width, height);
			colorIn.draw(p.x, p.y, width, height);
			hanjaIn.draw(p.x, p.y, width, height);
			selection.drawSelectionBox(textX, textY, 0);
		}
	}

	@Override
	public void setText(String text) {
		if (text.length() > maxTextLength) {
			this.text = text.substring(0, maxTextLength);
			cancelAllInputContext();
		} else {
			this.text = text;
		}
	}

	@Override
	public String getText() {
		return text;
	}

	@Override
	public int getAnchorCursor() {
		return selection.getAnchorCursor();
	}

	@Override
	public int getMovingCursor() {
		return selection.getMovingCursor();
	}

	@Override
	public void setAnchorCursor(int cursor) {
		selection.setAnchorCursor(cursor);
	}

	@Override
	public void setMovingCursor(int cursor) {
		selection.setMovingCursor(cursor);
	}

	@Override
	public void cancelAllInputContext() {
		krIn.cancelAssemble();
	}
}
