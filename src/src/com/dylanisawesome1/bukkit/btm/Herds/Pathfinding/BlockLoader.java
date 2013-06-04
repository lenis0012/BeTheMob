package src.com.dylanisawesome1.bukkit.btm.Herds.Pathfinding;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.block.Block;

import src.com.lenis0012.bukkit.btm.util.PathfindingUtil;

public class BlockLoader {
	private Location entityLocation;
	private ArrayList<Node> allNodes = new ArrayList<Node>();
	ArrayList<Node> availableNodes = new ArrayList<Node>();
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
				allNodes.add(new Node(entityLocation.getWorld().getHighestBlockAt(x, z)));
			}
		}
		getAvailableNodes();
	}
	public void getAvailableNodes() {
		for(Node node : allNodes) {
			if(!PathfindingUtil.isNodeObstructed(node)) {
				availableNodes.add(node);
			} else {
				node.setObstructed(true);
			}
		}
	}
}
