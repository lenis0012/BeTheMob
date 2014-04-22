package com.lenis0012.bukkit.btm.type;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;

public class MobDisguise extends Disguise {
	private EntityType type;
	
	public MobDisguise(Player player, EntityType type) {
		super(player);
		this.type = type;
		
		//Default datawatcher values
		dataWatcher.set(0, (byte) 0);
		if(type.toString().contains("ZOMBIE")) {
			dataWatcher.set(12, (byte) 0);
		} else {
			dataWatcher.set(12, (int) 0);
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public CommonPacket onPacketSend(CommonPacket oldPacket) {
		CommonPacket packet = new CommonPacket(PacketType.OUT_ENTITY_SPAWN_LIVING);
		
		//Obtain some data from original packet
		int entityId = oldPacket.read(PacketType.OUT_ENTITY_SPAWN_NAMED.entityId);
		int x = oldPacket.read(PacketType.OUT_ENTITY_SPAWN_NAMED.x);
		int y = oldPacket.read(PacketType.OUT_ENTITY_SPAWN_NAMED.y);
		int z = oldPacket.read(PacketType.OUT_ENTITY_SPAWN_NAMED.z);
		byte yaw = oldPacket.read(PacketType.OUT_ENTITY_SPAWN_NAMED.yaw);
		byte pitch = oldPacket.read(PacketType.OUT_ENTITY_SPAWN_NAMED.pitch);
		
		//Insert data into new packet
		packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.entityId, entityId);
		packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.entityType, (int) type.getTypeId());
		packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.x, x);
		packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.y, y);
		packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.z, z);
		packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.yaw, yaw);
		packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.pitch, pitch);
		packet.write(PacketType.OUT_ENTITY_SPAWN_LIVING.headYaw, yaw);
		packet.setDatawatcher(dataWatcher);
		
		return packet;
	}
}