package src.com.dylanisawesome1.bukkit.btm.Herds.Pathfinding;

import org.bukkit.block.Block;

public class Node {
	private Block nodeBlock;
	private boolean isObstructed=false;
//	public float pitch = 0f;
	public float yaw = 0f;
	public String direction;
	public Node(Block b) {
		setNodeBlock(b);
	}
	public Node(Block b, String direction) {
		setNodeBlock(b);
		this.direction=direction;
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
}