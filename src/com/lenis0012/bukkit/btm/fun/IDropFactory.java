package com.lenis0012.bukkit.btm.fun;

import java.util.Set;

import org.bukkit.inventory.ItemStack;

import com.lenis0012.bukkit.btm.api.Disguise;

public interface IDropFactory {

	/**
	 * @return The items to be dropped
	 */
	public Set<ItemStack> getDrops(Disguise disguise);
	
	/**
	 * @return The name of the drop factory
	 */
	public String getName();
}