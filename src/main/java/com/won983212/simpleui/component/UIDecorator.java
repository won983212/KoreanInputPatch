package com.won983212.simpleui.component;

import com.mojang.blaze3d.matrix.MatrixStack;

import java.awt.*;

public class UIDecorator extends UIStyledComponent<UIDecorator> {
    private UIComponent child = null;
    private boolean clicked = false;

    public UIDecorator setChild(UIComponent comp) {
        comp.parent = this;
        this.child = comp;
        return this;
    }

    @Override
    public void requestLayout() {
        if (parent == null) {
            invalidateSize();
            layout();
        } else
            parent.requestLayout();
    }

    @Override
    public void arrange(Rectangle available) {
        super.arrange(available);
        layout();
    }

    @Override
    public String serializeData() {
        if (child != null) {
            return child.serializeData();
        } else {
            return super.serializeData();
        }
    }

    @Override
    public void deserializeData(String serialized) {
        if (child != null) {
            child.deserializeData(serialized);
        } else {
            super.deserializeData(serialized);
        }
    }

    @Override
    public Dimension measureMinSize() {
        if (child != null) {
            return child.getLayoutMinSize();
        }
        return getLayoutMinSize();
    }

    public void layout() {
        if (child != null) {
            child.arrange(getInnerBounds());
        }
    }

    @Override
    public boolean containsRelative(int x, int y) {
        if (child != null) {
            return child.isInteractive() && child.containsRelative(x - child.x, y - child.y);
        }
        return false;
    }

    @Override
    public void invalidateSize() {
        if (child != null)
            child.invalidateSize();
        super.invalidateSize();
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (child != null)
            child.onKeyTyped(typedChar, keyCode);
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (child != null) {
            if (child.isInteractive() && child.containsRelative(mouseX - child.x, mouseY - child.y)) {
                if (child.onMouseClicked(mouseX - child.x, mouseY - child.y, mouseButton)) {
                    clicked = true;
                }
            }
        }

        return clicked;
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int state) {
        if (clicked) {
            if (child != null)
                child.onMouseReleased(mouseX, mouseY, state);
            clicked = false;
        }
    }

    @Override
    public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clicked) {
            if (child != null)
                child.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    public void onStaticMouseDown(int mouseX, int mouseY, int mouseButton) {
        if (child != null)
            child.onStaticMouseDown(mouseX, mouseY, mouseButton);
    }

    public void onLostFocus() {
        if (child != null)
            child.onLostFocus();
    }

    public void onGotFocus() {
        if (child != null)
            child.onGotFocus();
    }

    @Override
    public void renderComponent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        if (child != null) {
            matrixStack.translate(x, y, 0);
            child.draw(matrixStack, mouseX - child.x, mouseY - child.y, partialTicks);
            matrixStack.translate(-x, -y, 0);
        }
    }
}
