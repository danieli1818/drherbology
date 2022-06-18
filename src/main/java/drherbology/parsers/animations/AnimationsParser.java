package drherbology.parsers.animations;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.configuration.MemorySection;

import drherbology.exceptions.parse.ParseException;
import drherbology.parsers.Parser;
import drherbology.parsers.animations.types.HarvestConditionsParser;
import drherbology.parsers.animations.types.TimeConditionsParser;
import drherbology.plants.conditions.Animation;
import drherbology.plants.conditions.ConditionsFactory;

public class AnimationsParser implements Parser<Animation, MemorySection> {

	private static AnimationsParser instance;
	
	private Map<String, Parser<ConditionsFactory, MemorySection>> typesParsers;
	
	private AnimationsParser() {
		this.typesParsers = new HashMap<>();
		this.typesParsers.put("time", TimeConditionsParser.getInstance());
		this.typesParsers.put("harvest", HarvestConditionsParser.getInstance());
	}
	
	public static AnimationsParser getInstance() {
		if (instance == null) {
			instance = new AnimationsParser();
		}
		return instance;
	}
	
	@Override
	public Animation parse(MemorySection memorySection) throws ParseException {
		String toStateID = memorySection.getString("to_state");
		if (toStateID == null) {
			throw new ParseException("To state is missing!");
		}
		Object conditionsSetObject = memorySection.get("conditions");
		if (conditionsSetObject == null || !(conditionsSetObject instanceof MemorySection)) {
			throw new ParseException("No conditions are defined!");
		}
		MemorySection conditionsSetMemorySection = (MemorySection)conditionsSetObject;
		Set<ConditionsFactory> conditionsFactories = new HashSet<>();
		for (String conditionKey : conditionsSetMemorySection.getKeys(false)) {
			Object conditionObject = conditionsSetMemorySection.get(conditionKey);
			if (conditionObject == null || !(conditionObject instanceof MemorySection)) {
				throw new ParseException("Condition isn't valid!");
			}
			MemorySection conditionMemorySection = (MemorySection)conditionObject;
			ConditionsFactory conditionsFactory = parseCondition(conditionMemorySection);
			conditionsFactories.add(conditionsFactory);
			return new Animation(toStateID, conditionsFactories);
		}
		throw new ParseException("No conditions are defined!");
	}
	
	private ConditionsFactory parseCondition(MemorySection conditionMemorySection) {
		String type = conditionMemorySection.getString("type");
		if (type == null) {
			throw new ParseException("Condition's type is missing!");
		}
		Parser<ConditionsFactory, MemorySection> typeParser = this.typesParsers.get(type);
		if (typeParser == null) {
			throw new ParseException("Unknown condition type: " + type + "!");
		}
		return typeParser.parse(conditionMemorySection);
	}

}
