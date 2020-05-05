package won983212.kpatch.toolbar;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public abstract class TopViewToolbar {
	private IToolbarContainer context = null;
	
	public abstract void renderToolbar(IToolbarContainer c);
	
	public boolean isRender() {
		return context != null;
	}
	
	public void setRenderNext(IToolbarContainer context) {
		this.context = context;
	}
	
	public void render() {
		if(isRender()) {
			renderToolbar(context);
			context = null;
		}
	}
}
