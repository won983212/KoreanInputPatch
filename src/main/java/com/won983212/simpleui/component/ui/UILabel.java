package com.won983212.simpleui.component.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.simpleui.UITools;
import com.won983212.simpleui.component.UIStyledComponent;

public class UILabel extends UIStyledComponent<UILabel> {
    private String text;

    public UILabel(String text) {
        setText(text);
    }

    public UILabel setText(String text) {
        this.text = text;
        setMinimalSize(fontRenderer.width(text), fontRenderer.lineHeight);
        return this;
    }

    @Override
    public void renderComponent(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        String[] texts = text.split("\n");
        int stackedY = y;
        for (String str : texts) {
            UITools.drawText(fontRenderer, ms, str, x, stackedY, foregroundColor, textShadow, textArrange, width);
            stackedY += fontRenderer.lineHeight + 2;
        }
    }
}
