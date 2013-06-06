package src.com.dylanisawesome1.bukkit.btm.Herds;

import java.util.ArrayList;

import src.com.dylanisawesome1.bukkit.btm.Herds.Pathfinding.Node;
import src.com.lenis0012.bukkit.btm.util.PathfindingUtil;


public class HerdUpdateManager {
	/**
	 * 
	 * @param herds
	 *            - all the herds in existence
	 * @param timeLastMoved
	 *            - the last time this method returned true.
	 * @param moveTimeout
	 *            - the timeout between movement of herdentities. suggested
	 *            time: 100ms
	 * @return whether or not the statue successfully moved.
	 */
	public static Runnable runHerdUpdate(final ArrayList<Herd> herds, final long moveTimeout) {
		return new Runnable() {
			
			public void run() {
		for (Herd herd : herds) {
				for (HerdEntity hentity : herd.getHerdMembers()) {
					//System.out.println(hentity.getLocation().toString());
					hentity.update();
					if(System.currentTimeMillis()-hentity.timeLastMoved>=moveTimeout) {
						hentity.timeLastMoved = System.currentTimeMillis();
						if(hentity.nextDest!=null) {
							hentity.setPath(PathfindingUtil.getPathToLocation(new Node(hentity.getLocation().getBlock()), new Node(hentity.getLeader().getLocation().getBlock()), hentity.getType()));
							hentity.move(hentity.getLocation(), hentity.nextDest.getLocation().add(0.5D, 0.0D, 0.5D), true);
							
						}
					} 
				}
			}
			}
		};
	}
}
