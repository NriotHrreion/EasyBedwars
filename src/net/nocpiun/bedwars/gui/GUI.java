package net.nocpiun.bedwars.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.Inventory;

public abstract class GUI implements Listener {
	public abstract Inventory render(Player player);
	
	public void open(Player player) {
		player.openInventory(this.render(player));
	}
	
	public void onClick(InventoryClickEvent event) {
		event.setCancelled(true);
	}
}
