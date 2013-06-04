package src.com.lenis0012.bukkit.btm.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.dylanisawesome1.bukkit.btm.Herds.HerdEntity;

public class HerdEntityInteractEvent extends Event implements Cancellable {
	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean value) {
		this.cancelled = value;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	private Player player;
	private HerdEntity entityinteracted = null;
	private int action;
	private boolean isPlayer = false;
	
	public HerdEntityInteractEvent(Player player, HerdEntity entity, int action) {
		this.player = player;
		this.setAction(action);
		entityinteracted=entity;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public HerdEntity getEntityInteractedWith() {
		return this.entityinteracted;
	}
	
	public boolean isPlayer() {
		return this.isPlayer;
	}
	
	public void setEntityInteractedWith(HerdEntity entity) {
		entityinteracted=entity;
	}

	public int getAction() {
		return action;
	}

	public void setAction(int action) {
		this.action = action;
	}
}
