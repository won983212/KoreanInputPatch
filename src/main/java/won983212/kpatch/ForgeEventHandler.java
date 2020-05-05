package won983212.kpatch;

import java.lang.reflect.Field;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import won983212.kpatch.wrapper.TextfieldWrapper;

public class ForgeEventHandler {
	@SubscribeEvent
	public void onInitGui(GuiScreenEvent.InitGuiEvent.Post e) {
		GuiScreen screen = e.getGui();
		Field[] fields = screen.getClass().getDeclaredFields();
		try {
			for (Field f : fields) {
				if (f.getType() == GuiTextField.class) {
					f.setAccessible(true);
					f.set(screen, new TextfieldWrapper((GuiTextField) f.get(screen)));
				}
			}
		} catch (IllegalArgumentException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}
	}
}
