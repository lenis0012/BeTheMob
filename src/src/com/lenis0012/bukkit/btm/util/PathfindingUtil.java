package src.com.lenis0012.bukkit.btm.util;

import java.util.ArrayList;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

import src.com.dylanisawesome1.bukkit.btm.Herds.Pathfinding.Node;

public class PathfindingUtil {
	/**
	 * Get all the neighboring nodes of the block
	 * 
	 * @param node
	 *            - The node of which to get the neighbors
	 * @return the neighbors, "below", "above", "left", "right", "front", "behind", "leftfront", "rightfront", "leftbehind", and
	 *         "rightbehind"
	 */
	public static ArrayList<Node> getNeighboringNodes(Node node) {
		Location loc = node.getLocation();
		ArrayList<Node> nodes = new ArrayList<Node>();
		Block below = loc.getWorld().getBlockAt(loc.getBlockX(),
				loc.getBlockY() - 1, loc.getBlockZ());
		Block above = loc.getWorld().getBlockAt(loc.getBlockX(),
				loc.getBlockY() + 1, loc.getBlockZ());
		Block left = loc.getWorld().getBlockAt(loc.getBlockX() + 1,
				loc.getBlockY(), loc.getBlockZ());
		Block leftforward = loc.getWorld().getBlockAt(loc.getBlockX() + 1,
				loc.getBlockY(), loc.getBlockZ()+1);
		Block leftbackwards = loc.getWorld().getBlockAt(loc.getBlockX() + 1,
				loc.getBlockY(), loc.getBlockZ()-1);
		Block right = loc.getWorld().getBlockAt(loc.getBlockX() - 1,
				loc.getBlockY(), loc.getBlockZ());
		Block rightforwards = loc.getWorld().getBlockAt(loc.getBlockX() - 1,
				loc.getBlockY(), loc.getBlockZ()+1);
		Block rightbackwards = loc.getWorld().getBlockAt(loc.getBlockX() - 1,
				loc.getBlockY(), loc.getBlockZ()-1);
		Block front = loc.getWorld().getBlockAt(loc.getBlockX(),
				loc.getBlockY(), loc.getBlockZ() + 1);
		Block behind = loc.getWorld().getBlockAt(loc.getBlockX(),
				loc.getBlockY(), loc.getBlockZ() - 1);
		nodes.add(new Node(below, "below"));
		nodes.add(new Node(leftforward, "leftfront"));
		nodes.add(new Node(leftbackwards, "leftbehind"));
		nodes.add(new Node(rightforwards, "rightfront"));
		nodes.add(new Node(rightbackwards, "rightbehind"));
		nodes.add(new Node(above, "above"));
		nodes.add(new Node(left, "left"));
		nodes.add(new Node(right, "right"));
		nodes.add(new Node(front, "front"));
		nodes.add(new Node(behind, "behind"));
		return nodes;

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
	public static Node getLowestDistanceNode(ArrayList<Node> collection,
			Location destination, EntityType entitytype) {
		Node leastDist = collection.get(0);
		for (Node node : collection) {
			if (!isNodeObstructed(node, HerdUtil.getHeightInBlocks(entitytype), true)
					&& distanceBetweenNodes(destination, node) < distanceBetweenNodes(
							destination, leastDist)) {
				leastDist = node;
			}
		}
		return leastDist;
	}

	/**
	 * Get the entire path to a location using A*
	 * 
	 * @param startnode
	 *            - the node to start on
	 * @param endnode
	 *            - the destination node
	 * @return node - Next destination node
	 */
	public static ArrayList<Node> getPathToLocation(Node startnode, Node endnode, EntityType type) {
		ArrayList<Node> pathlocs = new ArrayList<Node>();
		Node curnode = endnode;
		int its=0;
		int maxits=5000;
		startnode.setNodeBlock(getBlockGravity(startnode.getNodeBlock()));
		while(!compareLocations(curnode.getLocation(), startnode.getLocation())) {
			
			curnode = getLowestDistanceNode(getNeighboringNodes(curnode), startnode.getLocation(), type);
			pathlocs.add(curnode);
			if(its>=maxits) {
				break;
			}
			its++;
		}
		return pathlocs;

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
	public static boolean isNodeObstructed(Node node, int height, boolean checkforoverhang) {
		if(node.getNodeBlock().getType().isSolid() /*&& node.getNodeBlock().getType() != Material.SNOW*/) {
			return true;
		}
		if(checkforoverhang) {
			for(int i=0;i<height-1;i++) {
				Block tmpblock = node.getLocation().add(0, i, 0).getBlock();
				if(tmpblock.getType().isSolid()) {
					return true;
				}
			}
		}
		return false;
	}
	public static Block getBlockGravity(Block block) {
		return block.getWorld().getHighestBlockAt(block.getLocation());
	}
	public static boolean compareLocations(Location loc1, Location loc2) {
		double x1 = loc1.getX();
		double x2 = loc2.getX();
		double y1 = loc1.getY();
		double y2 = loc2.getY();
		double z1 = loc1.getZ();
		double z2 = loc2.getZ();
		return x1==x2 && y1 == y2 && z1 == z2;
	}
}
