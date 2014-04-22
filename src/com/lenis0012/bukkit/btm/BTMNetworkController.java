package com.lenis0012.bukkit.btm;

import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.controller.EntityNetworkController;
import com.bergerkiller.bukkit.common.entity.type.CommonPlayer;
import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.lenis0012.bukkit.btm.type.Disguise;

public class BTMNetworkController extends EntityNetworkController<CommonPlayer> {
	private DisguiseManager disguiseManager;
	
	public BTMNetworkController() {
		this.disguiseManager = BeTheMob.getDisguiseManager();
	}
	
	@Override
	public CommonPacket getSpawnPacket() {
		Player player = entity.getEntity();
		CommonPacket packet = super.getSpawnPacket();
		Disguise disguise = disguiseManager.getDisguise(player);
		if(disguise != null) {
			return disguise.onPacketSend(packet);
		} else {
			return packet;
		}
	}
}