package won983212.kpatch;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import net.minecraft.client.Minecraft;

public class Configs {
	public static final Configs instance = new Configs();
	private static final File configFile = new File(Minecraft.getMinecraft().mcDataDir, "/config/koreaninput.cfg");
	private static final HashMap<String, String> defaultValues = new HashMap<>();
	private static Properties data = new Properties();
	
	public static final String IME_INDICATOR_VISIBLE = "imeIndicatorVisible";
	public static final String IME_INDICATOR_ANIMATE = "imeIndicatorAnimate";
	
	static {
		// IME Indicator를 표시합니다
		// true: 채팅창에서 사용, false: 아얘 안보임
		defaultValues.put(IME_INDICATOR_VISIBLE, "true");
		
		// IME Indicator에서 에니메이션 효과를 켭니다.
		defaultValues.put(IME_INDICATOR_ANIMATE, "true");
		
		// 설정파일 버전정보. 버전이 legacy하면 업그레이드한다.
		defaultValues.put("version", KoreanInputPatch.VERSION);
	}

	public static void load() {
		try {
			data.load(new FileInputStream(configFile));
			
			String cfgVersion = data.getProperty("version");
			if(cfgVersion == null || cfgVersion != KoreanInputPatch.VERSION) {
				System.out.println("설정파일이 이전버전입니다. 업그레이드합니다.");
				upgrade();
			} else if(data.values().size() != defaultValues.values().size()){
				System.out.println("설정파일이 손상되어서 수정했습니다.");
			}
		} catch (FileNotFoundException e) {
			System.out.println("설정파일이 없으므로 새로 생성함.");
			setDefault();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void save() {
		try {
			if(!configFile.getParentFile().exists())
				configFile.getParentFile().mkdirs();
			if(!configFile.exists())
				configFile.createNewFile();
			data.store(new FileOutputStream(configFile), "Korean Input Mod Configuration");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setDefault() {
		for(Entry<String, String> ent : defaultValues.entrySet()) {
			data.setProperty(ent.getKey(), ent.getValue());
		}
		save();
	}
	
	public static void upgrade() {
		for(Entry<String, String> ent : defaultValues.entrySet()) {
			String key = ent.getKey();
			if(!data.containsKey(key)) {
				data.setProperty(key, ent.getValue());
			}
		}
		
		Iterator<String> iter = data.stringPropertyNames().iterator();
		while(iter.hasNext()) {
			String key = iter.next();
			if(!defaultValues.containsKey(key)) {
				data.remove(key);
			}
		}
		
		data.setProperty("version", KoreanInputPatch.VERSION);
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
		if(str == null) {
			result = defaultValues.getOrDefault(key, "0");
		} else {
			result = str;
		}
		return Integer.parseInt(result);
	}
	
	public static double getDouble(String key) {
		String str = data.getProperty(key);
		String result;
		if(str == null) {
			result = defaultValues.getOrDefault(key, "0.0");
		} else {
			result = str;
		}
		return Double.parseDouble(result);
	}
	
	public static boolean getBoolean(String key) {
		return get(key).equals("true");
	}
}
