package won983212.kpatch.wrapper;

import java.lang.reflect.Field;

import com.google.common.base.Predicate;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiPageButtonList.GuiResponder;
import net.minecraft.client.gui.GuiTextField;
import won983212.kpatch.input.ColorInput;
import won983212.kpatch.input.IInputWrapper;
import won983212.kpatch.input.InputProcessor;
import won983212.kpatch.input.KoreanInput;
import won983212.kpatch.ui.indicators.GuiKoreanIndicator;

public class TextfieldWrapper extends GuiTextField implements IInputWrapper {
	private static final Field[] FIELDS = GuiTextField.class.getDeclaredFields();
	private KoreanInput krIn = new KoreanInput(this);
	private ColorInput colorIn = new ColorInput(this);

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
		if(!InputProcessor.processKeyInput(typedChar, keyCode, colorIn, krIn))
			return super.textboxKeyTyped(typedChar, keyCode);
		return true;
	}

	@Override
	public void drawTextBox() {
		if (isComponentFocused()) {
			FontRenderer fr = Minecraft.getMinecraft().fontRenderer;
			int x = this.x - 1;
			int y = this.y - fr.FONT_HEIGHT - 5;
			if (y < 2) {
				y = this.y + this.height + 3;
			}
			if (!getEnableBackgroundDrawing()) {
				x -= 1;
				y += y < 2 ? 1 : -1;
			}
			krIn.drawIndicator(x, y, getText().length(), getMaxStringLength());
			colorIn.drawIndicator(2, 2);
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

	@Override
	public boolean isComponentFocused() {
		return isFocused();
	}

	static {
		for (Field f : FIELDS) {
			f.setAccessible(true);
		}
	}
}
