package won983212.kpatch.input;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class SelectionCursorInput extends InputEngine {
	private int anchorCursor = 0;
	private int movingCursor = 0;

	public SelectionCursorInput(IInputWrapper base) {
		super(base);
	}

	public int getAnchorCursor() {
		return anchorCursor;
	}

	public int getMovingCursor() {
		return movingCursor;
	}

	public void setAnchorCursor(int cursor) {
		this.anchorCursor = slice(cursor);
	}

	public void setMovingCursor(int cursor) {
		this.movingCursor = slice(cursor);
	}

	public void setCursor(int cursor) {
		setAnchorCursor(cursor);
		setMovingCursor(cursor);
	}
	
	public String getSelectedText() {
		return input.getText().substring(getStartCursor(), getEndCursor());
	}

	private int slice(int x) {
		int len = input.getText().length();
		return x < 0 ? 0 : x > len ? len : x;
	}

	public boolean handleKeyTyped(char c, int i) {
		String text = input.getText();
		if (GuiScreen.isKeyComboCtrlA(i)) {
			setAnchorCursor(0);
			setMovingCursor(text.length());
			return true;
		} else if (GuiScreen.isKeyComboCtrlC(i)) {
			GuiScreen.setClipboardString(this.getSelectedText());
			return true;
		} else if (GuiScreen.isKeyComboCtrlV(i)) {
			write(GuiScreen.getClipboardString());
			return true;
		} else if (GuiScreen.isKeyComboCtrlX(i)) {
			GuiScreen.setClipboardString(this.getSelectedText());
			write("");
			return true;
		} else if (i == Keyboard.KEY_LEFT) {
			if (GuiScreen.isShiftKeyDown()) {
				if (movingCursor > 0)
					setMovingCursor(movingCursor - 1);
			} else if (anchorCursor != movingCursor) {
				setCursor(Math.min(anchorCursor, movingCursor));
			} else if (anchorCursor > 0) {
				setCursor(anchorCursor - 1);
			} else {
				return false;
			}
			return true;
		} else if (i == Keyboard.KEY_RIGHT) {
			if (GuiScreen.isShiftKeyDown()) {
				if (movingCursor < text.length())
					setMovingCursor(movingCursor + 1);
			} else if (anchorCursor != movingCursor) {
				setCursor(Math.max(anchorCursor, movingCursor));
			} else if (anchorCursor < text.length()) {
				setCursor(anchorCursor + 1);
			} else {
				return false;
			}
			return true;
		} else if (i == Keyboard.KEY_BACK) {
			int minCur = Math.min(anchorCursor, movingCursor);
			String s1 = text.substring(0, minCur);
			String s2 = text.substring(Math.max(anchorCursor, movingCursor));
			if (anchorCursor != movingCursor) {
				input.setText(s1 + s2);
				setCursor(minCur);
			} else if (s1.length() > 0) {
				input.setText(s1.substring(0, s1.length() - 1) + s2);
				setCursor(minCur - 1);
			} else {
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	public static void drawSelectionBox(int startX, int startY, int endX, int endY) {
		drawSelectionBox(startX, startY, endX, endY, -1);
	}

	public static void drawSelectionBox(int startX, int startY, int endX, int endY, int color) {
		if (startX < endX) {
			int i = startX;
			startX = endX;
			endX = i;
		}

		if (startY < endY) {
			int j = startY;
			startY = endY;
			endY = j;
		}

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();
		if (color != -1) {
			float f3 = (float) (color >> 24 & 255) / 255.0F;
			float f = (float) (color >> 16 & 255) / 255.0F;
			float f1 = (float) (color >> 8 & 255) / 255.0F;
			float f2 = (float) (color & 255) / 255.0F;
			GlStateManager.color(f, f1, f2, f3);
		} else {
			GlStateManager.color(0.0F, 0.0F, 255.0F, 255.0F);
			GlStateManager.enableColorLogic();
			GlStateManager.colorLogicOp(GlStateManager.LogicOp.OR_REVERSE);
		}
		GlStateManager.disableTexture2D();
		bufferbuilder.begin(7, DefaultVertexFormats.POSITION);
		bufferbuilder.pos((double) startX, (double) endY, 1000.0D).endVertex();
		bufferbuilder.pos((double) endX, (double) endY, 1000.0D).endVertex();
		bufferbuilder.pos((double) endX, (double) startY, 1000.0D).endVertex();
		bufferbuilder.pos((double) startX, (double) startY, 1000.0D).endVertex();
		tessellator.draw();
		if (color == -1) {
			GlStateManager.disableColorLogic();
		}
		GlStateManager.enableTexture2D();
	}
}
