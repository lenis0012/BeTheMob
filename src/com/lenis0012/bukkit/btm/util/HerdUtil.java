package com.lenis0012.bukkit.btm.util;

import java.util.ArrayList;

import com.dylanisawesome1.bukkit.btm.Herds.HerdEntity;

public class HerdUtil {
	/**
	 * Get a herd entity from minecraft's given id.
	 * @param id - The id of the entity
	 * @param herd - The array of herd entities
	 */
	public static HerdEntity getHerdEntityFromId(int id, ArrayList<HerdEntity> herd) {
		HerdEntity entity = null;
		for(HerdEntity e : herd) {
			if(e.getEntityId() == id) {
				entity = e;
			}
		}
		return entity;
	}
}
