package drherbology.parsers;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import drherbology.exceptions.parse.ParseException;

public class ItemStackParser implements Parser<ItemStack, MemorySection> {
	
	private static ItemStackParser instance;
	
	private ItemStackParser() {
		
	}
	
	public static ItemStackParser getInstance() {
		if (instance == null) {
			instance = new ItemStackParser();
		}
		return instance;
	}

	@Override
	public ItemStack parse(MemorySection memorySection) throws ParseException {
		if (memorySection == null) {
			throw new ParseException("Missing ItemStack data!");
		}
		String type = memorySection.getString("type");
		if (type == null) {
			throw new ParseException("Missing ItemStack type!");
		}
		type = type.toUpperCase();
		Material material = getMaterial(type);
		if (material == null) {
			throw new ParseException("Unknown ItemStack's type: \"" + type + "\"!");
		}
		ItemStack itemStack = new ItemStack(material);
		ItemMeta itemMeta = itemStack.getItemMeta();
		if (memorySection.contains("amount")) {
			int amount = memorySection.getInt("amount");
			if (amount <= 0) {
				throw new ParseException("Invalid ItemStack's amount. Should be an integer that is bigger than 0");
			}
			itemStack.setAmount(amount);
		}
		if (memorySection.contains("display_name")) {
			String displayName = memorySection.getString("display_name");
			if (displayName == null) {
				throw new ParseException("Invalid ItemStack's display name!");
			}
			itemMeta.setDisplayName(displayName);
		}
		if (memorySection.contains("lore")) {
			List<String> lore = memorySection.getStringList("lore");
			if (lore.isEmpty()) {
				throw new ParseException("Invalid ItemStack's lore!");
			}
			itemMeta.setLore(lore);
		}
		if (memorySection.contains("unbreakable")) {
			boolean unbreakable = memorySection.getBoolean("unbreakable");
			itemMeta.setUnbreakable(unbreakable);
		}
		itemStack.setItemMeta(itemMeta);
		return itemStack;
	}
	
	private static Material getMaterial(String material) {
		for (Material materialValue : Material.values()) {
			if (materialValue.name().equals(material)) {
				return materialValue;
			}
		}
		return null;
	}

}
