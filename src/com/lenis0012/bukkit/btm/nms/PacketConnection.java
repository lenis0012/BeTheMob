package com.lenis0012.bukkit.btm.nms;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_5_R1.CraftServer;
import org.bukkit.craftbukkit.v1_5_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_5_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import com.lenis0012.bukkit.btm.BeTheMob;
import com.lenis0012.bukkit.btm.api.Disguise;
import com.lenis0012.bukkit.btm.events.PlayerInteractDisguisedEvent;
import com.lenis0012.bukkit.btm.nms.wrappers.Packet;
import com.lenis0012.bukkit.btm.util.DynamicUtil;
import com.lenis0012.bukkit.btm.util.NetworkUtil;

import net.minecraft.server.v1_5_R1.EntityPlayer;
import net.minecraft.server.v1_5_R1.INetworkManager;
import net.minecraft.server.v1_5_R1.MinecraftServer;
import net.minecraft.server.v1_5_R1.Packet14BlockDig;
import net.minecraft.server.v1_5_R1.Packet7UseEntity;
import net.minecraft.server.v1_5_R1.PlayerConnection;
import net.minecraft.server.v1_5_R1.ServerConnection;
import net.minecraft.server.v1_5_R1.WorldServer;

@SuppressWarnings("unchecked")
public class PacketConnection extends PlayerConnection {
	private static Logger log = Logger.getLogger("Minecraft");
	private static final List<PlayerConnection> connections;
	private static final Constructor<Object> blockChange = DynamicUtil.getConstructor(DynamicUtil.getNMSClass("Packet53BlockChange"), int.class, int.class, int.class, DynamicUtil.getNMSClass("World"));
	
	static {
		CraftServer cs = (CraftServer)Bukkit.getServer();
		MinecraftServer server = cs.getServer();
		List<PlayerConnection> list = new ArrayList<PlayerConnection>();
		try {
			Field field = ServerConnection.class.getDeclaredField("c");
			field.setAccessible(true);
			list = (List<PlayerConnection>) field.get(server.ae());
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		connections = list;
	}
	
	private static void transfer(Class<?> fromClass, Object from, Object to) {
		if(fromClass == null)
			return;
		
		try {
			for(Field field : fromClass.getDeclaredFields()) {
				field.setAccessible(true);
				try {
					field.set(to, field.get(from));
				} catch(Exception e) {
					continue;
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		transfer(fromClass.getSuperclass(), from, to);
	}
	
	private static void transfer(Object from, Object to) {
		transfer(from.getClass(), from, to);
	}
	
	public static boolean canReplace(PlayerConnection con) {
		return (con instanceof PacketConnection) || con.getClass().equals(PlayerConnection.class);
	}
	
	public static void hook(Player player) {
		EntityPlayer ep = ((CraftPlayer) player).getHandle();
		CraftServer cs = (CraftServer)Bukkit.getServer();
		MinecraftServer server = cs.getServer();
		if(canReplace(ep.playerConnection)) {
			PacketConnection newConnection = new PacketConnection(server, ep.playerConnection.networkManager, ep);
			transfer(ep.playerConnection, newConnection);
			ep.playerConnection = newConnection;
			synchronized(connections) {
				ListIterator<PlayerConnection> it = connections.listIterator();
				while(it.hasNext()) {
					if(it.next().player == ep) {
						it.set(newConnection);
						return;
					}
				}
			}
		} else {
			log.severe("[BeTheMob] Could no replace playerConnection for player: " + player.getName());
			log.severe("Please install ProtcolLib if this happends for more players!");
		}
	}
	
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
		
		if(packet.e == 1 || packet.e == 2) {
			String name = this.player.getBukkitEntity().getName();
			if(plugin.disguises.containsKey(name)) {
				World world = this.player.getBukkitEntity().getWorld();
				WorldServer ws = ((CraftWorld) world).getHandle();
				int x = packet.a;
				int y = packet.b;
				int z = packet.c;
				this.player.playerInteractManager.a(x, y, z);
				if(ws.getTypeId(x, y, z) != 0) {
					Packet newPacket = new Packet(DynamicUtil.newInstance(blockChange, x, y, z, ws));
					NetworkUtil.sendGlobalPacket(newPacket, world);
				}
				
				return;
			}
		}
		
		super.a(packet);
	}
}
