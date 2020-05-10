package won983212.kpatch.wrapper;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.google.common.collect.Lists;
import com.google.gson.JsonParseException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiUtilRenderComponents;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemWrittenBook;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import won983212.kpatch.ObfuscatedReflection;
import won983212.kpatch.input.IInputWrapper;
import won983212.kpatch.input.Korean2Input;
import won983212.kpatch.input.SelectionCursorInput;
import won983212.kpatch.ui.popups.GuiKoreanIndicator;

public class EditBookWrapper extends GuiScreenBook implements IInputWrapper {
    private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
	private SelectionCursorInput selection = new SelectionCursorInput(this);
	private Korean2Input input = new Korean2Input(this);
	private GuiKoreanIndicator indicator = new GuiKoreanIndicator();
	
	private String textBuffer;
	private final boolean bookIsUnsigned;
	
	// reflected fields
	private Field bookGettingSigned;
	private Field bookTitle;
	private Field bookIsModified;
	private Field currPage;
	private Field bookTotalPages;
	private Field cachedComponents;
	
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
		currPage = ObfuscatedReflection.getPrivateField(GuiScreenBook.class, this, "currPage");
		bookTotalPages = ObfuscatedReflection.getPrivateField(GuiScreenBook.class, this, "bookTotalPages");
		cachedComponents = ObfuscatedReflection.getPrivateField(GuiScreenBook.class, this, "cachedComponents");
		
		// methods
		pageGetCurrent = ObfuscatedReflection.getPrivateMethod(GuiScreenBook.class, this, "pageGetCurrent");
		pageSetCurrent = ObfuscatedReflection.getPrivateMethod(GuiScreenBook.class, this, "pageSetCurrent", String.class);
		updateButtons = ObfuscatedReflection.getPrivateMethod(GuiScreenBook.class, this, "updateButtons");
		sendBookToServer = ObfuscatedReflection.getPrivateMethod(GuiScreenBook.class, this, "sendBookToServer", Boolean.TYPE);
		
		resetInput();
	}
	
	private void loadTextBuffer(boolean bookGettingSigned) {
		if (bookGettingSigned) {
			textBuffer = ObfuscatedReflection.getPrivateValue(bookTitle, this);
		} else {
			textBuffer = ObfuscatedReflection.invokeMethod(pageGetCurrent, this);
		}
	}
	
	private void resetInput() {
		if (this.bookIsUnsigned) {
			loadTextBuffer(ObfuscatedReflection.getPrivateValue(this.bookGettingSigned, this));
			selection.setCursor(textBuffer.length());
			input.cancelAssemble();
		}
	}
	
	private String getPageIndicatorText() {
		int cur = (int) ObfuscatedReflection.getPrivateValue(currPage, this) + 1;
		int total = ObfuscatedReflection.getPrivateValue(bookTotalPages, this);
		return I18n.format("book.pageIndicator", cur, total);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.bookIsUnsigned) {
			boolean bookGettingSigned = ObfuscatedReflection.getPrivateValue(this.bookGettingSigned, this);
			loadTextBuffer(bookGettingSigned);

			if (!input.handleKeyTyped(typedChar, keyCode)) {
				if (!selection.handleKeyTyped(typedChar, keyCode)) {
					if ((keyCode == 28 || keyCode == 156)) {
						if (bookGettingSigned) {
							if (!textBuffer.isEmpty()) {
								ObfuscatedReflection.invokeMethod(sendBookToServer, this, true);
								this.mc.displayGuiScreen((GuiScreen) null);
							}
						} else {
							selection.write("\n");
						}
					}
				}
			}
			
			if (bookGettingSigned) {
				ObfuscatedReflection.invokeMethod(updateButtons, this);
				ObfuscatedReflection.setPrivateValue(bookIsModified, this, true);
				ObfuscatedReflection.setPrivateValue(bookTitle, this, textBuffer);
			} else {
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
		if((boolean) ObfuscatedReflection.getPrivateValue(this.bookGettingSigned, this) == false) {
			if(!this.bookIsUnsigned || ObfuscatedReflection.getPrivateValue(cachedComponents, this) != null) {
				super.drawScreen(mouseX, mouseY, partialTicks);
				return;
			}
		}
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
		int i = (this.width - 192) / 2;
		int j = 2;
		this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);
		
		if ((boolean) ObfuscatedReflection.getPrivateValue(this.bookGettingSigned, this)) {
			String s = ObfuscatedReflection.getPrivateValue(bookTitle, this);
			String s1 = I18n.format("book.editTitle");
			int k = this.fontRenderer.getStringWidth(s1);
			this.fontRenderer.drawString(s1, i + 36 + (116 - k) / 2, 34, 0);
			int l = this.fontRenderer.getStringWidth(s);
			this.fontRenderer.drawString(s, i + 36 + (116 - l) / 2, 50, 0);
			String s2 = I18n.format("book.byAuthor", mc.player.getName());
			int i1 = this.fontRenderer.getStringWidth(s2);
			this.fontRenderer.drawString(TextFormatting.DARK_GRAY + s2, i + 36 + (116 - i1) / 2, 60, 0);
			String s3 = I18n.format("book.finalizeWarning");
			this.fontRenderer.drawSplitString(s3, i + 36, 82, 116, 0);
		} else {
			String s4 = getPageIndicatorText();
			String s5 = ObfuscatedReflection.invokeMethod(pageGetCurrent, this);
			this.fontRenderer.drawSplitString(s5, i + 36, 34, 116, 0);
			int j1 = this.fontRenderer.getStringWidth(s4);
			this.fontRenderer.drawString(s4, i - j1 + 192 - 44, 18, 0);
		}
		
		// components rendering (GuiScreen.drawScreen)
		for (i = 0; i < this.buttonList.size(); ++i) {
			((GuiButton) this.buttonList.get(i)).drawButton(this.mc, mouseX, mouseY, partialTicks);
		}
		for (j = 0; j < this.labelList.size(); ++j) {
			((GuiLabel) this.labelList.get(j)).drawLabel(this.mc, mouseX, mouseY);
		}
	}

	@Override
	public void setText(String text) {
		if (bookIsUnsigned) {
			if ((boolean) ObfuscatedReflection.getPrivateValue(this.bookGettingSigned, this)) {
				if (text.length() < 16) {
					textBuffer = text;
				}
			} else {
				int i = this.fontRenderer.getWordWrappedHeight(text + TextFormatting.BLACK + "_", 118);
				if (i <= 128 && text.length() < 256) {
					textBuffer = text;
				}
			}
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
