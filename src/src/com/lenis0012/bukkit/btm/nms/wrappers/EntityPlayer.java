package src.com.lenis0012.bukkit.btm.nms.wrappers;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.entity.Player;

import src.com.lenis0012.bukkit.btm.util.DynamicUtil;


public class EntityPlayer extends WrapperBase {
	private static Class<?> CraftPlayer = DynamicUtil.getCBClass("entity.CraftPlayer");
	private static Method getHandle = DynamicUtil.getMethod(CraftPlayer, "getHandle");
	
	private static Class<?> EntityPlayer = DynamicUtil.getNMSClass("EntityPlayer");
	private static Method getBukkitEntity = DynamicUtil.getMethod(EntityPlayer, "getBukkitEntity");
	private static Field playerInteractManager = DynamicUtil.getField(EntityPlayer, "playerInteractManager");
	private static Field playerConnection = DynamicUtil.getField(EntityPlayer, "playerConnection");
	
	private static Class<?> NMSEntity = DynamicUtil.getNMSClass("Entity");
	private static Method distance = DynamicUtil.getMethod(NMSEntity, "e", NMSEntity);
	
	private static Class<?> EntityLiving  = DynamicUtil.getNMSClass("EntityLiving");
	private static Method longDistance = DynamicUtil.getMethod(EntityLiving, "n", NMSEntity);
	
	private static Class<?> EntityHuman = DynamicUtil.getNMSClass("EntityHuman");
	private static Method interact = DynamicUtil.getMethod(EntityHuman, "p", NMSEntity);
	private static Method attack = DynamicUtil.getMethod(EntityHuman, "attack", NMSEntity);
	
	private static Class<?> PlayerInteractManager = DynamicUtil.getNMSClass("PlayerInteractManager");
	private static Method stopDigging = DynamicUtil.getMethod(PlayerInteractManager, "c", int.class, int.class, int.class);
	private static Method finishDigging = DynamicUtil.getMethod(PlayerInteractManager, "a", int.class, int.class, int.class);
	
	public EntityPlayer(Object handle) {
		super(handle);
	}
	
	public EntityPlayer(Player player) {
		Object handle = DynamicUtil.invoke(getHandle, player);
		setHandle(handle);
	}
	
	public Player getBukkitEntity() {
		Object bukkitEntity = DynamicUtil.invoke(getBukkitEntity, handle);
		return Player.class.cast(bukkitEntity);
	}
	
	public void stopDigging(int x, int y, int z) {
		Object pim = DynamicUtil.getValue(handle, playerInteractManager);
		DynamicUtil.invoke(stopDigging, pim, x, y, z);
	}
	
	public void finishDigging(int x, int y, int z) {
		Object pim = DynamicUtil.getValue(handle, playerInteractManager);
		DynamicUtil.invoke(finishDigging, pim, x, y, z);
	}
	
	public boolean longDistance(Object entity) {
		if(entity == null)
			throw new IllegalArgumentException("Entity is null!");
		if(handle == null)
			throw new IllegalArgumentException("Handle is null!");
		return DynamicUtil.invoke(longDistance, handle, entity);
	}
	
	public double distance(Object entity) {
		return DynamicUtil.invoke(distance, handle, entity);
	}
	
	public boolean interact(Object entity) {
		return DynamicUtil.invoke(interact, handle, entity);
	}
	
	public void attack(Object entity) {
		DynamicUtil.invoke(attack, handle, entity);
	}
	
	public Object getPlayerConnection() {
		return DynamicUtil.getValue(handle, playerConnection);
	}
	
	public void setPlayerConnection(Object value) {
		DynamicUtil.setValue(handle, playerConnection, value);
	}
}