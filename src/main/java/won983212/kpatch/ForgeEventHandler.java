package won983212.kpatch;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import won983212.kpatch.toolbar.IToolbarContainer;
import won983212.kpatch.toolbar.KoreanInputToolbar;
import won983212.kpatch.toolbar.ToolbarRenderer;
import won983212.kpatch.toolbar.TopViewToolbar;
import won983212.kpatch.wrapper.EditSignWrapper;
import won983212.kpatch.wrapper.TextfieldWrapper;

public class ForgeEventHandler {
	private final ToolbarRenderer toolbarRenderer = new ToolbarRenderer();

	@SubscribeEvent
	public void onInitGui(GuiScreenEvent.InitGuiEvent.Post e) { // 화면상의 모든 GuiTextfield fields에 wrapper를 씌움
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

	@SubscribeEvent
	public void onOpenGui(GuiOpenEvent e) {
		GuiScreen screen = e.getGui();
		if (screen != null) {
			if (screen.getClass() == GuiEditSign.class) {
				e.setCanceled(true);
				Minecraft.getMinecraft().displayGuiScreen(new EditSignWrapper((GuiEditSign) screen));
			}
		}
		/*
		 * if(screen instanceof GuiMainMenu) { e.setCanceled(true);
		 * Minecraft.getMinecraft().displayGuiScreen(new TestScreen()); }
		 */
	}

	@SubscribeEvent
	public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post e) {
		toolbarRenderer.render();
	}

	public void requestDrawToolbar(IToolbarContainer container, int toolbarId) {
		toolbarRenderer.requestDrawToolbar(container, toolbarId);
	}
}
