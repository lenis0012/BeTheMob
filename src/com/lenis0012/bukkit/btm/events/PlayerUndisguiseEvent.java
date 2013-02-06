package com.lenis0012.bukkit.btm.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import com.lenis0012.bukkit.btm.api.Disguise;

public class PlayerUndisguiseEvent extends Event implements Cancellable {
	private boolean cancelled = false;
	private HandlerList handlers = new HandlerList();
	
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
	
	private Player player;
	private Disguise disguise;
	
	public PlayerUndisguiseEvent(Player player, Disguise disguise) {
		this.player = player;
		this.disguise = disguise;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public Disguise getDisguise() {
		return this.disguise;
	}
}
