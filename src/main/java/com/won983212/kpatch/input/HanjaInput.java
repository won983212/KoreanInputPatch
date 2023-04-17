package com.won983212.kpatch.input;

import com.won983212.kpatch.Config;
import com.won983212.kpatch.Hanja;
import com.won983212.kpatch.IInputWrapper;
import com.won983212.kpatch.InputProcessor;
import com.won983212.kpatch.indicators.HanjaPickIndicator;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.glfw.GLFW;

public class HanjaInput extends InputProcessor {
    private int page = 1;
    private char key = 0;
    private char prevKey = 0;
    private Hanja[] hanjaCache = null;

    public HanjaInput(IInputWrapper base, FontRenderer font) {
        super(base, new HanjaPickIndicator(font));
    }

    public void setHanjaKey(char c) {
        page = 1;
        key = c;
        if (prevKey != key) {
            hanjaCache = Hanja.getHanjas(key);
            prevKey = key;
        }
        updateHanjaUI();
    }

    public void nextPage() {
        int max = 0;
        if (hanjaCache != null) {
            max = (int) Math.ceil(hanjaCache.length / 9.0);
        }
        if (page < max) {
            page++;
            updateHanjaUI();
        }
    }

    public void prevPage() {
        if (page > 1) {
            page--;
            updateHanjaUI();
        }
    }

    private void updateHanjaUI() {
        if (hanjaCache != null) {
            ((HanjaPickIndicator) indicator).setHanjaData(key, page, hanjaCache);
        }
    }

    @Override
    public boolean handleKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (keyCode == Config.Key.KEY_HANJA.getKey().getValue()) {
            if (!isShowIndicator) {
                if (getEndCursor() - getStartCursor() > 1)
                    return true;

                int cur = getEndCursor();
                String text = input.getValue();
                if (cur != 0) {
                    char pc = text.charAt(cur - 1);
                    if ((pc >= '가' && pc <= '힣') || "ㄱㄴㄷㄹㅁㅂㅅㅇㅈㅊㅋㅌㅍㅎㄲㄸㅃㅆ".indexOf(pc) != -1) {
                        setHanjaKey(pc);
                    } else {
                        return true;
                    }
                } else {
                    return true;
                }

                if (hanjaCache == null)
                    return true;
            }
            isShowIndicator = !isShowIndicator;
            return true;
        } else if (isShowIndicator) {
            if (keyCode == GLFW.GLFW_KEY_LEFT) {
                prevPage();
                return true;
            } else if (keyCode == GLFW.GLFW_KEY_RIGHT) {
                nextPage();
                return true;
            } else if (keyCode >= GLFW.GLFW_KEY_1 && keyCode <= GLFW.GLFW_KEY_9) { // pass to char typed
                return true;
            }
            isShowIndicator = false;
            return false;
        }
        return false;
    }

    @Override
    public boolean handleCharTyped(char c, int i) {
        if (isShowIndicator) {
            if (c >= '1' && c <= '9') {
                int idx = (page - 1) * 9 + c - '1';
                if (idx < hanjaCache.length) {
                    input.delete(-1);
                    input.write(String.valueOf(hanjaCache[idx].hanja));
                    input.cancelAllInputContext();
                }
            }
            isShowIndicator = false;
            return true;
        }
        return false;
    }

    @Override
    public void onMouseClick(double mouseX, double mouseY, int mouseButton) {
        isShowIndicator = false;
    }

}
