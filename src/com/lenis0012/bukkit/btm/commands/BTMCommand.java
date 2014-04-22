package com.lenis0012.bukkit.btm.commands;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BTMCommand implements CommandExecutor {
	private Map<String, BTMSubCommand> subCommands = new HashMap<String, BTMSubCommand>();
	
	public BTMCommand() {
		addSubCommand(new BTMCommandPlayer());
		addSubCommand(new BTMCommandMob());
	}
	
	public void addSubCommand(BTMSubCommand subCommand) {
		for(String alias : subCommand.getAliases()) {
			subCommands.put(alias, subCommand);
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(!(sender instanceof Player)) {
			sender.sendMessage("You must be a player to execute this command.");
			return true;
		}
		
		Player player = (Player) sender;
		if(args.length > 0) {
			BTMSubCommand subCommand = subCommands.get(args[0].toLowerCase());
			if(subCommand != null) {
				if(args.length > subCommand.getMinArgs()) {
					if(subCommand.getPermission() == null || player.hasPermission(subCommand.getPermission())) {
						subCommand.sender = player;
						subCommand.args = args;
						subCommand.execute();
					} else {
						message(player, "&cYou don't have permission to execute this command.");
					}
				} else {
					message(player, "&cNot enough arguments, type '/btm' for help.");
				}
			} else {
				message(player, "&cUnkown subcommand, type '/btm' for help.");
			}
		} else {
			message(player, "&a&l===== &6&lBeTheMob command help &a&l=====");
			for(BTMSubCommand subCommand : subCommands.values()) {
				message(player, "&a/btm " + subCommand.getAliases()[0] + " " + subCommand.getArguments() + " &7- " + subCommand.getDescription());
			}
		}
		
		return true;
	}
	
	private void message(Player player, String... messages) {
		for(String message : messages) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
		}
	}
}