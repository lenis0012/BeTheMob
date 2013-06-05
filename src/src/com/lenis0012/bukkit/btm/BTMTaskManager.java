package src.com.lenis0012.bukkit.btm;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import org.bukkit.entity.Player;

import src.com.dylanisawesome1.bukkit.btm.Herds.HerdUpdateManager;
import src.com.lenis0012.bukkit.btm.api.Disguise;

public class BTMTaskManager extends Thread {
	private BeTheMob plugin;
	// private static HashMap<String, List<Disguise>> render = new
	// HashMap<String, List<Disguise>>();
	private static Map<String, Location> locations = new HashMap<String, Location>();
	private int doTeleport = 10;
	long timeSinceLastMoved = 0l;

	public BTMTaskManager(BeTheMob plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		while (!this.isInterrupted()) {
			boolean teleport = this.doTeleport <= 0;
			synchronized (locations) {
				for (String user : plugin.disguises.keySet()) {
					Player player = Bukkit.getPlayer(user);
					if (player != null && player.isOnline()) {
						if (!locations.containsKey(user)) {
							locations.put(user, player.getLocation().clone());
							continue;
						}

						Disguise dis = plugin.disguises.get(user);
						Location lastLoc = locations.get(user);
						Location newLoc = player.getLocation();
						boolean locChanged = false;
						boolean moved = false;

						if (hasMoved(lastLoc, newLoc)) {
							moved = true;
							locChanged = true;
						} else if (hasLooked(lastLoc, newLoc))
							locChanged = true;

						if (locChanged) {
							dis.move(lastLoc, newLoc, moved);
							locations.put(user, newLoc.clone());
						}

						if (teleport)
							dis.teleport(newLoc);
					}
				}
			}

			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// Thead interrupted
			}
		}
	}

	private boolean hasMoved(Location lastLoc, Location newLoc) {
		return lastLoc.getX() != newLoc.getX()
				|| lastLoc.getY() != newLoc.getY()
				|| lastLoc.getZ() != newLoc.getZ();
	}

	private boolean hasLooked(Location lastLoc, Location newLoc) {
		return lastLoc.getYaw() != newLoc.getYaw()
				|| lastLoc.getPitch() != newLoc.getPitch();
	}

	/**
	 * Notigy a player changed his world
	 * 
	 * @param player
	 *            Player who changed world
	 */
	public static void notifyWorldChanged(Player player, World from) {
		String name = player.getName();
		BeTheMob plugin = BeTheMob.instance;

		if (plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			Location loc = dis.getPlayer().getLocation();
			dis.despawn(from);
			dis.setLocation(loc);
			dis.spawn(loc.getWorld());
			dis.refreshMovement();
		}
	}

	/**
	 * Notify a player left the game
	 * 
	 * @param player
	 *            Player who left
	 */
	public static void notifyPlayerLeft(Player player) {
		String name = player.getName();
		BeTheMob plugin = BeTheMob.instance;

		if (plugin.disguises.containsKey(name)) {
			Disguise dis = plugin.disguises.get(name);
			dis.despawn();
		}
	}

	public static void notifyPlayerUndisguised(String name) {
		synchronized (locations) {
			locations.remove(name);
		}
	}

	public void cancel() {
		this.interrupt();
	}
}