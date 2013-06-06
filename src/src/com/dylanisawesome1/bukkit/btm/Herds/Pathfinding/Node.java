package src.com.dylanisawesome1.bukkit.btm.Herds.Pathfinding;

import org.bukkit.Location;
import org.bukkit.block.Block;

public class Node {
	private Block nodeBlock;
	private boolean isObstructed=false;
//	public float pitch = 0f;
	public float yaw = 0f;
	public String direction;
	private Location location;
	public Node(Block b) {
		setNodeBlock(b);
		location = b.getLocation();
	}
	public Node(Block b, String direction) {
		setNodeBlock(b);
		this.direction=direction;
		location = b.getLocation();
		switch(this.direction) {
			case "below":
				location.setPitch(90f);
				location.setYaw(0f);
			case "above":
				location.setPitch(-90f);
				location.setYaw(0f);
			case "left":
				location.setYaw(90f);
				location.setPitch(0f);
			case "right":
				location.setYaw(270f);
				location.setPitch(0f);
			case "front":
				location.setYaw(180f);
				location.setPitch(0f);
			case "behind":
				location.setYaw(0f);
				location.setPitch(0f);
			case "leftfront":
				location.setYaw(135f);
				location.setPitch(0f);
			case "rightfront":
				location.setYaw(45f);
				location.setPitch(0f);
			case "leftbehind":
				location.setYaw(22.5f);
				location.setPitch(0f);
			case "rightbehind":
				location.setYaw(338f);
				location.setPitch(0f);
		}
		
	}
	public boolean isObstructed() {
		return isObstructed;
	}
	public void setObstructed(boolean isObstructed) {
		this.isObstructed = isObstructed;
	}
	public Block getNodeBlock() {
		return nodeBlock;
	}
	public void setNodeBlock(Block nodeBlock) {
		this.nodeBlock = nodeBlock;
	}
	public Location getLocation() {
		return location;
	}
}
