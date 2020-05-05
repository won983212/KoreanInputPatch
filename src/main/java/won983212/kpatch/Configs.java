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
	private static final File configFile = new File(Minecraft.getMinecraft().mcDataDir, "/config/koreaninput.cfg");
	private static final HashMap<String, String> defaultValues = new HashMap<>();
	private Properties data = new Properties();
	
	public static final String IME_INDICATOR_VISIBLE_MODE = "imeIndicatorVisibleMode";
	
	static {
		// IME Indicator를 언제 표시할 것인가
		// 0: 아얘 끄기, 1: 채팅창에서만 보임 (default), 2: 채팅창 & 텍스트 필드에서 보임
		defaultValues.put(IME_INDICATOR_VISIBLE_MODE, "1");
		
		// 설정파일 버전정보. 버전이 legacy하면 업그레이드한다.
		defaultValues.put("version", KoreanInputPatch.VERSION);
	}

	public void load() {
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
	
	public void save() {
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
	
	public void setDefault() {
		for(Entry<String, String> ent : defaultValues.entrySet()) {
			data.setProperty(ent.getKey(), ent.getValue());
		}
		save();
	}
	
	public void upgrade() {
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
	
	public void set(String key, Object obj) {
		data.setProperty(key, obj.toString());
	}
	
	public String get(String key, String defaultValue) {
		return data.getProperty(key, defaultValue);
	}
	
	public int getInt(String key, int defaultValue) {
		String data = this.data.getProperty(key);
		if(data == null) {
			return defaultValue;
		} else {
			return Integer.parseInt(data);
		}
	}
	
	public double getDouble(String key, double defaultValue) {
		String data = this.data.getProperty(key);
		if(data == null) {
			return defaultValue;
		} else {
			return Double.parseDouble(data);
		}
	}
}
