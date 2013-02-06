package com.lenis0012.bukkit.btm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.WeakHashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.lenis0012.bukkit.btm.api.Api;
import com.lenis0012.bukkit.btm.api.Disguise;

public class BeTheMob extends JavaPlugin {
	public int nextID = Short.MAX_VALUE;
	public WeakHashMap<String, Disguise> disguises = new WeakHashMap<String, Disguise>();
	public static BeTheMob instance;
	public List<String> hidden = new ArrayList<String>();
	private BTMTaskManager task;
	private static Api api;
	
	@Override
	public void onEnable() {
		instance = this;
		PluginManager pm = this.getServer().getPluginManager();
		
		getCommand("btm").setExecutor(new BTMCommand());
		pm.registerEvents(new BTMListener(), this);
		
		task = new BTMTaskManager(this);
		task.start();
		
		api = new Api(this);
	}
	
	@Override
	public void onDisable() {
		task.stop();
		
		Iterator<String> it = this.disguises.keySet().iterator();
		while(it.hasNext()) {
			String user = it.next();
			Disguise dis = this.disguises.get(user);
			dis.despawn();
			it.remove();
			
			Player p = Bukkit.getPlayer(user);
			if(p != null && p.isOnline()) {
				this.setHidden(p, false);
				p.sendMessage(ChatColor.GREEN+"You have been undisguised due a reload");
			} else
				hidden.remove(user);
		}
	}
	
	/**
	 * Get the api from the plugins
	 * 
	 * @return				API
	 */
	public static Api getApi() {
		return api;
	}
	
	/**
	 * Hide a player from the orthers
	 * 
	 * @param player		Player to hide
	 * @param value			Unhide or hide the player
	 */
	public void setHidden(Player player, boolean value) {
		String name = player.getName();
		if(value && !hidden.contains(name)) {
			hidden.add(name);
			for(Player online : Bukkit.getServer().getOnlinePlayers()) {
				online.hidePlayer(player);
			}
		}
		
		if(!value && hidden.contains(name)) {
			hidden.remove(name);
			for(Player online : Bukkit.getServer().getOnlinePlayers()) {
				online.showPlayer(player);
			}
		}
	}
	
	/**
	 * Check if a player is hidden
	 * 
	 * @param player		Player to check
	 * 
	 * @return				Player is hidden?
	 */
	public boolean isHidden(Player player) {
		String name = player.getName();
		
		return hidden.contains(name);
	}
}
