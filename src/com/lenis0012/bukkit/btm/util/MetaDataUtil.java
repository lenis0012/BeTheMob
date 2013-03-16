package com.lenis0012.bukkit.btm.util;

import java.util.List;

import net.minecraft.server.v1_5_R1.DataWatcher;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

public class MetaDataUtil {
	
	/**
	 * Get the DataWatcher of an animal
	 * 
	 * @param type			Entity type
	 * @param extras		List fo extras
	 * 
	 * @return				DataWatcher
	 */
	public static DataWatcher getDataWatcher(EntityType type, List<String> extras) {
		DataWatcher tmp = new DataWatcher();
		boolean changeAge = true;
		
		tmp.a(0, Byte.valueOf((byte) 0));
		
		//bat fix
		if(type == EntityType.BAT)
			tmp.a(16, Byte.valueOf((byte) -2));
		
		for(String extra : extras) {
			if(extra.equalsIgnoreCase("-baby")) {
				if(type.toString().contains("ZOMBIE"))
					tmp.a(12, Byte.valueOf((byte) -23999));
				else
					tmp.a(12, Integer.valueOf((int) -23999));
				changeAge = false;
			}
			
			if(extra.equalsIgnoreCase("-charged") && type == EntityType.CREEPER) {
				tmp.a(17, Byte.valueOf((byte) 1));
			}
			
			if(extra.toLowerCase().startsWith("-item:") && type == EntityType.ENDERMAN) {
				try {
					String item = extra.split(":")[1];
					Material mat = Material.getMaterial(Integer.valueOf(item));
					tmp.a(16, Byte.valueOf((byte) mat.getId()));
				} catch(Exception e) {}
			}
			
			if(type == EntityType.SHEEP) {
				SheepColor color = getColor(extra.replace("-", ""));
				if(color != null) {
					tmp.a(16, Byte.valueOf((byte) color.getId()));
				}
			}
			
			if(extra.equalsIgnoreCase("-sitting") && (type == EntityType.WOLF || type == EntityType.OCELOT)) {
				tmp.a(16, Byte.valueOf((byte) 1));
			}
			
			if(extra.equalsIgnoreCase("-angry") && type == EntityType.WOLF) {
				tmp.a(16, Byte.valueOf((byte) 2));
			}
			
			if(extra.equalsIgnoreCase("-tamed") && (type == EntityType.WOLF || type == EntityType.OCELOT)) {
				tmp.a(16, Byte.valueOf((byte) 4));
				if(type == EntityType.OCELOT)
					tmp.a(18, Byte.valueOf((byte) 3));
			}
			
			if(extra.equalsIgnoreCase("-saddled") && type == EntityType.PIG) {
				tmp.a(16, Byte.valueOf((byte) 1));
			}
			
			if(extra.toLowerCase().startsWith("-health:") && (type == EntityType.ENDER_DRAGON || type == EntityType.WITHER)) {
				try {
					int health = Integer.valueOf(extra.split(":")[1]);
					
					//handle max and min health
					if(health > 200) health = 200;
					if(health < 0) health = 0;
					
					tmp.a(16, Integer.valueOf((int) health));
				} catch(Exception e) {}
			}
		}
		
		//Zombie & PigZombie fix
		if(changeAge) {
			if(type.toString().contains("ZOMBIE"))
				tmp.a(12, Byte.valueOf((byte) 0));
			else
				tmp.a(12, Integer.valueOf((int) 0));
		}
		
		return tmp;
	}
	
	private static SheepColor getColor(String color) {
		try {
			return SheepColor.valueOf(color.toUpperCase());
		} catch(Exception e) {
			return null;
		}
	}
	
	private static enum SheepColor {
		WHITE(0),
		ORANGE(1),
		MAGENTA(2),
		LIGHTBLUE(3),
		YELLOW(4),
		LIME(5),
		PINK(6),
		GRAY(7),
		SILVER(8),
		CYAN(9),
		PURPLE(10),
		BLUE(11),
		BROWN(12),
		GREEN(13),
		RED(14),
		BLACK(15);
		
		private int id;
		
		SheepColor(int id) {
			this.id = id;
		}
		
		public int getId() {
			return this.id;
		}
	}
}
