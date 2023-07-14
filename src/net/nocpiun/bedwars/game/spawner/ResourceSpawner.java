package net.nocpiun.bedwars.game.spawner;

import org.bukkit.configuration.*;
import org.bukkit.plugin.Plugin;

public class ResourceSpawner {
	private Plugin plugin;
	
	private CopperSpawner copper;
	private IronSpawner iron;
	private DiamondSpawner diamond;
	private EmeraldSpawner emerald;
	
	public ResourceSpawner(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public void start() {
		final Configuration config = this.plugin.getConfig();
		
		this.copper = new CopperSpawner(config);
		this.iron = new IronSpawner(config);
		this.diamond = new DiamondSpawner(config);
		this.emerald = new EmeraldSpawner(config);
		
//		this.copper.runTaskTimer(this.plugin, 10, 20);
//		this.iron.runTaskTimer(this.plugin, 10, 500);
//		this.diamond.runTaskTimer(this.plugin, 10, 1200);
//		this.emerald.runTaskTimer(this.plugin, 10, 1100);
		
		// dev
		this.copper.runTaskTimer(this.plugin, 10, 20);
		this.iron.runTaskTimer(this.plugin, 10, 20);
		this.diamond.runTaskTimer(this.plugin, 10, 20);
		this.emerald.runTaskTimer(this.plugin, 10, 20);
	}
	
	public void stop() {
		this.copper.cancel();
		this.iron.cancel();
		this.diamond.cancel();
		this.emerald.cancel();
	}
}
