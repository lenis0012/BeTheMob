package com.lenis0012.bukkit.btm.commands;

import org.bukkit.entity.EntityType;

import com.lenis0012.bukkit.btm.type.MobDisguise;

public class BTMCommandMob extends BTMSubCommand {
	
	public BTMCommandMob() {
		super("mob" ,"animal");
		this.setMinArgs(1);
		this.setArguments("<type>");
		this.setDescription("Disguise as a mob.");
		this.setPermission("btm.disguise.mob");
	}
	
	@Override
	public void execute() {
		try {
			MobDisguise disguise = new MobDisguise(sender, EntityType.valueOf(args[1].toUpperCase()));
			disguise.disguise();
			message("&aYou are now disguised as a " + args[1].toLowerCase());
		} catch(Exception e) {
			message("&cInvalid entity type, type '/btm mobtypes' for a list.");
		}
	}
}