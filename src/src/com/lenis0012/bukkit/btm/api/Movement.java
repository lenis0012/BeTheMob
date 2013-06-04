package src.com.lenis0012.bukkit.btm.api;

import org.bukkit.Location;

import src.com.lenis0012.bukkit.btm.util.MathUtil;


/**
 * Wrapper for entity movement
 * 
 * @author lenis0012
 */
public class Movement {
	public int x, y, z;
	private int oldX, oldY, oldZ;
	
	public Movement(Location loc) {
		this.oldX = MathUtil.floor(loc.getX() * 32D);
		this.oldY = MathUtil.floor(loc.getY() * 32D);
		this.oldZ = MathUtil.floor(loc.getZ() * 32D);
	}
	
	/**
	 * Update the movement location
	 * 
	 * @param loc New location
	 */
	public void update(Location loc) {
		int newX = MathUtil.floor(loc.getX() * 32D);
		int newY = MathUtil.floor(loc.getY() * 32D);
		int newZ = MathUtil.floor(loc.getZ() * 32D);
		
		x = newX - oldX;
		y = newY - oldY;
		z = newZ - oldZ;
		
		oldX += x;
		oldY += y;
		oldZ += z;
	}
}
