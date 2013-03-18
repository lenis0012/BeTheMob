package com.lenis0012.bukkit.btm.api;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_5_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.lenis0012.bukkit.btm.nms.wrappers.Packet;
import com.lenis0012.bukkit.btm.util.MathUtil;

public class PacketGenerator implements IPacketGenerator{
	private Disguise dis;
	
	public PacketGenerator(Disguise dis) {
		this.dis = dis;
	}
	
	/**
	 * Slot List:
	 * 0 - item in hand
	 * 1 - boots
	 * 2 - leggings
	 * 3 - chestplate
	 * 4 - helmet
	 * @param slot The slot to update
	 * @return
	 */
	public Packet getEntityEquipmentPacket(int slot, ItemStack item){
		Packet packet = new Packet("Packet5EntityEquipment");
		packet.write("a", dis.getEntityId());
		packet.write("b", slot);
		packet.write("c", CraftItemStack.asNMSCopy(item));
		return packet;
	}
	
	public Packet getEntityHeadRotatePacket(){
		Packet packet = new Packet("Packet35EntityHeadRotation");
		packet.write("a", dis.getEntityId());
		packet.write("b", dis.getPlayer().getLocation().getYaw());
		return packet;
	}
	
	public Packet getDestroyEntityPacket(int EntityId){
		Packet packet = new Packet("Packet29DestroyEntity");
		packet.write("a", new Integer[] {EntityId});//The field 'a' is actually a array!
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
	 * @param status 		The status
	 * 
	 * @return				Packet
	 * 
	 */
	public Packet getEntityStatusPacket(byte status){
		Packet packet = new Packet("Packet38EntityStatus");
		packet.write("a", dis.getEntityId());
		packet.write("b", status);
		return packet;
	}
	
	public Packet getEntityLookPacket(){
		Packet packet = new Packet("Packet32EntityLook");
		
		byte yaw = getByteFromDegree(dis.getPlayer().getLocation().getYaw());
		byte pitch = getByteFromDegree(dis.getPlayer().getLocation().getPitch());
		if(dis.getDisguiseType() == EntityType.ENDER_DRAGON) { yaw = (byte) (yaw - 128); }
		if(dis.getDisguiseType() == EntityType.CHICKEN) { pitch = (byte) (pitch * -1); }
		
		packet.write("a", dis.getEntityId());
		packet.write("e", yaw);
		packet.write("f", pitch);
		return packet;
	}
	
	public Packet getEntityMoveLookPacket(Location to){
		Packet packet = new Packet("Packet33RelEntityMoveLook");
		byte x = (byte)to.getX();
		byte y = (byte)to.getY();
		byte z = (byte)to.getZ();
		byte yaw = getByteFromDegree(to.getYaw());
		byte pitch = getByteFromDegree(to.getPitch());
		
		if(dis.getDisguiseType() == EntityType.ENDER_DRAGON) { yaw = (byte) (yaw - 128); }
		if(dis.getDisguiseType() == EntityType.CHICKEN) { pitch = (byte) (pitch * -1); }
		
		if(x > 128 || x < -128 || y > 128 || y < -128 || z > 128 || z < -128)
			return getEntityTeleportPacket(to);
		return packet;
	}
	
	public Packet getEntityTeleportPacket(Location to){
		Packet packet = new Packet("Packet34EntityTeleport");
		int x = MathUtil.floor(to.getX() * 32.0D);
		int y = MathUtil.floor(to.getY() * 32.0D);
		int z = MathUtil.floor(to.getZ() * 32.0D);
		byte yaw = getByteFromDegree(to.getYaw());
		byte pitch = getByteFromDegree(to.getPitch());
		
		if(dis.getDisguiseType() == EntityType.ENDER_DRAGON) { yaw = (byte) (yaw - 128); }
		if(dis.getDisguiseType() == EntityType.CHICKEN) { pitch = (byte) (pitch * -1); }
		packet.write("a", dis.getEntityId());
		packet.write("b", x);
		packet.write("c", y);
		packet.write("d", z);
		packet.write("e", yaw);
		packet.write("f", pitch);
		return packet;
	}
	
	public Packet getNamedEntitySpawnPacket(){
		Packet packet = new Packet("Packet20NamedEntitySpawn");
		Location loc = dis.getLocation();
		int x = MathUtil.floor(loc.getX() * 32.0D);
		int y = MathUtil.floor(loc.getY() * 32.0D);
		int z = MathUtil.floor(loc.getZ() * 32.0D);
		byte yaw = getByteFromDegree(loc.getYaw());
		byte pitch = getByteFromDegree(loc.getPitch());
		packet.write("a", dis.getEntityId());
		packet.write("b", dis.getCustomName());
		packet.write("c", x);
		packet.write("d", y);
		packet.write("e", z);
		packet.write("f", yaw);
		packet.write("g", pitch);
		packet.write("h", dis.getPlayer().getItemInHand().getTypeId());
		packet.write("i", dis.getDataWatcher().getHandle());
		
		return packet;
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

	@Override
	public Packet getArmAnimationPacket(int animation) {
		Packet packet = new Packet("Packet18ArmAnimation");
		packet.write("a", dis.getEntityId());
		packet.write("b", animation);
		return packet;
	}

	@Override
	public Packet getBlockBreakAnimationPacket(Block block) {
		Packet packet = new Packet("Packet55BlockBreakAnimation");
		int damage = block.getState().getRawData();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		packet.write("a", dis.getEntityId());
		packet.write("b", x);
		packet.write("c", y);
		packet.write("d", z);
		packet.write("e", damage);
		return packet;
	}

	@Override
	public Packet getEntityMetadataPacket() {
		// TODO Auto-generated method stub
		return null;
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