package com.won983212.kpatch.input;

import com.won983212.kpatch.Config;
import com.won983212.kpatch.IInputWrapper;
import com.won983212.kpatch.InputProcessor;
import com.won983212.kpatch.indicators.ColorPickIndicator;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.glfw.GLFW;

public class ColorInput extends InputProcessor {
    public ColorInput(IInputWrapper base, FontRenderer font) {
        super(base, new ColorPickIndicator(font));
    }

    @Override
    public boolean handleKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (keyCode == Config.Key.KEY_COLOR.getKey().getValue()) {
            isShowIndicator = !isShowIndicator;
            return true;
        }
        return false;
    }

    @Override
    public boolean handleCharTyped(char c, int i) {
        if (isShowIndicator) {
            if (isColorCode(c)) {
                input.write("§" + c);
                input.cancelAllInputContext();
            }
            isShowIndicator = false;
            return true;
        }
        return false;
    }

    @Override
    public void onMouseClick(double mouseX, double mouseY, int mouseButton) {
        isShowIndicator = false;
    }

    private boolean isColorCode(char c){
        return "0123456789abcdeflmnokr".indexOf(c) != -1;
    }
}
