package com.won983212.simpleui.component.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.simpleui.UITools;
import com.won983212.simpleui.component.UIStyledComponent;

public class UIRectangle extends UIStyledComponent<UIRectangle> {
    @Override
    public void renderComponent(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        UITools.drawArea(ms, x, y, width, height, backgroundColor, borderShadow, borderColor, roundRadius);
    }
}
