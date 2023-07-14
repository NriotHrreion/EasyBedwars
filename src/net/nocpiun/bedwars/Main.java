package net.nocpiun.bedwars;

import java.util.*;

import org.bukkit.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.*;

public class Main extends JavaPlugin {
	@Override
	public void onEnable() {
		System.out.println("EasyBedwars is Loaded!");
		this.init();
	}
	
	@Override
	public void onDisable() {
		// Do nothing...
	}
	
	private void init() {
		Bukkit.getPluginCommand("bedwars").setExecutor(new CommandHandler());
		
		// config
		Configuration config = this.getConfig();
		config.addDefault("copper-points", new ArrayList<Location>());
		config.addDefault("iron-points", new ArrayList<Location>());
		config.addDefault("diamond-points", new ArrayList<Location>());
		config.addDefault("emerald-points", new ArrayList<Location>());
		this.saveDefaultConfig();
	}
}
