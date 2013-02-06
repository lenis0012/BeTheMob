package com.lenis0012.bukkit.btm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.lenis0012.bukkit.btm.api.Disguise;

public class BTMTaskManager {
	private BeTheMob plugin;
	private int task;
	private static HashMap<String, List<Disguise>> render = new HashMap<String, List<Disguise>>();
	
	public BTMTaskManager(BeTheMob plugin) {
		this.plugin = plugin;
	}
	
	public void start() {
		task = Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				BeTheMob plugin = BeTheMob.instance;
				for(Player player : Bukkit.getServer().getOnlinePlayers()) {
					String name = player.getName();
					
					List<Disguise> list;
					if(render.containsKey(name))
						list = render.get(name);
					else
						list = new ArrayList<Disguise>();
					
					for(String user : plugin.disguises.keySet()) {
						if(!user.equals(name)) {
							Disguise dis = plugin.disguises.get(user);
							Location l1 = player.getLocation();
							Location l2 = dis.getLocation();
							if(l2.getWorld() == l1.getWorld()) {
								double distance = l1.distance(l2);
								if(!list.contains(dis) && distance <= Bukkit.getViewDistance() * 10) {
									list.add(dis);
									dis.spawn(player);
								} else if(list.contains(dis) && distance > Bukkit.getViewDistance() * 10) {
									list.remove(dis);
									dis.unLoadFor(player);
								}
							}
						}
					}
					render.put(name, list);
				}
			}
		}, 20, 20);
	}
	
	/**
	 * Notigy a player changed his world
	 * 
	 * @param player		Player who changed world
	 */
	public static void notifyWorldChanged(Player player) {
		String name = player.getName();
		BeTheMob plugin = BeTheMob.instance;
		
		render.put(name, new ArrayList<Disguise>());
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			
			for(String user : render.keySet()) {
				List<Disguise> list = render.get(user);
				if(list.contains(dis)) {
					list.remove(dis);
					render.put(user, list);
				}
			}
		}
 	}
	
	/**
	 * Notify a player left the game
	 * 
	 * @param player		Player who left
	 */
	public static void notifyPlayerLeft(Player player) {
		String name = player.getName();
		BeTheMob plugin = BeTheMob.instance;
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			
			for(String user : render.keySet()) {
				List<Disguise> list = render.get(user);
				
				if(list.contains(dis)) {
					list.remove(dis);
					render.put(user, list);
				}
			}
		}
	}
	
	/**
	 * Add a player to be rendered
	 * 
	 * @param player		Player whjo got rendered
	 * @param dis			Disguise from the player
	 */
	public static void addPlayerToRender(Player player, Disguise dis) {
		String name = player.getName();
		
		List<Disguise> list;
		if(render.containsKey(name))
			list = render.get(name);
		else
			list = new ArrayList<Disguise>();
		
		list.add(dis);
		render.put(name, list);
	}
	
	public void stop() {
		Bukkit.getServer().getScheduler().cancelTask(task);
	}
}
