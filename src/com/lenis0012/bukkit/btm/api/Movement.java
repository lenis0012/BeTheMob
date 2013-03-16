package com.lenis0012.bukkit.btm.api;

import net.minecraft.server.v1_5_R1.MathHelper;

import org.bukkit.Location;

/**
 * Wrapper of entity movement
 * 
 * @author lenis0012
 */
public class Movement {
	public int x, y, z;
	private int oldX, oldY, oldZ;
	
	public Movement(Location loc) {
		this.oldX = MathHelper.floor(loc.getX() * 32D);
		this.oldY = MathHelper.floor(loc.getY() * 32D);
		this.oldZ = MathHelper.floor(loc.getZ() * 32D);
	}
	
	/**
	 * Update the movement location
	 * 
	 * @param loc New location
	 */
	public void update(Location loc) {
		int newX = MathHelper.floor(loc.getX() * 32D);
		int newY = MathHelper.floor(loc.getY() * 32D);
		int newZ = MathHelper.floor(loc.getZ() * 32D);
		
		x = newX - oldX;
		y = newY - oldY;
		z = newZ - oldZ;
		
		oldX += x;
		oldY += y;
		oldZ += z;
	}
}
