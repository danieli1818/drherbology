package drherbology.parsers.animations.types;

import org.bukkit.configuration.MemorySection;

import drherbology.parsers.Parser;
import drherbology.plants.conditions.ConditionsFactory;
import drherbology.plants.conditions.time.TimeConditionsFactory;

public class TimeConditionsParser implements Parser<ConditionsFactory, MemorySection> {

	private static TimeConditionsParser instance;
	
	private TimeConditionsParser() {
		
	}
	
	public static TimeConditionsParser getInstance() {
		if (instance == null) {
			instance = new TimeConditionsParser();
		}
		return instance;
	}
	
	@Override
	public ConditionsFactory parse(MemorySection memorySection) {
		if (memorySection == null) {
			return null;
		}
		Integer ticks = memorySection.getInt("time");
		return new TimeConditionsFactory(ticks);
	}

}
