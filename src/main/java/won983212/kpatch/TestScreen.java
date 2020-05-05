package won983212.kpatch;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

public class TestScreen extends GuiScreen {
	private GuiTextField tf;
	private GuiTextField tf2;

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		tf = new GuiTextField(0, fontRenderer, 5, 5, 200, 20);
		tf2 = new GuiTextField(1, fontRenderer, 5, 50, 200, 20);
		tf2.setMaxStringLength(30);
	}

	public void updateScreen() {
		this.tf.updateCursorCounter();
		this.tf2.updateCursorCounter();
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		this.tf.textboxKeyTyped(typedChar, keyCode);
		this.tf2.textboxKeyTyped(typedChar, keyCode);
		super.keyTyped(typedChar, keyCode);
	}

	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.tf.mouseClicked(mouseX, mouseY, mouseButton);
		this.tf2.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Gui.drawRect(0, 0, width, height, 0xff3f301d);
		this.tf.drawTextBox();
		this.tf2.drawTextBox();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
