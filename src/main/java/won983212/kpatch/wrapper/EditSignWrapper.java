package won983212.kpatch.wrapper;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.init.Blocks;
import net.minecraft.util.text.TextComponentString;
import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.kpatch.input.KoreanInput;
import won983212.kpatch.input.SelectionCursorInput;
import won983212.kpatch.ui.indicators.GuiKoreanIndicator;

public class EditSignWrapper extends GuiEditSign implements IInputWrapper {
	private SelectionCursorInput selection = new SelectionCursorInput(this);
	private KoreanInput krIn = new KoreanInput(this);
	
	private String textBuffer;

	public EditSignWrapper(GuiEditSign parent) {
		super(parent.tileSign);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 200 || keyCode == 208 || keyCode == 28 || keyCode == 156 || keyCode == 1) {
			super.keyTyped(typedChar, keyCode);
			if (keyCode != 1) {
				selection.setCursor(tileSign.signText[editLine].getUnformattedText().length());
				krIn.cancelAssemble();
			}
		} else {
			textBuffer = tileSign.signText[editLine].getUnformattedText();
			InputProcessor.processKeyInput(typedChar, keyCode, krIn, selection);
			tileSign.signText[editLine] = new TextComponentString(textBuffer);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		InputProcessor.processMouseClick(mouseX, mouseY, mouseButton, krIn, selection);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);

		String text = tileSign.signText[editLine].getUnformattedText();
		final int textWidth = fontRenderer.getStringWidth(text);
		final int minCur = selection.getStartCursor();
		final int maxCur = selection.getEndCursor();

		int y, indicatorY;
		Block block = this.tileSign.getBlockType();
		if (block == Blocks.STANDING_SIGN) {
			y = 71 + editLine * 10;
			indicatorY = 55;
		} else {
			y = 100 + editLine * 10;
			indicatorY = 85;
		}

		textBuffer = text;
		selection.drawSelectionBox((width - textWidth) / 2 - 1, y, 0);
		krIn.drawIndicator(width / 2 - 47, indicatorY, (int)(textWidth * 100 / 90.0), 100, "%");
	}

	@Override
	public void setText(String text) {
		int width = this.fontRenderer.getStringWidth(text);
		if (width > 90) {
			textBuffer = this.fontRenderer.trimStringToWidth(text, 90);
			krIn.cancelAssemble();
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
}
