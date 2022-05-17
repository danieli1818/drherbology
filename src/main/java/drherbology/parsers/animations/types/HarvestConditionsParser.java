package drherbology.parsers.animations.types;

import org.bukkit.configuration.MemorySection;

import drherbology.parsers.Parser;
import drherbology.plants.conditions.ConditionsFactory;
import drherbology.plants.conditions.harvest.HarvestConditionsFactory;

public class HarvestConditionsParser implements Parser<ConditionsFactory, MemorySection> {

	private static HarvestConditionsParser instance;
	
	private HarvestConditionsParser() {
		
	}
	
	public static HarvestConditionsParser getInstance() {
		if (instance == null) {
			instance = new HarvestConditionsParser();
		}
		return instance;
	}
	
	@Override
	public ConditionsFactory parse(MemorySection memorySection) {
		if (memorySection == null) {
			return null;
		}
		return new HarvestConditionsFactory();
	}

}
