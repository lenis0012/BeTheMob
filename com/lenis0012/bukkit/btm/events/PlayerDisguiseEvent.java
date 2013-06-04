package com.lenis0012.bukkit.btm.events;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerDisguiseEvent extends Event implements Cancellable {
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
	private EntityType type = null;
	private String name = null;
	private boolean isPlayer = false;
	
	public PlayerDisguiseEvent(Player player, EntityType type) {
		this.player = player;
		this.type = type;
	}
	
	public PlayerDisguiseEvent(Player player, String name) {
		this.player = player;
		this.name = name;
		this.isPlayer = true;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public EntityType getType() {
		return this.type;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isPlayer() {
		return this.isPlayer;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setType(EntityType type) {
		this.type = type;
	}
}
