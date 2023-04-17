package com.won983212.simpleui.component.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.kpatch.Config;
import com.won983212.simpleui.Theme;
import com.won983212.simpleui.UITools;
import com.won983212.simpleui.animation.DecimalAnimation;
import com.won983212.simpleui.component.UIComponent;
import com.won983212.simpleui.events.IStateChangedEventListener;

public class UISwitch extends UIComponent<UISwitch> {
    private boolean isActive;
    private IStateChangedEventListener<Boolean> event;
    private final DecimalAnimation barAnimation = new DecimalAnimation(100);

    public UISwitch() {
        this(false);
    }

    public UISwitch(boolean isActive) {
        setActive(isActive);
        setMinimalSize(24, 9);
    }

    public UISwitch setChangedEventListener(IStateChangedEventListener<Boolean> event) {
        this.event = event;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public UISwitch setActive(boolean active) {
        this.isActive = active;
        barAnimation.setReverse(!isActive);
        return this;
    }

    @Override
    public String serializeData() {
        return String.valueOf(isActive);
    }

    @Override
    public void deserializeData(String serialized) {
        isActive = serialized.equals("true");
    }

    @Override
    public void renderComponent(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        // background
        UITools.drawArea(ms, x, y, width, height, isActive ? Theme.PRIMARY : Theme.LIGHT_GRAY, 0, 0, 1);

        double animatedX = 0;
        if (Config.getBoolean(Config.UI_ANIMATE)) {
            barAnimation.setReverse(!isActive);
            animatedX = barAnimation.update() * width / 2;
        } else {
            animatedX = isActive ? width / 2 : 0;
        }

        // switch
        final float indX = (float) (x + 0.5 + animatedX);
        UITools.drawArea(ms, indX, y + 0.5f, width / 2 - 1, height - 1, Theme.BACKGROUND, 0, 0, 1);
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        isActive = !isActive;
        if (event != null)
            isActive = event.onChanged(this, isActive);
        if (Config.getBoolean(Config.UI_ANIMATE))
            barAnimation.play();
        return true;
    }
}
