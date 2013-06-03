package com.lenis0012.bukkit.btm.nms;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

import com.dylanisawesome1.bukkit.btm.Herds.HerdEntity;
import com.lenis0012.bukkit.btm.api.Disguise;
import com.lenis0012.bukkit.btm.api.IPacketGenerator;
import com.lenis0012.bukkit.btm.api.Movement;
import com.lenis0012.bukkit.btm.nms.wrappers.Packet;
import com.lenis0012.bukkit.btm.util.ColorUtil;
import com.lenis0012.bukkit.btm.util.CraftItemStack;
import com.lenis0012.bukkit.btm.util.MathUtil;

public class PacketGenerator implements IPacketGenerator {
	private Disguise dis;
	private HerdEntity ent;

	public PacketGenerator(Disguise dis) {
		this.dis = dis;
	}

	public PacketGenerator(HerdEntity ent) {
		this.ent = ent;
	}

	/**
	 * Slot List: 0 - item in hand 1 - boots 2 - leggings 3 - chestplate 4 -
	 * helmet
	 * 
	 * @param slot
	 *            The slot to update
	 * @return Packet
	 */
	@Override
	public Packet getEntityEquipmentPacket(int slot, ItemStack item) {
		Packet packet = new Packet("Packet5EntityEquipment");
		if (dis != null) {
			packet.write("a", dis.getEntityId());
		} else {
			packet.write("a", ent.getEntityId());
		}
		packet.write("b", slot);
		packet.write("c", CraftItemStack.asNMSCopy(item));
		return packet;
	}

	@Override
	public Packet getEntityHeadRotatePacket() {
		Packet packet = new Packet("Packet35EntityHeadRotation");
		byte yaw;
		if (dis != null) {
			yaw = this.getByteFromDegree(dis.getLocation().getYaw());
			packet.write("a", dis.getEntityId());
		} else {
			yaw = this.getByteFromDegree(ent.getLocation().getYaw());
			packet.write("a", ent.getEntityId());
		}
		packet.write("b", yaw);
		return packet;
	}

	@Override
	public Packet getDestroyEntityPacket() {
		Packet packet = new Packet("Packet29DestroyEntity");

		if (dis != null) {
			packet.write("a", new int[] { dis.getEntityId() }); // The field 'a'
																// is actually a
																// array!
		} else {
			packet.write("a", new int[] { ent.getEntityId() });
		}
		return packet;
	}

	/**
	 * 2 - entity hurt 3 - entity killed 6 - wolf taming 7 - wolf tamed 8 - wolf
	 * shaking water 9 - Player allowed to eat 10 - sheep eating grass
	 * 
	 * @param status
	 *            The status
	 * 
	 * @return Packet
	 * 
	 */
	@Override
	public Packet getEntityStatusPacket(byte status) {
		Packet packet = new Packet("Packet38EntityStatus");
		if (dis != null) {
			packet.write("a", dis.getEntityId());
		} else {
			packet.write("a", ent.getEntityId());
		}
		packet.write("b", status);
		return packet;
	}

	@Override
	public Packet getEntityLookPacket() {
		Packet packet = new Packet("Packet32EntityLook");
		byte yaw;
		byte pitch;

		if (dis != null) {
			yaw = getByteFromDegree(dis.getPlayer().getLocation().getYaw());
			pitch = getByteFromDegree(dis.getPlayer().getLocation().getPitch());
			if (dis.getDisguiseType() == EntityType.ENDER_DRAGON) {
				yaw = (byte) (yaw - 128);
			}
			if (dis.getDisguiseType() == EntityType.CHICKEN) {
				pitch = (byte) (pitch * -1);
			}

			packet.write("a", dis.getEntityId());
		} else {
			yaw = getByteFromDegree(ent.getLocation().getYaw());
			pitch = getByteFromDegree(ent.getLocation().getPitch());
			if (ent.getDisguiseType() == EntityType.ENDER_DRAGON) {
				yaw = (byte) (yaw - 128);
			}
			if (dis.getDisguiseType() == EntityType.CHICKEN) {
				pitch = (byte) (pitch * -1);
			}

			packet.write("a", ent.getEntityId());

		}
		packet.write("e", yaw);
		packet.write("f", pitch);
		return packet;
	}

	@Override
	public Packet getEntityMoveLookPacket(Movement movement) {
		Packet packet = new Packet("Packet33RelEntityMoveLook");
		Location loc;
		if (dis != null) {
			loc = dis.getLocation();
		} else {
			loc = ent.getLocation();
		}
		byte x = (byte) movement.x;
		byte y = (byte) movement.y;
		byte z = (byte) movement.z;
		byte yaw = getByteFromDegree(loc.getYaw());
		byte pitch = getByteFromDegree(loc.getPitch());
		if (dis != null) {
			if (dis.getDisguiseType() == EntityType.ENDER_DRAGON) {
				yaw = (byte) (yaw - 128);
			}
			if (dis.getDisguiseType() == EntityType.CHICKEN) {
				pitch = (byte) (pitch * -1);
			}
		} else {
			if (ent.getDisguiseType() == EntityType.ENDER_DRAGON) {
				yaw = (byte) (yaw - 128);
			}
			if (ent.getDisguiseType() == EntityType.CHICKEN) {
				pitch = (byte) (pitch * -1);
			}
		}
		if (x > 128 || x < -128 || y > 128 || y < -128 || z > 128 || z < -128)
			return getEntityTeleportPacket();

		if (dis != null) {
			packet.write("a", dis.getEntityId());
		} else {
			packet.write("a", ent.getEntityId());
		}
		packet.write("b", x);
		packet.write("c", y);
		packet.write("d", z);
		packet.write("e", yaw);
		packet.write("f", pitch);
		packet.write("g", true);

		return packet;
	}

	@Override
	public Packet getEntityTeleportPacket() {
		Packet packet = new Packet("Packet34EntityTeleport");
		Location to;
		if (dis != null) {
			to = dis.getLocation();
		} else {
			to = ent.getLocation();
		}
		int x = MathUtil.floor(to.getX() * 32.0D);
		int y = MathUtil.floor(to.getY() * 32.0D);
		int z = MathUtil.floor(to.getZ() * 32.0D);
		byte yaw = getByteFromDegree(to.getYaw());
		byte pitch = getByteFromDegree(to.getPitch());

		if (dis != null) {
			if (dis.getDisguiseType() == EntityType.ENDER_DRAGON) {
				yaw = (byte) (yaw - 128);
			}
			if (dis.getDisguiseType() == EntityType.CHICKEN) {
				pitch = (byte) (pitch * -1);
			}
			packet.write("a", dis.getEntityId());
		} else {
			if (ent.getDisguiseType() == EntityType.ENDER_DRAGON) {
				yaw = (byte) (yaw - 128);
			}
			if (ent.getDisguiseType() == EntityType.CHICKEN) {
				pitch = (byte) (pitch * -1);
			}
			packet.write("a", ent.getEntityId());

		}
		packet.write("b", x);
		packet.write("c", y);
		packet.write("d", z);
		packet.write("e", yaw);
		packet.write("f", pitch);
		return packet;
	}

	@Override
	public Packet getNamedEntitySpawnPacket() {
		Packet packet = new Packet("Packet20NamedEntitySpawn");
		Location loc = dis.getLocation();
		int x = MathUtil.floor(loc.getX() * 32.0D);
		int y = MathUtil.floor(loc.getY() * 32.0D);
		int z = MathUtil.floor(loc.getZ() * 32.0D);
		byte yaw = getByteFromDegree(loc.getYaw());
		byte pitch = getByteFromDegree(loc.getPitch());

		if (dis != null) {
			packet.write("a", dis.getEntityId());
			packet.write("b", ColorUtil.fixColors(dis.getCustomName()));
		} else {
			packet.write("a", ent.getEntityId());
			packet.write("b", ColorUtil.fixColors(ent.getName()));
		}
		packet.write("c", x);
		packet.write("d", y);
		packet.write("e", z);
		packet.write("f", yaw);
		packet.write("g", pitch);
		if (dis != null) {
		packet.write("h", dis.getPlayer().getItemInHand().getTypeId());
		} else {
			packet.write("h", ent.getItemInHand());
		}
		if (dis != null) {
			packet.setDataWatcher(dis.getDataWatcher());
		} else {
			packet.setDataWatcher(ent.getDatawatcher());
		}
		// packet.write("i", dis.getDataWatcher().getHandle());

		return packet;
	}

	@Override
	public Packet getMobSpawnPacket() {
		// Create packet
		Packet packet = new Packet("Packet24MobSpawn");
		Location loc;
		EntityType type;
		if (dis != null) {
			loc = dis.getLocation();
			type = dis.getDisguiseType();
		} else {
			loc = ent.getLocation();
			type = ent.getDisguiseType();
		}
		int x = MathUtil.floor(loc.getX() * 32.0D);
		int y = MathUtil.floor(loc.getY() * 32.0D);
		int z = MathUtil.floor(loc.getZ() * 32.0D);
		byte yaw = getByteFromDegree(loc.getYaw());
		byte pitch = getByteFromDegree(loc.getPitch());

		if (type == EntityType.ENDER_DRAGON) {
			yaw = (byte) (yaw - 128);
		}
		if (type == EntityType.CHICKEN) {
			pitch = (byte) (pitch * -1);
		}

		// Fill the packet with data
		if (dis != null) {
			packet.write("a", dis.getEntityId());
		} else {
			packet.write("a", ent.getEntityId());
		}
		packet.write("b", type.getTypeId());
		packet.write("c", x);
		packet.write("d", y);
		packet.write("e", z);
		packet.write("i", yaw);
		packet.write("j", pitch);
		packet.write("k", yaw);
		packet.write("f", 0);
		packet.write("g", 0);
		packet.write("h", 0);
		if (dis != null) {
			packet.setDataWatcher(dis.getDataWatcher());
		} else {
			packet.setDataWatcher(ent.getDatawatcher());
		}
		// packet.write("t", dis.getDataWatcher().getHandle());

		return packet;
	}

	@Override
	public Packet getVehicleSpawnPacket() {
		// Creae packet
		Packet packet = new Packet("Packet23VehicleSpawn");
		Location loc = dis.getLocation();
		EntityType type = dis.getDisguiseType();

		// Generate info
		int x = MathUtil.floor(loc.getX() * 32.0D);
		int y = MathUtil.floor(loc.getY() * 32.0D);
		int z = MathUtil.floor(loc.getZ() * 32.0D);
		int yaw = MathUtil.floor(loc.getYaw());
		int pitch = MathUtil.floor(loc.getPitch());

		// Write data to packet
		packet.write("a", dis.getEntityId());
		packet.write("j", type.getTypeId());
		packet.write("b", x);
		packet.write("c", y);
		packet.write("d", z);
		packet.write("h", yaw);
		packet.write("i", pitch);
		packet.write("e", 0);
		packet.write("f", 0);
		packet.write("g", 0);

		return packet;
	}

	@Override
	public Packet getArmAnimationPacket(int animation) {
		Packet packet = new Packet("Packet18ArmAnimation");
		if (dis != null) {
			packet.write("a", dis.getEntityId());
		} else {
			packet.write("a", ent.getEntityId());
		}
		packet.write("b", animation);
		return packet;
	}

	@Override
	public Packet getBlockBreakAnimationPacket(Block block) {
		Packet packet = new Packet("Packet55BlockBreakAnimation");
		int damage = block.getState().getRawData();
		int x = block.getX();
		int y = block.getY();
		int z = block.getZ();
		if (dis != null) {
		packet.write("a", dis.getEntityId());
		} else {
			packet.write("a", ent.getEntityId());
		}
		packet.write("b", x);
		packet.write("c", y);
		packet.write("d", z);
		packet.write("e", damage);
		return packet;
	}

	@Override
	public Packet getEntityMetadataPacket() {
		Packet packet = new Packet("Packet40EntityMetadata");

		if (dis != null) {
			packet.write("a", dis.getEntityId());
			packet.write("b", dis.getDataWatcher().getAll());
		} else {
			packet.write("a", ent.getEntityId());
			packet.write("b", ent.getDatawatcher().getAll());
		}

		return packet;
	}

	/*
	 * From CraftBukkit - Packet24MobSpawn.java line 31 - 33
	 * 
	 * All credits go to CraftBukkit
	 */
	private byte getByteFromDegree(float degree) {
		return (byte) (int) (degree * 256.0F / 360.0F);
	}
}