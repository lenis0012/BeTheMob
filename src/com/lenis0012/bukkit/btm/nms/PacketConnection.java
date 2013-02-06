package com.lenis0012.bukkit.btm.nms;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.lenis0012.bukkit.btm.BeTheMob;
import com.lenis0012.bukkit.btm.api.Disguise;
import com.lenis0012.bukkit.btm.events.PlayerInteractDisguisedEvent;

import net.minecraft.server.v1_4_R1.EntityPlayer;
import net.minecraft.server.v1_4_R1.INetworkManager;
import net.minecraft.server.v1_4_R1.MinecraftServer;
import net.minecraft.server.v1_4_R1.Packet7UseEntity;
import net.minecraft.server.v1_4_R1.PlayerConnection;

public class PacketConnection extends PlayerConnection {
	public PacketConnection(MinecraftServer minecraftserver, INetworkManager inetworkmanager, EntityPlayer entityplayer) {
		super(minecraftserver, inetworkmanager, entityplayer);
	}
	
	@Override
	public void a(Packet7UseEntity packet) {
		Player player = (Player)this.player.getBukkitEntity();
		BeTheMob plugin = BeTheMob.instance;
		CraftPlayer target = null;
		Disguise disguise = null;
		
		for(String user : plugin.disguises.keySet()) {
			Disguise dis = plugin.disguises.get(user);
			Player check = dis.getPlayer();
			if(check != null && check.isOnline() && dis.getEntityId() == packet.target) {
				target = (CraftPlayer)check;
				disguise = dis;
			}
		}
		
		if(target != null) {
			boolean flag = this.player.n(target.getHandle());
            double distance = 36.0D;
            
            if(!flag)
            	distance = 9.0D;
            
            if(this.player.e(target.getHandle()) < distance) {
            	if(packet.action == 0) {
	            	PlayerInteractDisguisedEvent ev = new PlayerInteractDisguisedEvent(player, disguise);
	            	Bukkit.getServer().getPluginManager().callEvent(ev);
	            	if(!ev.isCancelled()) {
	            		this.player.p(target.getHandle());
	            	}
            	} else if(packet.action == 1) {
            		this.player.attack(target.getHandle());
            	}
            }
			return;
		}
		
		super.a(packet);
	}
}
