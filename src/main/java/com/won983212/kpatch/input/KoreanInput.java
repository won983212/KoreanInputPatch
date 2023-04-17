package com.won983212.kpatch.input;

import com.won983212.kpatch.Config;
import com.won983212.kpatch.IInputWrapper;
import com.won983212.kpatch.Imm32;
import com.won983212.kpatch.InputProcessor;
import com.won983212.kpatch.indicators.KoreanInputIndicator;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.SharedConstants;
import org.lwjgl.glfw.GLFW;

public class KoreanInput extends InputProcessor {
    private static final String KeyMap = "`1234567890-=\\qwertyuiop[]asdfghjkl;'zxcvbnm/~!@#$%^&*()_+|QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?";
    private static final String TranslatedKey = "`1234567890-=\\ㅂㅈㄷㄱㅅㅛㅕㅑㅐㅔ[]ㅁㄴㅇㄹㅎㅗㅓㅏㅣ;'ㅋㅌㅊㅍㅠㅜㅡ/~!@#$%^&*()_+|ㅃㅉㄸㄲㅆㅛㅕㅑㅒㅖ{}ㅁㄴㅇㄹㅎㅗㅓㅏㅣ:\"ㅋㅌㅊㅍㅠㅜㅡ<>?";
    private static final String Chosung = "rRseEfaqQtTdwWczxvg";
    private static final String[] Jungsung = "k;o;i;O;j;p;u;P;h;hk;ho;hl;y;n;nj;np;nl;b;m;ml;l".split(";");
    private static final String[] Jongsung = ";r;R;rt;s;sw;sg;e;f;fr;fa;fq;ft;fx;fv;fg;a;q;qt;t;T;d;w;c;z;x;v;g".split(";");
    private static boolean isKorean = false;

    private int cho = -1;
    private int jung = -1;
    private int jong = 0;

    public KoreanInput(IInputWrapper wrapper, FontRenderer font) {
        this(wrapper, font, "자");
    }

    public KoreanInput(IInputWrapper wrapper, FontRenderer font, String unit) {
        super(wrapper, new KoreanInputIndicator(font, unit));
        isShowIndicator = true;
    }

    @Override
    public boolean handleKeyTyped(int keyCode, int scanCode, int modifiers) {
        if (isKorMode()) {
            if (keyCode == GLFW.GLFW_KEY_LEFT || keyCode == GLFW.GLFW_KEY_RIGHT || keyCode == GLFW.GLFW_KEY_ENTER) {
                cancelAssemble();
            }
        }
        if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            if (!isKorean)
                return false;
            backspaceKorean();
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_DELETE) {
            input.delete(1);
            cancelAssemble();
            return true;
        } else if (keyCode == Config.Key.KEY_KOR.getKey().getValue()) {
            Imm32.ChangeIME(true);
            isKorean = !isKorean;
            cancelAssemble();
            return true;
        }
        return false;
    }

    @Override
    public boolean handleCharTyped(char c, int i) {
        if (SharedConstants.isAllowedChatCharacter(c)) {
            if (isKorean) {
                if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
                    assembleKor(c);
                } else {
                    if (isAssemble())
                        cancelAssemble();
                    writeChar(c);
                }
            } else {
                writeChar(c);
            }
            return true;
        }
        return false;
    }

    private void assembleKor(char c) {
        if ("qwertyuiopasdfghjklzxcvbnmQWERTOP".indexOf(c) == -1)
            c = Character.toLowerCase(c);
        if (cho == -1) { // 초성 입력
            assembleChosung(c);
        } else if (jung == -1) { // 중성 입력
            assembleJungsung(c);
        } else if (jong == 0) { // 종성 입력
            assembleJongsung(c);
        } else { // 종성 복자음 조합
            assembleDoubleJongsung(c);
        }
    }

    private void assembleDoubleJongsung(char c) {
        int newJong = findArray(Jongsung, Jongsung[jong] + c);
        if (newJong != -1) {
            jong = newJong;
            writeAssembled();
        } else { // 복자음이 안 만들어지면
            int newCho = Chosung.indexOf(c);
            if (newCho != -1) {
                cancelAssemble();
                cho = newCho;
                writeChar(translate(c));
                input.setMovingCursor(input.getMovingCursor() - 1);
            } else {
                char lastChar;
                if (Jongsung[jong].length() == 2) {
                    lastChar = Jongsung[jong].charAt(1);
                } else {
                    lastChar = Jongsung[jong].charAt(0);
                }
                backspaceKorean();
                cancelAssemble();
                cho = Chosung.indexOf(lastChar);
                jung = findArray(Jungsung, c);
                input.setMovingCursor(input.getAnchorCursor());
                writeAssembled();
            }
        }
    }

    private void assembleJongsung(char c) {
        int newJung = findArray(Jungsung, Jungsung[jung] + c);
        if (newJung != -1) {
            jung = newJung;
            writeAssembled();
        } else { // 종성에 올 수 없는 문자 입력시
            int idx = findArray(Jongsung, c);
            if (idx != -1) {
                jong = idx;
                writeAssembled();
            } else {
                cancelAssemble();
                writeChar(translate(c));

                cho = Chosung.indexOf(c);
                if (cho != -1) {
                    input.setMovingCursor(input.getMovingCursor() - 1);
                }
            }
        }
    }

    private void assembleJungsung(char c) {
        jung = findArray(Jungsung, c);
        if (jung == -1) {
            cho = Chosung.indexOf(c);
            input.setMovingCursor(input.getAnchorCursor());
            writeChar(translate(c));
            input.setMovingCursor(input.getMovingCursor() - 1);
        } else {
            writeAssembled();
        }
    }

    private void assembleChosung(char c) {
        cho = Chosung.indexOf(c);
        writeChar(translate(c));
        if (cho != -1) {
            input.setMovingCursor(input.getMovingCursor() - 1);
        }
    }

    @Override
    public void onMouseClick(double mouseX, double mouseY, int mouseButton) {
        cancelAssemble();
    }

    private int findArray(String[] arr, String value) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(value))
                return i;
        }
        return -1;
    }

    private int findArray(String[] arr, char c) {
        return findArray(arr, String.valueOf(c));
    }

    private void backspaceKorean() {
        if (cho == -1) { // 아얘 assemble이 안된 상태. 그냥 백스페이스 ㄱ
            input.delete(-1);
        } else if (jung == -1) { // 초성만 있으면 문자 삭제.
            input.delete(-1);
            cancelAssemble();
        } else if (jong == 0) {
            if (Jungsung[jung].length() == 2) {
                jung = findArray(Jungsung, Jungsung[jung].charAt(0));
                writeChar(getAssembledChar(cho, jung, jong));
            } else {
                jung = -1;
                writeChar(translate(Chosung.charAt(cho)));
            }
        } else {
            if (Jongsung[jong].length() == 2) {
                jong = findArray(Jongsung, Jongsung[jong].charAt(0));
            } else {
                jong = 0;
            }
            writeChar(getAssembledChar(cho, jung, jong));
        }
        if (cho != -1) {
            input.setMovingCursor(input.getAnchorCursor() - 1);
        }
    }

    private char getAssembledChar(int cho, int jung, int jong) {
        return (char) (0xAC00 + (cho * 21 + jung) * 28 + jong);
    }

    private void writeAssembled() {
        writeChar(getAssembledChar(cho, jung, jong));
        input.setMovingCursor(input.getAnchorCursor() - 1);
    }

    private char translate(char en) {
        int idx = KeyMap.indexOf(en);
        if (idx != -1)
            return TranslatedKey.charAt(idx);
        return en;
    }

    private void writeChar(char c) {
        input.write(String.valueOf(c));
    }

    public boolean isAssemble() {
        return cho != -1;
    }

    public void cancelAssemble() {
        if (cho != -1 || jung != -1 || jong != 0) {
            cho = -1;
            jung = -1;
            jong = 0;
            input.setMovingCursor(input.getAnchorCursor());
        }
    }

    public void setLength(int len, int maxLen) {
        ((KoreanInputIndicator) indicator).setLength(len, maxLen);
    }

    public static boolean isKorMode() {
        return isKorean;
    }
}