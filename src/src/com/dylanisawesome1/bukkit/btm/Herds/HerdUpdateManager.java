package src.com.dylanisawesome1.bukkit.btm.Herds;

import java.util.ArrayList;

import org.bukkit.Location;

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
					hentity.update(50);  
					
					if(System.currentTimeMillis()-hentity.timeLastMoved>=moveTimeout) {
						hentity.timeLastMoved = System.currentTimeMillis();
						
						if(hentity.getEntityToAttack() != null) 
						{
							hentity.setPath(
								PathfindingUtil.getPathToLocation(
									hentity, 
									hentity.getEntityToAttack().getLocation()));
						}
						
						hentity.setPath(
							PathfindingUtil.getPathToLocation(
								hentity, 
								hentity.getLeader().getLocation())
								);


						if (hentity.getPath() != null){
							int sz = hentity.getPath().size();	
							if (sz>=2){
								Location moveto = hentity.getPath().get(sz-2);
								double d = PathfindingUtil.distanceBetweenLocs(moveto, hentity.getLeader().getLocation());
								
								if (d > 2.0 ){
									hentity.move(moveto, true);
									hentity.setLocation(moveto);
									hentity.refreshMovement();
								}
							}
						}							
						
					} 
				}
			}
			}
		};
	}
}
