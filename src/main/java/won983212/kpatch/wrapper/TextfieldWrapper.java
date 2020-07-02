package won983212.kpatch.wrapper;

import java.lang.reflect.Field;

import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import won983212.kpatch.IInputWrapper;
import won983212.kpatch.InputProcessor;
import won983212.kpatch.indicators.GuiColorSelector;
import won983212.kpatch.indicators.GuiKoreanIndicator;
import won983212.kpatch.input.ColorInput;
import won983212.kpatch.input.HanjaInput;
import won983212.kpatch.input.KoreanInput;
import won983212.simpleui.Theme;
import won983212.simpleui.UITools;

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
	
	@Override
	public void drawTextBox() {
		Minecraft mc = Minecraft.getMinecraft();
		if (isFocused()) {
			int ind_x = getEnableBackgroundDrawing() ? (this.x - 1) : (this.x - 2);
			int ind_y = getEnableBackgroundDrawing() ? (y < 2 ? y + 1 : y - 1) : y;
			krIn.setLength(getText().length(), getMaxStringLength());
			InputProcessor.processRenderOnIndicator(ind_x, ind_y, width, height, krIn, colorIn, hanjaIn);
		}
		// TODO Config button
		if(mc.currentScreen instanceof GuiChat) {
            final ScaledResolution scaledresolution = new ScaledResolution(mc);
            int mx = Mouse.getX() * scaledresolution.getScaledWidth() / mc.displayWidth;
            int my = (1 - Mouse.getY() / mc.displayHeight) * scaledresolution.getScaledHeight() - 1;
            int ind_w = mc.fontRenderer.getStringWidth("한글") + 8;
            UITools.drawArea(x + ind_w + 1, y - 14, 11, 11, Theme.BACKGROUND);
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

	@Override
	public void cancelAllInputContext() {
		krIn.cancelAssemble();
	}
}
