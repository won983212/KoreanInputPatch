package com.won983212.kpatch.ui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.math.vector.Matrix4f;
import org.lwjgl.opengl.GL11;

import java.util.List;

public class UITools {
    public static final int NONE = 0;
    public static final int CENTER_H = 1;
    public static final int CENTER_V = 2;
    public static final int CENTER_BOTH = CENTER_H | CENTER_V;

    private static IVertexBuilder color(IVertexBuilder builder, int color) {
        float f3 = (float) (color >> 24 & 255) / 255.0F;
        float f = (float) (color >> 16 & 255) / 255.0F;
        float f1 = (float) (color >> 8 & 255) / 255.0F;
        float f2 = (float) (color & 255) / 255.0F;
        return builder.color(f, f1, f2, f3);
    }

    public static void drawRect(MatrixStack matrixStack, float left, float top, float right, float bottom, int color) {
        Matrix4f matrix = matrixStack.last().pose();
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.disableTexture();
        RenderSystem.defaultBlendFunc();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        color(bufferbuilder.vertex(matrix, left, bottom, 0), color).endVertex();
        color(bufferbuilder.vertex(matrix, right, bottom, 0), color).endVertex();
        color(bufferbuilder.vertex(matrix, right, top, 0), color).endVertex();
        color(bufferbuilder.vertex(matrix, left, top, 0), color).endVertex();
        bufferbuilder.end();
        WorldVertexBufferUploader.end(bufferbuilder);
        RenderSystem.enableTexture();
        RenderSystem.disableBlend();
    }

    private static void drawArc(MatrixStack matrixStack, float x, float y, float radius, int color, int seperatedIndex) {
        Matrix4f matrix = matrixStack.last().pose();
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(GL11.GL_TRIANGLE_FAN, DefaultVertexFormats.POSITION_COLOR);
        color(bufferbuilder.vertex(matrix, x, y, 0), color).endVertex();
        for (int t = 0; t <= radius * 2; t++) {
            double theta = Math.PI / (2 * radius * 2) * t + (seperatedIndex % 4) * Math.PI / 2;
            color(bufferbuilder.vertex(matrix, (float) (x + radius * Math.cos(theta)), (float) (y - radius * Math.sin(theta)), 0), color).endVertex();
        }
        bufferbuilder.end();
    }

    private static void drawRoundRect(MatrixStack matrixStack, float left, float top, float right, float bottom, float rad, int color) {
        if (rad > 0) {
            drawRect(matrixStack, left + rad, top, right - rad, bottom, color);
            drawRect(matrixStack, left, top + rad, left + rad, bottom - rad, color);
            drawRect(matrixStack, left + rad, top + rad, right, bottom - rad, color);

            // render arcs
            RenderSystem.enableBlend();
            RenderSystem.disableTexture();
            RenderSystem.defaultBlendFunc();
            drawArc(matrixStack, left + rad, top + rad, rad, color, 1);
            drawArc(matrixStack, left + rad, bottom - rad, rad, color, 2);
            drawArc(matrixStack, right - rad, bottom - rad, rad, color, 3);
            drawArc(matrixStack, right - rad, top + rad, rad, color, 4);
            RenderSystem.enableTexture();
            RenderSystem.disableBlend();
        } else {
            drawRect(matrixStack, left, top, right, bottom, color);
        }
    }

    public static void drawArcRect(MatrixStack matrixStack, float left, float top, float right, float bottom, int color) {
        drawArcRect(matrixStack, left, top, right, bottom, color, NONE, NONE, 0);
    }

    public static void drawArcRect(MatrixStack matrixStack, float left, float top, float right, float bottom, int color, int shadow) {
        drawArcRect(matrixStack, left, top, right, bottom, color, shadow, NONE, 0);
    }

    public static void drawArcRect(MatrixStack matrixStack, float left, float top, float right, float bottom, int color, int shadow,
                                   int border) {
        drawArcRect(matrixStack, left, top, right, bottom, color, shadow, border, 0);
    }

    public static void drawArcRect(MatrixStack matrixStack, float left, float top, float right, float bottom, int color, int shadow,
                                   int border, int round) {
        if (left > right) {
            float i = left;
            left = right;
            right = i;
        }

        if (top > bottom) {
            float j = top;
            top = bottom;
            bottom = j;
        }

        if (shadow != NONE) {
            drawRoundRect(matrixStack, left + 1, top + 1, right + 1, bottom + 1, round, shadow);
        }

        if (border != NONE) {
            drawRoundRect(matrixStack, left, top, right, bottom, round, border);
            drawRoundRect(matrixStack, left + 0.5f, top + 0.5f, right - 0.5f, bottom - 0.5f, round, color);
        } else {
            drawRoundRect(matrixStack, left, top, right, bottom, round, color);
        }
    }

    public static void drawArea(MatrixStack matrixStack, float x, float y, float width, float height, int color) {
        drawArcRect(matrixStack, x, y, x + width, y + height, color);
    }

    public static void drawArea(MatrixStack matrixStack, float x, float y, float width, float height, int color, int shadow) {
        drawArcRect(matrixStack, x, y, x + width, y + height, color, shadow);
    }

    public static void drawArea(MatrixStack matrixStack, float x, float y, float width, float height, int color, int shadow, int border) {
        drawArcRect(matrixStack, x, y, x + width, y + height, color, shadow, border);
    }

    public static void drawArea(MatrixStack matrixStack, float x, float y, float width, float height, int color, int shadow, int border,
                                int round) {
        drawArcRect(matrixStack, x, y, x + width, y + height, color, shadow, border, round);
    }

    public static int drawText(FontRenderer fr, MatrixStack matrixStack, String text, float x, float y, int color, int shadow) {
        return drawText(fr, matrixStack, text, x, y, color, shadow, NONE, 1);
    }

    public static int drawText(FontRenderer fr, MatrixStack matrixStack, String text, float x, float y, int color, int shadow, int arrange) {
        return drawText(fr, matrixStack, text, x, y, color, shadow, arrange, 1);
    }

    public static int drawText(FontRenderer fr, MatrixStack matrixStack, String text, float x, float y, int color, int shadow, int arrange, float scale) {
        int len = fr.width(text);
        if (arrange != NONE) {
            if ((arrange & CENTER_H) > 0)
                x = x - fr.width(text) / 2f;
            if ((arrange & CENTER_V) > 0)
                y = y - fr.lineHeight / 2f;
        }

        if (scale != 1) {
            matrixStack.pushPose();
            matrixStack.scale(scale, scale, scale);
        }

        if (shadow != NONE) {
            fr.draw(matrixStack, text, x + 0.5f, y + 0.5f, shadow);
            len++;
        }

        fr.draw(matrixStack, text, x, y, color);

        if (scale != 1) {
            matrixStack.popPose();
        }
        return len;
    }
}
