package com.won983212.kpatch.indicators;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.kpatch.Hanja;
import com.won983212.kpatch.ui.Theme;
import com.won983212.kpatch.ui.UITools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class HanjaPickIndicator extends AbstractIndicator {
    private static final int TITLE_HEIGHT = 20;
    private static final int GAP = 3;

    private char key;
    private int page;
    private String pageText;
    private Hanja[] hanjas;

    public HanjaPickIndicator(FontRenderer font) {
        super(font);
        useTopmostRender(800);
    }

    public void setHanjaData(char key, int page, Hanja[] hanjas) {
        this.key = key;
        this.page = page;
        this.pageText = page + "/" + (int) Math.ceil(hanjas.length / 9.0);
        this.hanjas = hanjas;
        this.width = 0;

        FontRenderer fr = Minecraft.getInstance().font;
        int pageContentLength = 0;

        for (int i = 0; i < 9; i++) {
            int idx = (page - 1) * 9 + i;
            if (idx >= hanjas.length)
                break;

            pageContentLength++;
            width = Math.max(width, 30 + GAP * 2 + fr.width(hanjas[idx].meaning));
            width = Math.max(50, width);
        }

        this.height = TITLE_HEIGHT + GAP + pageContentLength * (fr.lineHeight + GAP) + 1;
    }

    public void draw(MatrixStack matrixStack, float x, float y) {
        // background
        UITools.drawArea(matrixStack, x, y, width, height, Theme.BACKGROUND, Theme.BACKGROUND_SHADOW);

        // title background
        UITools.drawArea(matrixStack, x, y, width, TITLE_HEIGHT, Theme.PRIMARY);

        // title
        final float keyX = (x + GAP + 1) / 2;
        final float keyY = (y - font.lineHeight + TITLE_HEIGHT / 2f) / 2;
        UITools.drawText(font, matrixStack, String.valueOf(key), keyX, keyY, Theme.PRIMARY_FOREGROUND, Theme.FOREGROUND_SHADOW_DARK, UITools.NONE, 2);

        // page indicator
        final float pageX = x + width - font.width(pageText) - GAP;
        final float pageY = y - font.lineHeight + TITLE_HEIGHT - GAP / 2f;
        font.drawShadow(matrixStack, pageText, pageX, pageY, Theme.PRIMARY_FOREGROUND);

        // page
        for (int i = 0; i < 9; i++) {
            int idx = (page - 1) * 9 + i;
            if (idx >= hanjas.length)
                break;

            float py = y + TITLE_HEIGHT + GAP + i * (font.lineHeight + GAP) + 1;
            UITools.drawText(font, matrixStack, String.valueOf(i + 1), x + GAP, py, Theme.FOREGROUND, Theme.FOREGROUND_SHADOW);
            font.draw(matrixStack, String.valueOf(hanjas[idx].hanja), x + 9 + GAP * 2, py, Theme.FOREGROUND);
            font.draw(matrixStack, hanjas[idx].meaning, x + 23 + GAP * 2, py, Theme.FOREGROUND);
        }
    }
}
