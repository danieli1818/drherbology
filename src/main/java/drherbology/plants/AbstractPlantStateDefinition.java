package drherbology.plants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import drherbology.plants.conditions.Animation;
import drherbology.plants.conditions.ConditionsFactory;
import drherbology.plants.conditions.ConditionsSet;
import drherbology.plants.harvest.HarvestRewardFunction;

public abstract class AbstractPlantStateDefinition implements ConditionsPlantStateDefinition {

	private PlantType plantType;
	private Map<String, Set<ConditionsFactory>> conditionsFactoriesMap;
	private List<HarvestRewardFunction> harvestRewardFunctions;
	
	public AbstractPlantStateDefinition(PlantType plantType) {
		this();
		this.plantType = plantType;
	}
	
	public AbstractPlantStateDefinition() {
		this.conditionsFactoriesMap = new HashMap<>();
		this.harvestRewardFunctions = new ArrayList<>();
	}
	
	public PlantType getPlantType() {
		return this.plantType;
	}
	
	public void setPlantType(PlantType plantType) {
		this.plantType = plantType;
	}
	
	public Map<ConditionsSet, String> getConditions(PlantState<?> plantState) {
		Map<ConditionsSet, String> conditionsSets = new HashMap<>();
		for (Map.Entry<String, Set<ConditionsFactory>> conditionsFactoriesSet : this.conditionsFactoriesMap.entrySet()) {
			conditionsSets.put(getConditionsSet(conditionsFactoriesSet.getValue(), plantState), conditionsFactoriesSet.getKey());
		}
		return conditionsSets;
	}
	
	private ConditionsSet getConditionsSet(Set<ConditionsFactory> conditionsFactoriesSet, PlantState<?> plantState) {
		ConditionsSet conditionsSet = new ConditionsSet();
		for (ConditionsFactory conditionsFactory : conditionsFactoriesSet) {
			conditionsSet.addCondition(conditionsFactory.createCondition(plantState));
		}
		conditionsSet.printConditionsSet();
		return conditionsSet;
	}
	
	public Set<ConditionsFactory> setConditionsOfState(String state, Set<ConditionsFactory> conditionsFactories) {
		if (state == null) {
			return null;
		}
		if (conditionsFactories == null) {
			return this.conditionsFactoriesMap.remove(state);
		}
		return this.conditionsFactoriesMap.put(state, conditionsFactories);
	}
	
	public Set<ConditionsFactory> setConditionsOfState(Animation animation) {
		if (animation == null) {
			return null;
		}
		return setConditionsOfState(animation.getToStateID(), animation.getConditionsFactories());
	}
	
	@Override
	public void onHarvest(Player player, ItemStack mainHandHeldItem, ItemStack offHandHeldItem) {
		for (HarvestRewardFunction harvestRewardFunction : harvestRewardFunctions) {
			harvestRewardFunction.runInSyncMode(player);
		}
	}
	
	public boolean canPlayerHarvestPlant(Player Player, ItemStack mainHandHeldItem, ItemStack offHandHeldItem) {
		return true;
	}
	
	@Override
	public boolean addHarvestRewardFunction(HarvestRewardFunction harvestRewardFunction) {
		if (harvestRewardFunction == null) {
			return false;
		}
		this.harvestRewardFunctions.add(harvestRewardFunction);
		return true;
	}
	
	@Override
	public boolean removeHarvestRewardFunction(HarvestRewardFunction harvestRewardFunction) {
		if (harvestRewardFunction == null) {
			return false;
		}
		return this.harvestRewardFunctions.remove(harvestRewardFunction);
	}
	
	@Override
	public void setHarvestRewardFunctions(Collection<HarvestRewardFunction> harvestRewardFunctions) {
		this.harvestRewardFunctions.clear();
		for (HarvestRewardFunction harvestRewardFunction : harvestRewardFunctions) {
			if (harvestRewardFunction != null) {
				this.harvestRewardFunctions.add(harvestRewardFunction);
			}
		}
		
	}
	
}
