package won983212.kpatch;

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

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		System.out.println("Hello, world!");
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
	}
}