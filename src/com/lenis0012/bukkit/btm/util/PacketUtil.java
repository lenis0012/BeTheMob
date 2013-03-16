package com.lenis0012.bukkit.btm.util;

import java.lang.reflect.Field;

import net.minecraft.server.v1_5_R1.DataWatcher;
import net.minecraft.server.v1_5_R1.ItemStack;
import net.minecraft.server.v1_5_R1.Packet;
import net.minecraft.server.v1_5_R1.Packet18ArmAnimation;
import net.minecraft.server.v1_5_R1.Packet20NamedEntitySpawn;
import net.minecraft.server.v1_5_R1.Packet24MobSpawn;
import net.minecraft.server.v1_5_R1.Packet29DestroyEntity;
import net.minecraft.server.v1_5_R1.Packet32EntityLook;
import net.minecraft.server.v1_5_R1.Packet33RelEntityMoveLook;
import net.minecraft.server.v1_5_R1.Packet34EntityTeleport;
import net.minecraft.server.v1_5_R1.Packet35EntityHeadRotation;
import net.minecraft.server.v1_5_R1.Packet38EntityStatus;
import net.minecraft.server.v1_5_R1.Packet40EntityMetadata;
import net.minecraft.server.v1_5_R1.Packet55BlockBreakAnimation;
import net.minecraft.server.v1_5_R1.Packet5EntityEquipment;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import com.lenis0012.bukkit.btm.api.Movement;

/**
 * Generates all packets needed in this plugin
 * 
 * @author lenis0012
 */
public class PacketUtil {
	/**
	 * Get the packet to spawn animal npc's
	 * 
	 * @param EntityID		Entity id
	 * @param loc			Location
	 * @param type			Entity type
	 * 
	 * @return				Packet
	 */
	public static Packet24MobSpawn getMobSpawnPacket(int EntityID, Location loc, EntityType type, DataWatcher tmp) {
		//create epty packet
		Packet24MobSpawn packet = new Packet24MobSpawn();
		
		//generate data
		int x = MathUtil.floor(loc.getX() * 32.0D);
		int y = MathUtil.floor(loc.getY() * 32.0D);
		int z = MathUtil.floor(loc.getZ() * 32.0D);
		byte yaw = getByteFromDegree(loc.getYaw());
		byte pitch = getByteFromDegree(loc.getPitch());
		
		//Enderdragon & Chicken fix
		if(type == EntityType.ENDER_DRAGON) { yaw = (byte) (yaw - 128); }
		if(type == EntityType.CHICKEN) { pitch = (byte) (pitch * -1); }
		
		//insert data to packet
		packet.a = EntityID;
		packet.b = type.getTypeId();
		packet.c = x;
		packet.d = y;
		packet.e = z;
		packet.i = yaw;
		packet.j = pitch;
		packet.k = yaw;
		packet.f = 0;
		packet.g = 0;
		packet.h = 0;
		
		//insert DataWatcher to packet
		try {
			Field field = packet.getClass().getDeclaredField("s");
			field.setAccessible(true);
			field.set(packet, tmp);
			field.setAccessible(false);
		} catch(Exception e) {
			System.err.println("Could not acces DataWatcher field");
		}
		
		return packet;
	}
	
	/**
	 * Get the packet to spawn human npc's
	 * 
	 * @param EntityID		Entity id
	 * @param loc			Location
	 * @param name			Name of the npc
	 * @param item			Item in hand
	 * 
	 * @return				Packet
	 */
	public static Packet20NamedEntitySpawn getNamedEntitySpawnPacket(int EntityID, Location loc, String name, int item, DataWatcher tmp) {
		//create empty packet
		Packet20NamedEntitySpawn packet = new Packet20NamedEntitySpawn();
		
		if(name.length() <= 16) {
			name = ColorUtil.fixColors(name);
		} else
			return null;
		
		//generate data
		int x = MathUtil.floor(loc.getX() * 32.0D);
		int y = MathUtil.floor(loc.getY() * 32.0D);
		int z = MathUtil.floor(loc.getZ() * 32.0D);
		byte yaw = getByteFromDegree(loc.getYaw());
		byte pitch = getByteFromDegree(loc.getPitch());
		
		//insert data into packet
		packet.a = EntityID;
		packet.b = name;
		packet.c = x;
		packet.d = y;
		packet.e = z;
		packet.f = yaw;
		packet.g = pitch;
		packet.h = item;
		
		//insert DataWatcher to packet
		try {
			Field field = packet.getClass().getDeclaredField("i");
			field.setAccessible(true);
			field.set(packet, tmp);
			field.setAccessible(false);
		} catch(Exception e) {
			System.err.println("Could not acces DataWatcher field");
		}
		
		return packet;
	}
	
	/**
	 * Get the packet to rotate npc body's
	 * 
	 * @param EntityID		Entity id
	 * @param loc			Location
	 * 
	 * @return				Packet
	 */
	public static Packet32EntityLook getEntityLookPacket(int EntityID, Location loc, EntityType type) {
		//generate data
		byte yaw = getByteFromDegree(loc.getYaw());
		byte pitch = getByteFromDegree(loc.getPitch());
		if(type == EntityType.ENDER_DRAGON) { yaw = (byte) (yaw - 128); }
		if(type == EntityType.CHICKEN) { pitch = (byte) (pitch * -1); }
		//create packet with data
		Packet32EntityLook packet = new Packet32EntityLook(EntityID, yaw, pitch);
		
		return packet;
	}
	
	/**
	 * Get the packet to rotate npc heads
	 * 
	 * @param EntityID		Entity id
	 * @param loc			Location
	 * 
	 * @return				Packet
	 */
	public static Packet35EntityHeadRotation getEntityHeadRotationPacket(int EntityID, Location loc, EntityType type) {
		//generate data
		byte yaw = getByteFromDegree(loc.getYaw());
		if(type == EntityType.ENDER_DRAGON) { yaw = (byte) (yaw - 128); }
		//Create packet with data
		Packet35EntityHeadRotation packet = new Packet35EntityHeadRotation(EntityID, yaw);
		
		return packet;
	}
	
	/**
	 * Get the packet to destroy npc's
	 * 
	 * @param EntityID		Entity id
	 * 
	 * @return				Packet
	 */
	public static Packet29DestroyEntity getDestroyEntityPacket(int EntityID) {
		//Create packet with data
		Packet29DestroyEntity packet = new Packet29DestroyEntity(EntityID);
		
		return packet;
	}
	
	/**
	 * 2 - entity hurt
	 * 3 - entity killed
	 * 6 - wolf taming
	 * 7 - wolf tamed
	 * 8 - wolf shaking water
	 * 9 - Player allowed to eat
	 * 10 - sheep eating grass
	 * 
	 * @param EntityID 		Entity id
	 * @param status 		The status
	 * 
	 * @return				Packet
	 * 
	 */
	public static Packet38EntityStatus getEntityStatusPacket(int EntityID, byte status){
		Packet38EntityStatus packet = new Packet38EntityStatus(EntityID, status);
		return packet;
	}
	
	
	/**
	 * Move an entity to a new location
	 * 
	 * @param EntityID		Entity id
	 * @param from			Location from
	 * @param to			Location to
	 * @param type			Entiyt type
	 * 
	 * @return				Packet
	 */
	public static Packet getEntityMoveLookPacket(int EntityID, Movement movement, Location to, EntityType type) {
		//gemerate data
		byte x = (byte)movement.x;
		byte y = (byte)movement.y;
		byte z = (byte)movement.z;
		byte yaw = getByteFromDegree(to.getYaw());
		byte pitch = getByteFromDegree(to.getPitch());
		
		if(type == EntityType.ENDER_DRAGON) { yaw = (byte) (yaw - 128); }
		if(type == EntityType.CHICKEN) { pitch = (byte) (pitch * -1); }
		
		if(x > 128 || x < -128 || y > 128 || y < -128 || z > 128 || z < -128)
			return  getEntityTeleportPacket(EntityID, to);
		
		//create packet with data
		Packet33RelEntityMoveLook packet = new Packet33RelEntityMoveLook(EntityID, x, y, z, yaw, pitch);
		
		return packet;
	}
	
	/**
	 * Get packet to swing arm of an entity
	 * 
	 * @param EntityID		Entiyt id
	 * @param animation		Animation id
	 * 
	 * @return				Packet
	 */
	public static Packet18ArmAnimation getArmAntimationPacket(int EntityID, int animation) {
		//create empty packet
		Packet18ArmAnimation packet = new Packet18ArmAnimation();
		packet.a = EntityID;
		packet.b = animation;
		
		return packet;
	}
	
	/**
	 * Get packet to do block damage animation
	 * 
	 * @param EntityID		Entity id
	 * @param block			Block to damage
	 * 
	 * @return				Packet
	 */
	public static Packet55BlockBreakAnimation getBlockBreakAnimationPacket(int EntityID, Block block) {
		//generate data
		int damage = block.getState().getRawData();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		
		//create packet with data
		Packet55BlockBreakAnimation packet = new Packet55BlockBreakAnimation(EntityID, x, y, z, damage);
		
		return packet;
	}
	
	/**
	 * Get packet to teleport an entity
	 * 
	 * @param EntityID		Entity id
	 * @param loc			Location
	 * 
	 * @return				Packet
	 */
	public static Packet34EntityTeleport getEntityTeleportPacket(int EntityID, Location loc) {
		//generate data
		int x = MathUtil.floor(loc.getX() * 32.0D);
		int y = MathUtil.floor(loc.getY() * 32.0D);
		int z = MathUtil.floor(loc.getZ() * 32.0D);
		byte yaw = getByteFromDegree(loc.getYaw());
		byte pitch = getByteFromDegree(loc.getPitch());
		
		//create packet with data
		Packet34EntityTeleport packet = new Packet34EntityTeleport(EntityID, x, y, z, yaw, pitch);
		
		return packet;
	}
	
	/**
	 * Get packet to change metadata
	 * 
	 * @param EntityID		Entity id
	 * @param tmp			New metadata
	 * 
	 * @return				Packet
	 */
	public static Packet40EntityMetadata getEntityMetadataPacket(int EntityID, DataWatcher tmp) {
		return new Packet40EntityMetadata(EntityID, tmp, true);
	}
	
	/**
	 * Get packet to change entity equipment
	 * 
	 * @param EntityID		Entity id
	 * @param slot			Slot id
	 * @param item			Item
	 * 
	 * @return				Packet
	 */
	public static Packet5EntityEquipment getEntityEquipmentPacket(int EntityID, int slot, ItemStack item){
		Packet5EntityEquipment packet;
		packet = new Packet5EntityEquipment(EntityID, slot, item);
		return packet;
	}
	
	/*
	 * From CraftBukkit - Packet24MobSpawn.java
	 * line 31 - 33
	 * 
	 * All credits go to CraftBukkit
	 */
	private static byte getByteFromDegree(float degree) {
		return (byte) (int)(degree * 256.0F / 360.0F);
	}
}
