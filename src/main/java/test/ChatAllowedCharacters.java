package test;

public class ChatAllowedCharacters {
	public static boolean isAllowedCharacter(char character) {
		return character != 65535 && character != 167 && character >= ' ' && character != 127;
	}
}