package com.lenis0012.bukkit.btm;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.lenis0012.bukkit.btm.type.Disguise;

public class DisguiseManager {
	private final Map<String, Disguise> disguises = new HashMap<String, Disguise>();
	
	/**
	 * Remove disguise entry from list.
	 * 
	 * @param player to clear
	 */
	public void removeDisguise(Player player) {
		disguises.remove(player.getName());
	}
	
	/**
	 * Add disguise entry to list.
	 * 
	 * @param player to use as key.
	 * @param disguise to store.
	 */
	public void setDisguised(Player player, Disguise disguise) {
		disguises.put(player.getName(), disguise);
	}
	
	/**
	 * Get a players disguise
	 * 
	 * @param player to get disguise from.
	 * @return players disguise entry, null if not found.
	 */
	public Disguise getDisguise(Player player) {
		return disguises.get(player.getName());
	}
	
	/**
	 * Check if the list contains a disguise entry.
	 * 
	 * @param player to check.
	 * @return Player is in disguise list?
	 */
	public boolean isDisguised(Player player) {
		return disguises.containsKey(player.getName());
	}
}