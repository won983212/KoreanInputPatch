package com.won983212.simpleui.component.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.kpatch.Config;
import com.won983212.simpleui.Theme;
import com.won983212.simpleui.UITools;
import com.won983212.simpleui.animation.DecimalAnimation;
import com.won983212.simpleui.component.UIStyledComponent;

public class UIKeyBox extends UIStyledComponent<UIKeyBox> {
    private static UIKeyBox editingBox = null;
    private final DecimalAnimation hoverAnimation = new DecimalAnimation(150);
    private boolean hover = false;
    private int key;

    public UIKeyBox() {
        this(0);
    }

    public UIKeyBox(int initialKey) {
        this.key = initialKey;
        setMinimalSize(50, 18);
        setTextShadow(Theme.BACKGROUND_SHADOW);
    }

    @Override
    public String serializeData() {
        return String.valueOf(key);
    }

    @Override
    public void deserializeData(String serialized) {
        key = Integer.parseInt(serialized);
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        editingBox = this;
        return true;
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (keyCode == 0)
            return;
        if (editingBox == this) {
            editingBox = null;
            key = keyCode;
            if (key == 256) {
                key = 0;
            }
        }
    }

    @Override
    protected void renderComponent(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        int color = backgroundColor;
        if (!isEnabled()) {
            color = Theme.LIGHT_GRAY;
        } else if (containsRelative(mouseX, mouseY)) {
            int adj;
            if (Config.getBoolean(Config.UI_ANIMATE)) {
                if (!hover) {
                    hoverAnimation.setReverse(false);
                    hoverAnimation.play();
                    hover = true;
                }
                adj = (int) (hoverAnimation.update() * 30);
            } else {
                adj = 30;
            }
            color = Theme.adjColor(backgroundColor, adj);
        } else {
            hover = false;
        }

        String keyText = "미지정";
        if (editingBox == this) {
            keyText = "...";
        } else if (key != 0) {
            keyText = String.valueOf(key);
        }

        UITools.drawArea(ms, x, y, width, height, color, borderShadow, borderColor, roundRadius);
        UITools.drawText(fontRenderer, ms, keyText, x + width / 2f, y + height / 2f, foregroundColor, textShadow, UITools.CENTER_BOTH);
    }

    public static boolean isKeyBoxEditing() {
        return editingBox != null;
    }
}
