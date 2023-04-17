package com.won983212.kpatch;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.kpatch.indicators.AbstractIndicator;

public class InputProcessor {
    protected IInputWrapper input;
    protected AbstractIndicator indicator;
    protected boolean isShowIndicator = false;

    protected InputProcessor(IInputWrapper input, AbstractIndicator indicator) {
        this.input = input;
        this.indicator = indicator;
    }

    public boolean handleCharTyped(char c, int i) {
        return false;
    }

    public boolean handleKeyTyped(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public void draw(MatrixStack matrixStack, int parentX, int parentY, int parentWidth, int parentHeight) {
        if (isShowIndicator) {
            indicator.draw(matrixStack, parentX, parentY, parentWidth, parentHeight);
        }
    }

    public void onMouseClick(double mouseX, double mouseY, int mouseButton) {
    }

    public int getStartCursor() {
        return Math.min(input.getAnchorCursor(), input.getMovingCursor());
    }

    public int getEndCursor() {
        return Math.max(input.getAnchorCursor(), input.getMovingCursor());
    }

    /**
     * 커서 위치에 text를 삽입합니다. 리턴값은 텍스트 삽입후 새로운 커서 위치입니다.
     */
    public static int processWrite(IInputWrapper input, int cursor1, int cursor2, String text) {
        int start = Math.min(cursor1, cursor2);
        int end = Math.max(cursor1, cursor2);

        String in = input.getValue();
        String s1 = in.substring(0, start);
        String s2 = in.substring(end);
        input.setValue(s1 + text + s2);
        return start + text.length(); // new cursor position
    }

    /**
     * amount만큼 문자를 삭제합니다. amount가 음수면 왼쪽으로, 양수면 오른쪽으로 삭제합니다.
     */
    public static int processDelete(IInputWrapper input, int cursor1, int cursor2, int delta) {
        int start = Math.min(cursor1, cursor2);
        int end = Math.max(cursor1, cursor2);

        String in = input.getValue();
        String s1 = in.substring(0, start);
        String s2 = in.substring(end);
        if (input.getMovingCursor() != input.getAnchorCursor()) {
            input.setValue(s1 + s2);
        } else if (delta < 0 && start > 0) {
            input.setValue(s1.substring(0, s1.length() - 1) + s2);
            start--;
        } else if (delta > 0 && s2.length() > 0) {
            input.setValue(s1 + s2.substring(1));
        }

        return start;
    }
}
