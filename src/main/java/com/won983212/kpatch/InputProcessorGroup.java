package com.won983212.kpatch;

import com.mojang.blaze3d.matrix.MatrixStack;

import java.util.ArrayList;

public class InputProcessorGroup {
    private final ArrayList<InputProcessor> processors = new ArrayList<>();

    public void addInputProcessor(InputProcessor processor) {
        processors.add(processor);
    }

    public boolean keyTyped(int keyCode, int scanCode, int modifiers) {
        for (InputProcessor p : processors) {
            if (p.handleKeyTyped(keyCode, scanCode, modifiers))
                return true;
        }
        return false;
    }

    public boolean charTyped(char typedChar, int keyCode) {
        for (InputProcessor p : processors) {
            if (p.handleCharTyped(typedChar, keyCode))
                return true;
        }
        return false;
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        for (InputProcessor p : processors) {
            p.onMouseClick(mouseX, mouseY, mouseButton);
        }
    }

    public void render(MatrixStack matrixStack, int parentX, int parentY, int parentWidth, int parentHeight) {
        for (InputProcessor p : processors) {
            p.draw(matrixStack, parentX, parentY, parentWidth, parentHeight);
        }
    }
}
