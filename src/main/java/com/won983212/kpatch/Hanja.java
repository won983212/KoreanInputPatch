package com.won983212.kpatch;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.IResource;
import net.minecraft.util.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class Hanja {
    private static final HashMap<Character, Hanja[]> HANJA_DATA = new HashMap<>();
    public char hanja;
    public String meaning;

    public Hanja(char hanja, String meaning) {
        this.hanja = hanja;
        this.meaning = meaning;
    }

    public boolean isSpecial() {
        return meaning.length() == 0;
    }

    @Override
    public String toString() {
        return isSpecial() ? String.valueOf(hanja) : (hanja + " " + meaning);
    }

    public static Hanja[] getHanjas(char key) {
        return HANJA_DATA.get(key);
    }

    public static void loadHanjas() {
        long start = System.nanoTime();
        try {
            ResourceLocation location = new ResourceLocation(KoreanInputPatch.MODID, "hanja");
            IResource res = Minecraft.getInstance().getResourceManager().getResource(location);
            BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream(), StandardCharsets.UTF_8));

            char snd = 0;
            ArrayList<Hanja> hanjaBuffer = new ArrayList<>();

            String buf;
            while ((buf = br.readLine()) != null) {
                if (buf.startsWith("#")) {
                    if (!hanjaBuffer.isEmpty()) {
                        HANJA_DATA.put(snd, hanjaBuffer.toArray(new Hanja[0]));
                        hanjaBuffer.clear();
                    }
                    snd = buf.charAt(1);
                } else {
                    hanjaBuffer.add(new Hanja(buf.charAt(0), buf.substring(1)));
                }
            }

            if (!hanjaBuffer.isEmpty())
                HANJA_DATA.put(snd, hanjaBuffer.toArray(new Hanja[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }
        long end = System.nanoTime();
        Logger.info("Hanja Loaded! (" + (end - start) / 1000000.0 + "ms)");
    }
}