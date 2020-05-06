package won983212.kpatch.wrapper;

import java.io.IOException;

import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.text.TextComponentString;
import won983212.kpatch.ObfuscatedReflection;
import won983212.kpatch.input.IInputWrapper;

public class EditSignWrapper extends GuiEditSign implements IInputWrapper {
	public EditSignWrapper(GuiEditSign parent) {
		super(ObfuscatedReflection.getPrivateValue(GuiEditSign.class, parent, "tileSign"));
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == 200 || keyCode == 208 || keyCode == 28 || keyCode == 156 || keyCode == 1) {
			super.keyTyped(typedChar, keyCode);
		} else {
			TileEntitySign tileSign = ObfuscatedReflection.getPrivateValue(GuiEditSign.class, this, "tileSign");
			int editLine = ObfuscatedReflection.getPrivateValue(GuiEditSign.class, this, "editLine");
			String s = tileSign.signText[editLine].getUnformattedText();
	
			if (keyCode == 14 && !s.isEmpty()) {
				s = s.substring(0, s.length() - 1);
			}
	
			if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && this.fontRenderer.getStringWidth(s + typedChar) <= 90) {
				s = s + typedChar;
			}
	
			tileSign.signText[editLine] = new TextComponentString(s);
		}
	}

	@Override
	public void setText(String text) {
		
	}

	@Override
	public String getText() {
		return null;
	}

	@Override
	public int getAnchorCursor() {
		return 0;
	}

	@Override
	public int getMovingCursor() {
		return 0;
	}

	@Override
	public void setAnchorCursor(int cursor) {
		
	}

	@Override
	public void setMovingCursor(int cursor) {
		
	}
}