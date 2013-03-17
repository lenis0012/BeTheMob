package com.lenis0012.bukkit.btm.util;

import net.minecraft.server.v1_5_R1.EntityPlayer;
import net.minecraft.server.v1_5_R1.Packet;

import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class NetworkUtil {
	
	/**
	 * Send a packet o a player
	 * 
	 * @param packet		Packet to send
	 * @param player		Player to recive packet
	 */
	public static void sendPacket(Packet packet, Player player) {
		if(packet == null || player == null)
			return;
		
		CraftPlayer cp = (CraftPlayer)player;
		EntityPlayer ep = cp.getHandle();
		ep.playerConnection.sendPacket(packet);
	}
	
	/**
	 * Send a global packet
	 * 
	 * @param packet		Packet to send
	 * @param world			World to recive packet
	 */
	public static void sendGlobalPacket(Packet packet, World world) {
		for(Player player : world.getPlayers()) {
			sendPacket(packet, player);
		}
	}
	
	/**
	 * Send a global packet with 1 player to be ignored
	 * 
	 * @param packet		Packet to send
	 * @param world			World to recive
	 * @param ignore		Player to be ignored
	 */
	public static void sendGlobalPacket(Packet packet, World world, Player ignore) {
		for(Player player : world.getPlayers()) {
			if(!player.getName().equals(ignore.getName()))
				sendPacket(packet, player);
		}
	}
}
