package net.nocpiun.bedwars.store;

import java.util.Arrays;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

public class BuffStoreVillager extends StoreVillager {
	private ItemStack[] items;
	
	public BuffStoreVillager(Location location) {
		super(location, "Team Buff");
	}
	
	@Override
	public Inventory render(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 54, "Team Buff [in dev]");
		
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
		if(event.getRightClicked().getCustomName().compareTo("Team Buff") != 0) return;
		
		final Inventory inventory = this.render(event.getPlayer());
		this.items = inventory.getContents();
		super.onTrade(event);
	}
	
	@EventHandler
	public void onClick(InventoryClickEvent event) {
		if(!Arrays.equals(event.getInventory().getContents(), this.items)) return;
		if(event.getCurrentItem() == null) return;
		if(event.getAction() != InventoryAction.PICKUP_ALL || event.getResult() != Result.ALLOW) return;
		
		super.onClick(event, event.getCurrentItem());
	}
}
