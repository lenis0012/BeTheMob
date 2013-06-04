package src.com.lenis0012.bukkit.btm.fun;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.lenis0012.bukkit.btm.api.Disguise;

/**
 * @author bowser123467
 *
 */
public class DropFactory implements IDropFactory {
	
	private Random random;
	
	public DropFactory() {
		random = new Random();
	}
	
	@Override
	public Set<ItemStack> getDrops(Disguise disguise) {
		Set<ItemStack> items = new HashSet<ItemStack>();
		if(disguise.isPlayer()) {
			if(disguise.getCustomName().equalsIgnoreCase("Notch")) {
				items.add(new ItemStack(Material.APPLE, 1));
			}else {
				Player pl = Bukkit.getPlayer(disguise.getCustomName());
				if(pl != null) {
					for(ItemStack i : pl.getInventory()) {
						items.add(i);
					}
				}
			}
		} else {
			EntityType type = disguise.getDisguiseType();
			if(type == EntityType.BAT) {
				//No drops
			} else if(type == EntityType.BLAZE) {
				if(random.nextInt(3) == 0)
					items.add(new ItemStack(Material.BLAZE_ROD, 1));
	    	} else if(type == EntityType.CAVE_SPIDER || type == EntityType.SPIDER) {
	    		if(random.nextInt(3) == 0)
	    			items.add(new ItemStack(Material.STRING, 1+random.nextInt(2)));
	    		if(random.nextInt(3) == 0)
	    			items.add(new ItemStack(Material.SPIDER_EYE, 1));
	    	} else if(type == EntityType.CHICKEN) {
	    	} else if(type == EntityType.COW || type == EntityType.MUSHROOM_COW) {
	    	} else if(type == EntityType.CREEPER) {
	    	} else if(type == EntityType.ENDER_DRAGON) {
	    	} else if(type == EntityType.ENDERMAN) {
	    	} else if(type == EntityType.GHAST) {
	    	} else if(type == EntityType.IRON_GOLEM) {
	    	} else if(type == EntityType.MAGMA_CUBE || type == EntityType.SLIME) {
	    	} else if(type == EntityType.OCELOT) {
	    	} else if(type == EntityType.PIG) {
	    	} else if(type == EntityType.PIG_ZOMBIE) {
	    	} else if(type == EntityType.SHEEP) {
	    	} else if(type == EntityType.SILVERFISH) {
	    	} else if(type == EntityType.SKELETON) {
	    	} else if(type == EntityType.WITHER) {
	    	} else if(type == EntityType.ZOMBIE) {
	    	} else {
	    		return null;
	    	}
		}
		return items;
	}
	
	@Override
	public String getName() {
		return "BTM Drop Manager";
	}
}