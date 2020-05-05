package won983212.kpatch;

import java.util.HashMap;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindFieldException;

public class ObfuscatedReflection {
	private static final HashMap<String, String> svgMap = new HashMap<>();

	static {
		// GuiEditSign.class
		svgMap.put("tileSign", "??a??");
		svgMap.put("editLine", "?????");
	}

	public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String fieldName) {
		return ObfuscationReflectionHelper.getPrivateValue(classToAccess, instance, fieldName, svgMap.get(fieldName));
	}

	public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String fieldName) {
		ObfuscationReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldName, svgMap.get(fieldName));
	}
}
