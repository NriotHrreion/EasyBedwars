package net.nocpiun.bedwars;

import java.util.*;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.configuration.*;

import net.nocpiun.bedwars.game.*;

public class CommandHandler implements CommandExecutor, TabExecutor {
	private Game game;
	private boolean isGameStart = false;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		if(!(sender instanceof Player)) return false;
		
		final String type = args[0];
		final Player player = (Player) sender;
		Plugin plugin = Bukkit.getPluginManager().getPlugin("EasyBedwars");
		Configuration config = plugin.getConfig();
		
		switch(type) {
		case "start":
			if(this.isGameStart) {
				sender.sendMessage("The game has already started");
				return true;
			}
			
			this.isGameStart = true;
			this.game = new Game(plugin);
			this.game.start();
			sender.sendMessage("§e§lGame Start!");
			break;
		case "stop":
			if(!this.isGameStart) {
				sender.sendMessage("The game has stopped");
				return true;
			}
			
			this.isGameStart = false;
			this.game.stop();
			this.game = null;
			sender.sendMessage("stopped");
			break;
		case "copper-point":
			List<Location> copperOrigin = (List<Location>) config.get("copper-points");
			if(args.length > 1 && args[1] == "clear") {
				copperOrigin.clear();
				config.set("copper-points", copperOrigin);
				return true;
			}
			copperOrigin.add(player.getLocation());
			config.set("copper-points", copperOrigin);
			sender.sendMessage("§aAdded");
			break;
		case "iron-point":
			List<Location> ironOrigin = (List<Location>) config.get("iron-points");
			if(args.length > 1 && args[1] == "clear") {
				ironOrigin.clear();
				config.set("iron-points", ironOrigin);
				return true;
			}
			ironOrigin.add(player.getLocation());
			config.set("iron-points", ironOrigin);
			sender.sendMessage("§aAdded");
			break;
		case "diamond-point":
			List<Location> diamondOrigin = (List<Location>) config.get("diamond-points");
			if(args.length > 1 && args[1] == "clear") {
				diamondOrigin.clear();
				config.set("diamond-points", diamondOrigin);
				return true;
			}
			diamondOrigin.add(player.getLocation());
			config.set("diamond-points", diamondOrigin);
			sender.sendMessage("§aAdded");
			break;
		case "emerald-point":
			List<Location> emeraldOrigin = (List<Location>) config.get("emerald-points");
			if(args.length > 1 && args[1] == "clear") {
				emeraldOrigin.clear();
				config.set("emerald-points", emeraldOrigin);
				return true;
			}
			emeraldOrigin.add(player.getLocation());
			config.set("emerald-points", emeraldOrigin);
			sender.sendMessage("§aAdded");
			break;
		case "red-point":
			config.set("red-point", player.getLocation());
			sender.sendMessage("§c§lRed §r§apoint is set");
			break;
		case "blue-point":
			config.set("blue-point", player.getLocation());
			sender.sendMessage("§b§lBlue §r§apoint is set");
			break;
		case "waiting-hub":
			config.set("waiting-hub", player.getLocation());
			sender.sendMessage("§f§lWaiting Hub §r§ais set");
			break;
		case "common-villager":
			break;
		case "buff-villager":
			break;
		default:
			return false;
		}
		
		plugin.saveConfig();
		
		return true;
	}
	
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String cmdName, String[] args) {
		final List<String> list = new ArrayList<>();
		list.add("start");
		list.add("stop");
		list.add("copper-point");
		list.add("iron-point");
		list.add("diamond-point");
		list.add("emerald-point");
		list.add("red-point");
		list.add("blue-point");
		list.add("waiting-hub");
		list.add("common-villager");
		list.add("buff-villager");
		
		if(sender instanceof Player) {
			if(args.length > 1) return null;
			return list;
		}
		
		return null;
	}
}
