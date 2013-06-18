package src.com.lenis0012.bukkit.btm;

import java.util.Set;

import net.minecraft.server.v1_5_R3.DamageSource;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;

import src.com.dylanisawesome1.bukkit.btm.Herds.Herd;
import src.com.dylanisawesome1.bukkit.btm.Herds.HerdEntity;
import src.com.lenis0012.bukkit.btm.api.Disguise;
import src.com.lenis0012.bukkit.btm.events.HerdEntityInteractEvent;
import src.com.lenis0012.bukkit.btm.nms.PlayerConnectionCallback;
import src.com.lenis0012.bukkit.btm.util.HerdUtil;


public class BTMListener implements Listener {
	@EventHandler (priority=EventPriority.MONITOR)
	public void onHerdEntityInteractedWith(HerdEntityInteractEvent evt) {
		if(evt.isCancelled())
			return;
		if(evt.getAction()==1) {
			//left click
			evt.getEntityInteractedWith().damage();
			evt.getEntityInteractedWith().playHurtSound();
			evt.getEntityInteractedWith().knockback();
			evt.getEntityInteractedWith().health-=HerdUtil.getDamageDealt(evt.getPlayer().getItemInHand());
			if(evt.getEntityInteractedWith().health<=0) {
				evt.getEntityInteractedWith().kill();
			}
		} else {
			if(evt.getEntityInteractedWith().getLeader().getName() == evt.getPlayer().getName()) {
					BeTheMob.instance.selectedentities.put(evt.getPlayer().getName(), evt.getEntityInteractedWith());
			}
			if(evt.getEntityInteractedWith().getType() == EntityType.COW && evt.getPlayer().getItemInHand().getType() == Material.BUCKET) {
				evt.getPlayer().getItemInHand().setType(Material.MILK_BUCKET);
			}
			if(evt.getEntityInteractedWith().getType() == EntityType.MAGMA_CUBE && evt.getPlayer().getItemInHand().getType() == Material.BUCKET) {
				evt.getPlayer().getItemInHand().setType(Material.LAVA_BUCKET);
				//Lawl :D
			}
		}
	}
	@EventHandler (priority=EventPriority.MONITOR)
	public void onCombust(final EntityCombustEvent event) {
		if(event.isCancelled())
			return;
		
		if(event.getEntityType() == EntityType.PLAYER){
			final Player player = (Player) event.getEntity();
			final String name = player.getName();
			final BeTheMob plugin = BeTheMob.instance;
			
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

				@Override
				public void run() {
					if(plugin.disguises.containsKey(name)) {
						final Disguise dis = plugin.disguises.get(name);
						dis.ignite();
						Bukkit.getScheduler().runTaskLaterAsynchronously(BeTheMob.instance, new Runnable() {

							@Override
							public void run() {
								dis.extinguish();
								
							}}, event.getDuration() * 20);

					}
				}
				
			});
		}
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerSwapItem(final PlayerItemHeldEvent event){
		final Player player = event.getPlayer();
		final String name = player.getName();
		final BeTheMob plugin = BeTheMob.instance;
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if(plugin.disguises.containsKey(name)) {
					Disguise dis = plugin.disguises.get(name);
					if(dis.isPlayer()) { //Only switch armour for player disguise types
						dis.changeItem(event.getNewSlot());
						dis.changeArmor(1);
						dis.changeArmor(2);
						dis.changeArmor(3);
						dis.changeArmor(4);
					}
				}
			}
			
		});
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerJoin(final PlayerJoinEvent event) {
		final BeTheMob plugin = BeTheMob.instance;
		final Player player = event.getPlayer();
		final String name = player.getName();
		
		for(Player online : Bukkit.getServer().getOnlinePlayers()) {
			if(plugin.isHidden(online))
				player.hidePlayer(online);
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if(plugin.disguises.containsKey(name)) {
					plugin.setHidden(player, true);
					Disguise dis = plugin.disguises.get(name);
					
					dis.spawn(player.getWorld());
				}
				
				for(String user : plugin.disguises.keySet()) {
					if(!user.equals(name)) {
						Disguise dis = plugin.disguises.get(user);
						dis.spawn(player);
					}
				}
			}
			
		});
		
		if(!plugin.protLib) {
			//Change the players connection
			PlayerConnectionCallback.hook(player);
		}
		
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerQuit(final PlayerQuitEvent event) {
		final BeTheMob plugin = BeTheMob.instance;
		final Player player = event.getPlayer();
		
		if(plugin.isHidden(player))
			plugin.setHidden(player, false);
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				BTMTaskManager.notifyPlayerLeft(player);
			}
			
		});
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerChangedWorld(final PlayerChangedWorldEvent event) {
		final BeTheMob plugin = BeTheMob.instance;
		final Player player = event.getPlayer();
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				BTMTaskManager.notifyWorldChanged(player, event.getFrom());
			}
			
		});
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerTeleport(final PlayerTeleportEvent event) {
		if(event.isCancelled())
			return;
		
		final Player player = event.getPlayer();
		final BeTheMob plugin = BeTheMob.instance;
		final String name = player.getName();
		final Location loc = event.getTo();
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if(plugin.disguises.containsKey(name)) {
					Disguise dis = plugin.disguises.get(name);
					dis.teleport(loc);
				}
			}
			
		});
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerAnimation(final PlayerAnimationEvent event) {
		if(event.isCancelled())
			return;
		
		final PlayerAnimationType type = event.getAnimationType();
		final Player player = event.getPlayer();
		final BeTheMob plugin = BeTheMob.instance;
		final String name = player.getName();
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if(plugin.disguises.containsKey(name)) {
					if(type == PlayerAnimationType.ARM_SWING) {
						Disguise dis = plugin.disguises.get(name);
						dis.swingArm();
					}
				}
			}
			
		});
	}
	@EventHandler (priority = EventPriority.MONITOR)
	public void onBlockDamage(final BlockDamageEvent event) {
		if(event.isCancelled())
			return;
		
		final Block block = event.getBlock();
		final Player player = event.getPlayer();
		final BeTheMob plugin = BeTheMob.instance;
		final String name = player.getName();
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if(plugin.disguises.containsKey(name)) {
					Disguise dis = plugin.disguises.get(name);
					dis.damageBlock(block);
				}
			}
			
		});
	}
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerDeath(final PlayerDeathEvent event) {
		final Player player = event.getEntity();
		final BeTheMob plugin = BeTheMob.instance;
		final String name = player.getName();
		
		if(BeTheMob.instance.getConfig().getBoolean("drop_real_item") && plugin.disguises.containsKey(name)){
			event.getDrops().clear();
			Set<ItemStack> items = BeTheMob.instance.getDropFactory().getDrops(BeTheMob.getApi().getDisguise(player));
			BeTheMob.instance.getLogger().info("Dropping "+items.size()+" items for "+name+".");
			if(items != null){
				for(ItemStack item : items){
					if(item != null)
						event.getDrops().add(item);
				}
			}
		}
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if(plugin.disguises.containsKey(name)) {
					Disguise dis = plugin.disguises.get(name);
					dis.kill();
					
				}
			}
			
		});
	}
	
	
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		final Player player = event.getPlayer();
		final BeTheMob plugin = BeTheMob.instance;
		final String name = player.getName();
		final Location loc = event.getRespawnLocation();
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if(plugin.disguises.containsKey(name)) {
					Disguise dis = plugin.disguises.get(name);
					dis.setLocation(loc);
					dis.spawn(loc.getWorld());
					dis.refreshMovement();
				}
				
				Bukkit.getScheduler().runTaskLater(BeTheMob.instance, new Runnable() {

					@Override
					public void run() {
						for(String s : BeTheMob.instance.disguises.keySet()){
							Disguise dis = BeTheMob.instance.disguises.get(s);
							dis.spawn(player);
						}
					}
				}, 25);
			}
			
		});
	}
	@EventHandler (priority = EventPriority.MONITOR)
	public void onEntityDamageByEntity(EntityDamageByEntityEvent evt) {
		Entity entity = evt.getDamager();
		if(evt.getEntity() instanceof Player) {
			final Player player = (Player)evt.getEntity();
			final String name = player.getName();
			for(Herd herd : BeTheMob.instance.herds) {
				if(herd.getLeader().getName() == name) {
					for(HerdEntity hentity : herd.getHerdMembers()) {
						if(entity instanceof LivingEntity) {
							hentity.setEntityToAttack((LivingEntity)entity);
						} else if(entity instanceof Projectile) {
							hentity.setEntityToAttack(((Projectile)entity).getShooter());							
						}
					}
				}
			}
		}
	}
	@EventHandler (priority = EventPriority.MONITOR)
	public void onEntityDamage(final EntityDamageEvent event) {
		if(event.isCancelled())
			return;
		
		final Entity entity = event.getEntity();
		final BeTheMob plugin = BeTheMob.instance;
		
		if(entity instanceof Player) {
			final Player player = (Player)entity;
			final String name = player.getName();
			Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

				@Override
				public void run() {
					if(plugin.disguises.containsKey(name)) {
						Disguise dis = plugin.disguises.get(name);
						dis.damage();
						dis.playHurtSound();//Wont play if disabled
					}
				}
				
			});
		}
	}
	@EventHandler (priority = EventPriority.MONITOR)
	public void onPlayerToggleSneak(final PlayerToggleSneakEvent event) {
		if(event.isCancelled())
			return;
		
		final Player player = event.getPlayer();
		final BeTheMob plugin = BeTheMob.instance;
		final String name = player.getName();
		
		Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {

			@Override
			public void run() {
				if(plugin.disguises.containsKey(name)) {
					Disguise dis = plugin.disguises.get(name);
					
					if(event.isSneaking())
						dis.crouch();
					else
						dis.uncrouch();
				}
			}
			
		});
	}
}
