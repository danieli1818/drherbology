package drherbology.plants;

import java.util.Map;
import java.util.Set;

import drherbology.plants.conditions.Animation;
import drherbology.plants.conditions.ConditionsFactory;
import drherbology.plants.conditions.ConditionsSet;

public interface ConditionsPlantStateDefinition extends PlantStateDefinition {

	public Map<ConditionsSet, String> getConditions(PlantState<?> plantState);
	
	public Set<ConditionsFactory> setConditionsOfState(String state, Set<ConditionsFactory> conditionsFactories);
	
	public Set<ConditionsFactory> setConditionsOfState(Animation animation);
	
}
