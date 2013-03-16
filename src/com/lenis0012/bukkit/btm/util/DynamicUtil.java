package com.lenis0012.bukkit.btm.util;

import java.lang.reflect.Field;

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
			field.set(instance, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}