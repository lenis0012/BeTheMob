package src.com.lenis0012.bukkit.btm;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import src.com.dylanisawesome1.bukkit.btm.Herds.Herd;
import src.com.dylanisawesome1.bukkit.btm.Herds.HerdEntity;
import src.com.dylanisawesome1.bukkit.btm.Herds.HerdUpdateManager;
import src.com.lenis0012.bukkit.btm.api.Api;
import src.com.lenis0012.bukkit.btm.api.Disguise;
import src.com.lenis0012.bukkit.btm.fun.IDropFactory;
import src.com.lenis0012.bukkit.btm.nms.PlayerConnectionCallback;
import src.com.lenis0012.bukkit.btm.nms.ProtocolLibManager;
import src.com.lenis0012.bukkit.btm.util.DynamicUtil;
import src.com.lenis0012.bukkit.btm.util.FileUtil;
import src.com.lenis0012.bukkit.btm.util.MathUtil;


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
	private FileConfiguration typeConfig;
	public ArrayList<Herd> herds = new ArrayList<Herd>();
	private static final int MAX_VERSION = 152;
	private static final int MIN_VERSION = 151;
	HashMap<String, HerdEntity> selectedentities = new HashMap<String,HerdEntity>();
	@Override
	public void onEnable() {
		instance = this;
		PluginManager pm = this.getServer().getPluginManager();
		String version = DynamicUtil.MC_VERSION;
		Bukkit.getScheduler().runTaskTimer(this, HerdUpdateManager.runHerdUpdate(herds, 20), 0, 10);
		if(!this.isCompatible(version)) {
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
		
		File configFile = new File(this.getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			try {
				FileUtil.copy(this.getResource("config.yml"), configFile);
			} catch (Exception e) {
				this.getLogger().log(Level.SEVERE, "Failed to create config.yml", e);
			}
		}
		
		File typesFile = new File(this.getDataFolder(), "types.yml");
		if(!typesFile.exists()) {
			try {
				FileUtil.copy(this.getResource("types.yml"), typesFile);
			} catch (Exception e) {
				this.getLogger().log(Level.SEVERE, "Failed to create config.yml", e);
			}
		}
		
		this.typeConfig = YamlConfiguration.loadConfiguration(typesFile);
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
	 * @return API
	 */
	public static Api getApi() {
		return api;
	}
	
	/**
	 * Hide a player from the orthers
	 * 
	 * @param player Player to hide
	 * @param value Unhide or hide the player
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
	 * @param player Player to check
	 * 
	 * @return Player is hidden?
	 */
	public boolean isHidden(Player player) {
		String name = player.getName();
		
		return hidden.contains(name);
	}
	
	/**
	 * Get the plugin ClassLoader
	 * 
	 * @return Plugin ClassLoader
	 */
	public ClassLoader getLoader() {
		return this.getClassLoader();
	}

	/**
	 * Get the current drop facotry of the plugin
	 * 
	 * @return Drop factory
	 */
	public IDropFactory getDropFactory() {
		return api.getDropFactory();
	}
	
	/**
	 * Get all whitelisted mob types
	 * 
	 * @return Whitelisted mob types
	 */
	public List<String> getMobList() {
		return typeConfig.getStringList("mobs");
	}
	
	/**
	 * Get all whitelisted vehicles
	 * 
	 * @return Whitelisted vehicles
	 */
	public List<String> getVehicleList() {
		return typeConfig.getStringList("vehicles");
	}
}
