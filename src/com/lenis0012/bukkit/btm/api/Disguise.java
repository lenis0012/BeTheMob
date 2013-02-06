package com.lenis0012.bukkit.btm.api;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lenis0012.bukkit.btm.BTMTaskManager;
import com.lenis0012.bukkit.btm.util.NetworkUtil;
import com.lenis0012.bukkit.btm.util.PacketUtil;

public class Disguise {
	private boolean isPlayer = false;
	private Location loc;
	private String name;
	private int itemInHand;
	private EntityType type = null;
	private int EntityID;
	private boolean spawned = false;
	private Player player;
	private List<String> extras;
	private Movement movement;
	
	public Disguise(Player player, int EntityID, Location loc, String name, int itemInHand) {
		this.isPlayer = true;
		this.loc = loc;
		this.EntityID = EntityID;
		this.name = name;
		this.itemInHand = itemInHand;
		this.player = player;
		this.movement = new Movement(loc);
	}
	
	public Disguise(Player player, int EntityID, Location loc, EntityType type, List<String> extras) {
		this.EntityID = EntityID;
		this.loc = loc;
		this.type = type;
		this.player = player;
		this.extras = extras;
		this.movement = new Movement(loc);
	}
	
	/**
	 * Check if the type is a player
	 * 
	 * @return			Tupe is a player
	 */
	public boolean isPlayer() {
		return this.isPlayer;
	}
	
	/**
	 * Get the location of the disguise
	 * 
	 * @return			Location
	 */
	public Location getLocation() {
		return this.loc;
	}
	
	/**
	 * Get the disguised player
	 * 
	 * @return			Player
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * Get the custom entity id of the disguise
	 * 
	 * @return			EntityId
	 */
	public int getEntityId() {
		return this.EntityID;
	}
	
	/**
	 * Set the location of the disguise
	 * 
	 * @param loc		Location
	 */
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	/**
	 * Spawn the disguise for a player
	 * 
	 * @param player		Player to recive spawn packet
	 */
	public void spawn(Player player) {
		if(this.spawned) {
			if(isPlayer)
				NetworkUtil.sendPacket(PacketUtil.getNamedEntitySpawnPacket(EntityID, loc, name, itemInHand), player);
			else
				NetworkUtil.sendPacket(PacketUtil.getMobSpawnPacket(EntityID, loc, type, extras), player);
		}
	}
	
	/**
	 * Spawn the disguise for a world
	 * 
	 * @param world			World to recive packet
	 */
	public void spawn(World world) {
		if(isPlayer)
			NetworkUtil.sendGlobalPacket(PacketUtil.getNamedEntitySpawnPacket(EntityID, loc, name, itemInHand), world, player);
		else
			NetworkUtil.sendGlobalPacket(PacketUtil.getMobSpawnPacket(EntityID, loc, type, extras), world, player);
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(!player.getName().equals(this.player.getName())) {
				Location l = player.getLocation();
				if(l.distance(loc) < Bukkit.getViewDistance() * 10) {
					BTMTaskManager.addPlayerToRender(player, this);
				}
			}
		}
		
		this.spawned = true;
	}
	
	/**
	 * Despawn the disguise
	 */
	public void despawn() {
		World world = loc.getWorld();
		NetworkUtil.sendGlobalPacket(PacketUtil.getDestroyEntityPacket(EntityID), world, player);
		this.spawned = false;
	}
	
	/**
	 * Despawn the disguise in a custom world
	 * 
	 * @param world			World to despawn disguise
	 */
	public void despawn(World world) {
		NetworkUtil.sendGlobalPacket(PacketUtil.getDestroyEntityPacket(EntityID), world, player);
		this.spawned = false;
	}
	
	/**
	 * Unload the disguise for a player
	 * 
	 * @param player		Player to unload disguise for
	 */
	public void unLoadFor(Player player) {
		NetworkUtil.sendPacket(PacketUtil.getDestroyEntityPacket(EntityID), player);
	}
	
	/**
	 * Move the disguise to a new location
	 * 
	 * @param event			Event to get moving from
	 */
	public void move(PlayerMoveEvent event) {
		if(!spawned)
			return;
		
		Location from = event.getFrom();
		Location to = event.getTo();
		boolean moved = from.getBlockX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ();
		World world = to.getWorld();
		this.loc = to;
		
		if(moved) {
			movement.update(to);
			NetworkUtil.sendGlobalPacket(PacketUtil.getEntityMoveLookPacket(EntityID, movement, to, type), world, player);
		} else
			NetworkUtil.sendGlobalPacket(PacketUtil.getEntityLookPacket(EntityID, to), world, player);
		
		NetworkUtil.sendGlobalPacket(PacketUtil.getEntityHeadRotationPacket(EntityID, to), world, player);
	}
	
	/**
	 * Teleport the disguise
	 * 
	 * @param loc			Location to teleport to
	 */
	public void teleport(Location loc) {
		this.despawn();
		this.loc = loc;
		this.spawn(loc.getWorld());
		this.movement = new Movement(loc);
	}
	
	/**
	 * Swing the arm of the disguise
	 */
	public void swingArm() {
		NetworkUtil.sendGlobalPacket(PacketUtil.getArmAntimationPacket(EntityID, 1), loc.getWorld());
	}
	
	/**
	 * Let the disguised player damage a block
	 * 
	 * @param block			Block to damage
	 */
	public void damageBlock(Block block) {
		NetworkUtil.sendGlobalPacket(PacketUtil.getBlockBreakAnimationPacket(EntityID, block), loc.getWorld());
	}
	
	/**
	 * Notify the disguised player got damaged
	 */
	public void damage() {
		NetworkUtil.sendGlobalPacket(PacketUtil.getArmAntimationPacket(EntityID, 2), loc.getWorld());
	}
	
	/**
	 * Refresh the movement of the disguised player
	 */
	public void refreshMovement() {
		this.movement = new Movement(loc);
	}
}
