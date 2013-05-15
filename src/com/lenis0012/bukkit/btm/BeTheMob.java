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
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.lenis0012.bukkit.btm.api.Api;
import com.lenis0012.bukkit.btm.api.Disguise;
import com.lenis0012.bukkit.btm.fun.DropFactory;
import com.lenis0012.bukkit.btm.fun.IDropFactory;
import com.lenis0012.bukkit.btm.nms.PlayerConnectionCallback;
import com.lenis0012.bukkit.btm.nms.ProtocolLibManager;
import com.lenis0012.bukkit.btm.util.DynamicUtil;
import com.lenis0012.bukkit.btm.util.MathUtil;

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
	private IDropFactory dropFactory;
	
	private static final int MAX_VERSION = 152;
	private static final int MIN_VERSION = 151;
	
	@Override
	public void onEnable() {
		instance = this;
		PluginManager pm = this.getServer().getPluginManager();
		String version = DynamicUtil.MC_VERSION;
		
		if(!this.isCompatible(version)) {
			version = version.isEmpty() ? "unknown" : version.substring(1);
			log.warning("[BeTheMob] BeTheMob has not been tested with this version of bukkit yet.");
			log.warning("[BeTheMob] The plugin might not work in '" + version + "'!");
		}
		
		getCommand("btm").setExecutor(new BTMCommand());
		pm.registerEvents(new BTMListener(), this);
		
		task = new BTMTaskManager(this);
		task.start();
		
		dropFactory = new DropFactory();//Setup basic drop factory
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
        if(getConfig().get("drop_real_item") == null){
        	getConfig().set("drop_real_item", false);
        	saveConfig();
        }
	}
	
	private boolean isCompatible(String version) {
		int realVersion = MathUtil.countNrs(version);
		return realVersion >= MIN_VERSION && realVersion <= MAX_VERSION;
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
	 * Changes the drop factory allowing plugins to control what items a disguise drops
	 * @param factory The drop factory to be registered
	 * @param plugin The plugin that is registering the factory
	 */
	public void registerDropFactory(IDropFactory factory, Plugin plugin) {
		this.dropFactory = factory;
		getLogger().info("Plugin "+plugin.getName()+" has registered drop manager "+factory.getName());
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

	public IDropFactory getDropFactory() {
		return dropFactory;
	}
}
