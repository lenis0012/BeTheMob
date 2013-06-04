package src.com.lenis0012.bukkit.btm.nms.wrappers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.bukkit.Bukkit;

import com.lenis0012.bukkit.btm.util.DynamicUtil;

public class MinecraftServer extends WrapperBase {
	private static Class<?> CraftServer = DynamicUtil.getCBClass("CraftServer");
	private static Method getServer = DynamicUtil.getMethod(CraftServer, "getServer");
	
	private static Class<?> MinecraftServer = DynamicUtil.getNMSClass("MinecraftServer");
	private static Method getServerConnection = DynamicUtil.getMethod(MinecraftServer, "ae");
	
	private static Class<?> ServerConnection = DynamicUtil.getNMSClass("ServerConnection");
	private static Field playerConnections = DynamicUtil.getField(ServerConnection, "c");
	
	public MinecraftServer() {
		Object handle = DynamicUtil.invoke(getServer, Bukkit.getServer());
		setHandle(handle);
	}
	
	public List<Object> getPlayerConnections() {
		Object sc = DynamicUtil.invoke(getServerConnection, handle);
		return DynamicUtil.getValue(sc, playerConnections);
	}
}
