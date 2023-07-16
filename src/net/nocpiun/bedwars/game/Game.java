package net.nocpiun.bedwars.game;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.Plugin;

import net.nocpiun.bedwars.Utils;
import net.nocpiun.bedwars.game.spawner.ResourceSpawner;
import net.nocpiun.bedwars.store.*;
import net.nocpiun.bedwars.team.*;

public class Game implements Listener {
	private Plugin plugin;
	private World world;
	private ResourceSpawner spawner;
	
	private List<StoreVillager> villagers = new ArrayList<>();
	private List<Location> placedBlockLocations = new ArrayList<>();
	
	public boolean isGameStart = false;
	
	public Game(Plugin plugin, World world) {
		this.plugin = plugin;
		this.world = world;
		this.spawner = new ResourceSpawner(this.plugin);
	}
	
	@SuppressWarnings("unchecked")
	public void start() {
		this.isGameStart = true;
		this.spawner.start();
		
		Configuration config = plugin.getConfig();
		
		// Game Listener (self)
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
		
		// Villagers
		List<Location> commonVillagerLocations = (List<Location>) config.get("common-villagers");
		List<Location> buffVillagerLocations = (List<Location>) config.get("buff-villagers");
		for(Location commonVillagerLocation : commonVillagerLocations) {
			CommonStoreVillager commonVillager = new CommonStoreVillager(commonVillagerLocation);
			Bukkit.getPluginManager().registerEvents(commonVillager, this.plugin);
			this.villagers.add(commonVillager);
		}
		for(Location buffVillagerLocation : buffVillagerLocations) {
			BuffStoreVillager buffVillager = new BuffStoreVillager(buffVillagerLocation);
			Bukkit.getPluginManager().registerEvents(buffVillager, this.plugin);
			this.villagers.add(buffVillager);
		}
		
		// Players
		TeamManager manager = TeamManager.get();
		List<Player> players = manager.getPlayersInTeam();
		if(players.size() != 0) {
			final Location redPoint = (Location) config.get("red-point");
			final Location bluePoint = (Location) config.get("blue-point");
			
			for(Player player : players) {
				player.setGameMode(GameMode.SURVIVAL);
				player.getInventory().clear();
				player.getEnderChest().clear();
				
				player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
				player.getInventory().setItem(1, new ItemStack(Material.WOODEN_SWORD));
			}
			
			// Spawnpoint & Teleport
			for(Player redPlayer : manager.red.players) {
				redPlayer.setBedSpawnLocation(redPoint, true);
				redPlayer.teleport(redPoint);
			}
			for(Player bluePlayer : manager.blue.players) {
				bluePlayer.setBedSpawnLocation(bluePoint, true);
				bluePlayer.teleport(bluePoint);
			}
		} else { // No player joined in the game
			this.stop();
		}
	}
	
	public void stop() {
		this.isGameStart = false;
		this.spawner.stop();
		
		for(Entity entity : this.world.getEntities()) {
			if(entity instanceof Item) {
				entity.remove();
			}
		}
		
		// Game Listener (self)
		HandlerList.unregisterAll(this);
		this.placedBlockLocations.clear();
		
		// Villagers
		for(StoreVillager villager : this.villagers) {
			villager.remove();
			HandlerList.unregisterAll(villager);
		}
		this.villagers.clear();
	}
	
	private void breakBed(TeamType team, Player player) {
		// @TODO
	}
	
	@EventHandler
	public void onPlaceBlock(BlockPlaceEvent event) {
		if(!this.isGameStart) return;
		
		Block block = event.getBlockPlaced();
		Location location = block.getLocation();
		Configuration config = this.plugin.getConfig();
		Location redPoint = (Location) config.get("red-point"),
				bluePoint = (Location) config.get("blue-point");
		
		if(
			redPoint.equals(location) &&
			bluePoint.equals(location)
		) {
			event.setCancelled(true);
		} else {
			this.placedBlockLocations.add(location);
		}
	}
	
	@EventHandler
	public void onDestroyBlock(BlockBreakEvent event) {
		if(!this.isGameStart) return;
		
		Block block = event.getBlock();
		boolean canBreak = false;
		
		for(Location blockLocation : this.placedBlockLocations) {
			if(block.getLocation().equals(blockLocation)) {
				canBreak = true;
				this.placedBlockLocations.remove(blockLocation);
				break;
			}
		}
		
		Material blockType = block.getType();
		if(blockType == Material.RED_BED || blockType == Material.BLUE_BED) {
			Player player = event.getPlayer();
			
			// Whether the player who broke the bed is in the team whose bed is broken by him(her)
			if(
				(
					TeamManager.get().isInTeam(player, TeamType.RED) &&
					blockType == Material.RED_BED
				) ||
				(
					TeamManager.get().isInTeam(player, TeamType.BLUE) &&
					blockType == Material.BLUE_BED
				)
			) {
				canBreak = false;
				event.setCancelled(true);
				return;
			}
			
			canBreak = true;
			
			event.setDropItems(false);
			switch(blockType) {
			case RED_BED:
				this.breakBed(TeamType.RED, player);
				break;
			case BLUE_BED:
				this.breakBed(TeamType.BLUE, player);
				break;
			default: // impossible
				return;
			}
		}
		
		if(!canBreak) event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		Player player = event.getPlayer();
		if(!TeamManager.get().hasPlayerJoinedTeam(player)) return;
		
		if(TeamManager.get().getTeam(player).getHasBed()) {
			player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
			player.getInventory().setItem(1, new ItemStack(Material.WOODEN_SWORD));
			player.setNoDamageTicks(80);
		} else { // The bed has been broken, unable to respawn
			player.setGameMode(GameMode.SPECTATOR);
			Utils.sendMessageToEveryone("§6§l"+ player.getName() +" §ris OUT");
		}
	}
}
