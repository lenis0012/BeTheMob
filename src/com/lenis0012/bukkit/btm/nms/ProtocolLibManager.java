package com.lenis0012.bukkit.btm.nms;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.lenis0012.bukkit.btm.BeTheMob;
import com.lenis0012.bukkit.btm.api.Disguise;
import com.lenis0012.bukkit.btm.events.PlayerInteractDisguisedEvent;
import com.lenis0012.bukkit.btm.nms.wrappers.EntityPlayer;
import com.lenis0012.bukkit.btm.nms.wrappers.Packet;
import com.lenis0012.bukkit.btm.util.NetworkUtil;

public class ProtocolLibManager {
	private BeTheMob btm;
	
	public ProtocolLibManager(BeTheMob plugin) {
		this.btm = plugin;
	}
	
	public void start() {
		ProtocolManager pm = ProtocolLibrary.getProtocolManager();
		pm.addPacketListener(new PacketAdapter(btm, ConnectionSide.CLIENT_SIDE,
				ListenerPriority.NORMAL, Packets.Client.USE_ENTITY, Packets.Client.BLOCK_DIG) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				Player player = event.getPlayer();
				String name = player.getName();
				
				if(event.getPacketID() == Packets.Client.USE_ENTITY) {
					int eid = packet.getIntegers().read(1);
					int action = packet.getIntegers().read(2);
					
					for(String user : btm.disguises.keySet()) {
						if(!user.equals(name)) {
							Disguise dis = btm.disguises.get(user);
							if(dis.getEntityId() == eid) {
								EntityPlayer epAtt = new EntityPlayer(player);
								EntityPlayer epDef = new EntityPlayer(dis.getPlayer());
								
								boolean flag = epAtt.longDistance(epDef);
					            double distance = 36.0D;
					            
					            if(!flag)
					            	distance = 9.0D;
					            
					            if(epAtt.distance(epDef) < distance) {
					            	if(action == 0) {
					            		PlayerInteractDisguisedEvent ev = new PlayerInteractDisguisedEvent(player, dis);
						            	Bukkit.getServer().getPluginManager().callEvent(ev);
						            	if(!ev.isCancelled())
						            		epAtt.interact(epDef);
					            	} else if(action == 1) {
					            		epAtt.attack(epDef);
					            		Player t_player = epDef.getBukkitEntity();
										t_player.getWorld().playSound(t_player.getLocation(), Sound.HURT_FLESH, 63F, 1F);
					            	}
					            	
					            	event.setCancelled(true);
					            }
							}
						}
					}
				} else if(event.getPacketID() == Packets.Client.BLOCK_DIG) {
					EntityPlayer ep = new EntityPlayer(player);
					World world = player.getWorld();
					int action = packet.getIntegers().read(1);
					if(action == 1 || action == 2) {
						int x = packet.getIntegers().read(2);
						int y = packet.getIntegers().read(3);
						int z = packet.getIntegers().read(4);
						int type = world.getBlockTypeIdAt(x, y, z);
						int data = world.getBlockAt(x, y, z).getData();
						
						if(action == 1)
							ep.stopDigging(x, y, z);
						else
							ep.finishDigging(x, y, z);
						
						Packet newPacket = new Packet("Packet53BlockChange");
						newPacket.write("a", x);
						newPacket.write("b", y);
						newPacket.write("c", z);
						
						Location loc = player.getLocation();
						double d0 = loc.getX() - (x + 0.5);
						double d1 = loc.getY() - (y + 0.5) + 1.5;
						double d2 = loc.getZ() - (z + 0.5);
						double d3 = d0 * d0 + d1 * d1 + d2 * d2;
						
						if(d3 > 36D)
							return;
						
						if(y >= 256)
							return;
						
						newPacket.write("material", type);
						newPacket.write("data", data);
						NetworkUtil.sendGlobalPacket(newPacket, world);
						
						event.setCancelled(true);
					}
				}
			}
		});
	}
}
