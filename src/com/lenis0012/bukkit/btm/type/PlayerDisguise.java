package com.lenis0012.bukkit.btm.type;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.bergerkiller.bukkit.common.protocol.PacketType;
import com.bergerkiller.bukkit.common.utils.CommonUtil;

public class PlayerDisguise extends Disguise {
	private String name;
	
	public PlayerDisguise(Player player, String name) {
		super(player);
		this.name = name;
	}
	
	@Override
	public CommonPacket onPacketSend(CommonPacket oldPacket) {
		//Just modify the gameProfile
		oldPacket.write(PacketType.OUT_ENTITY_SPAWN_NAMED.profile, CommonUtil.getGameProfile(name));
		
		return oldPacket;
	}
}