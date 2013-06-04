package src.com.lenis0012.bukkit.btm.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import com.google.common.collect.Iterables;

import src.com.dylanisawesome1.bukkit.btm.Herds.Pathfinding.Node;

public class PathfindingUtil {
	/**
	 * Get all the neigboring nodes of the block
	 * 
	 * @param node
	 *            - The node of which to get the neighbors
	 * @return the neighbors, "below", "above", "left", "right", "front", and
	 *         "behind"
	 */
	public static HashMap<String, Node> getNeighboringNodes(Node node) {
		Location loc = node.getNodeBlock().getLocation();
		HashMap<String, Node> nodes = new HashMap<String, Node>();
		Block below = loc.getWorld().getBlockAt(loc.getBlockX(),
				loc.getBlockY() + 1, loc.getBlockZ());
		Block above = loc.getWorld().getBlockAt(loc.getBlockX(),
				loc.getBlockY() - 1, loc.getBlockZ());
		Block left = loc.getWorld().getBlockAt(loc.getBlockX() + 1,
				loc.getBlockY(), loc.getBlockZ());
		Block right = loc.getWorld().getBlockAt(loc.getBlockX() - 1,
				loc.getBlockY(), loc.getBlockZ());
		Block front = loc.getWorld().getBlockAt(loc.getBlockX(),
				loc.getBlockY(), loc.getBlockZ() + 1);
		Block behind = loc.getWorld().getBlockAt(loc.getBlockX(),
				loc.getBlockY(), loc.getBlockZ() - 1);
		nodes.put("below", new Node(below));
		nodes.put("above", new Node(above));
		nodes.put("left", new Node(left));
		nodes.put("right", new Node(right));
		nodes.put("front", new Node(front));
		nodes.put("behind", new Node(behind));
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
		return loc1.distance(node.getNodeBlock().getLocation());

	}

	/**
	 * Get the node that is the least distance from the destination.
	 * 
	 * @param collection
	 *            - the collection of nodes to iterate through.
	 * @param destination
	 *            - The final destination
	 * @return Node - the node that is the closest out of the list
	 */
	public static Node getLowestDistanceNode(Collection<Node> collection,
			Location destination) {
		Node leastDist = Iterables.get(collection, 0);
		for (Node node : collection) {
			if (!node.isObstructed()
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
	 * @return Nodes - all the locations in the path
	 */
	public static ArrayList<Node> getPathToLocation(Node startnode, Node endnode) {
		int maxits = 50;
		int its = 0;
		//max iterations before giving up
		ArrayList<Node> pathlocs = new ArrayList<Node>();
		Node curnode = endnode;
		while(!curnode.equals(startnode)) {
			pathlocs.add(curnode);
			curnode = getLowestDistanceNode(getNeighboringNodes(curnode).values(), startnode.getNodeBlock().getLocation());
			curnode.setNodeBlock(getBlockGravity(curnode.getNodeBlock()));
			System.out.println(curnode.getNodeBlock().getX()+ ", " + curnode.getNodeBlock().getY()+", "+curnode.getNodeBlock().getZ());
			its++;
			if(its>=maxits)
				break;
		}
		return pathlocs;
		
	}

	/**
	 * Get if a node is an obstacle for a*
	 * 
	 * @param node
	 *            - The node which to check
	 * @return isObstructed - Is the node obstructed?
	 */
	public static boolean isNodeObstructed(Node node) {
		Node nodeBelow = getNeighboringNodes(node).get("below");
		HashMap<String, Node> nodesaround = getNeighboringNodes(nodeBelow);
		if (node.getNodeBlock().getType() != Material.AIR) {
			for (String s : nodesaround.keySet()) {
				if (nodesaround.get(s).getNodeBlock().getType() == Material.AIR) {
					return true;
				}
			}
		}
		return false;

	}
	public static Block getBlockGravity(Block block) {
		return block.getWorld().getHighestBlockAt(block.getLocation());
	}
}
