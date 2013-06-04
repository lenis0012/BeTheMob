package src.com.lenis0012.bukkit.btm.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class DynamicUtil {
	public static final String MC_VERSION;
	
	static {
		String version = "";
		if(!checkVersion(version)) {
			StringBuilder builder = new StringBuilder();
			
			for(int a = 0; a < 10; a++) {
				for(int b = 0; b < 10; b++) {
					for(int c = 0; c < 10; c++) {
						builder.setLength(0);
						builder.append('v').append(a).append('_').append(b).append("_R").append(c);
						version = "."+builder.toString();
						if(checkVersion(version)) {
							a = b = c = 10;
						}
					}
				}
			}
		}
		
		MC_VERSION = version;
	}
	
	private static boolean checkVersion(String version) {
		try {
			Class.forName("net.minecraft.server"+version+".World");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	public static final String NMS_ROOT = "net.minecraft.server" + MC_VERSION;
	public static final String CB_ROOT = "org.bukkit.craftbukkit" + MC_VERSION;
	
	public static Class<?> getNMSClass(String name) {
		try {
			return Class.forName(NMS_ROOT + "." + name);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Invalid NMS class: " + name);
		}
	}
	
	public static Class<?> getCBClass(String name) {
		try {
			return Class.forName(CB_ROOT + "." + name);
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException("Invalid CB class: " + name);
		}
	}
	
	public static Field getField(Class<?> fromClass, String name) {
		try {
			return fromClass.getDeclaredField(name);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getValue(Object instance, Field field) {
		try {
			if(!field.isAccessible())
				field.setAccessible(true);
			return (T) field.get(instance);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static void setValue(Object instance, Field field, Object value) {
		try {
			if(!field.isAccessible())
				field.setAccessible(true);
			field.set(instance, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	public static Method getMethod(Class<?> fromClass, String name, Class<?>... params) {
		try {
			return fromClass.getDeclaredMethod(name, params);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T invoke(Method method, Object instance, Object... values) {
		try {
			if(!method.isAccessible())
				method.setAccessible(true);
			return (T) method.invoke(instance, values);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public static Constructor<Object> getConstructor(Class<?> fromClass, Class<?>... params) {
		try {
			return (Constructor<Object>) fromClass.getConstructor(params);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Object newInstance(Constructor<Object> constructor, Object... values) {
		try {
			return constructor.newInstance(values);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static Object newInstance(Class<?> fromClass) {
		try {
			return fromClass.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		return null;
	}
}