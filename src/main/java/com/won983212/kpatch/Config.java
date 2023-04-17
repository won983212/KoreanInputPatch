package com.won983212.kpatch;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Mod.EventBusSubscriber
public class Config {
    private static final File configFile = new File(Minecraft.getInstance().gameDirectory, "/config/koreaninput.cfg");
    private static final HashMap<String, String> defaultValues = new HashMap<>();
    private static final Properties data = new Properties();

    public static final String IME_INDICATOR_VISIBLE_MODE = "imeIndicatorVisible";
    public static final String IME_INDICATOR_ANIMATE = "indicatorAnimate";
    
    public static final String UI_ANIMATE = "uiAnimate";

    static {
        // IME Indicator를 표시합니다
        // 0: 끄기, 1: 채팅창에서만 표시, 2: 모든 입력 필드(택스트 필드, 책, 팻말)에서 표시
        defaultValues.put(IME_INDICATOR_VISIBLE_MODE, "1");

        // IME Indicator에서 에니메이션 효과를 켭니다.
        defaultValues.put(IME_INDICATOR_ANIMATE, "true");
    }

    public static boolean load() {
        Key.registerKeys();
        try {
            data.load(new FileInputStream(configFile));
            if (data.values().size() != defaultValues.values().size()) {
                Logger.warn("Config file is incorrect! We correct it now.");
                upgrade();
                return false;
            }
            return true;
        } catch (FileNotFoundException e) {
            Logger.warn("There is no cfg file. Created new one.");
            setDefault();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void save() {
        try {
            if (!configFile.getParentFile().exists())
                configFile.getParentFile().mkdirs();
            if (!configFile.exists())
                configFile.createNewFile();
            data.store(new FileOutputStream(configFile), "Korean Input Mod Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setDefault() {
        for (Map.Entry<String, String> ent : defaultValues.entrySet()) {
            data.setProperty(ent.getKey(), ent.getValue());
        }
        save();
    }

    public static void upgrade() {
        for (Map.Entry<String, String> ent : defaultValues.entrySet()) {
            String key = ent.getKey();
            if (!data.containsKey(key)) {
                data.setProperty(key, ent.getValue());
            }
        }

        for (String key : data.stringPropertyNames()) {
            if (!defaultValues.containsKey(key)) {
                data.remove(key);
            }
        }

        save();
    }

    public static void set(String key, Object obj) {
        data.setProperty(key, obj.toString());
    }

    public static String get(String key) {
        return data.getProperty(key, defaultValues.getOrDefault(key, ""));
    }

    public static int getInt(String key) {
        String str = data.getProperty(key);
        String result;
        if (str == null) {
            result = defaultValues.getOrDefault(key, "0");
        } else {
            result = str;
        }
        return Integer.parseInt(result);
    }

    public static double getDouble(String key) {
        String str = data.getProperty(key);
        String result;
        if (str == null) {
            result = defaultValues.getOrDefault(key, "0.0");
        } else {
            result = str;
        }
        return Double.parseDouble(result);
    }

    public static boolean getBoolean(String key) {
        return get(key).equals("true");
    }

    public static class Key {
        public static final KeyBinding KEY_KOR;
        public static final KeyBinding KEY_HANJA;
        public static final KeyBinding KEY_COLOR;

        public static void registerKeys() {
            ClientRegistry.registerKeyBinding(KEY_KOR);
            ClientRegistry.registerKeyBinding(KEY_HANJA);
            ClientRegistry.registerKeyBinding(KEY_COLOR);
        }

        static {
            KEY_KOR = new KeyBinding("key.kor.desc", GLFW.GLFW_KEY_RIGHT_ALT, "key.koreaninput.category");
            KEY_HANJA = new KeyBinding("key.hanja.desc", GLFW.GLFW_KEY_RIGHT_CONTROL, "key.koreaninput.category");
            KEY_COLOR = new KeyBinding("key.color.desc", GLFW.GLFW_KEY_INSERT, "key.koreaninput.category");
        }
    }
}
