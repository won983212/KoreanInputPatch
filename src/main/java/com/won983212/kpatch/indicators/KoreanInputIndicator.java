package com.won983212.kpatch.indicators;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.kpatch.Config;
import com.won983212.kpatch.input.KoreanInput;
import com.won983212.kpatch.ui.Theme;
import com.won983212.kpatch.ui.UITools;
import com.won983212.kpatch.ui.animation.AnimationBase;
import com.won983212.kpatch.ui.animation.ColorAnimation;
import com.won983212.kpatch.ui.animation.DecimalAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.ChatScreen;

public class KoreanInputIndicator extends AbstractIndicator {
    private final DecimalAnimation alertBoomingAnimation = new DecimalAnimation(150);
    private final DecimalAnimation alertWidthAnimation = new DecimalAnimation(200);
    private final DecimalAnimation modeChangeAnimation = new DecimalAnimation(200);
    private final ColorAnimation modeBgColorAnimation = new ColorAnimation(400, Theme.BACKGROUND, 0);

    private String alertText;
    private boolean prevKrMode = KoreanInput.isKorMode();
    private int prevLen = 0;

    private int len = 0;
    private int maxLen = 0;
    private String unit = "자";


    public KoreanInputIndicator(FontRenderer font, String unit) {
        super(font);
        useTopmostRender();
        this.unit = unit;
        this.width = 30;
        this.height = 11;
        alertBoomingAnimation.setCompileType(AnimationBase.COMPILE_MOUNTAIN);
        modeBgColorAnimation.setCompileType(AnimationBase.COMPILE_MOUNTAIN);
    }

    public void setLength(int len, int maxLen) {
        this.len = len;
        this.maxLen = maxLen;
    }

    @Override
    public void draw(MatrixStack matrixStack, float x, float y) {
        final int conf = Config.getInt(Config.IME_INDICATOR_VISIBLE_MODE);
        final boolean useAnimation = Config.getBoolean(Config.IME_INDICATOR_ANIMATE);

        if (conf == 0) { // 0이면 무조건 렌더링 안함
            return;
        } else if (conf == 1 && !(Minecraft.getInstance().screen instanceof ChatScreen)) { // 1이면 채팅 ui에서만 렌더링
            return;
        } // 2이상은 무조건 렌더링

        boolean kr = KoreanInput.isKorMode();
        String idiText = (kr ? "한글" : "영문");
        int textWidth = font.width(idiText);

        if (useAnimation && kr != prevKrMode) {
            modeChangeAnimation.play();
            modeBgColorAnimation.play();
            modeBgColorAnimation.setEndColor(kr ? Theme.adjColor(Theme.PRIMARY, 70) : Theme.adjColor(Theme.SECONDARY, 70));
            prevKrMode = kr;
        }

        int alertBg = calculateAlertBackground(useAnimation);

        // kor indicator bg
        renderIndeicatorBackground(matrixStack, x, y, textWidth);
        renderIndicatorBadge(matrixStack, x, y, kr);
        renderAlertBackground(matrixStack, x, y, useAnimation, textWidth, alertBg);

        // kor indicator text
        float textX = x + 2 + (textWidth + 6 - font.width(idiText)) / 2f;
        UITools.drawText(font, matrixStack, idiText, textX, y + 1, Theme.FOREGROUND, Theme.FOREGROUND_SHADOW_LIGHT);
    }

    private int calculateAlertBackground(boolean useAnimation) {
        int alertBg;
        if (maxLen == len) {
            alertText = "꽉참";
            alertBg = Theme.DANGER;
        } else if (len > maxLen * 0.7) {
            if (useAnimation) {
                if (alertText == null) {
                    alertWidthAnimation.play();
                }
                if (maxLen - len < 7 && prevLen != len) {
                    alertBoomingAnimation.play();
                    prevLen = len;
                }
            }
            alertText = maxLen - len + unit;
            alertBg = Theme.WARN;
        } else {
            alertText = null;
            alertBg = -1;
        }
        return alertBg;
    }

    private void renderIndeicatorBackground(MatrixStack matrixStack, float x, float y, int textWidth) {
        if (modeBgColorAnimation.isRunning()) {
            UITools.drawArea(matrixStack, x, y, textWidth + 8, height, modeBgColorAnimation.update());
        } else {
            UITools.drawArea(matrixStack, x, y, textWidth + 8, height, Theme.BACKGROUND);
        }
    }

    private void renderIndicatorBadge(MatrixStack matrixStack, float x, float y, boolean kr) {
        if (modeChangeAnimation.isRunning()) {
            UITools.drawArea(matrixStack, x, y, 2, height, !kr ? Theme.PRIMARY : Theme.SECONDARY);
            UITools.drawArea(matrixStack, x, y, 2, (int) (height * modeChangeAnimation.update()), kr ? Theme.PRIMARY : Theme.SECONDARY);
        } else {
            UITools.drawArea(matrixStack, x, y, 2, height, kr ? Theme.PRIMARY : Theme.SECONDARY);
        }
    }

    private void renderAlertBackground(MatrixStack matrixStack, float x, float y, boolean useAnimation, int textWidth, int alertBg) {
        if (alertText != null) {
            final float alertX = x + textWidth + 10;
            final float alertWidth = 26;

            // alert bg
            float p = useAnimation ? alertWidthAnimation.update().floatValue() : 1f;
            UITools.drawArcRect(matrixStack, alertX, y, alertX + alertWidth * p, y + height, alertBg);

            // alert text
            if (p > 0.9) {
                float scale = useAnimation ? 1 + alertBoomingAnimation.update().floatValue() * 0.3f : 1;
                int alertTextX = (int) ((alertX + (alertWidth - font.width(alertText) * scale) / 2) / scale);
                int alertTextY = (int) ((y + (height - font.lineHeight * scale) / 2) / scale);
                UITools.drawText(font, matrixStack, alertText, alertTextX, alertTextY, Theme.PRIMARY_FOREGROUND, Theme.FOREGROUND_SHADOW, UITools.NONE,
                        useAnimation ? scale : 1);
            }
        }
    }
}
