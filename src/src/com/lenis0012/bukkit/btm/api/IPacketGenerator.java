package src.com.lenis0012.bukkit.btm.api;

import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import src.com.lenis0012.bukkit.btm.nms.wrappers.Packet;


/**
 * PacketGenerator interface
 * 
 * @author lenis0012
 */
public interface IPacketGenerator {

	/**
	 * Get the mob spawn packet for a disguise
	 * 
	 * @return Packet
	 */
	public Packet getMobSpawnPacket();
	
	/**
	 * Get the player spawn packet for a disguise
	 * 
	 * @return Packet
	 */
	public Packet getNamedEntitySpawnPacket();
	
	/**
	 * Get the vehicle spawn packet for a disguise
	 * 
	 * @return Packet
	 */
	public Packet getVehicleSpawnPacket();
	
	/**
	 * Get the entity look packet for a disguise
	 * 
	 * @return
	 */
	public Packet getEntityLookPacket();
	
	/**
	 * Get the head rotation packet for an entity
	 * 
	 * @return  Packet
	 */
	public Packet getEntityHeadRotatePacket();
	
	/**
	 * Get the entity destroy packet
	 * 
	 * @param EntityId entity id
	 * @return Packet
	 */
	public Packet getDestroyEntityPacket();
	
	/**
	 * Get the entity status packet
	 * 
	 * @param status Status value
	 * @return Packet
	 */
	public Packet getEntityStatusPacket(byte status);
	
	/**
	 * Get the entity movement packet
	 * 
	 * @param movement Movement changes
	 * @return Packet
	 */
	public Packet getEntityMoveLookPacket(Movement movement);
	/**
	 * Get the entity set velocity packet
	 * @param Velocity - The entitie's velocity
	 * @return Packet
	 */
	public Packet getEntityVelocityPacket(Vector velocity);
	/**
	 * Get the arm animation packet
	 * 
	 * @param animation Animation id
	 * @return Packet
	 */
	public Packet getArmAnimationPacket(int animation);
	
	/**
	 * Get the block break packet
	 * 
	 * @param block Block to be broken
	 * @return Packet
	 */
	public Packet getBlockBreakAnimationPacket(Block block);
	
	/**
	 * Get the entity teleport packet
	 * 
	 * @return Packet
	 */
	public Packet getEntityTeleportPacket();
	
	/**
	 * Get the entity metadata packet
	 * 
	 * @return Packet
	 */
	public Packet getEntityMetadataPacket();
	
	/**
	 * Get the entity equipment packet
	 * 
	 * @param slot Slot id
	 * @param item Item
	 * @return Packet
	 */
	public Packet getEntityEquipmentPacket(int slot, ItemStack item);
}