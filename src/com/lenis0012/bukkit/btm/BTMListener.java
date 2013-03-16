package com.lenis0012.bukkit.btm;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.lenis0012.bukkit.btm.api.Disguise;
import com.lenis0012.bukkit.btm.nms.PacketConnection;

public class BTMListener implements Listener {
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerMove(PlayerMoveEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		String name = player.getName();
		BeTheMob plugin = BeTheMob.instance;
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.move(event);

		}
		
	}
	
	@EventHandler (priority=EventPriority.MONITOR)
	public void onCombust(EntityCombustEvent event) {
		if(event.isCancelled())
			return;
		
		if(event.getEntityType() == EntityType.PLAYER){
			Player player = (Player) event.getEntity();
			String name = player.getName();
			BeTheMob plugin = BeTheMob.instance;
			if(plugin.disguises.containsKey(name)) {
				final Disguise dis = plugin.disguises.get(name);
				dis.ignite();
				Bukkit.getScheduler().runTaskLater(BeTheMob.instance, new Runnable() {

					@Override
					public void run() {
						dis.extinguish();
						
					}}, event.getDuration()*20);

			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerSwapItem(PlayerItemHeldEvent event){
		Player player = event.getPlayer();
		String name = player.getName();
		BeTheMob plugin = BeTheMob.instance;
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			if(dis.isPlayer()) { //Only switch armour for player disguise types
				dis.changeItem(player, event.getNewSlot());
				dis.changeArmor(player, 1);
				dis.changeArmor(player, 2);
				dis.changeArmor(player, 3);
				dis.changeArmor(player, 4);
			}
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
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
		
		if(!plugin.protLib) {
			//Change the players connection
			PacketConnection.hook(player);
		}
		
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
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
	
	@EventHandler (priority = EventPriority.MONITOR)
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
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerTeleport(PlayerTeleportEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		Location loc = event.getTo();
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.teleport(loc);
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerAnimation(PlayerAnimationEvent event) {
		if(event.isCancelled())
			return;
		
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
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onBlockDamage(BlockDamageEvent event) {
		if(event.isCancelled())
			return;
		
		Block block = event.getBlock();
		Player player = event.getPlayer();
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.damageBlock(block);
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerDeath(PlayerDeathEvent event) {
		Player player = event.getEntity();
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.kill();
		}
		
	}
	
	
	@EventHandler (priority = EventPriority.MONITOR)
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
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onEntityDamage(EntityDamageEvent event) {
		if(event.isCancelled())
			return;
		
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
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		if(event.isCancelled())
			return;
		
		Player player = event.getPlayer();
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		
		if(plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			
			if(event.isSneaking())
				dis.crouch();
			else
				dis.uncrouch();
		}
	}
}
