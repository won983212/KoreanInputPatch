package com.won983212.kpatch;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.win32.StdCallLibrary;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFWNativeWin32;

public class Imm32 {
    public static void ChangeIME(boolean useAlphanumeric) {
        if(!Platform.isWindows())
            return;

        IImm32 context = IImm32.INSTANCE;
        long hWnd = GLFWNativeWin32.glfwGetWin32Window(Minecraft.getInstance().getWindow().getWindow());
        long hIMC = context.ImmGetContext(hWnd);
        int dwConversion = (useAlphanumeric ? IImm32.IME_CMODE_ALPHANUMERIC : IImm32.IME_CMODE_NATIVE);
        context.ImmSetConversionStatus(hIMC, dwConversion, 0);
    }

    private interface IImm32 extends StdCallLibrary {
        IImm32 INSTANCE = Native.loadLibrary("Imm32", IImm32.class);

        int IME_CMODE_ALPHANUMERIC = 0x0;
        int IME_CMODE_NATIVE = 0x1;

        long ImmGetContext(long hWnd);
        boolean ImmSetConversionStatus(long hIMC, int fdwConversion, int fdwSentence);
    }
}
