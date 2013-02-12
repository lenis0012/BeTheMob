package com.lenis0012.bukkit.btm.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.lenis0012.bukkit.btm.api.Disguise;

public class PlayerInteractDisguisedEvent extends Event implements Cancellable {
	private boolean cancelled = false;
	private static final HandlerList handlers = new HandlerList();
	
	@Override
	public boolean isCancelled() {
		return cancelled;
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
	private Disguise disguised;
	
	public PlayerInteractDisguisedEvent(Player player, Disguise disguised) {
		this.player = player;
		this.disguised = disguised;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Disguise getDisguised() {
		return disguised;
	}
}
