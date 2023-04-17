package com.won983212.simpleui.component.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.kpatch.Config;
import com.won983212.simpleui.Theme;
import com.won983212.simpleui.UITools;
import com.won983212.simpleui.animation.IntervalAnimation;
import com.won983212.simpleui.component.UIStyledComponent;
import com.won983212.simpleui.events.IStateChangedEventListener;

public class UITab extends UIStyledComponent<UITab> {
    private static final int ITEM_HEIGHT = 28;

    private int selected = 0;
    private int prevSelected = 0;
    private String[] values = null;
    private final IntervalAnimation transitionAnimation = new IntervalAnimation(100);
    private IStateChangedEventListener<Integer> event;

    public UITab() {
        setBackgroundColor(0xffcfcfcf);
        setForegroundColor(Theme.PRIMARY);
    }

    public UITab setSelectedEvent(IStateChangedEventListener<Integer> event) {
        this.event = event;
        return this;
    }

    public UITab setSelectedIndex(int idx) {
        if (idx < 0) {
            idx = 0;
        } else if (idx >= values.length) {
            idx = values.length - 1;
        }

        this.selected = idx;

        if (event != null) {
            this.selected = event.onChanged(this, selected);
        }

        return this;
    }

    public UITab setTabValues(String[] values) {
        this.values = values;
        return this;
    }

    @Override
    public void renderComponent(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        if (values == null)
            return;

        // background
        UITools.drawArea(ms, x, y, width, height, backgroundColor);

        int selectedY = y + selected * ITEM_HEIGHT;
        if (Config.getBoolean(Config.UI_ANIMATE)) {
            if (prevSelected != selected) {
                transitionAnimation.setRange(prevSelected * ITEM_HEIGHT, selected * ITEM_HEIGHT);
                transitionAnimation.play();
                prevSelected = selected;
            }
            selectedY = (int) (transitionAnimation.update() + y);
        }

        // selected indicator
        UITools.drawArea(ms, x, selectedY, width, ITEM_HEIGHT, Theme.WHITE);
        UITools.drawArea(ms, x, selectedY, 2, ITEM_HEIGHT, foregroundColor);

        for (int i = 0; i < values.length; i++) {
            int textColor = Theme.DARK_GRAY;
            String value = values[i];
            if (i == selected) {
                textColor = foregroundColor;
                value = "§l" + value;
            }
            UITools.drawText(fontRenderer, ms, value, x + 12, y + ITEM_HEIGHT * (i + 0.5f), textColor, 0, UITools.CENTER_V);
        }
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (values != null) {
            int cy = mouseY - y;
            if (cy < values.length * ITEM_HEIGHT) {
                setSelectedIndex(cy / ITEM_HEIGHT);
            }
        }
        return true;
    }
}
