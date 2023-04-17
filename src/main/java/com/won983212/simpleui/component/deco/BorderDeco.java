package com.won983212.simpleui.component.deco;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.simpleui.Theme;
import com.won983212.simpleui.UITools;
import com.won983212.simpleui.component.UIComponent;
import com.won983212.simpleui.component.UIDecorator;

public class BorderDeco extends UIDecorator {
    public BorderDeco(UIComponent child) {
        setChild(child);
        setForegroundColor(Theme.LIGHT_GRAY);
    }

    @Override
    public void renderComponent(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        super.renderComponent(ms, mouseX, mouseY, partialTicks);
        UITools.drawArea(ms, x, y, 0.5f, height, foregroundColor);
        UITools.drawArea(ms, x, y + height - 0.5f, width, 0.5f, foregroundColor);
        UITools.drawArea(ms, x + width - 0.5f, y, 0.5f, height, foregroundColor);
        UITools.drawArea(ms, x, y, width, 0.5f, foregroundColor);
    }
}
