package drherbology.parsers.harvest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.MemorySection;

import drherbology.exceptions.parse.ParseException;
import drherbology.parsers.Parser;
import drherbology.parsers.harvest.types.ItemHarvestRewardParser;
import drherbology.plants.harvest.HarvestRewardFunction;

public class HarvestRewardsParser implements Parser<List<HarvestRewardFunction>, List<MemorySection>> {
	
	private static HarvestRewardsParser instance;
	
	private Map<String, Parser<HarvestRewardFunction, MemorySection>> typesParsers;
	
	private HarvestRewardsParser() {
		this.typesParsers = new HashMap<>();
		this.typesParsers.put("item", ItemHarvestRewardParser.getInstance());
	}
	
	public static HarvestRewardsParser getInstance() {
		if (instance == null) {
			instance = new HarvestRewardsParser();
		}
		return instance;
	}

	@Override
	public List<HarvestRewardFunction> parse(List<MemorySection> memorySections) throws ParseException {
		List<HarvestRewardFunction> harvestRewardsFunctions = new ArrayList<>();
		for (MemorySection memorySection : memorySections) {
			if (memorySection == null) {
				continue;
			}
			String type = memorySection.getString("type");
			if (type == null) {
				throw new ParseException("Missing type in harvest rewards!");
			}
			Parser<HarvestRewardFunction, MemorySection> typeParser = this.typesParsers.get(type);
			if (typeParser == null) {
				throw new ParseException("Unknown harvest reward type: " + type);
			}
			HarvestRewardFunction harvestRewardFunction = typeParser.parse(memorySection);
			if (harvestRewardFunction == null) {
				throw new ParseException("Invalid harvest reward: " + memorySection.getCurrentPath() + " with the type: " + type);
			}
			harvestRewardsFunctions.add(harvestRewardFunction);
		}
		return harvestRewardsFunctions;
	}

}
