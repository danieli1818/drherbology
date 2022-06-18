package drherbology.parsers.harvest.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;

import drherbology.exceptions.parse.ParseException;
import drherbology.parsers.ItemStackParser;
import drherbology.parsers.Parser;
import drherbology.plants.harvest.HarvestRewardFunction;
import drherbology.plants.harvest.ItemStackHarvestRewardFunction;
import drherbology.utils.files.ConfigurationsHelper;

public class ItemHarvestRewardParser implements Parser<HarvestRewardFunction, MemorySection> {
	
	private static ItemHarvestRewardParser instance;
	
	private ItemHarvestRewardParser() {
		
	}
	
	public static ItemHarvestRewardParser getInstance() {
		if (instance == null) {
			instance = new ItemHarvestRewardParser();
		}
		return instance;
	}

	@Override
	public HarvestRewardFunction parse(MemorySection memorySection) throws ParseException {
		if (memorySection == null) {
			return null;
		}
		Object itemsObject = memorySection.get("items");
		if (itemsObject == null || !(itemsObject instanceof MemorySection)) {
			throw new ParseException("Items are missing!");
		}
		MemorySection itemsMemorySection = (MemorySection)itemsObject;
		List<MemorySection> itemStacksMemorySections = ConfigurationsHelper.getInstance().getListOfMemorySections(itemsMemorySection);
		if (itemStacksMemorySections == null) {
			throw new ParseException("Invalid items!");
		}
		Collection<ItemStack> itemStacksReward = new ArrayList<>();
		for (MemorySection itemStackMemorySection : itemStacksMemorySections) {
			ItemStack itemStack = ItemStackParser.getInstance().parse(itemStackMemorySection);
			if (itemStack == null) {
				throw new ParseException("Invalid item: " + itemsMemorySection.getCurrentPath());
			}
			itemStacksReward.add(itemStack);
		}
		return new ItemStackHarvestRewardFunction(itemStacksReward);
	}

}
