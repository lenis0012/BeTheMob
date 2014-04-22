package com.lenis0012.bukkit.btm.commands;

import com.lenis0012.bukkit.btm.type.PlayerDisguise;

public class BTMCommandPlayer extends BTMSubCommand {
	
	public BTMCommandPlayer() {
		super("player" ,"human");
		this.setMinArgs(1);
		this.setArguments("<name>");
		this.setDescription("Disguise as a player.");
	}

	@Override
	public void execute() {
		PlayerDisguise disguise = new PlayerDisguise(sender, args[1]);
		disguise.disguise();
		message("&aYou are now disguised as a player named " + args[1]);
	}
}