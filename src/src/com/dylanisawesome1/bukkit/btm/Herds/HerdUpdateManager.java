package src.com.dylanisawesome1.bukkit.btm.Herds;

import java.util.ArrayList;

import src.com.dylanisawesome1.bukkit.btm.Herds.Pathfinding.Node;


public class HerdUpdateManager {
	/**
	 * 
	 * @param herds - all the herds in existence
	 * @param timeLastMoved - the last time this method returned true.
	 * @param moveTimeout - the timeout between movement of herdentities. suggested time: 100ms
	 * @return whether or not the statue successfully moved.
	 */
	public static boolean updateHerds(ArrayList<Herd> herds, long timeLastMoved, long moveTimeout) {
		for (Herd herd : herds) {
			for (HerdEntity hentity : herd.getHerdMembers()) {
				hentity.update();
				if(System.currentTimeMillis()-timeLastMoved>=moveTimeout) {
					hentity.moveToNode(new Node(hentity.getLeader().getLocation().getBlock()));
					return true;
				} else {
					return false;
				}
			}
		}
		return false;
	}
}
