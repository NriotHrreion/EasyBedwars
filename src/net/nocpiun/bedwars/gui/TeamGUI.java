package net.nocpiun.bedwars.gui;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import net.nocpiun.bedwars.team.*;

public class TeamGUI extends GUI {
	private static final String title = "Choose a Team";
	
	@Override
	public Inventory render(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 27, title);
		
		// Border
		for(int i = 0; i < inventory.getSize(); i++) {
			if(
				Math.floor(i / 9) == 0 ||
				Math.floor(i / 9) == 2 ||
				i % 9 == 0 ||
				i % 9 == 8
			) {
				ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
				ItemMeta meta = itemStack.getItemMeta();
				meta.setDisplayName(" ");
				itemStack.setItemMeta(meta);
				inventory.setItem(i, itemStack);
			}
		}

		inventory.setItem(10, new ItemStack(Material.RED_WOOL));
		inventory.setItem(11, new ItemStack(Material.BLUE_WOOL));
		
		return inventory;
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(!event.getView().getTitle().equals(title)) return;
		if(event.getCurrentItem() == null) return;
		
		Material chosenTeam = event.getCurrentItem().getType();
		Player player = (Player) event.getWhoClicked();
		switch(chosenTeam) {
		case RED_WOOL:
			TeamManager.get().addPlayer(TeamType.RED, player);
			break;
		case BLUE_WOOL:
			TeamManager.get().addPlayer(TeamType.BLUE, player);
			break;
		default:
			break;
		}
		
		super.onClick(event);
	}
}
