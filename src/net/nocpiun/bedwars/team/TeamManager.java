package net.nocpiun.bedwars.team;

import java.util.*;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class TeamManager {
	private static TeamManager instance;
	
	public Team red;
	public Team blue;
	
	private Scoreboard scoreboard;
	private Objective objective;
	
	private TeamManager() {
		this.red = new Team("§c§lRed");
		this.blue = new Team("§b§lBlue");
		
		this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
		this.objective = this.scoreboard.registerNewObjective("Bedwars", Criteria.DUMMY, "Bedwars");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.setPlayerAmountDisplay("§c§lRed", 0);
		this.setPlayerAmountDisplay("§b§lBlue", 0);
	}
	
	public Team getTeam(TeamType teamType) {
		switch(teamType) {
		case RED:
			return this.red;
		case BLUE:
			return this.blue;
		default: // impossible
			return null;
		}
	}
	
	public Team getTeam(Player player) {
		for(Player redPlayer : this.red.players) {
			if(redPlayer.equals(player)) return this.red;
		}
		
		for(Player bluePlayer : this.blue.players) {
			if(bluePlayer.equals(player)) return this.blue;
		}
		
		return null;
	}
	
	public void addPlayer(TeamType teamType, Player player) {
		Team team = this.getTeam(teamType);
		
		if(teamType == TeamType.RED && this.blue.isInTeam(player)) {
			this.blue.removePlayer(player);
			team.addPlayer(player);
			player.sendMessage("§aYou joined the team");
			return;
		} else if(teamType == TeamType.BLUE && this.red.isInTeam(player)) {
			this.red.removePlayer(player);
			team.addPlayer(player);
			player.sendMessage("§aYou joined the team");
			return;
		}
		
		if(team.addPlayer(player)) {
			player.sendMessage("§aYou joined the team");
		} else {
			player.sendMessage("§cUnable to join the team");
		}
	}
	
	public void removePlayer(TeamType teamType, Player player) {
		this.getTeam(teamType).removePlayer(player);
		player.sendMessage("§aYou are no longer in the team");
	}
	
	public void cleanTeam(TeamType teamType) {
		this.getTeam(teamType).clear();
	}
	
	public boolean isInTeam(Player player, TeamType teamType) {
		return this.getTeam(teamType).isInTeam(player);
	}
	
	public void setPlayerAmountDisplay(String name, int value) {
		this.getScore(name).setScore(value);
	}
	
	public Score getScore(String name) {
		return (Score) this.scoreboard.getScores(name).toArray()[0];
	}
	
	public boolean hasPlayerJoinedTeam(Player player) {
		return this.red.isInTeam(player) || this.blue.isInTeam(player);
	}
	
	public List<Player> getPlayersInTeam() {
		List<Player> list = new ArrayList<>();
		
		for(Player redPlayer : this.red.players) {
			list.add(redPlayer);
		}
		for(Player bluePlayer : this.blue.players) {
			list.add(bluePlayer);
		}
		
		return list;
	}
	
	public void setHasBed(TeamType teamType, boolean hasBed) {
		this.getTeam(teamType).setHasBed(hasBed);
	}
	
	public boolean getHasBed(TeamType teamType) {
		return this.getTeam(teamType).getHasBed();
	}
	
	public void terminate() {
		this.objective.unregister();
	}
	
	public static TeamManager get() {
		if(TeamManager.instance == null) TeamManager.instance = new TeamManager();
		
		return TeamManager.instance;
	}
}
