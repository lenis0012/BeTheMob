package com.lenis0012.bukkit.btm.type;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.bergerkiller.bukkit.common.protocol.CommonPacket;
import com.lenis0012.bukkit.btm.BeTheMob;
import com.lenis0012.bukkit.btm.DisguiseManager;

public abstract class Disguise {
	protected Player player;
	
	public Disguise(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return this.getPlayer();
	}
	
	public abstract CommonPacket onPacketSend(CommonPacket oldPacket);
	
	public void disguise() {
		//Make sure that entry is added to disguise manager.
		DisguiseManager disguiseManager = BeTheMob.getDisguiseManager();
		disguiseManager.setDisguised(player, this);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			//Use vanish method to update spawn packet
			if(!player.equals(p) && player.canSee(p)) {
				p.hidePlayer(player);
				p.showPlayer(player);
			}
		}
	}
	
	public void undisguise() {
		//Make sure that entry is removed from disguise manager
		DisguiseManager disguiseManager = BeTheMob.getDisguiseManager();
		disguiseManager.removeDisguise(player);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			//Use vanish method to update spawn packet
			if(!player.equals(p) && player.canSee(p)) {
				p.hidePlayer(player);
				p.showPlayer(player);
			}
		}
	}
}