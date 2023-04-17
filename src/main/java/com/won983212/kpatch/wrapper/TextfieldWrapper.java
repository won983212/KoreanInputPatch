package com.won983212.kpatch.wrapper;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.kpatch.IInputWrapper;
import com.won983212.kpatch.InputProcessorGroup;
import com.won983212.kpatch.input.ColorInput;
import com.won983212.kpatch.input.HanjaInput;
import com.won983212.kpatch.input.KoreanInput;
import net.minecraft.client.gui.widget.TextFieldWidget;

public class TextfieldWrapper implements IInputWrapper {
    private final TextFieldWidget widget;
    private final InputProcessorGroup inputGroup;
    private final KoreanInput krIn;

    public TextfieldWrapper(TextFieldWidget impl) {
        widget = impl;
        inputGroup = new InputProcessorGroup();
        inputGroup.addInputProcessor(new ColorInput(this, impl.font));
        inputGroup.addInputProcessor(new HanjaInput(this, impl.font));
        inputGroup.addInputProcessor(krIn = new KoreanInput(this, impl.font));
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return widget.canConsumeInput() && inputGroup.keyTyped(keyCode, scanCode, modifiers);
    }

    public boolean charTyped(char typedChar, int keyCode) {
        return widget.canConsumeInput() && inputGroup.charTyped(typedChar, keyCode);
    }

    public void mouseClicked(double mouseX, double mouseY, int mouseButton) {
        inputGroup.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public void renderButton(MatrixStack matrixStack) {
        if (widget.canConsumeInput()) {
            int ind_x = widget.bordered ? (widget.x - 1) : (widget.x - 2);
            int ind_y = widget.bordered ? (widget.y < 2 ? widget.y + 1 : widget.y - 1) : widget.y;
            krIn.setLength(this.getValue().length(), widget.maxLength);
            inputGroup.render(matrixStack, ind_x, ind_y, widget.getWidth(), widget.getHeight());
        }
    }

    @Override
    public void setAnchorCursor(int cursor) {
        int endCur = getMovingCursor();
        widget.setCursorPosition(cursor);
        widget.setHighlightPos(endCur);
    }

    @Override
    public void setMovingCursor(int cursor) {
        widget.setHighlightPos(cursor);
    }

    @Override
    public void write(String text) {
        widget.insertText(text);
    }

    @Override
    public void delete(int delta) {
        widget.deleteChars(delta);
    }

    @Override
    public void setValue(String text) {
        widget.setValue(text);
    }

    @Override
    public String getValue() {
        return widget.getValue();
    }

    @Override
    public int getAnchorCursor() {
        return widget.getCursorPosition();
    }

    @Override
    public int getMovingCursor() {
        return widget.highlightPos;
    }

    @Override
    public void cancelAllInputContext() {
        krIn.cancelAssemble();
    }

}
