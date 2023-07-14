package net.nocpiun.bedwars.game.spawner;

import java.util.*;

import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.*;

public class DiamondSpawner extends BukkitRunnable {
	private Configuration config;
	
	public DiamondSpawner(Configuration config) {
		this.config = config;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		final List<Location> locations = (List<Location>) config.get("diamond-points");
		final ItemStack itemStack = new ItemStack(Material.DIAMOND);
		
		for(int i = 0; i < locations.size(); i++) {
			locations.get(i).getWorld().dropItem(locations.get(i), itemStack).setPickupDelay(0);
		}
	}
}
