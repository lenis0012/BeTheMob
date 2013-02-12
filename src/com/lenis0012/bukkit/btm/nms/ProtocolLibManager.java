package com.lenis0012.bukkit.btm.nms;

import net.minecraft.server.v1_4_R1.EntityPlayer;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
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

public class ProtocolLibManager {
	private BeTheMob btm;
	
	public ProtocolLibManager(BeTheMob plugin) {
		this.btm = plugin;
	}
	
	public void start() {
		ProtocolManager pm = ProtocolLibrary.getProtocolManager();
		pm.addPacketListener(new PacketAdapter(btm, ConnectionSide.CLIENT_SIDE,
				ListenerPriority.NORMAL, Packets.Client.USE_ENTITY) {
			@Override
			public void onPacketReceiving(PacketEvent event) {
				if(event.getPacketID() == Packets.Client.USE_ENTITY) {
					PacketContainer packet = event.getPacket();
					Player player = event.getPlayer();
					String name = player.getName();
					int eid = packet.getIntegers().read(1);
					int action = packet.getIntegers().read(2);
					
					for(String user : btm.disguises.keySet()) {
						if(!user.equals(name)) {
							Disguise dis = btm.disguises.get(user);
							if(dis.getEntityId() == eid) {
								CraftPlayer cpAtt = (CraftPlayer)dis.getPlayer();
								CraftPlayer cpDef = (CraftPlayer)player;
								EntityPlayer epAtt = cpDef.getHandle();
								EntityPlayer epDef = cpAtt.getHandle();
								
								boolean flag = epAtt.n(epDef);
					            double distance = 36.0D;
					            
					            if(!flag)
					            	distance = 9.0D;
					            
					            if(epAtt.e(epDef) < distance) {
					            	if(action == 0) {
					            		PlayerInteractDisguisedEvent ev = new PlayerInteractDisguisedEvent(player, dis);
						            	Bukkit.getServer().getPluginManager().callEvent(ev);
						            	if(!ev.isCancelled()) {
						            		epAtt.p(epDef);
						            	}
					            	} else if(action == 1) {
					            		epAtt.attack(epDef);
					            	}
					            	event.setCancelled(true);
					            }
							}
						}
					}
				}
			}
		});
	}
}
