package src.com.lenis0012.bukkit.btm.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.World;
import org.bukkit.entity.Player;

import com.lenis0012.bukkit.btm.nms.wrappers.Packet;

public class NetworkUtil {
	private static Method toPlayerHandle = DynamicUtil.getMethod(DynamicUtil.getCBClass("entity.CraftPlayer"), "getHandle");
	private static Field playerConnection = DynamicUtil.getField(DynamicUtil.getNMSClass("EntityPlayer"), "playerConnection");
	private static Method sendPacket = DynamicUtil.getMethod(DynamicUtil.getNMSClass("PlayerConnection"), "sendPacket", DynamicUtil.getNMSClass("Packet"));
	
	/**
	 * Send a packet o a player
	 * 
	 * @param packet		Packet to send
	 * @param player		Player to recive packet
	 */
	public static void sendPacket(Packet packet, Player player) {
		if(packet == null || player == null)
			return;
		
		Object ep = DynamicUtil.invoke(toPlayerHandle, player);
		Object pcon = DynamicUtil.getValue(ep, playerConnection);
		DynamicUtil.invoke(sendPacket, pcon, packet.getHandle());
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
