package com.lenis0012.bukkit.btm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.lenis0012.bukkit.btm.api.Api;
import com.lenis0012.bukkit.btm.api.Disguise;
import com.lenis0012.bukkit.btm.nms.PlayerConnectionCallback;
import com.lenis0012.bukkit.btm.nms.ProtocolLibManager;
import com.lenis0012.bukkit.btm.util.DynamicUtil;

public class BeTheMob extends JavaPlugin {
	public int nextID = Short.MAX_VALUE;
	public Map<String, Disguise> disguises = new HashMap<String, Disguise>();
	public static BeTheMob instance;
	public List<String> hidden = new ArrayList<String>();
	private BTMTaskManager task;
	private static Api api;
	public boolean protLib = false;
	private ProtocolLibManager protocol;
	public Logger log = Logger.getLogger("Minecraft");
	
	private String COMPAT_VERSION = "1_5_R1";
	
	@Override
	public void onEnable() {
		instance = this;
		PluginManager pm = this.getServer().getPluginManager();
		
		if(!this.isCompatible(COMPAT_VERSION)) {
			String version = DynamicUtil.MC_VERSION;
			version = version.isEmpty() ? "unknown" : version.substring(1);
			log.warning("[BeTheMob] BeTheMob has not been tested with this version of bukkit yet.");
			log.warning("[BeTheMob] The plugin might not work in '" + version + "'!");
		}
		
		getCommand("btm").setExecutor(new BTMCommand());
		pm.registerEvents(new BTMListener(), this);
		
		task = new BTMTaskManager(this);
		task.start();
		
		api = new Api(this);
		
		if(pm.getPlugin("ProtocolLib") != null) {
			this.protocol = new ProtocolLibManager(this);
			protocol.start();
			this.protLib = true;
			log.info("[BeTheMob] Hooked with ProtocolLib");
		}
		
		if(!protLib) {
			for(Player player : Bukkit.getServer().getOnlinePlayers()) {
				PlayerConnectionCallback.hook(player);
			}
		}
        saveDefaultConfig();//Saves the default config. Will not overwrite
	}
	
	private boolean isCompatible(String version) {
		try {
			Class.forName("net.minecraft.server." + version + ".World");
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	@Override
	public void onDisable() {
		task.cancel();
		
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
	
	public ClassLoader getLoader() {
		return this.getClassLoader();
	}
}
