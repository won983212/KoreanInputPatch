package won983212.simpleui.components;

import java.awt.Point;

import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.kpatch.indicators.GuiColorSelector;
import won983212.kpatch.indicators.GuiIndicator;
import won983212.kpatch.indicators.GuiKoreanIndicator;
import won983212.kpatch.input.ColorInput;
import won983212.kpatch.input.HanjaInput;
import won983212.kpatch.input.KoreanInput;
import won983212.kpatch.input.SelectionCursorInput;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;
import won983212.simpleui.components.panels.UIComponent;
import won983212.simpleui.components.panels.UIStyledComponent;

public class UITextField extends UIStyledComponent<UITextField> implements IInputWrapper {
	private String text = "";
	private int maxTextLength = 256;
	private boolean isFocused = false;
	private int lineOffset = 0;
	private String hint = "Hint test";
	
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
	
	public UITextField setHint(String hint) {
		this.hint = hint;
		return this;
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
		double textX = x + 2;
		double textY = y + (height - fontRenderer.FONT_HEIGHT) / 2f;
		int color = foregroundColor;
		boolean showHint = text.length() == 0 && !isFocused;
		
		if(!isEnabled() || showHint) {
			color = Theme.LIGHT_GRAY;
		}
		
		if(isFocused) {
			UITools.drawArea(x, y, width, height, Theme.PRIMARY, 0, 0, roundRadius);
			UITools.drawArea(x + 0.5, y + 0.5, width - 1, height - 1, backgroundColor, borderShadow, 0, roundRadius);
		} else {
			UITools.drawArea(x, y, width, height, backgroundColor, borderShadow, borderColor, roundRadius);
		}
		
		String text = fontRenderer.trimStringToWidth(this.text.substring(lineOffset), width);
		if(showHint) {
			text = hint;
		}
		
		UITools.drawText(fontRenderer, text, (float) textX, (float) textY, color, textShadow);
		
		if (isFocused) {
			Point p = calculateActualLocation();
			krIn.setLength(this.text.length(), maxTextLength);
			krIn.draw(p.x, p.y, width, height);
			colorIn.draw(p.x, p.y, width, height);
			hanjaIn.draw(p.x, p.y, width, height);
			
			int startCur = Math.max(0, selection.getStartCursor() - lineOffset);
			int endCur = Math.min(text.length(), selection.getEndCursor() - lineOffset);
			if(startCur < text.length() + 1 && endCur >= 0) {
				double minX = textX + fontRenderer.getStringWidth(text.substring(0, startCur));
				double maxX = textX + fontRenderer.getStringWidth(text.substring(0, endCur));
				minX = Math.min(textX + width, minX);
				maxX = Math.min(textX + width - (textX - x), maxX);
				selection.drawSelectionBox(minX, textY, maxX, textY + fontRenderer.FONT_HEIGHT);
			}
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
		
		int fullTextLen = text.length();
		if(fullTextLen < lineOffset) {
			lineOffset = fullTextLen;
		}
		
		int textLen = fontRenderer.trimStringToWidth(this.text.substring(lineOffset), width).length();
		if(cursor - lineOffset > textLen) {
			lineOffset = cursor - textLen;
		} else if(cursor - lineOffset < 0) {
			lineOffset = cursor;
		}
	}

	@Override
	public void cancelAllInputContext() {
		krIn.cancelAssemble();
	}
}
