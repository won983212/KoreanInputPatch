package com.won983212.simpleui.component.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.simpleui.Arranges;
import com.won983212.simpleui.Theme;
import com.won983212.simpleui.UITools;
import com.won983212.simpleui.component.StackPanel;
import com.won983212.simpleui.component.StackPanel.Orientation;
import com.won983212.simpleui.component.UIComponent;
import com.won983212.simpleui.component.UIStyledComponent;
import com.won983212.simpleui.component.deco.BorderDeco;
import com.won983212.simpleui.events.IClickEventListener;
import com.won983212.simpleui.events.IStateChangedEventListener;

import java.awt.*;

public class UICombobox extends UIStyledComponent<UICombobox> implements IClickEventListener<UICombobox> {
    private int selected = -1;
    private Object[] items = null;
    private IStateChangedEventListener<Integer> event = null;
    private final BorderDeco comboboxPopup;
    private final StackPanel popupMenu;

    public UICombobox() {
        this(-1, null);
    }

    public UICombobox(int selected, Object[] items) {
        setMinimalSize(60, 14);
        setBackgroundColor(Theme.WHITE);
        setForegroundColor(Theme.BLACK);
        setBorder(Theme.GRAY);

        final UICombobox combobox = this;
        popupMenu = new StackPanel().setOrientation(Orientation.VERTICAL);
        comboboxPopup = new BorderDeco(popupMenu) {
            @Override
            public void onStaticMouseDown(int mouseX, int mouseY, int mouseButton) {
                if (!combobox.containsAbsolute(mouseX, mouseY)) {
                    setVisible(false);
                }
            }
        };

        comboboxPopup.setMinimalSize(60, 0);
        comboboxPopup.setArrange(Arranges.TL);
        comboboxPopup.setVisible(false);
        setItems(items);
        setSelectedItem(selected);
    }

    @Override
    public UICombobox setMinimalSize(int width, int height) {
        if (comboboxPopup != null) {
            comboboxPopup.setMinimalSize(width, 0);
        }
        return super.setMinimalSize(width, height);
    }

    private void constructItems() {
        popupMenu.clearComponents();
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                popupMenu.add(new UIButton(items[i].toString())
                        .setBackgroundColor(0xffffffff).setForegroundColor(0xff000000).setTextShadow(0)
                        .setMetadata(i).setClickListener(this).setBorderShadow(Theme.BACKGROUND_SHADOW).setFlat());
            }
        }
    }

    public UICombobox setItems(Object[] items) {
        this.items = items;
        constructItems();
        return this;
    }

    public UICombobox setSelectedItem(int index) {
        if (items != null && items.length > index) {
            selected = index;
        }
        return this;
    }

    public UICombobox setChangedEvent(IStateChangedEventListener<Integer> event) {
        this.event = event;
        return this;
    }

    @Override
    public String serializeData() {
        return String.valueOf(selected);
    }

    @Override
    public void deserializeData(String serialized) {
        selected = Integer.parseInt(serialized);
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        Point p = calculateActualLocation();
        comboboxPopup.setRelativeLocation(p.x, p.y + height);
        comboboxPopup.setVisible(true);
        return true;
    }

    @Override
    protected void renderComponent(MatrixStack ms, int mouseX, int mouseY, float partialTicks) {
        float textX = x + 2;
        float textY = y + (height - fontRenderer.lineHeight) / 2f;
        int color = foregroundColor;

        if (!isEnabled()) {
            color = Theme.LIGHT_GRAY;
        }

        // background
        UITools.drawArea(ms, x, y, width, height, backgroundColor, borderShadow, borderColor, roundRadius);

        // selected label
        if (selected != -1 && items != null && items.length > selected) {
            UITools.drawText(fontRenderer, ms, items[selected].toString(), textX, textY, foregroundColor, textShadow);
        }

        // arrow
        String arrow = String.valueOf(comboboxPopup.isVisible() ? '▲' : '▼');
        UITools.drawText(fontRenderer, ms, arrow, x + width - fontRenderer.width(arrow) - 2, textY, foregroundColor, 0);
    }

    @Override
    public void onClick(UIComponent<UICombobox> comp, int mouseX, int mouseY, int mouseButton) {
        int idx = (int) comp.metadata;
        selected = idx;
        if (event != null) {
            event.onChanged(this, idx);
        }
    }
}
