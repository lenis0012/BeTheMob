package com.lenis0012.bukkit.btm.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.lenis0012.bukkit.btm.BTMTaskManager;
import com.lenis0012.bukkit.btm.BeTheMob;

/**
 * API class from BeTheMob
 * 
 * @author lenis0012
 */
public class Api {
	private BeTheMob plugin;
	
	public Api(BeTheMob plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Create a mob disguise
	 * 
	 * @param player		Player to disguise
	 * @param loc			Location to spawn the disguise
	 * @param type			EntityType of the disguise
	 * @param extras		List of extras
	 * 
	 * @return				Disguise
	 */
	public Disguise createDisguise(Player player, Location loc, EntityType type, List<String> extras) {
		if(extras == null) extras = new ArrayList<String>();
		return new Disguise(player, plugin.nextID--, loc, type, extras);
	}
	
	/**
	 * Create a player disguise
	 * 
	 * @param player		Player to be disguised
	 * @param loc			Location to spawn the disguise
	 * @param name			Name of the player to be disguised as
	 * @param itemInHand	Item in the hand of the disguise
	 * 
	 * @return				Disguise
	 */
	public Disguise createDisguise(Player player, Location loc, String name, int itemInHand) {
		return new Disguise(player, plugin.nextID--, loc, name, itemInHand);
	}
	
	/**
	 * Add a disguise to the plugin
	 * 
	 * @param player		Player to disguise
	 * @param disguise		Disguise to add
	 */
	public void addDisguise(Player player, Disguise disguise) {
		String name = player.getName();
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			plugin.disguises.remove(name);
			dis.despawn();
		}
		
		disguise.spawn(player.getLocation().getWorld());
		plugin.disguises.put(name, disguise);
		plugin.setHidden(player, true);
	}
	
	/**
	 * Remove the disguise from a player
	 * 
	 * @param player		Player to remove from disguise list
	 */
	public void removeDisguise(Player player) {
		String name = player.getName();
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			plugin.disguises.remove(name);
			dis.despawn();
			plugin.setHidden(player, false);
			BTMTaskManager.notifyPlayerUndisguised(name);
		}
	}
}