package won983212.kpatch.input;

import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class SelectionCursorInput extends InputEngine {
	private int anchorCursor = 0;
	private int movingCursor = 0;

	// caret state cache
	private int prevStartCaret = -1;
	private int prevEndCaret = -1;
	private String prevText = null;

	private CaretPosition min = new CaretPosition();
	private CaretPosition max = new CaretPosition();
	private List<String> cachedWrappingText = null;

	private int[] cachedCaretOffset = new int[0];

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
	
	public boolean checkIsOutOfRange(int len) {
		return anchorCursor < 0 || movingCursor < 0 || anchorCursor > len || movingCursor > len;
	}
	
	public String getSelectedText() {
		return input.getText().substring(getStartCursor(), getEndCursor());
	}
	
	public void write(String str) {
		super.write(str);
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

	public void drawSelectionBox(FontRenderer fr, int x, int y) {
		String text = input.getText();
		int minX = x + fr.getStringWidth(text.substring(0, getStartCursor()));
		int maxX = x + fr.getStringWidth(text.substring(0, getEndCursor()));
		drawSelectionBox(minX, y, maxX, y + fr.FONT_HEIGHT);
	}
	
	public void drawSelectionBox(FontRenderer fr, int x, int y, int maxWidth) {
		if(maxWidth <= 1) {
			drawSelectionBox(fr, x, y);
			return;
		}

		boolean update = false;
		String text = input.getText();
		if (text != prevText) {
			cachedWrappingText = fr.listFormattedStringToWidth(text + "_", maxWidth);
			if (!cachedWrappingText.isEmpty()) {
				int lastIdx = cachedWrappingText.size() - 1;
				String last = cachedWrappingText.get(lastIdx);
				cachedWrappingText.set(lastIdx, last.substring(0, last.length() - 1));
			}
			prevText = text;
			update = true;
		}

		int start = getStartCursor();
		if (start != prevStartCaret) {
			min = calculateCaretPosition(text, fr, x, start);
			prevStartCaret = start;
			update = true;
		}

		int end = getEndCursor();
		if (end != prevEndCaret) {
			max = calculateCaretPosition(text, fr, x, end);
			prevEndCaret = end;
			update = true;
		}

		if (update) {
			int len = max.y - min.y;
			cachedCaretOffset = new int[len];
			for (int i = 0; i < len; i++) {
				cachedCaretOffset[i] = fr.getStringWidth(cachedWrappingText.get(min.y + i));
			}
		}

		y += min.y * fr.FONT_HEIGHT;
		if (min.y == max.y) { // for single line
			drawSelectionBox(min.x, y, max.x, y + fr.FONT_HEIGHT);
		} else { // for multiple lines
			// first & center lines
			for (int i = 0; i < cachedCaretOffset.length; i++) {
				int endY = y + (i + 1) * fr.FONT_HEIGHT;
				drawSelectionBox(i == 0 ? min.x : x, y + i * fr.FONT_HEIGHT, x + cachedCaretOffset[i], endY);
			}
			// end line
			y += (max.y - min.y) * fr.FONT_HEIGHT;
			drawSelectionBox(x, y, max.x, y + fr.FONT_HEIGHT);
		}
	}

	private CaretPosition calculateCaretPosition(String text, FontRenderer fr, int offsetX, int caret) {
		CaretPosition pos = new CaretPosition();
		int len = 0, tempLen = 0;
		for (String s : cachedWrappingText) {
			tempLen = len + s.length();
			if (tempLen < text.length() && text.charAt(tempLen) == '\n') {
				tempLen = s.length() + 1; // \n by user
			} else {
				tempLen = s.length(); // inserted \n by listFormattedStringToWidth
			}
			if (len + tempLen > caret) {
				break;
			}
			len += tempLen;
			pos.y++;
		}
		pos.x = offsetX + fr.getStringWidth(text.substring(len, caret));
		return pos;
	}

	// draw caret. color will be selected automatically.
	public static void drawSelectionBox(int startX, int startY, int endX, int endY) {
		int color = -1;
		if (startX == endX) {
			endX += 1;
			color = 0xff000000;
		}
		drawSelectionBox(startX, startY, endX, endY, color);
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

	private class CaretPosition {
		public int x = 0; // actual x location
		public int y = 0; // y'th line location
	}
}
