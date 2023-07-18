package net.nocpiun.bedwars;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.type.Bed;
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
	
	public static void setBed(Block start, Location location, Material material) {
		final BlockFace facing = Utils.getCardinalDirection(location).getOppositeFace();
		
		for(Bed.Part part : Bed.Part.values()) {
			start.setBlockData(Bukkit.createBlockData(material, data -> {
				((Bed) data).setPart(part);
				((Bed) data).setFacing(facing);
			}));
			start = start.getRelative(facing.getOppositeFace());
		}
	}
	
	public static BlockFace getCardinalDirection(Location location) {
        double rotation = (location.getYaw() - 180) % 360;
        
        if(rotation < 0) {
            rotation += 360.0;
        }
        
        if(0 <= rotation && rotation < 22.5) {
            return BlockFace.NORTH;
        } else if(22.5 <= rotation && rotation < 67.5) {
            return BlockFace.NORTH_EAST;
        } else if(67.5 <= rotation && rotation < 112.5) {
            return BlockFace.EAST;
        } else if(112.5 <= rotation && rotation < 157.5) {
            return BlockFace.SOUTH_EAST;
        } else if(157.5 <= rotation && rotation < 202.5) {
            return BlockFace.SOUTH;
        } else if(202.5 <= rotation && rotation < 247.5) {
            return BlockFace.SOUTH_WEST;
        } else if(247.5 <= rotation && rotation < 292.5) {
            return BlockFace.WEST;
        } else if(292.5 <= rotation && rotation < 337.5) {
            return BlockFace.NORTH_WEST;
        } else if(337.5 <= rotation && rotation < 360.0) {
            return BlockFace.NORTH;
        } else {
            return null;
        }
    }
}
