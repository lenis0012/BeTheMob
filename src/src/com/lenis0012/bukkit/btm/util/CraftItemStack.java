package src.com.lenis0012.bukkit.btm.util;

import java.lang.reflect.Method;

import org.bukkit.inventory.ItemStack;

public class CraftItemStack {
	private static Class<?> TEMPLATE = DynamicUtil.getCBClass("inventory.CraftItemStack");
	private static Method toHandle = DynamicUtil.getMethod(TEMPLATE, "asNMSCopy", ItemStack.class);
	
	public static Object asNMSCopy(ItemStack original) {
		return DynamicUtil.invoke(toHandle, null, original);
	}
}
