package won983212.kpatch.wrapper;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import won983212.kpatch.input.IInputWrapper;
import won983212.kpatch.input.Korean2Input;
import won983212.kpatch.input.SelectionCursorInput;
import won983212.kpatch.ui.popups.GuiKoreanIndicator;

public class EditBookWrapper extends GuiScreenBook implements IInputWrapper {
    private static final ResourceLocation BOOK_GUI_TEXTURES = new ResourceLocation("textures/gui/book.png");
	private SelectionCursorInput selection = new SelectionCursorInput(this);
	private Korean2Input input = new Korean2Input(this);
	private GuiKoreanIndicator indicator = new GuiKoreanIndicator();
	
	public EditBookWrapper(GuiScreenBook parent) {
		super(Minecraft.getMinecraft().player, parent.book, parent.bookIsUnsigned);
		resetInput();
	}
	
	private void resetInput() {
		if (this.bookIsUnsigned) {
			selection.setCursor(getText().length());
			input.cancelAssemble();
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (this.bookIsUnsigned) {
			if (!input.handleKeyTyped(typedChar, keyCode)) {
				if (!selection.handleKeyTyped(typedChar, keyCode)) {
					if ((keyCode == 28 || keyCode == 156)) {
						if (bookGettingSigned) {
							if (!bookTitle.isEmpty()) {
								sendBookToServer(true);
								this.mc.displayGuiScreen((GuiScreen) null);
							}
						} else {
							selection.write("\n");
						}
					}
				}
			}
			
			if (bookGettingSigned) {
				updateButtons();
				bookIsModified = true;
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
	
	private void drawCursor(boolean isGettingSigned) {
		int i = (this.width - 192) / 2;
		if(isGettingSigned) {
			int l = this.fontRenderer.getStringWidth(bookTitle);
			selection.drawSelectionBox(fontRenderer, i + 36 + (116 - l) / 2, 50, 0);
		} else {
			selection.drawSelectionBox(fontRenderer, i + 36, 34, 116);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if(!bookGettingSigned) {
			if(!this.bookIsUnsigned || cachedComponents != null) {
				super.drawScreen(mouseX, mouseY, partialTicks);
				return;
			}
		}
		
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BOOK_GUI_TEXTURES);
		int i = (this.width - 192) / 2;
		int j = 2;
		this.drawTexturedModalRect(i, 2, 0, 0, 192, 192);
		
		if (bookGettingSigned) {
			String s1 = I18n.format("book.editTitle");
			int k = this.fontRenderer.getStringWidth(s1);
			this.fontRenderer.drawString(s1, i + 36 + (116 - k) / 2, 34, 0);
			int l = this.fontRenderer.getStringWidth(bookTitle);
			this.fontRenderer.drawString(bookTitle, i + 36 + (116 - l) / 2, 50, 0);
			String s2 = I18n.format("book.byAuthor", mc.player.getName());
			int i1 = this.fontRenderer.getStringWidth(s2);
			this.fontRenderer.drawString(TextFormatting.DARK_GRAY + s2, i + 36 + (116 - i1) / 2, 60, 0);
			String s3 = I18n.format("book.finalizeWarning");
			this.fontRenderer.drawSplitString(s3, i + 36, 82, 116, 0);
		} else {
			String s4 = I18n.format("book.pageIndicator", currPage + 1, bookTotalPages);
			this.fontRenderer.drawSplitString(pageGetCurrent(), i + 36, 34, 116, 0);
			int j1 = this.fontRenderer.getStringWidth(s4);
			this.fontRenderer.drawString(s4, i - j1 + 192 - 44, 18, 0);
		}
		
		drawCursor(bookGettingSigned);
		
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
			if (bookGettingSigned) {
				if (text.length() < 16) {
					bookTitle = text;
				}
			} else {
				int i = this.fontRenderer.getWordWrappedHeight(text + TextFormatting.BLACK + "_", 118);
				if (i <= 128 && text.length() < 256) {
					pageSetCurrent(text);
				}
			}
		}
	}

	@Override
	public String getText() {
		if (bookGettingSigned) {
			return bookTitle;
		} else {
			return pageGetCurrent();
		}
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
