package net.nocpiun.bedwars.game;

import org.bukkit.plugin.Plugin;

import net.nocpiun.bedwars.game.spawner.ResourceSpawner;

public class Game {
	private Plugin plugin;
	private ResourceSpawner spawner;
	
	public Game(Plugin plugin) {
		this.plugin = plugin;
		this.spawner = new ResourceSpawner(this.plugin);
	}
	
	public void start() {
		this.spawner.start();
	}
	
	public void stop() {
		this.spawner.stop();
	}
}
