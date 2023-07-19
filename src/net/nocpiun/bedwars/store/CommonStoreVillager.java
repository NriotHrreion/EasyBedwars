package net.nocpiun.bedwars.store;

import java.util.*;

import org.bukkit.*;
import org.bukkit.enchantments.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.Event.Result;
import org.bukkit.event.player.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import net.nocpiun.bedwars.*;

public class CommonStoreVillager extends StoreVillager {
	private static final String title = "Common Equipments";
	
	public CommonStoreVillager(Location location) {
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
				meta.setDisplayName(" ");
				itemStack.setItemMeta(meta);
				inventory.setItem(i, itemStack);
			}
		}
		// Goods
		inventory.setItem(10, Utils.createStoreItem(Material.WHITE_WOOL, "Wool", 16, 4, CurrencyType.COPPER));
		inventory.setItem(11, Utils.createStoreItem(Material.OAK_PLANKS, "Oak Planks", 16, 4, CurrencyType.IRON));
		inventory.setItem(12, Utils.createStoreItem(Material.END_STONE, "End Stone", 12, 24, CurrencyType.COPPER));
		inventory.setItem(13, Utils.createStoreItem(Material.OBSIDIAN, "Obsidian", 4, 4, CurrencyType.EMERALD));
		inventory.setItem(14, Utils.createStoreItem(Material.LADDER, "Ladder", 16, 4, CurrencyType.COPPER));
		// Weapons
		inventory.setItem(19, Utils.createStoreItem(Material.STONE_SWORD, "Stone Sword", 1, 10, CurrencyType.COPPER));
		inventory.setItem(20, Utils.createStoreItem(Material.IRON_SWORD, "Iron Sword", 1, 7, CurrencyType.IRON));
		inventory.setItem(21, Utils.createStoreItem(Material.DIAMOND_SWORD, "Diamond Sword", 1, 6, CurrencyType.EMERALD));
		inventory.setItem(23, Utils.createStoreItem(Material.BOW, "Bow", 1, 12, CurrencyType.IRON));
		final HashMap<Enchantment, Integer> excaliburEnchantments = new HashMap<>();
		excaliburEnchantments.put(Enchantment.KNOCKBACK, 1);
		inventory.setItem(22, Utils.createStoreItem(Material.STICK, "Excalibur", 1, 5, CurrencyType.IRON, excaliburEnchantments));
		// Armors
		inventory.setItem(28, Utils.createStoreItem(Material.CHAINMAIL_CHESTPLATE, "Chainmail Armor", 1, 40, CurrencyType.COPPER));
		inventory.setItem(29, Utils.createStoreItem(Material.IRON_CHESTPLATE, "Iron Armor", 1, 12, CurrencyType.IRON));
		inventory.setItem(30, Utils.createStoreItem(Material.DIAMOND_CHESTPLATE, "Diamond Armor", 1, 6, CurrencyType.EMERALD));
		// Tools
		final HashMap<Enchantment, Integer> efficientEnchantments = new HashMap<>();
		efficientEnchantments.put(Enchantment.DIG_SPEED, 2);
		inventory.setItem(37, Utils.createStoreItem(Material.SHEARS, "Shears", 1, 20, CurrencyType.COPPER, efficientEnchantments));
		inventory.setItem(38, Utils.createStoreItem(Material.IRON_PICKAXE, "Pickaxe", 1, 12, CurrencyType.COPPER, efficientEnchantments));
		inventory.setItem(39, Utils.createStoreItem(Material.STONE_AXE, "Axe", 1, 3, CurrencyType.IRON));
		inventory.setItem(40, Utils.createStoreItem(Material.ARROW, "Arrow", 8, 2, CurrencyType.IRON));
		inventory.setItem(41, Utils.createStoreItem(Material.GOLDEN_APPLE, "Golden Apple", 1, 3, CurrencyType.IRON));
		inventory.setItem(42, Utils.createStoreItem(Material.ENDER_PEARL, "Ender Pearl", 1, 4, CurrencyType.EMERALD));
		
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
		if(event.getResult() != Result.ALLOW) return;
		
		super.onClick(event, event.getCurrentItem());
	}
}
