package drherbology.plants.conditions;

import drherbology.plants.PlantState;

public interface ConditionsFactory {

	public Condition createCondition(PlantState<?> plantState);
	
}
