package won983212.kpatch.wrapper;

import com.google.common.base.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiTextField;
import won983212.kpatch.input.IInputWrapper;
import won983212.kpatch.input.Korean2Input;
import won983212.kpatch.ui.popups.GuiKoreanIndicator;

public class TextfieldWrapper extends GuiTextField implements IInputWrapper {
	private GuiTextField textfield;
	private Korean2Input input;
	
	// TODO Implement indicator 
	// this indicator is only for GuiChat
	private GuiKoreanIndicator indicator = new GuiKoreanIndicator();

	public TextfieldWrapper(GuiTextField impl) {
		super(impl.getId(), null, impl.x, impl.y, impl.width, impl.height);
		this.textfield = impl;
		this.input = new Korean2Input(this);
	}

	@Override
	public boolean textboxKeyTyped(char typedChar, int keyCode) {
		if (!input.handleKeyTyped(typedChar, keyCode)) {
			return textfield.textboxKeyTyped(typedChar, keyCode);
		}
		return true;
	}

	@Override
	public void drawTextBox() {
		//TODO DEBUG
		//if (Minecraft.getMinecraft().currentScreen instanceof GuiChat && isComponentFocused()) {
			FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
			int x = textfield.x - 2;
			int y = textfield.y - fr.FONT_HEIGHT - 6;
			indicator.drawIndicator(x, y, getText().length(), getMaxStringLength());
		//}
		textfield.drawTextBox();
	}

	@Override
	public void setAnchorCursor(int cursor) {
		int endCur = getMovingCursor();
		setCursorPosition(cursor);
		setSelectionPos(endCur);
	}

	@Override
	public void setMovingCursor(int cursor) {
		setSelectionPos(cursor);
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
	public boolean isComponentFocused() {
		return textfield.isFocused();
	}

	// ========== Pass Overrides ===========

	@Override
	public void setGuiResponder(GuiResponder guiResponderIn) {
		textfield.setGuiResponder(guiResponderIn);
	}

	@Override
	public void updateCursorCounter() {
		textfield.updateCursorCounter();
	}

	@Override
	public void setText(String textIn) {
		textfield.setText(textIn);
	}

	@Override
	public String getText() {
		return textfield.getText();
	}

	@Override
	public String getSelectedText() {
		return textfield.getSelectedText();
	}

	@Override
	public void setValidator(Predicate<String> theValidator) {
		textfield.setValidator(theValidator);
	}

	@Override
	public void writeText(String textToWrite) {
		textfield.writeText(textToWrite);
	}

	@Override
	public void setResponderEntryValue(int idIn, String textIn) {
		textfield.setResponderEntryValue(idIn, textIn);
	}

	@Override
	public void deleteWords(int num) {
		textfield.deleteWords(num);
	}

	@Override
	public void deleteFromCursor(int num) {
		textfield.deleteFromCursor(num);
	}

	@Override
	public int getId() {
		return textfield.getId();
	}

	@Override
	public int getNthWordFromCursor(int numWords) {
		return textfield.getNthWordFromCursor(numWords);
	}

	@Override
	public int getNthWordFromPos(int n, int pos) {
		return textfield.getNthWordFromPos(n, pos);
	}

	@Override
	public int getNthWordFromPosWS(int n, int pos, boolean skipWs) {
		return textfield.getNthWordFromPosWS(n, pos, skipWs);
	}

	@Override
	public void moveCursorBy(int num) {
		textfield.moveCursorBy(num);
	}

	@Override
	public void setCursorPosition(int pos) {
		textfield.setCursorPosition(pos);
	}

	@Override
	public void setCursorPositionZero() {
		textfield.setCursorPositionZero();
	}

	@Override
	public void setCursorPositionEnd() {
		textfield.setCursorPositionEnd();
	}

	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		return textfield.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void setMaxStringLength(int length) {
		textfield.setMaxStringLength(length);
	}

	@Override
	public int getMaxStringLength() {
		return textfield.getMaxStringLength();
	}

	@Override
	public int getCursorPosition() {
		return textfield.getCursorPosition();
	}

	@Override
	public boolean getEnableBackgroundDrawing() {
		return textfield.getEnableBackgroundDrawing();
	}

	@Override
	public void setEnableBackgroundDrawing(boolean enableBackgroundDrawingIn) {
		textfield.setEnableBackgroundDrawing(enableBackgroundDrawingIn);
	}

	@Override
	public void setTextColor(int color) {
		textfield.setTextColor(color);
	}

	@Override
	public void setDisabledTextColour(int color) {
		textfield.setDisabledTextColour(color);
	}

	@Override
	public void setFocused(boolean isFocusedIn) {
		textfield.setFocused(isFocusedIn);
	}

	@Override
	public boolean isFocused() {
		return textfield.isFocused();
	}

	@Override
	public void setEnabled(boolean enabled) {
		textfield.setEnabled(enabled);
	}

	@Override
	public int getSelectionEnd() {
		return textfield.getSelectionEnd();
	}

	@Override
	public int getWidth() {
		return textfield.getWidth();
	}

	@Override
	public void setSelectionPos(int position) {
		textfield.setSelectionPos(position);
	}

	@Override
	public void setCanLoseFocus(boolean canLoseFocusIn) {
		textfield.setCanLoseFocus(canLoseFocusIn);
	}

	@Override
	public boolean getVisible() {
		return textfield.getVisible();
	}

	@Override
	public void setVisible(boolean isVisible) {
		textfield.setVisible(isVisible);
	}
}
