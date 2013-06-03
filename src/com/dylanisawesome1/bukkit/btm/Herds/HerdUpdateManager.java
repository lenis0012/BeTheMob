package com.dylanisawesome1.bukkit.btm.Herds;

import java.util.ArrayList;


public class HerdUpdateManager {
	public static  void updateHerds(final ArrayList<Herd> herds) {
		for (Herd herd : herds) {
			for (HerdEntity hentity : herd.getHerdMembers()) {
				hentity.update();
			}
		}
	}
}
