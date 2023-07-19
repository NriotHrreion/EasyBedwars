package net.nocpiun.bedwars.game;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.configuration.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;
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
	private List<Player> alivePlayers = new ArrayList<>();
	
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
				player.getEnderChest().clear();
				
				this.playerInit(player);
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
			
			this.alivePlayers.addAll(players);
		} else { // No player joined in the game
			this.stop();
			return;
		}
		
		// Beds
		final Location redBed = (Location) config.get("red-bed");
		final Location blueBed = (Location) config.get("blue-bed");
		Utils.setBed(redBed.getBlock(), redBed, Material.RED_BED);
		Utils.setBed(blueBed.getBlock(), blueBed, Material.BLUE_BED);
	}
	
	public void stop() {
		this.isGameStart = false;
		this.spawner.stop();
		
		for(Entity entity : this.world.getEntities()) {
			if(entity instanceof Item) {
				entity.remove();
			}
		}
		
		Configuration config = this.plugin.getConfig();
		TeamManager manager = TeamManager.get();
		
		// Game Listener (self)
		HandlerList.unregisterAll(this);
		this.placedBlockLocations.forEach(location -> {
			location.getBlock().setType(Material.AIR);
		});
		this.placedBlockLocations.clear();
		
		// Villagers
		for(StoreVillager villager : this.villagers) {
			villager.remove();
			HandlerList.unregisterAll(villager);
		}
		this.villagers.clear();
		
		// Players
		List<Player> players = manager.getPlayersInTeam();
		for(Player player : players) {
			player.getInventory().clear();
			player.teleport((Location) config.get("waiting-hub"));
		}
		
		// Teams
		manager.init();
	}
	
	private void playerInit(Player player) {
		if(!TeamManager.get().hasPlayerJoinedTeam(player)) return;
		TeamType team = TeamManager.get().getTeamType(player);

		player.getInventory().clear();
		player.getInventory().setItem(0, new ItemStack(Material.COMPASS));
		player.getInventory().setItem(1, new ItemStack(Material.WOODEN_SWORD));
		
		ItemStack hat = new ItemStack(Material.LEATHER_HELMET);
		LeatherArmorMeta hatMeta = (LeatherArmorMeta) hat.getItemMeta();
		switch(team) {
		case RED:
			hatMeta.setColor(Color.fromRGB(176, 46, 38));
			break;
		case BLUE:
			hatMeta.setColor(Color.fromRGB(60, 68, 170));
			break;
		default: // impossible
			break;
		}
		hat.setItemMeta(hatMeta);
		player.getInventory().setHelmet(hat);
	}
	
	private void breakBed(TeamType teamType, Player player) {
		Team team = TeamManager.get().getTeam(teamType);
		team.setHasBed(false);
		Utils.sendMessageToEveryone("§6§l"+ player.getName() +" §r§ebroke the bed of §r"+ team.name);
		
		player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 0);
		team.players.forEach(teamPlayer -> {
			teamPlayer.sendTitle("§c§lYour Bed is Broken", null, 10, 50, 10);
			teamPlayer.playSound(teamPlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_DEATH, 1, 0);
		});
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
		TeamManager manager = TeamManager.get();
		Player player = event.getPlayer();
		if(!manager.hasPlayerJoinedTeam(player)) return;
		
		if(manager.getTeam(player).getHasBed()) {
			this.playerInit(player);
			player.setNoDamageTicks(80);
		} else { // The bed has been broken, unable to respawn
			this.alivePlayers.remove(player);
			player.setGameMode(GameMode.SPECTATOR);
			Utils.sendMessageToEveryone("§6§l"+ player.getName() +" §ris OUT");
		}
		
		Team team = null;
		boolean isEnded = true;
		for(Player _player : this.alivePlayers) {
			if(team == null) {
				team = manager.getTeam(_player);
			} else if(!manager.getTeam(_player).equals(team)) {
				isEnded = false;
				break;
			}
		}
		if(isEnded) {
			for(Player _player : this.alivePlayers) {
				_player.sendTitle("§6§lVictory", null, 10, 50, 10);
			}
			Utils.sendMessageToEveryone(team.name +" §rhas §lWON §rthe game");
			this.stop();
		}
	}
	
	@EventHandler
	public void onPlayerCraft(CraftItemEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onPlayerDamaged(EntityDamageByEntityEvent event) {
		if(!(event.getDamager() instanceof Player) || !(event.getEntity() instanceof Player)) return;
		
		Player damager = (Player) event.getDamager();
		Player damaged = (Player) event.getEntity();
		TeamManager manager = TeamManager.get();
		
		if(manager.getTeam(damager).equals(manager.getTeam(damaged))) {
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInteractBlock(PlayerInteractEvent event) {
//		if(
//			event.getClickedBlock() != null &&
//			(
//				event.getClickedBlock().getType() == Material.CHEST ||
//				event.getClickedBlock().getType() == Material.ENDER_CHEST
//			)
//		) return;
//		
//		event.setCancelled(true);
		if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            switch(block.getType()) {
            case BARREL:
            case ANVIL:
            case DECORATED_POT:
            case SPRUCE_HANGING_SIGN:
            case SPRUCE_SIGN:
            case SPRUCE_TRAPDOOR:
            	event.setCancelled(true);
            	break;
            default:
            	return;
            }
        }
	}
	
	@EventHandler
	public void onInteractEntity(PlayerInteractEntityEvent event) {
		if(!(event.getRightClicked() instanceof ItemFrame)) return;
		
		// Prevent rotating and putting in item in item frame
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onInteractAtEntity(PlayerInteractAtEntityEvent event) {
		event.setCancelled(true);
	}
	
	@EventHandler
	public void onTakeOutItem(EntityDamageByEntityEvent event) {
		if(!(event.getEntity() instanceof ItemFrame)) return;
		
		// Prevent taking out item in item frame
		event.setCancelled(true);
	}
}
