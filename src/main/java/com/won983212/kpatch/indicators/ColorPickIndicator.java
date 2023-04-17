package com.won983212.kpatch.indicators;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.won983212.kpatch.ui.Theme;
import com.won983212.kpatch.ui.UITools;
import net.minecraft.client.gui.FontRenderer;

public class ColorPickIndicator extends AbstractIndicator {
    private static final float COLOR_PANEL_SIZE = 14;
    private static final float MARGIN = 3;
    private static final float GAP = 3;
    private static final int COLUMNS = 6;
    private static final int ROWS = 4;
    private static final int BORDER_COLOR = Theme.FOREGROUND_SHADOW_DARK;

    private static final String CODE = "0123456789abcdefklmnor";
    private static final int[] COLOR = {
            0x000000, 0x0000aa, 0x00aa00, 0x00aaaa, 0xaa0000, 0xaa00aa, 0xffaa00, 0xaaaaaa,    // 0 1 2 3 4 5 6 7
            0x555555, 0x5555ff, 0x55ff55, 0x55ffff, 0xff5555, 0xff55ff, 0xffff55, 0xffffff, // 8 9 a b c d e f
            0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff, 0xffffff                        // k l m n o r
    };


    public ColorPickIndicator(FontRenderer font) {
        super(font);
        useTopmostRender(800);
        this.width = (int) (COLUMNS * (COLOR_PANEL_SIZE + GAP) - GAP + MARGIN * 2);
        this.height = (int) (ROWS * (COLOR_PANEL_SIZE + GAP) - GAP + MARGIN * 2);
    }

    public void draw(MatrixStack matrixStack, float x, float y) {
        // background
        UITools.drawArea(matrixStack, x, y, width, height, Theme.BACKGROUND, Theme.BACKGROUND_SHADOW);

        int k = 0;
        for (int j = 0; j < ROWS; j++) {
            for (int i = 0; i < COLUMNS; i++) {
                if (k > 15 && j != ROWS - 1) break;

                float px = x + MARGIN + i * (COLOR_PANEL_SIZE + GAP);
                float py = y + MARGIN + j * (COLOR_PANEL_SIZE + GAP);
                char c = CODE.charAt(k);

                // color panel
                UITools.drawArcRect(matrixStack, px, py, px + COLOR_PANEL_SIZE, py + COLOR_PANEL_SIZE, COLOR[k] | 0xff000000, 0, BORDER_COLOR);
                if (k > 15) {
                    UITools.drawText(font, matrixStack, "§" + c + "A", px + COLOR_PANEL_SIZE / 2, py + COLOR_PANEL_SIZE / 2 + 1, Theme.FOREGROUND, 0, UITools.CENTER_BOTH);
                }

                // border label background
                UITools.drawArcRect(matrixStack, px + COLOR_PANEL_SIZE - 4.5f, py + COLOR_PANEL_SIZE - 5, px + COLOR_PANEL_SIZE, py + COLOR_PANEL_SIZE, BORDER_COLOR);

                // border label
                UITools.drawText(font, matrixStack, String.valueOf(c), (px + COLOR_PANEL_SIZE) * 2 - 7, (py + COLOR_PANEL_SIZE) * 2 - 9,
                        Theme.PRIMARY_FOREGROUND, UITools.NONE, UITools.NONE, 0.5f);
                k++;
            }
        }
    }
}
