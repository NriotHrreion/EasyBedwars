package net.nocpiun.bedwars;

import java.util.*;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.enchantments.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.nocpiun.bedwars.store.CurrencyType;

public class Utils {
	public static Material currencyTypeToMaterial(CurrencyType currencyType) {
		switch(currencyType) {
		case COPPER:
			return Material.COPPER_INGOT;
		case IRON:
			return Material.IRON_INGOT;
		case DIAMOND:
			return Material.DIAMOND;
		case EMERALD:
			return Material.EMERALD;
		default:
			return Material.AIR;
		}
	}
	
	public static ItemStack createStoreItem(Material itemType, String name, int amount, int price, CurrencyType currencyType) {
		ItemStack item = new ItemStack(itemType);
		
		final List<String> lore = new ArrayList<>();
		lore.add("§fAmount: "+ amount);
		lore.add("§fPrice: §6"+ price +" "+ currencyType.toString());
		lore.add("");
		lore.add("§aClick to Buy");
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r§e"+ name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		item.setAmount(amount);
		
		return item;
	}
	
	public static ItemStack createStoreItem(Material itemType, String name, int amount, int price, CurrencyType currencyType, Map<Enchantment, Integer> enchantments) {
		ItemStack item = new ItemStack(itemType);
		
		final List<String> lore = new ArrayList<>();
		lore.add("§fAmount: "+ amount);
		lore.add("§fPrice: §6"+ price +" "+ currencyType.toString());
		lore.add("");
		lore.add("§aClick to Buy");
		
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("§r§e"+ name);
		meta.setLore(lore);
		item.setItemMeta(meta);
		
		item.addUnsafeEnchantments(enchantments);
		item.setAmount(amount);
		
		return item;
	}
	
	public static void sendMessageToEveryone(String message) {
		for(Player player : Bukkit.getServer().getOnlinePlayers()) {
			player.sendMessage(message);
		}
	}
}
