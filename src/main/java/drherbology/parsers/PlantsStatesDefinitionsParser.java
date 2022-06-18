package drherbology.parsers;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import drherbology.exceptions.parse.ParseException;
import drherbology.parsers.animations.AnimationsParser;
import drherbology.parsers.harvest.HarvestRewardsParser;
import drherbology.plants.ConditionsPlantStateDefinition;
import drherbology.plants.ModelPlantStateDefinition;
import drherbology.plants.PlantStateDefinition;
import drherbology.plants.conditions.Animation;
import drherbology.plants.harvest.HarvestRewardFunction;
import drherbology.utils.files.ConfigurationsHelper;
import drherbology.utils.files.parsers.MathParser;

public class PlantsStatesDefinitionsParser implements Parser<PlantStateDefinition, MemorySection> {
	
	private static PlantsStatesDefinitionsParser instance;
	
	private PlantsStatesDefinitionsParser() {
		
	}
	
	public static PlantsStatesDefinitionsParser getInstance() {
		if (instance == null) {
			instance = new PlantsStatesDefinitionsParser();
		}
		return instance;
	}

	@Override
	public PlantStateDefinition parse(MemorySection memorySection) throws ParseException {
		if (memorySection == null) {
			throw new ParseException("Invalid plant state definition data!");
		}
		String type = memorySection.getString("type");
		ConditionsPlantStateDefinition plantStateDefinition = null;
		if ("model".equals(type)) {
			plantStateDefinition = parseModel(memorySection);
		} else if ("block".equals(type)) {
			plantStateDefinition =  parseBlock(memorySection);
		} else {
			throw new ParseException("Unknown plant state definition type!");
		}
		if (plantStateDefinition == null) {
			return null;
		}
		Object animationsObject = memorySection.get("animations");
		if (animationsObject == null || !(animationsObject instanceof MemorySection)) {
			return plantStateDefinition;
		}
		MemorySection animationsMemorySection = (MemorySection)animationsObject;
		for (String animation_id : animationsMemorySection.getKeys(false)) {
			Object animationObject = animationsMemorySection.get(animation_id);
			if (animationObject == null || !(animationObject instanceof MemorySection)) {
				continue;
			}
			MemorySection animationMemorySection = (MemorySection)animationObject;
			Animation animation = AnimationsParser.getInstance().parse(animationMemorySection);
			if (animation == null) {
				continue;
			}
			plantStateDefinition.setConditionsOfState(animation);
		}
		Object harvestRewardsObject = memorySection.get("harvest_rewards");
		if (harvestRewardsObject != null && harvestRewardsObject instanceof MemorySection) {
			MemorySection harvestRewardsMemorySection = (MemorySection)harvestRewardsObject;
			List<MemorySection> memorySections = ConfigurationsHelper.getInstance().getListOfMemorySections(harvestRewardsMemorySection);
			if (memorySections == null) {
				throw new ParseException("Harvest rewards isn't valid!");
			}
			List<HarvestRewardFunction> harvestRewardsFunctions = HarvestRewardsParser.getInstance().parse(memorySections);
			if (harvestRewardsFunctions == null) {
				throw new ParseException("Harvest rewards isn't valid!");
			}
			for (HarvestRewardFunction harvestRewardFunction : harvestRewardsFunctions) {
				if (harvestRewardFunction == null) {
					continue;
				}
				plantStateDefinition.addHarvestRewardFunction(harvestRewardFunction);
			}
		}
		return plantStateDefinition;
	}
	
	private ConditionsPlantStateDefinition parseModel(MemorySection memorySection) {
		MemorySection itemMS = ConfigurationsHelper.getInstance().getMemorySection(memorySection, "item");
		if (itemMS == null) {
			throw new ParseException("Missing model plant state definition's item!");
		}
		ItemStack itemStack = ItemStackParser.getInstance().parse(itemMS);
		if (itemStack == null) {
			throw new ParseException("Invalid model plant state definition's item!");
		}
		String vectorOffsetString = memorySection.getString("offset");
		if (vectorOffsetString == null) {
			return new ModelPlantStateDefinition(itemStack);
		}
		Vector vectorOffset = MathParser.parseVector(vectorOffsetString);
		if (vectorOffset == null) {
			return new ModelPlantStateDefinition(itemStack);
		}
		return new ModelPlantStateDefinition(itemStack, vectorOffset);
	}
	
	private ConditionsPlantStateDefinition parseBlock(MemorySection memorySection) {
		return null;
	}

}
