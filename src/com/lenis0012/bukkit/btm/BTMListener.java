package com.lenis0012.bukkit.btm;

import net.minecraft.server.v1_4_R1.EntityPlayer;
import net.minecraft.server.v1_4_R1.MinecraftServer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_4_R1.CraftServer;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import com.lenis0012.bukkit.btm.api.Disguise;
import com.lenis0012.bukkit.btm.nms.PacketConnection;

public class BTMListener implements Listener {
	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		String name = player.getName();
		BeTheMob plugin = BeTheMob.instance;
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.move(event);
		}
	}
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		BeTheMob plugin = BeTheMob.instance;
		Player player = event.getPlayer();
		String name = player.getName();
		
		for(Player online : Bukkit.getServer().getOnlinePlayers()) {
			if(plugin.isHidden(online))
				player.hidePlayer(online);
		}
		
		if(plugin.disguises.containsKey(name)) {
			plugin.setHidden(player, true);
			Disguise dis = plugin.disguises.get(name);
			
			dis.spawn(player.getWorld());
		}
		
		for(String user : plugin.disguises.keySet()) {
			if(!user.equals(name)) {
				Disguise dis = plugin.disguises.get(user);
				dis.spawn(player);
			}
		}
		
		//Change the players connection
		CraftServer cs = (CraftServer)Bukkit.getServer();
		MinecraftServer server = cs.getServer();
		CraftPlayer cp = (CraftPlayer)player;
		EntityPlayer ep = cp.getHandle();
		
		ep.playerConnection = new PacketConnection(server, ep.playerConnection.networkManager, ep);
		
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		BeTheMob plugin = BeTheMob.instance;
		Player player = event.getPlayer();
		String name = player.getName();
		
		if(plugin.isHidden(player))
			plugin.setHidden(player, false);
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			
			dis.despawn();
		}
		
		for(String user : plugin.disguises.keySet()) {
			if(!user.equals(name)) {
				Disguise dis = plugin.disguises.get(user);
				dis.unLoadFor(player);
			}
		}
		
		BTMTaskManager.notifyPlayerLeft(player);
	}
	
	@EventHandler
	public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		Location loc = player.getLocation();
		
		BTMTaskManager.notifyWorldChanged(player);
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.despawn(event.getFrom());
			dis.setLocation(loc);
			dis.spawn(loc.getWorld());
			dis.refreshMovement();
		}
	}
	
	@EventHandler
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		Player player = event.getPlayer();
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		Location loc = event.getTo();
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.teleport(loc);
		}
	}
	
	@EventHandler
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		PlayerAnimationType type = event.getAnimationType();
		Player player = event.getPlayer();
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		
		if(plugin.disguises.containsKey(name)) {
			if(type == PlayerAnimationType.ARM_SWING) {
				Disguise dis = plugin.disguises.get(name);
				dis.swingArm();
			}
		}
	}
	
	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		Block block = event.getBlock();
		Player player = event.getPlayer();
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.damageBlock(block);
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.despawn();
		}
		
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		Location loc = event.getRespawnLocation();
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.setLocation(loc);
			dis.spawn(loc.getWorld());
			dis.refreshMovement();
		}
	}
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event) {
		Entity entity = event.getEntity();
		BeTheMob plugin = BeTheMob.instance;
		
		if(entity instanceof Player) {
			Player player = (Player)entity;
			String name = player.getName();
			
			if(plugin.disguises.containsKey(name)) {
				Disguise dis = plugin.disguises.get(name);
				dis.damage();
			}
		}
	}
}
