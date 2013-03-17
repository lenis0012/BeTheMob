package com.lenis0012.bukkit.btm.api;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import com.lenis0012.bukkit.btm.nms.wrappers.Packet;
import com.lenis0012.bukkit.btm.util.MathUtil;

public class PacketGenerator {
	private Disguise dis;
	
	public PacketGenerator(Disguise dis) {
		this.dis = dis;
	}
	
	public Packet getMobSpawnPacket() {
		//Create packet
		Packet packet = new Packet("Packet24MobSpawn");
		
		Location loc = dis.getLocation();
		EntityType type = dis.getDisguiseType();
		
		int x = MathUtil.floor(loc.getX() * 32.0D);
		int y = MathUtil.floor(loc.getY() * 32.0D);
		int z = MathUtil.floor(loc.getZ() * 32.0D);
		byte yaw = getByteFromDegree(loc.getYaw());
		byte pitch = getByteFromDegree(loc.getPitch());
		
		if(type == EntityType.ENDER_DRAGON) { yaw = (byte) (yaw - 128); }
		if(type == EntityType.CHICKEN) { pitch = (byte) (pitch * -1); }
		
		//Fill the packet with data
		packet.write("a", dis.getEntityId());
		packet.write("b", type.getTypeId());
		packet.write("c", x);
		packet.write("d", y);
		packet.write("e", z);
		packet.write("i", yaw);
		packet.write("j", pitch);
		packet.write("k", yaw);
		packet.write("f", 0);
		packet.write("g", 0);
		packet.write("h", 0);
		
		//Set the Datawatcher
		packet.write("t", dis.getDataWatcher().getHandle());
		return packet;
	}
	
	/*
	 * From CraftBukkit - Packet24MobSpawn.java
	 * line 31 - 33
	 * 
	 * All credits go to CraftBukkit
	 */
	private byte getByteFromDegree(float degree) {
		return (byte) (int)(degree * 256.0F / 360.0F);
	}
}