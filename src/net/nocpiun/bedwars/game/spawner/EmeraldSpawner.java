package net.nocpiun.bedwars.game.spawner;

import java.util.*;

import org.bukkit.*;
import org.bukkit.configuration.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.*;

public class EmeraldSpawner extends BukkitRunnable {
	private Configuration config;
	
	public EmeraldSpawner(Configuration config) {
		this.config = config;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		final List<Location> locations = (List<Location>) config.get("emerald-points");
		final ItemStack itemStack = new ItemStack(Material.EMERALD);
		
		for(Location location : locations) {
			location.getWorld().dropItem(location, itemStack).setPickupDelay(0);
		}
	}
}
