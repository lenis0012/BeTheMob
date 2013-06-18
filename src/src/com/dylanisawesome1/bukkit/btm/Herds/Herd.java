package src.com.dylanisawesome1.bukkit.btm.Herds;

import java.util.ArrayList;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import src.com.dylanisawesome1.bukkit.btm.Herds.Pathfinding.Node;
import src.com.lenis0012.bukkit.btm.BeTheMob;


public class Herd {
	private ArrayList<HerdEntity> herdMembers = new ArrayList<HerdEntity>();
	private EntityType herdType;
	private Player leader;
	public Herd(EntityType type, Player leader) {
		this.herdType=type;
		setLeader(leader);
	}
	/**
	 * Get the herd type of all members in the herd.
	 * @return Herd Type - The type of entity the herd is.
	 */
	public EntityType getHerdType() {
		return herdType;
	}
	/**
	 * Get the leader of the herd.
	 * @return Leader - the leader of the herd
	 */
	public Player getLeader() {
		return leader;
	}
	/**
	 * Set the player the herd should follow.
	 * @param Leader - The leader of the herds
	 * @param leader
	 */
	public void setLeader(Player leader) {
		this.leader = leader;
	}
	/**
	 * Get all the members of a herd
	 * @return Herd Members - All herd members
	 */
	public ArrayList<HerdEntity> getHerdMembers() {
		return herdMembers;
	}
	/**
	 * Adds a member to the herd.
	 * @param entity - The entity to add
	 */
	public void addHerdMember(HerdEntity entity) {
		herdMembers.add(entity);
		entity.spawn(entity.getLocation().getWorld());
	}
	/**
	 * Populate the herd with members.
	 * @param membercount - the number of members to add
	 * @param radius - the maximum distance the entity can be from the leader
	 */
	public void populateHerd(int membercount, int radius) {
		for(int i=0;i<membercount;i++) {
			Random rand = new Random();
			Location entityloc = new Location(leader.getWorld(), leader.getLocation().getX()+rand.nextInt(radius)-(radius/2), 
					leader.getLocation().getY(), leader.getLocation().getZ()+rand.nextInt(radius)-(radius/2));
			HerdEntity hentity = new HerdEntity(BeTheMob.instance.nextID--, entityloc, 0, 
					herdType, leader);
			addHerdMember(hentity);
			
		}
	}
	/**
	 * Spawn all herd entities in the herd members array
	 */
	public void spawnHerdMembers() {
		for(HerdEntity e : herdMembers) {
			e.spawn(leader.getWorld());
		}
	}
}
