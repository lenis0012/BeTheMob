package com.lenis0012.bukkit.btm.util;

import java.util.ArrayList;
import java.util.HashMap;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class PathfindingUtil {
	public static HashMap<String, Block> getNeighboringNodes(Location loc) {
		HashMap<String, Block> nodes = new HashMap<String, Block>();
		Block below = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()+1, loc.getBlockZ());
		Block above = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY()-1, loc.getBlockZ());
		Block left = loc.getWorld().getBlockAt(loc.getBlockX()+1, loc.getBlockY(), loc.getBlockZ());
		Block right = loc.getWorld().getBlockAt(loc.getBlockX()-1, loc.getBlockY(), loc.getBlockZ());
		Block front = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()+1);
		Block behind = loc.getWorld().getBlockAt(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()-1);
		nodes.put("below", below);
		nodes.put("above", above);
		nodes.put("left", left);
		nodes.put("right", right);
		nodes.put("front", front);
		nodes.put("behind", behind);
		return nodes;
	}
	public static double distanceBetweenNodes(Location loc1, Location loc2) {
		return loc1.distance(loc2);
	}
	public static int getNodeWorth(Location destination, Block node) {
		if(isNodeObstructed(node.getLocation())) {
			return -1;
			//AKA, DO NOT GO HERE
		} else {
			return (int) PathfindingUtil.distanceBetweenNodes(destination, node.getLocation());
		}
	}
	public static boolean isNodeObstructed(Location loc) {
		Block blockbelow = getNeighboringNodes(loc).get("below");
		HashMap<String, Block> blocksaround =  getNeighboringNodes(blockbelow.getLocation());
		if(blockbelow.getType()!=Material.AIR) {
			for(String s : blocksaround.keySet()) {
				if(s=="left" || s=="right" || s=="front" || s=="behind") {
					if(blocksaround.get(s).getType() == Material.AIR) {
						return true;
					}
				}
			}
		}
		return false;
	}
}
