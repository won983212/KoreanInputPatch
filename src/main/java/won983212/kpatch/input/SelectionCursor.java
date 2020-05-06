package won983212.kpatch.input;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;

public class SelectionCursor {
	private int anchorCursor = 0;
	private int movingCursor = 0;
	private IInputWrapper base;

	public SelectionCursor(IInputWrapper base) {
		this.base = base;
	}

	public int getAnchorCursor() {
		return anchorCursor;
	}

	public int getMovingCursor() {
		return movingCursor;
	}

	public int getMinCursor() {
		return Math.min(anchorCursor, movingCursor);
	}
	
	public int getMaxCursor() {
		return Math.max(anchorCursor, movingCursor);
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

	private int slice(int x) {
		int len = base.getText().length();
		return x < 0 ? 0 : x > len ? len : x;
	}

	public void keyTyped(int i) {
		String text = base.getText();
		if (i == Keyboard.KEY_LEFT) {
			if (GuiScreen.isShiftKeyDown()) {
				if (movingCursor > 0)
					setMovingCursor(movingCursor - 1);
			} else if (anchorCursor != movingCursor) {
				setCursor(Math.min(anchorCursor, movingCursor));
			} else if (anchorCursor > 0) {
				setCursor(anchorCursor - 1);
			}
		} else if (i == Keyboard.KEY_RIGHT) {
			if (GuiScreen.isShiftKeyDown()) {
				if (movingCursor < text.length())
					setMovingCursor(movingCursor + 1);
			} else if (anchorCursor != movingCursor) {
				setCursor(Math.max(anchorCursor, movingCursor));
			} else if (anchorCursor < text.length()) {
				setCursor(anchorCursor + 1);
			}
		} else if (i == Keyboard.KEY_BACK) {
			int minCur = Math.min(anchorCursor, movingCursor);
			String s1 = text.substring(0, minCur);
			String s2 = text.substring(Math.max(anchorCursor, movingCursor));
			if (anchorCursor != movingCursor) {
				base.setText(s1 + s2);
				setCursor(minCur);
			} else if (s1.length() > 0) {
				base.setText(s1.substring(0, s1.length() - 1) + s2);
				setCursor(minCur - 1);
			}
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
		if(color != -1) {
			float f3 = (float)(color >> 24 & 255) / 255.0F;
	        float f = (float)(color >> 16 & 255) / 255.0F;
	        float f1 = (float)(color >> 8 & 255) / 255.0F;
	        float f2 = (float)(color & 255) / 255.0F;
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
		if(color == -1) {
			GlStateManager.disableColorLogic();
		}
		GlStateManager.enableTexture2D();
	}
}
