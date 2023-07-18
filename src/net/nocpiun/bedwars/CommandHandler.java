package net.nocpiun.bedwars;

import java.util.*;

import org.bukkit.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.configuration.*;

import net.nocpiun.bedwars.game.*;
import net.nocpiun.bedwars.gui.*;

public class CommandHandler implements CommandExecutor, TabExecutor {
	private Plugin plugin;
	private Game game;
	
	public CommandHandler(Plugin plugin) {
		this.plugin = plugin;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdName, String[] args) {
		if(!(sender instanceof Player)) return false;
		
		final String type = args[0];
		final Player player = (Player) sender;
		final boolean isOp = player.hasPermission("bedwars.admin");
		Configuration config = plugin.getConfig();
		
		switch(type) {
		case "start":
			if(!isOp) break;
			if(this.game != null && this.game.isGameStart) {
				sender.sendMessage("The game has already started");
				return true;
			}
			
			this.game = new Game(this.plugin, player.getWorld());
			this.game.start();
			sender.sendMessage("§e§lGame Start!");
			break;
		case "stop":
			if(!isOp) break;
			if(this.game == null || !this.game.isGameStart) {
				sender.sendMessage("The game has stopped");
				return true;
			}
			
			this.game.stop();
			this.game = null;
			sender.sendMessage("stopped");
			break;
		case "copper-point":
			if(!isOp) break;
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
			if(!isOp) break;
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
			if(!isOp) break;
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
			if(!isOp) break;
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
			if(!isOp) break;
			config.set("red-point", player.getLocation());
			sender.sendMessage("§c§lRed §r§apoint is set");
			break;
		case "blue-point":
			if(!isOp) break;
			config.set("blue-point", player.getLocation());
			sender.sendMessage("§b§lBlue §r§apoint is set");
			break;
		case "red-bed":
			if(!isOp) break;
			config.set("red-bed", player.getLocation());
			sender.sendMessage("§c§lRed §r§abed is set");
			break;
		case "blue-bed":
			if(!isOp) break;
			config.set("blue-bed", player.getLocation());
			sender.sendMessage("§b§lBlue §r§abed is set");
			break;
		case "waiting-hub":
			if(!isOp) break;
			config.set("waiting-hub", player.getLocation());
			sender.sendMessage("§f§lWaiting Hub §r§ais set");
			break;
		case "common-villager":
			if(!isOp) break;
			List<Location> commonOrigin = (List<Location>) config.get("common-villagers");
			commonOrigin.add(player.getLocation());
			config.set("common-villagers", commonOrigin);
			sender.sendMessage("§aAdded");
			break;
		case "buff-villager":
			if(!isOp) break;
			List<Location> buffOrigin = (List<Location>) config.get("buff-villagers");
			buffOrigin.add(player.getLocation());
			config.set("buff-villagers", buffOrigin);
			sender.sendMessage("§aAdded");
			break;
		case "clear-villager":
			if(!isOp) break;
			config.set("common-villagers", new ArrayList<>());
			config.set("buff-villagers", new ArrayList<>());
			sender.sendMessage("§aCleared");
			break;
		case "choose-team":
			for(Player onlinePlayer : this.plugin.getServer().getOnlinePlayers()) {
				GUI teamGUI = new TeamGUI();
				Bukkit.getPluginManager().registerEvents(teamGUI, this.plugin);
				teamGUI.open(onlinePlayer);
			}
			Utils.sendMessageToEveryone("§aOpened the team GUI");
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
		list.add("red-bed");
		list.add("blue-bed");
		list.add("waiting-hub");
		list.add("common-villager");
		list.add("buff-villager");
		list.add("clear-villager");
		list.add("choose-team");
		
		if(sender instanceof Player) {
			if(args.length > 1) return null;
			return list;
		}
		
		return null;
	}
}
