package src.com.lenis0012.bukkit.btm.api;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import src.com.lenis0012.bukkit.btm.BTMTaskManager;
import src.com.lenis0012.bukkit.btm.BeTheMob;
import src.com.lenis0012.bukkit.btm.fun.DropFactory;
import src.com.lenis0012.bukkit.btm.fun.IDropFactory;


/**
 * API class from BeTheMob
 * 
 * @author lenis0012
 */
public class Api {
	private BeTheMob plugin;
	private IDropFactory dropFactory;
	
	public Api(BeTheMob plugin) {
		this.plugin = plugin;
		this.dropFactory = new DropFactory();
	}
	
	/**
	 * Check if a player id disguised
	 * 
	 * @param player Player
	 * @return Disguised?
	 */
	public boolean isDisguised(Player player) {
		return plugin.disguises.containsKey(player.getName());
	}
	
	/**
	 * Gets a disguise from a player
	 * 
	 * @param player Player
	 * @return Disguise (null if player not disguised)
	 */
	public Disguise getDisguise(Player player) {
		return plugin.disguises.get(player.getName());
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
		boolean isVehicle = plugin.getVehicleList().contains(type.toString().toLowerCase());
		return new Disguise(player, plugin.nextID--, loc, type, extras, isVehicle);
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
	 * @param player Player to remove from disguise list
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
	
	/**
	 * Register a drop factory
	 * 
	 * @param dropFactory Drop Factory
	 * @param plugin Plugin who owns factory
	 */
	public void registerDropFactory(IDropFactory dropFactory, Plugin plugin) {
		this.dropFactory = dropFactory;
		plugin.getLogger().info("Drop factory '"+dropFactory.getName()+"' was registered by plugin: " + plugin.getName());
	}
	
	/**
	 * Gets the current drop factory
	 * 
	 * @return DropFactory
	 */
	public IDropFactory getDropFactory() {
		return dropFactory;
	}
}