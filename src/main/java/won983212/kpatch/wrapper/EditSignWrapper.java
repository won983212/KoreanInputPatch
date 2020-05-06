package won983212.kpatch.wrapper;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import won983212.kpatch.ObfuscatedReflection;
import won983212.kpatch.input.IInputWrapper;
import won983212.kpatch.input.Korean2Input;
import won983212.kpatch.input.SelectionCursor;

public class EditSignWrapper extends GuiEditSign implements IInputWrapper {
	private SelectionCursor selection = new SelectionCursor(this);
	private Korean2Input input = new Korean2Input(this);
	private TileEntitySign tileSign;
	private String textBuffer;

	public EditSignWrapper(TileEntitySign sign) {
		super(sign);
		this.tileSign = sign;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 200 || keyCode == 208 || keyCode == 28 || keyCode == 156) {
			super.keyTyped(typedChar, keyCode);
			selection.setCursor(tileSign.signText[getEditline()].getUnformattedText().length());
		} else if (keyCode == 1) {
			super.keyTyped(typedChar, keyCode);
		} else {
			int editLine = getEditline();
			textBuffer = tileSign.signText[editLine].getUnformattedText();
			if (!input.handleKeyTyped(typedChar, keyCode)) {
				selection.keyTyped(keyCode);
			}
			tileSign.signText[editLine] = new TextComponentString(textBuffer);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		Block block = this.tileSign.getBlockType();

		// TODO set x,y by block type.
		if (block == Blocks.STANDING_SIGN) {
			// standing
		} else {
			// block
		}
		
		String text = tileSign.signText[getEditline()].getUnformattedText();
		int minCur = Math.min(getAnchorCursor(), getMovingCursor());
		int maxCur = Math.max(getAnchorCursor(), getMovingCursor());
		int x1 = (width - fontRenderer.getStringWidth(text)) / 2 + fontRenderer.getStringWidth(text.substring(0, minCur)) - 1;
		int x2 = x1 + (minCur == maxCur ? 1 : fontRenderer.getStringWidth(text.substring(minCur, maxCur)));
		int y = 71;
		SelectionCursor.drawSelectionBox(x1, y, x2, y + fontRenderer.FONT_HEIGHT);
	}

	private int getEditline() {
		return ObfuscatedReflection.getPrivateValue(GuiEditSign.class, this, "editLine");
	}

	@Override
	public void setText(String text) {
		int width = this.fontRenderer.getStringWidth(text);
		if (width > 90) {
			textBuffer = this.fontRenderer.trimStringToWidth(text, 90);
		} else {
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
