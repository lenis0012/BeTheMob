package src.com.lenis0012.bukkit.btm.util;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import src.com.dylanisawesome1.bukkit.btm.Herds.Herd;
import src.com.dylanisawesome1.bukkit.btm.Herds.HerdEntity;
import src.com.dylanisawesome1.bukkit.btm.Herds.Pathfinding.Node;
import src.com.lenis0012.bukkit.btm.BeTheMob;

public class PathfindingUtil {
	public static double moveinc=0.1;
	
	/**
	 * Get the entire path to a location using A*
	 * 
	 * @param startnode
	 *            - the node to start on
	 * @param endnode
	 *            - the destination node
	 * @return node - Next destination node
	 */
	public static ArrayList<Location> getPathToLocation(HerdEntity startnode, Location curnode) {
		ArrayList<Location> pathlocs = new ArrayList<Location>();
		
		int its=0;
		int maxits=5000;
		
		//startnode.setNodeBlock(getBlockGravity(startnode.getNodeBlock()));
		while(distanceBetweenLocs(curnode, startnode.getLocation()) >2 ) {
			
			curnode = getLowestDistanceNode(startnode, getNeighboringNodes(curnode), startnode.getLocation(), pathlocs);
			if(curnode==null)
				return pathlocs;
			pathlocs.add(curnode);
			if(its>=maxits) {
//				System.out.println("MAXITS");
				return null;
			}
			its++;
		}
		return pathlocs;

	}
	
	
	/**
	 * Get all the neighboring nodes of the block
	 * 
	 * @param node
	 *            - The node of which to get the neighbors
	 * @return the neighbors, "below", "above", "left", "right", "front", "behind", "leftfront", "rightfront", "leftbehind", and
	 *         "rightbehind"
	 */
	
	public static ArrayList<Location> getNeighboringNodes(Location l) {
		double x = (double) l.getX();
		double y = (double) l.getY();
		double z = (double) l.getZ();
		
		ArrayList<Location> Locs = new ArrayList<Location>() ;
		
		Location below = l.clone();
		below.setY( y-moveinc);
		Locs.add(below);
		
		Location above = l.clone();
		above.setY( y+moveinc);
		Locs.add(above);
		
		Location left = l.clone();
		left.setX( x+moveinc);
		Locs.add(left);
		
		Location right = l.clone();
		right.setX( x-moveinc);
		Locs.add(right);
	
		Location front = l.clone();
		front.setZ( z+moveinc);
		Locs.add(front);
		
		Location behind = l.clone();
		behind.setZ( z-0.5);
		Locs.add(behind);
		
		Location leftforward = l.clone();
		leftforward.setX( x+moveinc);
		leftforward.setZ( z+moveinc);
		Locs.add(leftforward);
		
		Location leftbackward = l.clone();
		leftbackward.setX( x+moveinc);
		leftbackward.setZ( z-moveinc);
		Locs.add(leftbackward);
		
		Location rightforward = l.clone();
		rightforward.setX( x-moveinc);
		rightforward.setZ( z+moveinc);
		Locs.add(rightforward);
		
		Location rightbackward = l.clone();
		rightbackward.setX( x-moveinc);
		rightbackward.setZ( z-moveinc);
		Locs.add(rightbackward);
		return Locs;

	}

	/**
	 * Find the distance between a node and a location
	 * 
	 * @param loc1
	 *            - the location, a or b
	 * @param node
	 *            - the node, of location a or b
	 * @return distance - the distance between the nodes
	 */
	public static double distanceBetweenNodes(Location loc1, Node node) {
		return loc1.distance(node.getLocation());
	}
	
	public static double distanceBetweenLocs(Location loc1, Location loc2) {
		return loc1.distance(loc2);
	}

	/**
	 * Get the node that is the least distance from the destination.
	 * 
	 * @param collection
	 *            - the collection of nodes to iterate through.
	 * @param destination
	 *            - The final destination
	 * @param entitytype
	 * 			  - The type of the entity, for finding the height in blocks
	 * @return Node - the node that is the closest out of the list
	 */
	public static Location getLowestDistanceNode(HerdEntity hentity, ArrayList<Location> collection,
			Location destination, ArrayList<Location> path) {
			
		Location leastDist = null;
		for (Location loc : collection) {
			if (!isNodeObstructed(loc, hentity, HerdUtil.getHeightInBlocks(hentity.getType()), true, path)) {
				if (leastDist == null) leastDist = loc; 
				else if (distanceBetweenLocs(destination, loc) < 
						distanceBetweenLocs(destination, leastDist)) leastDist = loc;
			}
		}
		return leastDist;
	}



	/**
	 * Get if a node is an obstacle for a*
	 * 
	 * @param node
	 *            - The node which to check
	 * @param height
	 * 			  - The height of the mob
	 * @param checkforoverhang
	 * 			  - Check for overhangs above this?
	 * @return isObstructed - Is the node obstructed?
	 */
	public static boolean isNodeObstructed(Location loc, HerdEntity entity, int height, boolean checkforoverhang, ArrayList<Location> path) {
		
		// cant move into a solid block
		if (loc.getBlock().getType().isSolid()) return true;
		
		//can't move where you already did move
		if (path.contains(loc)) return true;
		
		//cant move into other herd members (but not yourself)
		for(Herd herd : BeTheMob.instance.herds) {
			for(HerdEntity hentity : herd.getHerdMembers()) {
				/* compare with location of all entities except ourself */
				if (entity != hentity) {
					if(distanceBetweenLocs(hentity.getLocation(), loc) <=2) {
						return true;
					}
				}
			}
		}
//		if(checkforoverhang) {
//			for(int i=0;i<height-1;i++) {
//				Block tmpblock = node.getLocation().add(0, i, 0).getBlock();
//				if(tmpblock.getType().isSolid()) {
//					return true;
//				}
//			}
//		}
		return false;
	}
	public static Block getBlockGravity(Block block) {
		return block.getWorld().getHighestBlockAt(block.getLocation());
	}
}
