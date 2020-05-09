package won983212.kpatch.wrapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import won983212.kpatch.ObfuscatedReflection;
import won983212.kpatch.input.IInputWrapper;
import won983212.kpatch.input.Korean2Input;
import won983212.kpatch.input.SelectionCursorInput;
import won983212.kpatch.ui.popups.GuiKoreanIndicator;

public class EditBookWrapper extends GuiScreenBook implements IInputWrapper {
	private SelectionCursorInput selection = new SelectionCursorInput(this);
	private Korean2Input input = new Korean2Input(this);
	private GuiKoreanIndicator indicator = new GuiKoreanIndicator();
	
	private String textBuffer;
	private final boolean bookIsUnsigned;
	
	// reflected fields
	private Field bookGettingSigned;
	
	// reflected methods
	private Method pageGetCurrent;
	private Method pageSetCurrent;
	
	public EditBookWrapper(ItemStack book, boolean isUnsigned) {
		super(Minecraft.getMinecraft().player, book, isUnsigned);
		bookIsUnsigned = isUnsigned;
		
		bookGettingSigned = ObfuscatedReflection.getPrivateField(GuiScreenBook.class, this, "bookGettingSigned");
		pageGetCurrent = ObfuscatedReflection.getPrivateMethod(GuiScreenBook.class, this, "pageGetCurrent");
		pageSetCurrent = ObfuscatedReflection.getPrivateMethod(GuiScreenBook.class, this, "pageSetCurrent", String.class);
	}
	
	private void resetInput() {
		textBuffer = ObfuscatedReflection.invokeMethod(pageGetCurrent, this);
		selection.setCursor(textBuffer.length());
		input.cancelAssemble();
	}
	
	private boolean getBookGettingSigned() {
		return ObfuscatedReflection.getPrivateValue(bookGettingSigned, this);
	}
	
	// TODO Implement
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.bookIsUnsigned) {
			if (this.getBookGettingSigned()) {
				// this.keyTypedInTitle(typedChar, keyCode);
			} else {
				textBuffer = ObfuscatedReflection.invokeMethod(pageGetCurrent, this);
				if (!input.handleKeyTyped(typedChar, keyCode)) {
					selection.handleKeyTyped(typedChar, keyCode);
				}
				ObfuscatedReflection.invokeMethod(pageSetCurrent, this, textBuffer);
			}
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);
		resetInput();
	}

	@Override
	public boolean handleComponentClick(ITextComponent component) {
		boolean ret = super.handleComponentClick(component);
		if(ret) resetInput();
		return ret;
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void setText(String text) {
		textBuffer = text;
	}

	@Override
	public String getText() {
		return textBuffer;
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
	public boolean isComponentFocused() {
		return true;
	}
}
