package com.lenis0012.bukkit.btm.util;

public class MathUtil {
	/**
	 * Floor a double to an integer
	 * 
	 * @param value		Value to floor
	 * 
	 * @return			Floored double
	 */
	public static int floor(double value) {
		int floored = (int)value;
		return value < floored ? floored - 1 : floored;
	}
}
