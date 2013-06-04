package com.dylanisawesome1.bukkit.btm.Herds.Pathfinding;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class BlockLoader {
	private Location entityLocation;
	private ArrayList<Block> possibleblocks = new ArrayList<Block>();
	public BlockLoader(Location location) {
		setEntityLocation(location);
	}
	public Location getEntityLocation() {
		return entityLocation;
	}
	public void setEntityLocation(Location entityLocation) {
		this.entityLocation = entityLocation;
	}
	public void loadBlocks(int maxdistance) {
		for(int x=entityLocation.getBlockX()-maxdistance/2;x<entityLocation.getBlockX()+maxdistance/2;x++) {
			for(int z=entityLocation.getBlockZ()-maxdistance/2;z<entityLocation.getBlockZ()+maxdistance/2;z++) {
				possibleblocks.add(entityLocation.getWorld().getHighestBlockAt(x, z));
			}
		}
	}
	public void getObstructedPaths() {
		
	}
}
