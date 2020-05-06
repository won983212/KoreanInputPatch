package won983212.kpatch.wrapper;

import java.io.IOException;

import org.lwjgl.util.Rectangle;

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
import won983212.kpatch.toolbar.IToolbarContainer;

public class EditSignWrapper extends GuiEditSign implements IInputWrapper, IToolbarContainer {
	private SelectionCursor selection = new SelectionCursor(this);
	private Korean2Input input = new Korean2Input(this);
	private TileEntitySign tileSign;
	private String textBuffer;
	private Rectangle uibounds;

	public EditSignWrapper(TileEntitySign sign) {
		super(sign);
		this.tileSign = sign;
		if (this.tileSign.getBlockType() == Blocks.STANDING_SIGN) {
			uibounds = new Rectangle(0, 0, 0, 0);
		} else {
			uibounds = new Rectangle(0, 0, 0, 0);
		}
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 200 || keyCode == 208 || keyCode == 28 || keyCode == 156 || keyCode == 1) {
			super.keyTyped(typedChar, keyCode);
			if (keyCode != 1) {
				selection.setCursor(tileSign.signText[getEditline()].getUnformattedText().length());
			}
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

		int editLine = getEditline();
		String text = tileSign.signText[editLine].getUnformattedText();
		final int minCur = selection.getMinCursor();
		final int maxCur = selection.getMaxCursor();

		int x1 = (width - fontRenderer.getStringWidth(text)) / 2 - 1;
		int x2, y, color;

		Block block = this.tileSign.getBlockType();
		if (block == Blocks.STANDING_SIGN) {
			y = 71 + editLine * 10;
		} else {
			y = 100 + editLine * 10;
		}

		x1 += fontRenderer.getStringWidth(text.substring(0, minCur));
		if (minCur == maxCur) {
			x2 = x1 + 1;
			color = 0xff000000;
		} else {
			x2 = x1 + fontRenderer.getStringWidth(text.substring(minCur, maxCur));
			color = -1;
		}

		SelectionCursor.drawSelectionBox(x1, y, x2, y + fontRenderer.FONT_HEIGHT, color);
		input.drawIndicator(this);
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

	@Override
	public int getX() {
		return uibounds.getX();
	}

	@Override
	public int getY() {
		return uibounds.getY();
	}

	@Override
	public int getWidth() {
		return uibounds.getWidth();
	}

	@Override
	public int getHeight() {
		return uibounds.getHeight();
	}
}
