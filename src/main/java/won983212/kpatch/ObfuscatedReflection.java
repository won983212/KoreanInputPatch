package won983212.kpatch;

import java.security.InvalidParameterException;
import java.util.HashMap;

import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindFieldException;

public class ObfuscatedReflection {
	private static final HashMap<String, String> svgMap = new HashMap<>();

	// TODO LATEST: setup svg names.
	static {
		// GuiEditSign.class
		svgMap.put("tileSign", "??a??");
		svgMap.put("editLine", "?????");

		// GuiScreenBook.class
		svgMap.put("book", "?????");
		svgMap.put("bookIsUnsigned", "?????");
	}

	public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String fieldName) {
		checkFieldName(fieldName);
		return ObfuscationReflectionHelper.getPrivateValue(classToAccess, instance, fieldName, svgMap.get(fieldName));
	}

	public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String fieldName) {
		checkFieldName(fieldName);
		ObfuscationReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldName, svgMap.get(fieldName));
	}
	
	private static void checkFieldName(String fieldName) {
		if(!svgMap.containsKey(fieldName))
			throw new InvalidParameterException("Can't find specified fieldname: " + fieldName);
	}
}
