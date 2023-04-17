package com.won983212.kpatch.indicators;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.kpatch.ForgeEventHandler;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public abstract class AbstractIndicator {
    private static final int INDICATOR_MARIGN = 3;
    protected float width;
    protected float height;
    protected FontRenderer font;
    private boolean useTopmostRender = false;
    private int topmostZIndex = 400;

    public AbstractIndicator(FontRenderer font) {
        this.font = font;
    }

    protected abstract void draw(MatrixStack matrixStack, float x, float y);

    protected void useTopmostRender(){
        useTopmostRender(topmostZIndex);
    }

    protected void useTopmostRender(int topmostZIndex){
        useTopmostRender = true;
        this.topmostZIndex = topmostZIndex;
    }

    /**
     * 부모 패널의 bounds만 넘겨주면 위, 아래, 왼쪽, 오른쪽 순으로 자동으로 배치해서 그립니다. 위쪽에 그릴 수 없으면 아래, 아래에 못그리면 왼쪽... etc
     */
    public void draw(MatrixStack matrixStack, int parentX, int parentY, int parentWidth, int parentHeight) {
        MainWindow window = Minecraft.getInstance().getWindow();
        int screenWidth = window.getGuiScaledWidth();
        int screenHeight = window.getGuiScaledHeight();

        float x = parentX;
        float y = parentY - INDICATOR_MARIGN - this.height;

        if (y < 0) {
            y = parentY + parentHeight + INDICATOR_MARIGN;
            if (y + this.height > screenHeight) {
                x = parentX - this.width - INDICATOR_MARIGN;
                y = parentY + (parentHeight - this.height) / 2;
                if (x < 0) {
                    x = parentX + parentWidth + INDICATOR_MARIGN;
                    if (x > screenWidth) {
                        x = parentX;
                        y = parentY - INDICATOR_MARIGN - this.height;
                    }
                }
            }
        }

        final float finalX = x;
        final float finalY = y;

        if(useTopmostRender){
            ForgeEventHandler.addTopRenderQueue((stack) -> draw(stack, finalX, finalY), topmostZIndex);
        } else {
            draw(matrixStack, finalX, finalY);
        }
    }
}
