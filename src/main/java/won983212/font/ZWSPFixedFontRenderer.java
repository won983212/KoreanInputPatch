package won983212.font;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

public class ZWSPFixedFontRenderer extends FontRenderer {
	public ZWSPFixedFontRenderer(Minecraft mc) {
		this(mc, mc.isUnicode());
	}
	
	public ZWSPFixedFontRenderer(Minecraft mc, boolean isUnicode) {
		super(mc.gameSettings, new ResourceLocation("textures/font/ascii.png"), mc.getTextureManager(), isUnicode);
		setBidiFlag(mc.getLanguageManager().isCurrentLanguageBidirectional());
		((IReloadableResourceManager) mc.getResourceManager()).registerReloadListener(this);
	}

	@Override
	public int getCharWidth(char character) {
		if(character == '\u200B') return 0;
		return super.getCharWidth(character);
	}
}
