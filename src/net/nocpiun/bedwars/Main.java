package net.nocpiun.bedwars;

import java.util.*;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import net.nocpiun.bedwars.team.TeamManager;

import org.bukkit.configuration.*;
import org.bukkit.entity.Player;

public class Main extends JavaPlugin implements CommandExecutor {
	@Override
	public void onEnable() {
		System.out.println("EasyBedwars is Loaded!");
		Utils.sendMessageToEveryone("§e§oThanks for using EasyBedwars!");
		this.init();
	}
	
	@Override
	public void onDisable() {
		TeamManager.get().terminate();
	}
	
	private void init() {
		Bukkit.getPluginCommand("bedwars").setExecutor(new CommandHandler(this));
		Bukkit.getPluginCommand("hub").setExecutor(this);
		
		// config
		Configuration config = this.getConfig();
		config.addDefault("copper-points", new ArrayList<Location>());
		config.addDefault("iron-points", new ArrayList<Location>());
		config.addDefault("diamond-points", new ArrayList<Location>());
		config.addDefault("emerald-points", new ArrayList<Location>());
		config.addDefault("common-villagers", new ArrayList<Location>());
		config.addDefault("buff-villagers", new ArrayList<Location>());
		this.saveDefaultConfig();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		if(!(sender instanceof Player)) return false;
		
		Player player = (Player) sender;
		player.teleport((Location) this.getConfig().get("waiting-hub"));
		return true;
	}
}
