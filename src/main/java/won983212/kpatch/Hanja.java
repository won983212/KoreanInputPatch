package won983212.kpatch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;

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
			IResource res = Minecraft.getMinecraft().getResourceManager().getResource(location);
			InputStream is = res.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));

			char snd = 0;
			ArrayList<Hanja> hanjaBuffer = new ArrayList<>();

			String buf = null;
			while((buf = br.readLine()) != null) {
				if(buf.startsWith("#")) {
					if(!hanjaBuffer.isEmpty()) {
						HANJA_DATA.put(snd, hanjaBuffer.toArray(new Hanja[hanjaBuffer.size()]));
						hanjaBuffer.clear();
					}
					snd = buf.charAt(1);
				} else {
					hanjaBuffer.add(new Hanja(buf.charAt(0), buf.substring(1)));
				}
			}
			
			if(!hanjaBuffer.isEmpty()) {
				HANJA_DATA.put(snd, hanjaBuffer.toArray(new Hanja[hanjaBuffer.size()]));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = System.nanoTime();
		System.out.println("Hanja Loaded! (" + (end - start) / 1000000.0 + "ms)");
	}
}