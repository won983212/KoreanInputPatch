package won983212.kpatch;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.InvalidParameterException;
import java.util.HashMap;

import net.minecraft.client.gui.GuiScreenBook;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToAccessFieldException;
import net.minecraftforge.fml.relauncher.ReflectionHelper.UnableToFindFieldException;

// TODO 나중에 AccessTransformer로 대체할 예정
public class ObfuscatedReflection {
	private static final HashMap<String, String> svgMap = new HashMap<>();

	static {
		// GuiEditSign.class
		svgMap.put("tileSign", "i");
		svgMap.put("editLine", "i");

		// GuiScreenBook.class
		svgMap.put("book", "i");
		svgMap.put("bookIsUnsigned", "i");
		svgMap.put("bookGettingSigned", "i");
		svgMap.put("bookTitle", "i");
		svgMap.put("bookIsModified", "i");
		svgMap.put("currPage", "i");
		svgMap.put("bookTotalPages", "i");
		svgMap.put("cachedComponents", "i");
		
		svgMap.put("f#pageGetCurrent", "i");
		svgMap.put("f#pageSetCurrent", "i");
		svgMap.put("f#updateButtons", "i");
		svgMap.put("f#sendBookToServer", "i");
	}
	
	public static <T, E> T getPrivateValue(Class<? super E> classToAccess, E instance, String fieldName) {
		checkFieldName(fieldName);
		return ObfuscationReflectionHelper.getPrivateValue(classToAccess, instance, fieldName, svgMap.get(fieldName));
	}
	
	public static <T> T getPrivateValue(Field field, Object instance) {
		try {
			return (T) field.get(instance);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T, E> void setPrivateValue(Class<? super T> classToAccess, T instance, E value, String fieldName) {
		checkFieldName(fieldName);
		ObfuscationReflectionHelper.setPrivateValue(classToAccess, instance, value, fieldName, svgMap.get(fieldName));
	}
	
	public static void setPrivateValue(Field field, Object instance, Object value) {
		try {
			field.set(instance, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static Method getPrivateMethod(Class classToAccess, String funcName, Class... parameterTypes) {
		checkMethodName(funcName);
		String[] names = ObfuscationReflectionHelper.remapFieldNames(classToAccess.getName(), funcName, svgMap.get("f#" + funcName));
		Exception error = null;
		for(String name : names) {
			try {
				Method m = classToAccess.getDeclaredMethod(name, parameterTypes);
				m.setAccessible(true);
				return m;
			} catch (Exception e) {
				error = e;
			}
		}
		throw new UnableToFindFieldException(names, error);
	}
	
	public static Field getPrivateField(Class classToAccess, String fieldName) {
		checkFieldName(fieldName);
		String[] names = ObfuscationReflectionHelper.remapFieldNames(classToAccess.getName(), fieldName, svgMap.get(fieldName));
		Exception error = null;
		for(String name : names) {
			try {
				Field m = classToAccess.getDeclaredField(name);
				m.setAccessible(true);
				return m;
			} catch (Exception e) {
				error = e;
			}
		}
		throw new UnableToFindFieldException(names, error);
	}
	
	public static <T> T invokeMethod(Method method, Object instance, Object... args) {
		try {
			return (T) method.invoke(instance, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void checkFieldName(String fieldName) {
		if(!svgMap.containsKey(fieldName))
			throw new InvalidParameterException("Can't find specified fieldname: " + fieldName);
	}
	
	private static void checkMethodName(String methodName) {
		if(!svgMap.containsKey("f#" + methodName))
			throw new InvalidParameterException("Can't find specified methodName: " + methodName);
	}
}
