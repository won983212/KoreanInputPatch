package won983212.kpatch;

import java.lang.reflect.Field;
import java.util.Queue;

import com.google.common.collect.Queues;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.gui.GuiScreenDemo;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiEditSign;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntitySign;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import won983212.kpatch.wrapper.EditBookWrapper;
import won983212.kpatch.wrapper.EditSignWrapper;
import won983212.kpatch.wrapper.TextfieldWrapper;

public class ForgeEventHandler {
	private Queue<Runnable> afterRenderQueue = Queues.newArrayDeque();

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
			Minecraft mc = Minecraft.getMinecraft();
			Class cls = screen.getClass();
			boolean cancel = true;
			
			if (cls == GuiEditSign.class) {
				mc.displayGuiScreen(new EditSignWrapper((GuiEditSign) screen));
			} else if(cls == GuiScreenBook.class) {
				mc.displayGuiScreen(new EditBookWrapper((GuiScreenBook) screen));
			} else {
				cancel = false;
			}
			
			e.setCanceled(cancel);
		}
		
		/*if (screen instanceof GuiMainMenu) {
			e.setCanceled(true);
			Minecraft.getMinecraft().displayGuiScreen(new TestScreen());
		}*/
	}

	@SubscribeEvent
	public void onDrawScreen(GuiScreenEvent.DrawScreenEvent.Post e) {
		synchronized (this.afterRenderQueue) {
			while (!this.afterRenderQueue.isEmpty()) {
				this.afterRenderQueue.poll().run();
			}
		}
	}
	
	public void addTopRenderQueue(Runnable runnable) {
		synchronized (this.afterRenderQueue) {
			afterRenderQueue.add(runnable);
		}
	}
}
