package won983212.kpatch;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = KoreanInputPatch.MODID, name = KoreanInputPatch.NAME, version = KoreanInputPatch.VERSION)
public class KoreanInputPatch {
	public static final String MODID = "koreaninputpatch";
	public static final String NAME = "KoreanInputPatch";
	public static final String VERSION = "0.1";

	@Mod.Instance(KoreanInputPatch.MODID)
	public static KoreanInputPatch instance;
	
	public final Configs conf = new Configs();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		conf.load();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(ForgeEventHandler.instance);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}