package won983212.kpatch.wrapper;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class EditBookWrapper extends GuiScreenBook {
	public EditBookWrapper(ItemStack book, boolean isUnsigned) {
		super(Minecraft.getMinecraft().player, book, isUnsigned);
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		System.out.println("KEYTYPED");
		super.keyTyped(typedChar, keyCode);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
}
