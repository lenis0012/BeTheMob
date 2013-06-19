package src.com.lenis0012.bukkit.btm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;

import src.com.dylanisawesome1.bukkit.btm.Herds.Herd;
import src.com.dylanisawesome1.bukkit.btm.Herds.HerdEntity;
import src.com.lenis0012.bukkit.btm.api.Api;
import src.com.lenis0012.bukkit.btm.api.Disguise;
import src.com.lenis0012.bukkit.btm.events.PlayerDisguiseEvent;
import src.com.lenis0012.bukkit.btm.events.PlayerUndisguiseEvent;
import src.com.lenis0012.bukkit.btm.util.HerdUtil;
import src.com.lenis0012.bukkit.btm.util.Util;

public class BTMCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be executed as player");
			return true;
		}

		Player player = (Player) sender;
		BeTheMob plugin = BeTheMob.instance;
		String name = player.getName();
		PluginManager pm = Bukkit.getServer().getPluginManager();
		Api api = BeTheMob.getApi();

		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("mob")) {
				if (args.length > 1) {
					api.removeDisguise(player);

					EntityType type = this.parseType(args[1], true);

					if (type != null) {
						if (this.hasPerms(player, "btm.disguise.mob." + args[1])
								|| this.hasPerms(player, "btm.disguise.mob.*")) {
							PlayerDisguiseEvent ev = new PlayerDisguiseEvent(
									player, type);
							pm.callEvent(ev);
							if (!ev.isCancelled()) {
								Location loc = player.getLocation();
								List<String> extras = this.parseExtras(args,
										player);
								Disguise dis = api.createDisguise(player, loc,
										type, extras);
								api.addDisguise(player, dis);
							}
						} else
							err(player, "Not enough permissions");
					} else
						err(player, "Invalid entity type");
				} else
					err(player, "Invalid arguments");
			} else if (args[0].equalsIgnoreCase("vehicle")) {
				if (args.length > 1) {
					api.removeDisguise(player);

					EntityType type = this.parseType(args[1], false);

					if (type != null) {
						if (this.hasPerms(player, "btm.disguise.vehicle."
								+ args[1])
								|| this.hasPerms(player,
										"btm.disguise.vehicle.*")) {
							PlayerDisguiseEvent ev = new PlayerDisguiseEvent(
									player, type);
							pm.callEvent(ev);
							if (!ev.isCancelled()) {
								Location loc = player.getLocation();
								List<String> extras = this.parseExtras(args,
										player);
								Disguise dis = api.createDisguise(player, loc,
										type, extras);
								api.addDisguise(player, dis);
								inf(player, "Succesfully diguised as a "
										+ args[1]);
							}
						} else
							err(player, "Not enough permissions");
					} else
						err(player, "Invalid entity type");
				} else
					err(player, "Invalid arguments");
			} else if (args[0].equalsIgnoreCase("player")) {
				api.removeDisguise(player);

				if (args.length > 1) {
					String target = args[1];
					if (this.hasPerms(player, "btm.disguise.player." + target)
							|| this.hasPerms(player, "btm.disguise.player.*")) {
						if (target.length() > 16) {
							err(player, "Names must be shorter than 17 chars");
							return true;
						}

						PlayerDisguiseEvent ev = new PlayerDisguiseEvent(
								player, target);
						pm.callEvent(ev);
						if (!ev.isCancelled()) {
							int itemInHand = player.getItemInHand().getTypeId();
							Location loc = player.getLocation();
							Disguise dis = new Disguise(player,
									plugin.nextID--, loc, ev.getName(),
									itemInHand);
							dis.spawn(loc.getWorld());
							plugin.disguises.put(name, dis);
							plugin.setHidden(player, true);
							inf(player, "Succesfully diguised as player: "
									+ args[1]);
						}
					} else
						err(player, "Not enough permissions");
				} else
					err(player, "Invalid arguments");
			} else if (args[0].equalsIgnoreCase("off")) {
				if (plugin.disguises.containsKey(name)) {
					Disguise dis = plugin.disguises.get(name);

					PlayerUndisguiseEvent ev = new PlayerUndisguiseEvent(
							player, dis);
					pm.callEvent(ev);
					if (!ev.isCancelled()) {
						api.removeDisguise(player);
						inf(player, "You are no longer disguised");
					}
				} else
					err(player, "You are not disguised");
			} else if (args[0].equalsIgnoreCase("reload")) {
				plugin.reloadConfig();
				inf(player, "Configuration reloaded!");
			} else if (args[0].equalsIgnoreCase("list")) {
				List<String> list = plugin.getMobList();
				player.sendMessage(ChatColor.GOLD + "Mob types: ");
				for (String message : this.getMessages(list))
					player.sendMessage(message);

				list = plugin.getVehicleList();
				player.sendMessage(" \n" + ChatColor.GOLD + "Vehicle types: ");
				for (String message : this.getMessages(list))
					player.sendMessage(message);
			} else if (args[0].equalsIgnoreCase("herd")) {

				if (args.length >= 3) {
					if (args[1].equalsIgnoreCase("addmember")) {
						if (Util.containsEnum(EntityType.class,
								args[2].toUpperCase())) {
							inf(player, "Added herd member:  " + args[2]);
							if (HerdUtil.getHerdFromPlayer(player) != null) {
								int radius = 20;
								Location entityloc = new Location(
										player.getWorld(), player.getLocation()
												.getX()
												+ new Random().nextInt(radius)
												- (radius / 2), player
												.getLocation().getY(), player
												.getLocation().getZ()
												+ new Random().nextInt(radius)
												- (radius / 2));
								
								HerdEntity entity = new HerdEntity(
										BeTheMob.instance.nextID--, entityloc,
										0, EntityType.valueOf(args[2].toUpperCase()),
										player);
								
								HerdUtil.getHerdFromPlayer(player).addHerdMember(entity);
							} else {
								Herd herd = new Herd(EntityType.valueOf(args[2]
										.toUpperCase()), player);
								herd.populateHerd(1, 20);
								herd.spawnHerdMembers();
								BeTheMob.instance.herds.add(herd);
							}
						} else {
							err(player, "Invalid mob type: " + ChatColor.BOLD
									+ args[2]);
						}
					}

				}
				if (args.length >= 2) {
					if (args[1].equalsIgnoreCase("removemember")) {
						if (BeTheMob.instance.selectedentities
								.containsKey(player.getName())) {
							BeTheMob.instance.selectedentities.get(
									player.getName()).kill();
							BeTheMob.instance.selectedentities.remove(player
									.getName());
						}
					}
				}
			} else if(args[0].equalsIgnoreCase("debug")) {
				player.getWorld().spawnFallingBlock(player.getLocation(), Material.BEDROCK, (byte) 0);
			}else			
				err(player,
						"Invalid argument, try mob, player, vehicle, list or off");

		} else
			err(player, "Usage: " + cmd.getUsage());

		return true;
	}

	private boolean hasPerms(Player player, String node) {
		return player.hasPermission(node) || player.hasPermission("btm.*");
	}

	private List<String> parseExtras(String[] args, Player player) {
		List<String> extras = new ArrayList<String>();

		if (args.length > 2) {
			for (int i = 2; i < args.length; i++) {
				if (player
						.hasPermission("btm.flag." + args[i].replace("-", ""))
						|| player.hasPermission("btm.flag.*"))
					extras.add(args[i]);
			}
		}

		return extras;
	}

	private void inf(Player player, String inf) {
		player.sendMessage(ChatColor.GREEN + inf);
	}

	private void err(Player player, String err) {
		player.sendMessage(ChatColor.RED + err);
	}

	private EntityType parseType(String toParse, boolean isMob) {
		BeTheMob plugin = BeTheMob.instance;
		System.out.println(isMob);
		if (isMob && !plugin.getMobList().contains(toParse.toLowerCase()))
			return null;
		else if (!isMob
				&& !plugin.getVehicleList().contains(toParse.toUpperCase()))
			return null;

		for (EntityType type : EntityType.values()) {
			if (type.toString().equalsIgnoreCase(toParse))
				return type;
		}
		return null;
	}

	private String[] getMessages(List<String> list) {
		List<String> messages = new ArrayList<String>();
		String current = "";
		int i = 100;
		for (String message : list) {
			if (i - (message.length() + 2) < 0) {
				messages.add(current);
				i = 100;
			}

			current += i < 100 ? ", " + message : message;
		}

		messages.add(current);
		return messages.toArray(new String[0]);
	}
}
