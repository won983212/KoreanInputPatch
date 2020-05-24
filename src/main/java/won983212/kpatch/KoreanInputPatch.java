package won983212.kpatch;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import won983212.font.ZWSPFixedFontRenderer;

@Mod(modid = KoreanInputPatch.MODID, name = KoreanInputPatch.NAME, version = KoreanInputPatch.VERSION)
public class KoreanInputPatch {
	public static final String MODID = "openkoreanime";
	public static final String NAME = "Open Minecraft Korean IME";
	public static final String VERSION = "0.1";

	@Mod.Instance(KoreanInputPatch.MODID)
	public static KoreanInputPatch instance;
	
	private final ForgeEventHandler eventHandler = new ForgeEventHandler();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Configs.load();
		Hanja.loadHanjas();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(eventHandler);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		mc.fontRenderer = new ZWSPFixedFontRenderer(mc);
	}
	
	public ForgeEventHandler getEventHandler() {
		return eventHandler;
	}
}