package com.won983212.kpatch;

import com.won983212.kpatch.ui.screen.ConfigScreen;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(KoreanInputPatch.MODID)
public class KoreanInputPatch {
    public static final String MODID = "assets/koreanime";

    public KoreanInputPatch() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY,
                () -> (mc, screen) -> new ConfigScreen(screen)
        );

        Config.load();
        Hanja.loadHanjas();
    }
}