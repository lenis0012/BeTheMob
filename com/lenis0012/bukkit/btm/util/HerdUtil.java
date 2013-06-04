package src.com.lenis0012.bukkit.btm.util;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

import com.dylanisawesome1.bukkit.btm.Herds.HerdEntity;

public class HerdUtil {
	/**
	 * Get a herd entity from minecraft's given id.
	 * @param id - The id of the entity
	 * @param herd - The array of herd entities
	 */
	public static HerdEntity getHerdEntityFromId(int id, ArrayList<HerdEntity> herd) {
		HerdEntity entity = null;
		for(HerdEntity e : herd) {
			if(e.getEntityId() == id) {
				entity = e;
			}
		}
		return entity;
	}
	public static int getDamageDealt(ItemStack item) {
		switch(item.getType()) {
		case DIAMOND_SWORD: 
			return 7;
		case DIAMOND_AXE: 
			return 6;
		case DIAMOND_PICKAXE: 
			return 5;
		case DIAMOND_SPADE: 
			return 4;
		case IRON_SWORD: 
			return 6;
		case IRON_AXE: 
			return 5;
		case IRON_PICKAXE: 
			return 4;
		case IRON_SPADE: 
			return 3;
		case STONE_SWORD: 
			return 5;
		case STONE_AXE: 
			return 4;
		case STONE_PICKAXE: 
			return 3;
		case STONE_SPADE: 
			return 2;
		case WOOD_SWORD: 
			return 4;
		case GOLD_SWORD: 
			return 4;
		case WOOD_AXE: 
			return 3;
		case GOLD_AXE: 
			return 3;
		case WOOD_PICKAXE: 
			return 2;
		case GOLD_PICKAXE: 
			return 2;
		case WOOD_SPADE: 
			return 1;
		case GOLD_SPADE: 
			return 1;
		case ARROW:
			return 1;
			//minimum amount of dmg a bow can do.
		case FIRE:
			return 1;
		case LAVA:
			return 4;
		case TNT:
			return 24;
		default:
			return 1;
			
		}
	}
}