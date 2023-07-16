package net.nocpiun.bedwars.store;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.entity.Villager.Profession;
import org.bukkit.event.player.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.*;

import net.nocpiun.bedwars.*;
import net.nocpiun.bedwars.gui.GUI;

public abstract class StoreVillager extends GUI {
	private Villager villager;
	
	public StoreVillager(Location location, String name) {
		this.villager = (Villager) location.getWorld().spawnEntity(location, EntityType.VILLAGER);
		
		this.villager.setProfession(Profession.NONE);
		this.villager.setCustomNameVisible(true);
		this.villager.setCustomName(name);
		this.villager.setAI(false);
		this.villager.setCanPickupItems(false);
		this.villager.setGravity(false);
		this.villager.setSilent(true);
		this.villager.setCollidable(false);
		this.villager.setInvulnerable(true);
		this.villager.setNoDamageTicks(9999999);
	}
	
	@Override
	public abstract Inventory render(Player player);
	
	public void onTrade(PlayerInteractEntityEvent event) {
		event.setCancelled(true);
		
		this.open(event.getPlayer());
	}
	
	public void onClick(InventoryClickEvent event, ItemStack clickedItem) {
		super.onClick(event);
		
		final int amount = clickedItem.getAmount();
		
		ItemMeta meta = clickedItem.getItemMeta();
		List<String> lore = meta.getLore();
		
		if(lore == null) return;
		
		String[] strBlocks = lore.get(1).replace("§fPrice: §6", "").split(" ");
		final int price = Integer.valueOf(strBlocks[0]);
		final CurrencyType currencyType = CurrencyType.valueOf(strBlocks[1]);
		final Material currencyMaterial = Utils.currencyTypeToMaterial(currencyType);
		
		Player player = (Player) event.getWhoClicked();
		PlayerInventory inventory = player.getInventory();
		// Check the ability to pay
		if(inventory.contains(currencyMaterial, price)) {
			// Pay
			for(int i = 0; i < price; i++) {
				inventory.removeItem(new ItemStack(currencyMaterial));
			}
			// Obtain the item
			ItemStack _item = new ItemStack(clickedItem.getType());
			ItemMeta _meta = _item.getItemMeta();
			_meta.setDisplayName(meta.getDisplayName());
			_item.setItemMeta(_meta);
			_item.addUnsafeEnchantments(clickedItem.getEnchantments());
			_item.setAmount(amount);
			inventory.addItem(_item);
		} else { // Unable to pay
			player.sendMessage("§cYour money is not enough to buy this");
		}
	}
	
	public void remove() {
		this.villager.remove();
	}
	
	public Location getLocation() {
		return this.villager.getLocation();
	}
}
