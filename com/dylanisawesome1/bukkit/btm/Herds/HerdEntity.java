package com.dylanisawesome1.bukkit.btm.Herds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.dylanisawesome1.bukkit.btm.Herds.Pathfinding.BlockLoader;
import com.lenis0012.bukkit.btm.BeTheMob;
import com.lenis0012.bukkit.btm.api.IPacketGenerator;
import com.lenis0012.bukkit.btm.api.Movement;
import com.lenis0012.bukkit.btm.nms.PacketGenerator;
import com.lenis0012.bukkit.btm.nms.wrappers.DataWatcher;
import com.lenis0012.bukkit.btm.util.MetaDataUtil;
import com.lenis0012.bukkit.btm.util.NetworkUtil;

public class HerdEntity {
	private boolean isPlayer = false;
	private Location loc;
	private String name;
	private int itemInHand;
	private EntityType type;
	private int EntityID;
	private boolean spawned = false;
	private Movement movement;
	private DataWatcher dw;
	BlockLoader loader;
	private List<String> extras = new ArrayList<String>();
	private ArrayList<Location> destinations = new ArrayList<Location>();
	//Extras is currently unimplemented
	private IPacketGenerator gen;
	public int health;
	public HerdEntity(int EntityID, Location loc, String name, int itemInHand, EntityType type) {
		this.setEntityID(EntityID);
		this.setLocation(loc);
		this.setName(name);
		this.setItemInHand(itemInHand);
		initHealth();
		loader = new BlockLoader(getLocation());
		loader.loadBlocks(20);
		gen = new PacketGenerator(this);
	}
	public HerdEntity(int EntityID, Location loc, int itemInHand, EntityType type) {
		this.setEntityID(EntityID);
		this.setLocation(loc);
		this.setItemInHand(itemInHand);
		this.type=type;
		initHealth();
		loader = new BlockLoader(getLocation());
		loader.loadBlocks(20);
		gen = new PacketGenerator(this);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getEntityId() {
		return EntityID;
	}
	public void setEntityID(int entityID) {
		EntityID = entityID;
	}
	public EntityType getType() {
		return type;
	}
	public void setType(EntityType type) {
		this.type = type;
	}
	public Movement getMovement() {
		return movement;
	}
	public void setMovement(Movement movement) {
		this.movement = movement;
	}
	public void setItemInHand(int itemInHand) {
		this.itemInHand = itemInHand;
	}
	public boolean isPlayer() {
		return isPlayer;
	}
	public void setPlayer(boolean isPlayer) {
		this.isPlayer = isPlayer;
	}
	public Location getLocation() {
		return loc;
	}
	public void setLocation(Location loc) {
		this.loc = loc;
	}
	public DataWatcher getDatawatcher() {
		return dw;
	}
	public void setDatawatcher(DataWatcher dw) {
		this.dw = dw;
	}	/**
	 * Spawn the entity for a player
	 * 
	 * @param player Player to recive spawn packet
	 */
	public void spawn(Player player) {
		if(this.spawned) {
			if(isPlayer) {
				NetworkUtil.sendPacket(gen.getNamedEntitySpawnPacket(), player);
			} else{
				NetworkUtil.sendPacket(gen.getMobSpawnPacket(), player);
			}
		}
	}
	
	/**
	 * Spawn the entity for a world
	 * 
	 * @param world World to recive packet
	 */
	public void spawn(World world) {
		if(isPlayer) {
			dw = new DataWatcher();
			dw.set(0, Byte.valueOf((byte) 0));
			dw.set(12, Integer.valueOf((int) 0));
			NetworkUtil.sendGlobalPacket(gen.getNamedEntitySpawnPacket(), world);
		} else{
			dw = MetaDataUtil.getDataWatcher(type, extras);
			NetworkUtil.sendGlobalPacket(gen.getMobSpawnPacket(), world);
		}
		this.spawned = true;
	}
	
	/**
	 * Despawn the entity
	 */
	public void despawn() {
		World world = loc.getWorld();
		NetworkUtil.sendGlobalPacket(gen.getDestroyEntityPacket(), world);
		this.spawned = false;
	}
	
	/**
	 * Kill the entity with the normal fall over animation
	 * (Warning kills and despawns entity)
	 */
	public void kill() {
		NetworkUtil.sendGlobalPacket(gen.getEntityStatusPacket((byte) 3), loc.getWorld());
	}
	
	/**
	 * 'Ignites' the disguise
	 */
	public void ignite(){
		dw.set(0, Byte.valueOf((byte) 1));
		updateMetaData();
	}
	
	/**
	 * Stops fire on the entity
	 */
	public void extinguish(){
		dw.set(0, Byte.valueOf((byte) 0));
		updateMetaData();
	}
	
	
	/**
	 * Despawn the disguise in a custom world
	 * 
	 * @param world	World to despawn disguise
	 */
	public void despawn(World world) {
		NetworkUtil.sendGlobalPacket(gen.getDestroyEntityPacket(), world);
		this.spawned = false;
	}
	
	/**
	 * Unload the disguise for a player
	 * 
	 * @param player Player to unload disguise for
	 */
	public void unLoadFor(Player player) {
		NetworkUtil.sendPacket(gen.getDestroyEntityPacket(), player);
	}
	
	/**
	 * @param item Item to change the currently held item to
	 * slot 0 is the item in hand
	 */
	public void changeItem(int new_slot, org.bukkit.inventory.ItemStack item) {
		org.bukkit.inventory.ItemStack bukkitstack = null;
		if(item == null){
			bukkitstack = new org.bukkit.inventory.ItemStack(Material.AIR, 1);
		}else{
			bukkitstack = item;
		}
		this.itemInHand = bukkitstack.getTypeId();
		NetworkUtil.sendGlobalPacket(gen.getEntityEquipmentPacket(0, bukkitstack), loc.getWorld());
	}
	
	/**
	 * @param armor What type of armor to put on entity
	 * @param slot Which armour slot
	 * 1 boots
	 * 2 leggings
	 * 3 chestplate
	 * 4 helmet
	 */
	public void changeArmor(int slot, org.bukkit.inventory.ItemStack armor) {
		org.bukkit.inventory.ItemStack bukkitstack = armor;
		
		if(bukkitstack == null){
			bukkitstack = new org.bukkit.inventory.ItemStack(Material.AIR);
		}
		NetworkUtil.sendGlobalPacket(gen.getEntityEquipmentPacket(slot, bukkitstack), loc.getWorld());
	}
	
	/**
	 * Move the disguise to a new location
	 * 
	 * @param event Event to get moving from
	 */
	public void move(Location from, Location to, boolean moved) {
		if(!spawned)
			return;
		
		World world = to.getWorld();
		this.loc = to;
		
		if(moved) {
			movement.update(to);
			NetworkUtil.sendGlobalPacket(gen.getEntityMoveLookPacket(movement), world);
		} else
			NetworkUtil.sendGlobalPacket(gen.getEntityLookPacket(), world);
		NetworkUtil.sendGlobalPacket(gen.getEntityHeadRotatePacket(), world);
	}
	
	/**
	 * Teleport the entity
	 * 
	 * @param loc Location to teleport to
	 */
	public void teleport(Location loc) {
		this.loc = loc;
		NetworkUtil.sendGlobalPacket(gen.getEntityTeleportPacket(), loc.getWorld());
		this.movement = new Movement(loc);
	}
	
	/**
	 * Swing the arm of the entity
	 */
	public void swingArm() {
		NetworkUtil.sendGlobalPacket(gen.getArmAnimationPacket(1), loc.getWorld());
	}
	
	/**
	 * Let the entity damage a block
	 * 
	 * @param block	Block to damage
	 */
	public void damageBlock(Block block) {
		NetworkUtil.sendGlobalPacket(gen.getBlockBreakAnimationPacket(block), loc.getWorld());
	}
	
	/**
	 * Notify the entity got damaged
	 */
	public void damage() {
		NetworkUtil.sendGlobalPacket(gen.getArmAnimationPacket(2), loc.getWorld());
	}
	/**
	 * Make the entity fly backwards
	 */
	public void knockback() {
		short velx = 0;
		short vely = 5;
		short velz = 5;
		loc.setY(loc.getY()+5);
		loc.setZ(loc.getZ()+5);
		NetworkUtil.sendGlobalPacket(gen.getEntityVelocityPacket(new Vector(velx, vely, velz)), getLocation().getWorld());
	}
	
	/**
	 * Make the entity crouch
	 */
	public void crouch() {
		if(this.isPlayer) {
			byte b = dw.getByte(0);
			dw.watch(0, Byte.valueOf((byte)(b | 1 << 1)));
			this.updateMetaData();
		}
	}
	
	/**
	 * Make the entity uncrouch
	 */
	public void uncrouch() {
		if(this.isPlayer) {
			byte b = dw.getByte(0);
			dw.watch(0, Byte.valueOf((byte)(b & (1 << 1 ^ 0xFFFFFFFF))));
			this.updateMetaData();
		}
	}
	
	public void updateMetaData() {
		NetworkUtil.sendGlobalPacket(gen.getEntityMetadataPacket(), loc.getWorld());
	}
	
	/**
	 * Refresh the movement of the entity
	 */
	public void refreshMovement() {
		this.movement = new Movement(loc);
	}
	
	/**
	 * Get the type of the entity
	 */
    public EntityType getDisguiseType() {
    	return type;
    }
    
    /**
     * Get the packet generator
     * 
     * @return Packet geneator
     */
    public IPacketGenerator getPacketGenerator() {
    	return this.gen;
    }

    /**
     * Get the item in the players hand
     * 
     * @return Item in hand
     */
	public int getItemInHand() {
		return itemInHand;
	}
    
    public void playHurtSound() {
        if(BeTheMob.instance.getConfig().getBoolean("play_sound_on_hurt_herd")) {
				getWorld().playSound(getLocation(), getHurtSound(), 1, 1);
        }
    
    }
    
    private World getWorld() {
		return getLocation().getWorld();
	}
	/**
     * Gets the correct damaged sound
     * If the entity is a zombie it will be a zombie one
     * Etc.
     */
    public Sound getHurtSound() {
    	Random random = new Random();
    	if(type == EntityType.BAT){
    		return Sound.BAT_HURT;
    	} else if(type == EntityType.BLAZE){
    		return Sound.BLAZE_HIT;
    	} else if(type == EntityType.CAVE_SPIDER || type == EntityType.SPIDER){
    		return Sound.SPIDER_DEATH;// Spider death is hurt
    	}else if(type == EntityType.CHICKEN){
    		return Sound.CHICKEN_HURT;
    	}else if(type == EntityType.COW || type == EntityType.MUSHROOM_COW){
    		return Sound.COW_HURT;
    	}else if(type == EntityType.CREEPER){
    		return Sound.CREEPER_HISS;
    	}else if(type == EntityType.ENDER_DRAGON){
    		return Sound.ENDERDRAGON_HIT;
    	}else if(type == EntityType.ENDERMAN){
    		return Sound.ENDERMAN_HIT;
    	}else if(type == EntityType.GHAST){
    		return random.nextBoolean() ? Sound.GHAST_SCREAM : Sound.GHAST_SCREAM2;
    	}else if(type == EntityType.IRON_GOLEM){
    		return Sound.IRONGOLEM_HIT;
    	}else if(type == EntityType.MAGMA_CUBE || type == EntityType.SLIME){
    		return random.nextBoolean() ? Sound.SLIME_WALK : Sound.SLIME_WALK2;
    	}else if(type == EntityType.OCELOT){
    		return random.nextBoolean() ? Sound.CAT_HIT : Sound.CAT_HISS;
    	}else if(type == EntityType.PIG){
    		return Sound.PIG_DEATH;
    	} else if(type == EntityType.PIG_ZOMBIE){
    		return Sound.ZOMBIE_PIG_HURT;
    	}else if(isPlayer){
    		return Sound.HURT_FLESH;
    	}else if(type == EntityType.SHEEP){
    		return Sound.SHEEP_IDLE;
    	}else if(type == EntityType.SILVERFISH){
    		return Sound.SILVERFISH_HIT;
    	}else if(type == EntityType.SKELETON){
    		return Sound.SKELETON_HURT;
    	}else if(type == EntityType.WITHER){
    		return Sound.WITHER_HURT;
    	}else if(type == EntityType.ZOMBIE){
    		return Sound.ZOMBIE_HURT;
    	}
    	
    	return null;
    }
	public boolean isHumanoid() {
		return isPlayer() || getType() == EntityType.SKELETON
				|| getType() == EntityType.ZOMBIE
				|| getType() == EntityType.PIG_ZOMBIE;
	}
	public boolean canHoldBlocks() {
		return isHumanoid() || getType() == EntityType.ENDERMAN;
	}
	public void update() {
	}
	public void initHealth() {
    	if(type == EntityType.BAT){
    		health = 6;
    	} else if(type == EntityType.BLAZE){
    		health = 20;
    	} else if(type == EntityType.CAVE_SPIDER){
    		health = 12;
    	} else if(type == EntityType.SPIDER) {
    		health = 16;
    	}
    	else if(type == EntityType.CHICKEN){
    		health = 4;
    	}else if(type == EntityType.COW || type == EntityType.MUSHROOM_COW){
    		health = 10;
    	}else if(type == EntityType.CREEPER){
    		health = 20;
    	}else if(type == EntityType.ENDER_DRAGON){
    		health = 200;
    	}else if(type == EntityType.ENDERMAN){
    		health = 40;
    	}else if(type == EntityType.GHAST){
    		health = 10;
    	}else if(type == EntityType.IRON_GOLEM){
    		health = 100;
    	}else if(type == EntityType.MAGMA_CUBE || type == EntityType.SLIME){
    		health = 16;
    				//default health for a big slime
    	}else if(type == EntityType.OCELOT){
    		health = 10;
    	}else if(type == EntityType.PIG){
    		health = 10;
    	} else if(type == EntityType.PIG_ZOMBIE){
    		health = 20;
    	}else if(isPlayer){
    		health = 20;
    	}else if(type == EntityType.SHEEP){
    		health = 8;
    	}else if(type == EntityType.SILVERFISH){
    		health = 8;
    	}else if(type == EntityType.SKELETON){
    		health = 20;
    	}else if(type == EntityType.WITHER){
    		health = 300;
    	}else if(type == EntityType.ZOMBIE){
    		health = 20;
    	}
		
	}
}
