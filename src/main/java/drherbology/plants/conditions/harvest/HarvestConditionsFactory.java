package drherbology.plants.conditions.harvest;

import drherbology.plants.PlantState;
import drherbology.plants.conditions.Condition;
import drherbology.plants.conditions.ConditionsFactory;

public class HarvestConditionsFactory implements ConditionsFactory {

	@Override
	public Condition createCondition(PlantState<?> plantState) {
		HarvestCondition harvestCondition = new HarvestCondition();
		HarvestConditionsManager.getInstance().addHarvestConditionToPlantState(plantState, harvestCondition);
		return harvestCondition;
	}

}
