package com.won983212.simpleui.component;

import com.mojang.blaze3d.matrix.MatrixStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.ListIterator;

public class UIPanel extends UIStyledComponent<UIPanel> {
    protected ArrayList<UIComponent> components = new ArrayList<>();
    protected UIComponent clicked = null;
    private UIComponent focusd = null;

    public UIPanel add(UIComponent comp) {
        comp.parent = this;
        components.add(comp);
        return this;
    }

    public UIPanel clearComponents() {
        components.clear();
        focusd = null;
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
    public Dimension measureMinSize() {
        Dimension dim = new Dimension();
        for (UIComponent obj : components) {
            Dimension clientDim = obj.getLayoutMinSize();
            dim.width = Math.max(dim.width, clientDim.width);
            dim.height = Math.max(dim.height, clientDim.height);
        }
        return dim;
    }

    /**
     * 자식 컴포넌트들을 전부 arrange합니다.
     */
    public void layout() {
        Rectangle available = getInnerBounds();
        for (UIComponent obj : components) {
            obj.arrange(available);
        }
    }

    @Override
    public boolean containsRelative(int x, int y) {
        for (UIComponent obj : components) {
            if (obj.isInteractive() && obj.containsRelative(x - obj.x, y - obj.y))
                return true;
        }
        return false;
    }

    @Override
    public void invalidateSize() {
        for (UIComponent obj : components)
            obj.invalidateSize();
        super.invalidateSize();
    }

    /**
     * 컴포넌트 리스트상 <code>index</code> 위치에 있는 component를 반환합니다.
     */
    public UIComponent getComponent(int index) {
        return components.get(index);
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        for (UIComponent comp : components) {
            if (comp instanceof UIPanel || focusd == comp) {
                comp.onKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        ListIterator<UIComponent> li = components.listIterator(components.size());
        clicked = null;

        while (li.hasPrevious()) {
            UIComponent comp = li.previous();
            if (comp.isInteractive() && comp.containsRelative(mouseX - comp.x, mouseY - comp.y)) {
                if (comp.onMouseClicked(mouseX - comp.x, mouseY - comp.y, mouseButton)) {
                    if (clicked == null) {
                        setFocusdComponent(comp);
                        clicked = comp;
                    }
                }
            }
        }

        if (clicked != null) {
            return true;
        } else {
            setFocusdComponent(null);
            return false;
        }
    }

    @Override
    public void onStaticMouseDown(int mouseX, int mouseY, int mouseButton) {
        for (UIComponent comp : components) {
            comp.onStaticMouseDown(mouseX, mouseY, mouseButton);
        }
    }

    @Override
    public void onMouseReleased(int mouseX, int mouseY, int state) {
        if (clicked != null) {
            clicked.onMouseReleased(mouseX, mouseY, state);
            clicked = null;
        }
    }

    @Override
    public void onMouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clicked != null) {
            clicked.onMouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
        }
    }

    @Override
    public void setFocusdComponent(UIComponent obj) {
        if (focusd == obj)
            return;
        if (focusd != null)
            focusd.onLostFocus();
        focusd = obj;
        if (focusd != null)
            focusd.onGotFocus();
    }

    @Override
    public void onLostFocus() {
        super.onLostFocus();
        setFocusdComponent(null);
    }

    @Override
    public void renderComponent(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        for (UIComponent comp : components) {
            matrixStack.translate(x, y, 0);
            comp.draw(matrixStack, mouseX - comp.x, mouseY - comp.y, partialTicks);
            matrixStack.translate(-x, -y, 0);
        }
    }
}
