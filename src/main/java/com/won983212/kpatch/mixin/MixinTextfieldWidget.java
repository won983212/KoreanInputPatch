package com.won983212.kpatch.mixin;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.won983212.kpatch.wrapper.TextfieldWrapper;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TextFieldWidget.class)
public class MixinTextfieldWidget {
    private TextfieldWrapper wrapper;

    @Inject(method = "<init>*", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        wrapper = new TextfieldWrapper((TextFieldWidget) (Object) this);
    }

    @Inject(method = "keyPressed", at = @At("HEAD"), cancellable = true)
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> ci) {
        if (wrapper.keyPressed(keyCode, scanCode, modifiers))
            ci.setReturnValue(true);
    }

    @Inject(method = "charTyped", at = @At("HEAD"), cancellable = true)
    private void charTyped(char typedChar, int keyCode, CallbackInfoReturnable<Boolean> ci) {
        if (wrapper.charTyped(typedChar, keyCode))
            ci.setReturnValue(true);
    }

    @Inject(method = "mouseClicked", at = @At("RETURN"))
    private void mouseClicked(double mouseX, double mouseY, int mouseButton, CallbackInfoReturnable<Boolean> ci) {
        wrapper.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Inject(method = "renderButton", at = @At("HEAD"))
    private void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partial, CallbackInfo ci) {
        wrapper.renderButton(matrixStack);
    }
}
