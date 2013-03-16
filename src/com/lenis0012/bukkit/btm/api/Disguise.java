package com.lenis0012.bukkit.btm.api;

import java.util.List;

import net.minecraft.server.v1_5_R1.DataWatcher;
import net.minecraft.server.v1_5_R1.ItemStack;
import net.minecraft.server.v1_5_R1.Packet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_5_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import com.lenis0012.bukkit.btm.BTMTaskManager;
import com.lenis0012.bukkit.btm.util.MetaDataUtil;
import com.lenis0012.bukkit.btm.util.NetworkUtil;
import com.lenis0012.bukkit.btm.util.PacketUtil;

/**
 * Data wrapper of a player disguise
 * 
 * @author lenis0012
 */
public class Disguise {
	private boolean isPlayer = false;
	private Location loc;
	private String name;
	private int itemInHand;
	private EntityType type;
	private int EntityID;
	private boolean spawned = false;
	private Player player;
	private List<String> extras;
	private Movement movement;
	private DataWatcher dw;
	
	public Disguise(Player player, int EntityID, Location loc, String name, int itemInHand) {
		this.isPlayer = true;
		this.loc = loc;
		this.type = EntityType.PLAYER;
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
	 * @return Tupe is a player
	 */
	public boolean isPlayer() {
		return this.isPlayer;
	}
	
	/**
	 * Get the location of the disguise
	 * 
	 * @return Location
	 */
	public Location getLocation() {
		return this.loc;
	}
	
	/**
	 * Get the disguised player
	 * 
	 * @return Player
	 */
	public Player getPlayer() {
		return this.player;
	}
	
	/**
	 * Get the custom entity id of the disguise
	 * 
	 * @return EntityId
	 */
	public int getEntityId() {
		return this.EntityID;
	}
	
	/**
	 * Set the location of the disguise
	 * 
	 * @param loc Location
	 */
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	
	/**
	 * Spawn the disguise for a player
	 * 
	 * @param player Player to recive spawn packet
	 */
	public void spawn(Player player) {
		if(this.spawned) {
			if(isPlayer) {
				NetworkUtil.sendPacket(PacketUtil.getNamedEntitySpawnPacket(EntityID, loc, name, itemInHand, dw), player);
			} else {
				NetworkUtil.sendPacket(PacketUtil.getMobSpawnPacket(EntityID, loc, type, dw), player);
			}
		}
	}
	
	/**
	 * Spawn the disguise for a world
	 * 
	 * @param world World to recive packet
	 */
	public void spawn(World world) {
		if(isPlayer) {
			dw = new DataWatcher();
			dw.a(0, Byte.valueOf((byte) 0));
			dw.a(12, Integer.valueOf((int) 0));
			NetworkUtil.sendGlobalPacket(PacketUtil.getNamedEntitySpawnPacket(EntityID, loc, name, itemInHand, dw), world, player);
		} else {
			dw = MetaDataUtil.getDataWatcher(type, extras);
			NetworkUtil.sendGlobalPacket(PacketUtil.getMobSpawnPacket(EntityID, loc, type, dw), world, player);
		}
		
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			if(!player.getName().equals(this.player.getName())) {
				Location l = player.getLocation();
				if(l.getWorld().getName().equals(world.getName())) {
					if(l.distance(loc) < Bukkit.getViewDistance() * 10) {
						BTMTaskManager.addPlayerToRender(player, this);
					}
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
	 * Kill the entity with the normal fall over animation
	 * (Warning kills and despawns disguise)
	 */
	public void kill(){
		NetworkUtil.sendGlobalPacket(PacketUtil.getEntityStatusPacket(EntityID, (byte) 3), player.getWorld());
	}
	
	/**
	 * 'Ignites' the disguise
	 */
	public void ignite(){
		dw.a(0, Byte.valueOf((byte) 1));
		updateMetaData();
	}
	
	/**
	 * Stops fire on the entity
	 */
	public void extinguish(){
		dw.a(0, Byte.valueOf((byte) 0));
		updateMetaData();
	}
	
	
	/**
	 * Despawn the disguise in a custom world
	 * 
	 * @param world	World to despawn disguise
	 */
	public void despawn(World world) {
		NetworkUtil.sendGlobalPacket(PacketUtil.getDestroyEntityPacket(EntityID), world, player);
		this.spawned = false;
	}
	
	/**
	 * Unload the disguise for a player
	 * 
	 * @param player Player to unload disguise for
	 */
	public void unLoadFor(Player player) {
		NetworkUtil.sendPacket(PacketUtil.getDestroyEntityPacket(EntityID), player);
	}
	
	/**
	 * @param player Player to change disguises item in hand for
	 * slot 0 is the item in hand
	 */
	public void changeItem(Player player, int new_slot){
		org.bukkit.inventory.ItemStack bukkitstack = null;
		if(player.getInventory().getItem(new_slot) == null){
			bukkitstack = new org.bukkit.inventory.ItemStack(Material.AIR, 1);
		}else{
			bukkitstack = player.getInventory().getItem(new_slot);
		}
		ItemStack item = CraftItemStack.asNMSCopy(bukkitstack);
		NetworkUtil.sendGlobalPacket(PacketUtil.getEntityEquipmentPacket(EntityID, 0, item), player.getWorld());
	}
	
	/**
	 * @param player Player to set disguises armor
	 * @param slot Which armour slot
	 * 1 boots
	 * 2 leggings
	 * 3 chestplate
	 * 4 helmet
	 */
	public void changeArmor(Player player, int slot){
		org.bukkit.inventory.ItemStack bukkitstack = null;
		switch(slot){
		case 1:
			bukkitstack = player.getInventory().getBoots();
			break;
		case 2:
			bukkitstack = player.getInventory().getLeggings();
			break;
		case 3:
			bukkitstack = player.getInventory().getChestplate();
			break;
		case 4:
			bukkitstack = player.getInventory().getHelmet();
			break;
		default:
			return;
		}
		if(bukkitstack == null){
			bukkitstack = new org.bukkit.inventory.ItemStack(Material.AIR);
		}
		ItemStack item = CraftItemStack.asNMSCopy(bukkitstack);
		NetworkUtil.sendGlobalPacket(PacketUtil.getEntityEquipmentPacket(EntityID, slot, item), player.getWorld());
	}
	
	/**
	 * Move the disguise to a new location
	 * 
	 * @param event Event to get moving from
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
			NetworkUtil.sendGlobalPacket(PacketUtil.getEntityLookPacket(EntityID, to, getDisguiseType()), world, player);
		NetworkUtil.sendGlobalPacket(PacketUtil.getEntityHeadRotationPacket(EntityID, to, getDisguiseType()), world, player);
	}
	
	/**
	 * Teleport the disguise
	 * 
	 * @param loc Location to teleport to
	 */
	public void teleport(Location loc) {
		this.loc = loc;
		NetworkUtil.sendGlobalPacket(PacketUtil.getEntityTeleportPacket(EntityID, loc, type), loc.getWorld(), this.player);
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
	 * @param block	Block to damage
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
	 * Make the entity crouch
	 */
	public void crouch() {
		if(this.isPlayer) {
			byte b = dw.getByte(0);
			dw.watch(0, Byte.valueOf((byte)(b | 1 << 1)));
			this.updateMetaData();
		}
	}
	
	/**
	 * Make the entity uncrouch
	 */
	public void uncrouch() {
		if(this.isPlayer) {
			byte b = dw.getByte(0);
			dw.watch(0, Byte.valueOf((byte)(b & (1 << 1 ^ 0xFFFFFFFF))));
			this.updateMetaData();
		}
	}
	
	public void updateMetaData() {
		NetworkUtil.sendGlobalPacket(PacketUtil.getEntityMetadataPacket(EntityID, dw), loc.getWorld(), player);
	}
	
	/**
	 * Refresh the movement of the disguised player
	 */
	public void refreshMovement() {
		this.movement = new Movement(loc);
	}
	
	/**
	 * Get the type of the disguise
	 */
    public EntityType getDisguiseType() {
    	return type;
    }
    
    public Packet getSpawnPacket() {
    	if(isPlayer) {
			dw = new DataWatcher();
			dw.a(0, Byte.valueOf((byte) 0));
			dw.a(12, Integer.valueOf((int) 0));
			return PacketUtil.getNamedEntitySpawnPacket(EntityID, loc, name, itemInHand, dw);
		} else {
			dw = MetaDataUtil.getDataWatcher(type, extras);
			return PacketUtil.getMobSpawnPacket(EntityID, loc, type, dw);
		}
    }
}
