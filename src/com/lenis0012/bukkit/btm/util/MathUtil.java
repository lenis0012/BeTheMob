package com.lenis0012.bukkit.btm.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MathUtil {
	/**
	 * Floor a double to an integer
	 * 
	 * @param value	Value to floor
	 * 
	 * @return Floored double
	 */
	public static int floor(double value) {
		int floored = (int)value;
		return value < floored ? floored - 1 : floored;
	}
	
	/**
	 * Get all numbers from a Sting
	 * 
	 * @param str String to be checked
	 * @return Integers the String contained
	 */
	public static int countNrs(String str) {
		String result = "";
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(str);
		
		while(matcher.find()) {
			result += matcher.group();
		}
		
		return result != "" ? Integer.valueOf(result) : 0;
	}
	
	/**
	 * Parse an integer with a default
	 * 
	 * @param value Value
	 * @param def Default
	 * @return Parsed string with default
	 */
	public static int parseIntWithDefault(String value, int def) {
		int par1Int = def;
		
		try {
			par1Int = Integer.parseInt(value);
		} catch(Exception e) {
			;
		}
		
		return par1Int;
	}
	
	/**
	 * Floor a float
	 * 
	 * @param value Float to floor
	 * @return flored float
	 */
	public static int floor(float value) {
		int rounded = (int) value;
		return value < rounded ? rounded - 1 : rounded;
	}
	
	/**
	 * Roof a double
	 * 
	 * @param value Double to roof
	 * @return roofed value
	 */
	public static int roof(double value) {
		int rounded = (int) value;
		return rounded < value ? rounded + 1 : rounded;
	}
}
