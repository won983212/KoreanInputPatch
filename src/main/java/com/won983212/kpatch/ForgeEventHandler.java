package com.won983212.kpatch;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.Queue;

@Mod.EventBusSubscriber(modid = KoreanInputPatch.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeEventHandler {
    private static final Queue<PostRenderInfo> postRenderQueue = Queues.newArrayDeque();

    @SubscribeEvent
    public static void onPostRenderGui(GuiScreenEvent.DrawScreenEvent.Post e) {
        synchronized (postRenderQueue) {
            if(!postRenderQueue.isEmpty()) {
                MatrixStack stack = e.getMatrixStack();
                while (!postRenderQueue.isEmpty()) {
                    PostRenderInfo renderInfo = postRenderQueue.poll();
                    RenderSystem.enableDepthTest();
                    stack.pushPose();
                    stack.translate(0, 0, renderInfo.zIndex);
                    renderInfo.renderer.render(stack);
                    stack.popPose();
                }
            }
        }
    }

    /**
     * Gui 최상단에 rendering합니다. <code>Runnable</code>로 그립니다.
     */
    public static void addTopRenderQueue(IPostRenderer renderer, int zIndex) {
        synchronized (postRenderQueue) {
            postRenderQueue.add(new PostRenderInfo(renderer, zIndex));
        }
    }

    public interface IPostRenderer {
        void render(MatrixStack matrixStack);
    }

    private static class PostRenderInfo {
        private final int zIndex;
        private final IPostRenderer renderer;

        private PostRenderInfo(IPostRenderer renderer, int zIndex){
            this.renderer = renderer;
            this.zIndex = zIndex;
        }
    }
}
