package won983212.kpatch;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class TestScreen extends GuiScreen {
	private GuiTextField tf;

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		tf = new GuiTextField(0, fontRenderer, 50, 50, 200, 20);
		tf.setMaxStringLength(30);
	}

	public void updateScreen() {
		this.tf.updateCursorCounter();
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		this.tf.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.tf.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Gui.drawRect(0, 0, width, height, 0xff3f301d);
		this.tf.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
