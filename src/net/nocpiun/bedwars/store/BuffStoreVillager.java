package net.nocpiun.bedwars.store;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

public class BuffStoreVillager extends StoreVillager {
	private static final String title = "Team Buff";
	
	public BuffStoreVillager(Location location) {
		super(location, title);
	}
	
	@Override
	public Inventory render(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 54, title);
		
		// Border
		for(int i = 0; i < inventory.getSize(); i++) {
			if(
				Math.floor(i / 9) == 0 ||
				Math.floor(i / 9) == 5 ||
				i % 9 == 0 ||
				i % 9 == 8
			) {
				ItemStack itemStack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
				ItemMeta meta = itemStack.getItemMeta();
				meta.setDisplayName("");
				itemStack.setItemMeta(meta);
				inventory.setItem(i, itemStack);
			}
		}
		
		return inventory;
	}
	
	@EventHandler
	public void onTrade(PlayerInteractEntityEvent event) {
		if(!(event.getRightClicked() instanceof AbstractVillager)) return;
		if(!event.getRightClicked().getCustomName().equals(title)) return;
		
		super.onTrade(event);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(!event.getView().getTitle().equals(title)) return;
		if(event.getCurrentItem() == null) return;
		
		super.onClick(event, event.getCurrentItem());
	}
}
