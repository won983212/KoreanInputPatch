package won983212.kpatch.wrapper;

import java.awt.event.KeyEvent;

import org.lwjgl.input.Keyboard;

import com.google.common.base.Predicate;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import won983212.kpatch.inputengine.IInputWrapper;
import won983212.kpatch.inputengine.KoreanInputEngine;
import net.minecraft.client.gui.GuiTextField;

public class TextfieldWrapper extends GuiTextField implements IInputWrapper {
	private GuiTextField impl;
	private KoreanInputEngine input;

	public TextfieldWrapper(GuiTextField impl) {
		super(impl.getId(), null, impl.x, impl.y, impl.width, impl.height);
		this.impl = impl;
		this.input = new KoreanInputEngine(this);
	}

	@Override
	public void setGuiResponder(GuiResponder guiResponderIn) {
		impl.setGuiResponder(guiResponderIn);
	}

	@Override
	public void updateCursorCounter() {
		impl.updateCursorCounter();
	}

	@Override
	public void setText(String textIn) {
		impl.setText(textIn);
	}

	@Override
	public String getText() {
		return impl.getText();
	}

	@Override
	public String getSelectedText() {
		return impl.getSelectedText();
	}

	@Override
	public void setValidator(Predicate<String> theValidator) {
		impl.setValidator(theValidator);
	}

	@Override
	public void writeText(String textToWrite) {
		impl.writeText(textToWrite);
	}

	@Override
	public void setResponderEntryValue(int idIn, String textIn) {
		impl.setResponderEntryValue(idIn, textIn);
	}

	@Override
	public void deleteWords(int num) {
		impl.deleteWords(num);
	}

	@Override
	public void deleteFromCursor(int num) {
		impl.deleteFromCursor(num);
	}

	@Override
	public int getId() {
		return impl.getId();
	}

	@Override
	public int getNthWordFromCursor(int numWords) {
		return impl.getNthWordFromCursor(numWords);
	}

	@Override
	public int getNthWordFromPos(int n, int pos) {
		return impl.getNthWordFromPos(n, pos);
	}

	@Override
	public int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
		return impl.getNthWordFromPosWS(n, pos, skipWs);
	}

	@Override
	public void moveCursorBy(int num) {
		impl.moveCursorBy(num);
	}

	@Override
	public void setCursorPosition(int pos) {
		impl.setCursorPosition(pos);
	}

	@Override
	public void setCursorPositionZero() {
		impl.setCursorPositionZero();
	}

	@Override
	public void setCursorPositionEnd() {
		impl.setCursorPositionEnd();
	}

	@Override
	public boolean textboxKeyTyped(char typedChar, int keyCode) {
		if (KoreanInputEngine.isKorMode()) {
			if (keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT || keyCode == Keyboard.KEY_RETURN) {
				input.cancelAssemble();
			}
		}
		if (!input.handleKeyTyped(typedChar, keyCode)) {
			return impl.textboxKeyTyped(typedChar, keyCode);
		}
		return true;
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		return impl.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawTextBox() {
		impl.drawTextBox();
	}

	@Override
	public void setMaxStringLength(int length) {
		impl.setMaxStringLength(length);
	}

	@Override
	public int getMaxStringLength() {
		return impl.getMaxStringLength();
	}

	@Override
	public int getCursorPosition() {
		return impl.getCursorPosition();
	}

	@Override
	public boolean getEnableBackgroundDrawing() {
		return impl.getEnableBackgroundDrawing();
	}

	@Override
	public void setEnableBackgroundDrawing(boolean enableBackgroundDrawingIn) {
		impl.setEnableBackgroundDrawing(enableBackgroundDrawingIn);
	}

	@Override
	public void setTextColor(int color) {
		impl.setTextColor(color);
	}

	@Override
	public void setDisabledTextColour(int color) {
		impl.setDisabledTextColour(color);
	}

	@Override
	public void setFocused(boolean isFocusedIn) {
		impl.setFocused(isFocusedIn);
	}

	@Override
	public boolean isFocused() {
		return impl.isFocused();
	}

	@Override
	public void setEnabled(boolean enabled) {
		impl.setEnabled(enabled);
	}

	@Override
	public int getSelectionEnd() {
		return impl.getSelectionEnd();
	}

	@Override
	public int getWidth() {
		return impl.getWidth();
	}

	@Override
	public void setSelectionPos(int position) {
		impl.setSelectionPos(position);
	}

	@Override
	public void setCanLoseFocus(boolean canLoseFocusIn) {
		impl.setCanLoseFocus(canLoseFocusIn);
	}

	@Override
	public boolean getVisible() {
		return impl.getVisible();
	}

	@Override
	public void setVisible(boolean isVisible) {
		impl.setVisible(isVisible);
	}

	@Override
	public int getAnchorCursor() {
		return getCursorPosition();
	}

	@Override
	public int getMovingCursor() {
		return getSelectionEnd();
	}

	@Override
	public void setAnchorCursor(int cursor) {
		int end_cur = getMovingCursor();
		setCursorPosition(cursor);
		setSelectionPos(end_cur);
	}

	@Override
	public void setMovingCursor(int cursor) {
		setSelectionPos(cursor);
	}
}
