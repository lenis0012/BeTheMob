package com.lenis0012.bukkit.btm.util;

import org.bukkit.ChatColor;

public class ColorUtil {
	
	/**
	 * Replace all colors in a message
	 * 
	 * @param message		Message to replace colors
	 * 
	 * @return				Message with fixed colors
	 */
	public static String fixColors(String message) {
		if(message == null)
			return message;
		
		message = message.replaceAll("&0", ChatColor.BLACK.toString());
		message = message.replaceAll("&1", ChatColor.DARK_BLUE.toString());
		message = message.replaceAll("&2", ChatColor.DARK_GREEN.toString());
		message = message.replaceAll("&3", ChatColor.DARK_AQUA.toString());
		message = message.replaceAll("&4", ChatColor.DARK_RED.toString());
		message = message.replaceAll("&5", ChatColor.DARK_PURPLE.toString());
		message = message.replaceAll("&6", ChatColor.GOLD.toString());
		message = message.replaceAll("&7", ChatColor.GRAY.toString());
		message = message.replaceAll("&8", ChatColor.DARK_GRAY.toString());
		message = message.replaceAll("&9", ChatColor.BLUE.toString());
		message = message.replaceAll("&a", ChatColor.GREEN.toString());
		message = message.replaceAll("&b", ChatColor.AQUA.toString());
		message = message.replaceAll("&c", ChatColor.RED.toString());
		message = message.replaceAll("&d", ChatColor.LIGHT_PURPLE.toString());
		message = message.replaceAll("&e", ChatColor.YELLOW.toString());
		message = message.replaceAll("&f", ChatColor.WHITE.toString());
		message = message.replaceAll("&r", ChatColor.WHITE.toString());
		return message;
	}
}
