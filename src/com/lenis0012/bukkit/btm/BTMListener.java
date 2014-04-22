package com.lenis0012.bukkit.btm;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.bergerkiller.bukkit.common.entity.CommonEntity;
import com.bergerkiller.bukkit.common.entity.type.CommonPlayer;
import com.bergerkiller.bukkit.common.utils.CommonUtil;

public class BTMListener implements Listener {
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		final Player player = event.getPlayer();
		
		//Hook custom network controller.
		//Make sure to hook after its been registered.
		CommonUtil.nextTick(new Runnable() {

			@Override
			public void run() {
				CommonPlayer commonPlayer = CommonEntity.get(player);
				commonPlayer.setNetworkController(new BTMNetworkController());
			}
		});
	}
}