package com.lenis0012.bukkit.btm.api;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.lenis0012.bukkit.btm.nms.wrappers.Packet;

public interface IPacketGenerator {

	public Packet getMobSpawnPacket();
	public Packet getNamedEntitySpawnPacket();
	public Packet getEntityLookPacket();
	public Packet getEntityHeadRotatePacket();
	public Packet getDestroyEntityPacket(int EntityId);
	public Packet getEntityStatusPacket(byte status);
	public Packet getEntityMoveLookPacket(Location to);
	public Packet getArmAnimationPacket(int animation);
	public Packet getBlockBreakAnimationPacket(Block block);
	public Packet getEntityTeleportPacket(Location to);
	public Packet getEntityMetadataPacket();
	public Packet getEntityEquipmentPacket(int slot, ItemStack item);
	
}
