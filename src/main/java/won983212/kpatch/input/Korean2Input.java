package won983212.kpatch.input;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ChatAllowedCharacters;
import won983212.kpatch.Configs;
import won983212.kpatch.KoreanInputPatch;

public class Korean2Input extends InputEngine {
	private static final String KeyMap = "`1234567890-=\\qwertyuiop[]asdfghjkl;'zxcvbnm/~!@#$%^&*()_+|QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?";
	private static final String TranslatedKey = "`1234567890-=\\ㅂㅈㄷㄱㅅㅛㅕㅑㅐㅔ[]ㅁㄴㅇㄹㅎㅗㅓㅏㅣ;'ㅋㅌㅊㅍㅠㅜㅡ/~!@#$%^&*()_+|ㅃㅉㄸㄲㅆㅛㅕㅑㅒㅖ{}ㅁㄴㅇㄹㅎㅗㅓㅏㅣ:\"ㅋㅌㅊㅍㅠㅜㅡ<>?";
	private static final String Chosung = "rRseEfaqQtTdwWczxvg";
	private static final String[] Jungsung = "k;o;i;O;j;p;u;P;h;hk;ho;hl;y;n;nj;np;nl;b;m;ml;l".split(";");
	private static final String[] Jongsung = ";r;R;rt;s;sw;sg;e;f;fr;fa;fq;ft;fx;fv;fg;a;q;qt;t;T;d;w;c;z;x;v;g".split(";");
	private static boolean isKorean = false;

	private int cho = -1;
	private int jung = -1;
	private int jong = 0;

	public Korean2Input(IInputWrapper wrapper) {
		super(wrapper);
	}

	public boolean handleKeyTyped(char c, int i) {
		if(!input.isComponentFocused())
			return false;
		if (isKorMode()) {
			if (i == Keyboard.KEY_LEFT || i == Keyboard.KEY_RIGHT || i == Keyboard.KEY_RETURN) {
				cancelAssemble();
			}
		}
		if (ChatAllowedCharacters.isAllowedCharacter(c)) {
			if (isKorean) {
				if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z') {
					if ("qwertyuiopasdfghjklzxcvbnmQWERTOP".indexOf(c) == -1)
						c = Character.toLowerCase(c);
					if (cho == -1) { // 초성 입력
						cho = Chosung.indexOf(c);
						write(translate(c));
						if (cho != -1) {
							input.setMovingCursor(input.getMovingCursor() - 1);
						}
					} else if (jung == -1) { // 중성 입력
						jung = findArray(Jungsung, c);
						if (jung == -1) {
							cho = Chosung.indexOf(c);
							input.setMovingCursor(input.getAnchorCursor());
							write(translate(c));
							input.setMovingCursor(input.getMovingCursor() - 1);
						} else {
							writeAssembled();
						}
					} else if (jong == 0) { // 종성 입력
						int newJung = findArray(Jungsung, Jungsung[jung] + String.valueOf(c));
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
								write(translate(c));
								
								cho = Chosung.indexOf(c);
								if (cho != -1) {
									input.setMovingCursor(input.getMovingCursor() - 1);
								}
							}
						}
					} else { // 종성 복자음 조합
						int newJong = findArray(Jongsung, Jongsung[jong] + String.valueOf(c));
						if (newJong != -1) {
							jong = newJong;
							writeAssembled();
						} else { // 복자음이 안 만들어지면
							int newCho = Chosung.indexOf(c);
							if (newCho != -1) {
								cancelAssemble();
								cho = newCho;
								write(translate(c));
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
				} else {
					if (isAssemble())
						cancelAssemble();
					write(c);
				}
			} else {
				write(c);
			}
			return true;
		} else if (i == Keyboard.KEY_BACK) {
			if (!isKorean)
				return false;
			backspaceKorean();
			return true;
		} else if (i == Keyboard.KEY_LCONTROL) {
			isKorean = !isKorean;
			cancelAssemble();
			return true;
		}
		return false;
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
			backspace();
		} else if (jung == -1) { // 초성만 있으면 문자 삭제.
			backspace();
			cancelAssemble();
		} else if (jong == 0) {
			if (Jungsung[jung].length() == 2) {
				jung = findArray(Jungsung, Jungsung[jung].charAt(0));
				write(getAssembledChar(cho, jung, jong));
			} else {
				jung = -1;
				write(translate(Chosung.charAt(cho)));
			}
		} else {
			if (Jongsung[jong].length() == 2) {
				jong = findArray(Jongsung, Jongsung[jong].charAt(0));
			} else {
				jong = 0;
			}
			write(getAssembledChar(cho, jung, jong));
		}
		if (cho != -1) {
			input.setMovingCursor(input.getAnchorCursor() - 1);
		}
	}

	private char getAssembledChar(int cho, int jung, int jong) {
		return (char) (0xAC00 + (cho * 21 + jung) * 28 + jong);
	}

	private void writeAssembled() {
		write(getAssembledChar(cho, jung, jong));
		input.setMovingCursor(input.getAnchorCursor() - 1);
	}

	private char translate(char en) {
		int idx = KeyMap.indexOf(en);
		if (idx != -1)
			return TranslatedKey.charAt(idx);
		return en;
	}

	public boolean isAssemble() {
		return cho != -1;
	}

	public void cancelAssemble() {
		if(cho != -1 || jung != -1 || jong != 0) {
			cho = -1;
			jung = -1;
			jong = 0;
			input.setMovingCursor(input.getAnchorCursor());
		}
	}

	public static boolean isKorMode() {
		return isKorean;
	}
}