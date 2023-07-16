package net.nocpiun.bedwars.team;

import java.util.*;

import org.bukkit.entity.Player;

public class Team {
	public static final int MAX_PLAYERS = 3;
	
	public final String name;
	public List<Player> players = new ArrayList<>();
	private boolean hasBed = true;
	
	public Team(String name) {
		this.name = name;
	}
	
	public boolean addPlayer(Player player) {
		if(this.isInTeam(player)) return false;
		if(this.players.size() + 1 > Team.MAX_PLAYERS) return false;
		
		TeamManager manager = TeamManager.get();
		this.players.add(player);
		manager.setPlayerAmountDisplay(name, manager.getScore(name).getScore() + 1);
		return true;
	}
	
	public void removePlayer(Player player) {
		if(!this.isInTeam(player)) return;
		TeamManager manager = TeamManager.get();
		this.players.remove(player);
		if(manager.getScore(name).getScore() > 0) {
			manager.setPlayerAmountDisplay(name, manager.getScore(name).getScore() - 1);
		}
	}
	
	public void clear() {
		this.players.clear();
	}
	
	public boolean isInTeam(Player player) {
		return this.players.contains(player);
	}
	
	public void setHasBed(boolean hasBed) {
		this.hasBed = hasBed;
	}
	
	public boolean getHasBed() {
		return this.hasBed;
	}
}
