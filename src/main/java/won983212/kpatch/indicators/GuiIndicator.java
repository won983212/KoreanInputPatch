package won983212.kpatch.indicators;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;

public abstract class GuiIndicator {
	private static final int INDICATOR_MARIGN = 3;
	protected int width;
	protected int height;
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public abstract void draw(int x, int y);
	
	/**
	 * 부모 패널의 bounds만 넘겨주면 위, 아래, 왼쪽, 오른쪽 순으로 자동으로 배치해서 그립니다. 위쪽에 그릴 수 없으면 아래, 아래에 못그리면 왼쪽.. 이런 식입니다.
	 */
	public void draw(int parentX, int parentY, int parentWidth, int parentHeight) {
		ScaledResolution scaledresolution = new ScaledResolution(Minecraft.getMinecraft());
        int screenWidth = scaledresolution.getScaledWidth();
        int screenHeight = scaledresolution.getScaledHeight();
        
		int x = parentX;
		int y = parentY - INDICATOR_MARIGN - this.height;
		
		if (y < 0) {
			y = parentY + parentHeight + INDICATOR_MARIGN;
			if(y + this.height > screenHeight) {
				x = parentX - this.width - INDICATOR_MARIGN;
				y = parentY + (parentHeight - this.height) / 2;
				if(x < 0) {
					x = parentX + parentWidth + INDICATOR_MARIGN;
					if(x > screenWidth) {
						x = parentX;
						y = parentY - INDICATOR_MARIGN - this.height;
					}
				}
			}
		}
		draw(x, y);
	}
}
