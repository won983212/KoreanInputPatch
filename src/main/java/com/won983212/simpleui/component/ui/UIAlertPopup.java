package com.won983212.simpleui.component.ui;

import com.won983212.simpleui.Arranges;
import com.won983212.simpleui.DirWeights;
import com.won983212.simpleui.Theme;
import com.won983212.simpleui.component.GridPanel;
import com.won983212.simpleui.component.StackPanel;
import com.won983212.simpleui.component.UIComponent;
import com.won983212.simpleui.component.UIPanel;
import com.won983212.simpleui.events.IClickEventListener;

public class UIAlertPopup extends UIPanel implements IClickEventListener {
    private UIButton cancel = new UIButton("취소").setClickListener(this);
    private UIButton ok = new UIButton("확인").setClickListener(this);
    private UILabel title = new UILabel("");
    private UILabel content = new UILabel("");
    private IClickEventListener event = null;

    public UIAlertPopup() {
        add(new UIRectangle().setBackgroundColor(0xc0000000));

        GridPanel wnd = new GridPanel();
        wnd.setArrange(Arranges.CC);
        wnd.setMinimalSize(160, 90);
        wnd.addEmptyColumn();
        wnd.addRows("17,*,auto");

        wnd.add(GridPanel.setLayout(new UIRectangle(), 0, 0, 1, 1));
        wnd.add(GridPanel.setLayout(title.setArrange(Arranges.CC), 0, 0, 1, 1));
        wnd.add(GridPanel.setLayout(new UIRectangle().setBackgroundColor(Theme.BACKGROUND), 0, 1, 1, 2));
        wnd.add(GridPanel.setLayout(content.setArrange(Arranges.TC)
                .setMinimalSize(160, 11).setMargin(new DirWeights(6)).setForegroundColor(Theme.BLACK), 0, 1, 1, 1));

        StackPanel buttonPanel = new StackPanel();
        buttonPanel.add(cancel.setMinimalSize(25, 9).setMargin(new DirWeights(3)));
        buttonPanel.add(ok.setMinimalSize(25, 9).setMargin(new DirWeights(3, 3, 0, 3)));
        wnd.add(GridPanel.setLayout(buttonPanel.setArrange(Arranges.CR), 0, 2, 1, 1));

        add(wnd);
        setVisible(false);
    }

    public UIAlertPopup setCallbackEvent(IClickEventListener e, int cancelId, int okId) {
        cancel.setId(cancelId);
        ok.setId(okId);
        event = e;
        return this;
    }

    public void showMessage(String title, String message) {
        this.title.setText(title);
        this.content.setText(message);
        layout();
        setVisible(true);
    }

    @Override
    public boolean onMouseClicked(int mouseX, int mouseY, int mouseButton) {
        super.onMouseClicked(mouseX, mouseY, mouseButton);
        return true;
    }

    @Override
    public void onKeyTyped(char typedChar, int keyCode) {
        if (keyCode == 256) {
            setVisible(false);
        } else {
            super.onKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void onClick(UIComponent comp, int mouseX, int mouseY, int mouseButton) {
        if (event != null) {
            event.onClick(comp, mouseX, mouseY, mouseButton);
        }
        setVisible(false);
    }
}
