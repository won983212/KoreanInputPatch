package won983212.kpatch.wrapper;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.kpatch.input.ColorInput;
import won983212.kpatch.input.HanjaInput;
import won983212.kpatch.input.KoreanInput;
import won983212.kpatch.ui.Point2i;
import won983212.kpatch.ui.indicators.GuiColorSelector;
import won983212.kpatch.ui.indicators.GuiHanjaSelector;

public class TextfieldWrapper extends GuiTextField implements IInputWrapper {
	private static final Field[] FIELDS = GuiTextField.class.getDeclaredFields();
	private KoreanInput krIn = new KoreanInput(this);
	private ColorInput colorIn = new ColorInput(this);
	private HanjaInput hanjaIn = new HanjaInput(this);

	public TextfieldWrapper(GuiTextField impl) {
		super(impl.getId(), Minecraft.getMinecraft().fontRenderer, impl.x, impl.y, impl.width, impl.height);

		try {
			for (Field f : FIELDS) {
				f.set(this, f.get(impl));
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean textboxKeyTyped(char typedChar, int keyCode) {
		if (!isFocused()) {
			return false;
		} else {
			if (!InputProcessor.processKeyInput(typedChar, keyCode, colorIn, hanjaIn, krIn))
				return super.textboxKeyTyped(typedChar, keyCode);
			return true;
		}
	}
	
	@Override
	public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
		InputProcessor.processMouseClick(mouseX, mouseY, mouseButton, colorIn, hanjaIn, krIn);
		return super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	private Point2i getPopupLocation(int height) {
		int x = this.x - 1;
		int y = this.y - 3 - height;
		
		if (y < 2) {
			y = this.y + this.height + 3;
		}
		
		if (!getEnableBackgroundDrawing()) {
			x -= 1;
			y += y < 2 ? 1 : -1;
		}
		
		return new Point2i(x, y);
	}
	
	@Override
	public void drawTextBox() {
		if (isFocused()) {
			FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
			krIn.drawIndicator(getPopupLocation(fr.FONT_HEIGHT + 2), getText().length(), getMaxStringLength());
			colorIn.drawIndicator(getPopupLocation(GuiColorSelector.HEIGHT));
			hanjaIn.drawIndicator(getPopupLocation(GuiHanjaSelector.HEIGHT));
		}
		super.drawTextBox();
	}

	@Override
	public void setAnchorCursor(int cursor) {
		int endCur = getMovingCursor();
		setCursorPosition(cursor);
		setSelectionPos(endCur);
	}

	@Override
	public void setMovingCursor(int cursor) {
		setSelectionPos(cursor);
	}

	@Override
	public int getAnchorCursor() {
		return getCursorPosition();
	}

	@Override
	public int getMovingCursor() {
		return getSelectionEnd();
	}

	static {
		for (Field f : FIELDS) {
			f.setAccessible(true);
		}
	}
}
