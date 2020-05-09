package won983212.kpatch.wrapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
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
	private Field bookTitle;
	private Field bookIsModified;
	
	// reflected methods
	private Method pageGetCurrent;
	private Method pageSetCurrent;
	private Method updateButtons;
	private Method sendBookToServer;
	
	public EditBookWrapper(ItemStack book, boolean isUnsigned) {
		super(Minecraft.getMinecraft().player, book, isUnsigned);
		bookIsUnsigned = isUnsigned;
		
		// fields
		bookGettingSigned = ObfuscatedReflection.getPrivateField(GuiScreenBook.class, this, "bookGettingSigned");
		bookTitle = ObfuscatedReflection.getPrivateField(GuiScreenBook.class, this, "bookTitle");
		bookIsModified = ObfuscatedReflection.getPrivateField(GuiScreenBook.class, this, "bookIsModified");
		
		// methods
		pageGetCurrent = ObfuscatedReflection.getPrivateMethod(GuiScreenBook.class, this, "pageGetCurrent");
		pageSetCurrent = ObfuscatedReflection.getPrivateMethod(GuiScreenBook.class, this, "pageSetCurrent", String.class);
		updateButtons = ObfuscatedReflection.getPrivateMethod(GuiScreenBook.class, this, "updateButtons");
		sendBookToServer = ObfuscatedReflection.getPrivateMethod(GuiScreenBook.class, this, "sendBookToServer", Boolean.TYPE);
	}
	
	private void resetInput() {
		if ((boolean) ObfuscatedReflection.getPrivateValue(this.bookGettingSigned, this)) {
			textBuffer = ObfuscatedReflection.invokeMethod(pageGetCurrent, this);
		} else {
			textBuffer = ObfuscatedReflection.getPrivateValue(bookTitle, this);
		}
		selection.setCursor(textBuffer.length());
		input.cancelAssemble();
	}
	
	// TODO Implement
	// ERROR: Title isn't working.
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.bookIsUnsigned) {
			boolean bookGettingSigned = ObfuscatedReflection.getPrivateValue(this.bookGettingSigned, this);
			if (bookGettingSigned) {
				textBuffer = ObfuscatedReflection.getPrivateValue(bookTitle, this);
				if (!input.handleKeyTyped(typedChar, keyCode)) {
					if (!selection.handleKeyTyped(typedChar, keyCode)) {
						if ((keyCode == 28 || keyCode == 156) && !textBuffer.isEmpty()) {
							ObfuscatedReflection.invokeMethod(sendBookToServer, this, true);
							this.mc.displayGuiScreen((GuiScreen) null);
						}
					}
				}
				ObfuscatedReflection.invokeMethod(updateButtons, this);
				ObfuscatedReflection.setPrivateValue(bookIsModified, this, true);
				ObfuscatedReflection.setPrivateValue(bookTitle, this, textBuffer);
			} else {
				textBuffer = ObfuscatedReflection.invokeMethod(pageGetCurrent, this);
				if (!input.handleKeyTyped(typedChar, keyCode)) {
					if(!selection.handleKeyTyped(typedChar, keyCode)) {
						if(keyCode == 28 || keyCode == 156) {
							selection.write("\n");
						}
					}
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
		int i = this.fontRenderer.getWordWrappedHeight(text + TextFormatting.BLACK + "_", 118);
		if (i <= 128 && text.length() < 256) {
			textBuffer = text;
		}
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
