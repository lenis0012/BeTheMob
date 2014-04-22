package com.lenis0012.bukkit.btm.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.lenis0012.bukkit.btm.BeTheMob;
import com.lenis0012.bukkit.btm.DisguiseManager;

public abstract class BTMSubCommand {
	/**
	 * Initial params
	 */
	private String[] aliases;
	private int minArgs = 0;
	private String permission = null;
	private String description = "description unavailable";
	private String arguments = "";
	
	/**
	 * Execution params
	 */
	protected Player sender;
	protected String[] args;
	protected BeTheMob plugin;
	protected DisguiseManager disguiseManager;
	
	public BTMSubCommand(String... aliases) {
		this.aliases = aliases;
		this.plugin = BeTheMob.getInstance();
		this.disguiseManager = BeTheMob.getDisguiseManager();
	}
	
	public String[] getAliases() {
		return this.aliases;
	}
	
	public int getMinArgs() {
		return minArgs;
	}

	protected void setMinArgs(int minArgs) {
		this.minArgs = minArgs;
	}

	public String getPermission() {
		return permission;
	}

	protected void setPermission(String permission) {
		this.permission = permission;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getArguments() {
		return arguments;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	public abstract void execute();
	
	protected void message(String message) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
}