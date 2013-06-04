package com.lenis0012.bukkit.btm.nms.wrappers;

import java.lang.reflect.Field;

import com.lenis0012.bukkit.btm.util.DynamicUtil;

public class PlayerConnection extends WrapperBase {
	private static Class<?> PlayerConnection = DynamicUtil.getNMSClass("PlayerConnection");
	private static Field minecraftServer = DynamicUtil.getField(PlayerConnection, "minecraftServer");
	private static Field networkManager = DynamicUtil.getField(PlayerConnection, "networkManager");
	private static Field player = DynamicUtil.getField(PlayerConnection, "player");
	
	public PlayerConnection(Object handle) {
		super(handle);
	}
	
	public Object getServer() {
		return DynamicUtil.getValue(handle, minecraftServer);
	}
	
	public Object getNetworkManager() {
		return DynamicUtil.getValue(handle, networkManager);
	}
	
	public EntityPlayer getPlayer() {
		Object playerHandle = DynamicUtil.getValue(handle, player);
		return new EntityPlayer(playerHandle);
	}
}
