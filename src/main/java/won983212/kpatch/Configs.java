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

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;

public class Configs {
	public static final Configs instance = new Configs();
	private static final File configFile = new File(Minecraft.getMinecraft().mcDataDir, "/config/koreaninput.cfg");
	private static final HashMap<String, String> defaultValues = new HashMap<>();
	private static Properties data = new Properties();

	// General
	/*public static final String INPUT_KEYBOARD_ARRAY = "inputKeyboardArray";
	public static final String CHAT_CONVERT_KOR_MODE = "chatConvertKorMode";*/
	
	// HotKeys
	public static final String KEY_KOR = "keyKor";
	public static final String KEY_HANJA = "keyHanja";
	public static final String KEY_COLOR = "keyColor";
	
	// UI
	public static final String IME_INDICATOR_VISIBLE_MODE = "imeIndicatorVisible";
	public static final String IME_INDICATOR_ANIMATE = "imeIndicatorAnimate";
	public static final String UI_ANIMATE = "uiAnimate";
	
	static {
		/*// 키보드 자판 배열을 설정합니다.
		// 0: 두벌식, 1: 세벌식 390, 2: 세벌식 최종
		defaultValues.put(INPUT_KEYBOARD_ARRAY, "0");
		
		// 채팅 영타 변환 모드
		// 0: 끄기, 1: 한글이 포함되지 않은 문장만 변환, 2: 항상 변환
		defaultValues.put(CHAT_CONVERT_KOR_MODE, "0");*/
		
		
		// 한영 변환키를 설정합니다. (기본 LControl)
		defaultValues.put(KEY_KOR, String.valueOf(Keyboard.KEY_LCONTROL));
		
		// 한자 입력기 키를 설정합니다. (기본 한자키)
		defaultValues.put(KEY_HANJA, String.valueOf(Keyboard.KEY_KANJI));
		
		// 색 입력기 키를 설정합니다. (기본 Insert키)
		defaultValues.put(KEY_COLOR, String.valueOf(Keyboard.KEY_INSERT));
		
		
		// IME Indicator를 표시합니다
		// 0: 끄기, 1: 채팅창에서만 표시, 2: 모든 입력 필드(택스트 필드, 책, 팻말)에서 표시
		defaultValues.put(IME_INDICATOR_VISIBLE_MODE, "1");
		
		// IME Indicator에서 에니메이션 효과를 켭니다.
		defaultValues.put(IME_INDICATOR_ANIMATE, "true");
		
		// SimpleUI에서 에니메이션 효과를 켭니다.
		defaultValues.put(UI_ANIMATE, "true");
		
		
		// 설정파일 버전정보. 버전이 legacy하면 업그레이드한다.
		defaultValues.put("version", KoreanInputPatch.VERSION);
	}

	public static boolean load() {
		try {
			data.load(new FileInputStream(configFile));
			
			String cfgVersion = data.getProperty("version");
			if(cfgVersion == null || !cfgVersion.equals(KoreanInputPatch.VERSION)) {
				System.out.println("설정파일이 이전버전입니다. 업그레이드합니다.");
				System.out.println("설정버전: " + cfgVersion + " / 최신버전: " + KoreanInputPatch.VERSION);
				upgrade();
				return false;
			} else if(data.values().size() != defaultValues.values().size()){
				System.out.println("설정파일이 손상되어서 수정했습니다.");
				return false;
			}
			return true;
		} catch (FileNotFoundException e) {
			System.out.println("설정파일이 없으므로 새로 생성함.");
			setDefault();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
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
