package com.lenis0012.bukkit.btm.nms;

import net.minecraft.server.v1_5_R1.EntityPlayer;
import net.minecraft.server.v1_5_R1.INetworkManager;
import net.minecraft.server.v1_5_R1.MinecraftServer;
import net.minecraft.server.v1_5_R1.Packet14BlockDig;
import net.minecraft.server.v1_5_R1.Packet7UseEntity;
import net.minecraft.server.v1_5_R1.PlayerConnection;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.lenis0012.bukkit.btm.BeTheMob;
import com.lenis0012.bukkit.btm.api.Disguise;
import com.lenis0012.bukkit.btm.events.PlayerInteractDisguisedEvent;

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
            		target.getWorld().playSound(target.getLocation(), Sound.HURT_FLESH, 63F, 1F);
            	}
            }
			return;
		}
		
		super.a(packet);
	}
	
	@Override
	public void a(Packet14BlockDig packet) {
		BeTheMob plugin = BeTheMob.instance;
		super.a(packet);
		
		if(packet.e == 1) {
			String name = this.player.getBukkitEntity().getName();
			if(plugin.disguises.containsKey(name)) {
				Disguise dis = plugin.disguises.get(this);
				dis.damageBlock(this.player.getBukkitEntity().getWorld().getBlockAt(packet.a, packet.b, packet.c));
			}
		}
	}
}
