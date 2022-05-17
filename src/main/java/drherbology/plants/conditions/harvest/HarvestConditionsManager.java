package drherbology.plants.conditions.harvest;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import drherbology.plants.PlantState;
import drherbology.plants.harvest.events.HarvestEvent;

public class HarvestConditionsManager implements Listener {

	private static HarvestConditionsManager instance;
	
	private ConcurrentHashMap<PlantState<?>, Set<HarvestCondition>> harvestConditionsOfPlantStates;
	
	private HarvestConditionsManager() {
		this.harvestConditionsOfPlantStates = new ConcurrentHashMap<>();
	}
	
	public boolean addHarvestConditionToPlantState(PlantState<?> plantState, HarvestCondition harvestCondition) {
		if (harvestCondition == null) {
			return false;
		}
		Set<HarvestCondition> harvestConditionsOfPlantState = 
				this.harvestConditionsOfPlantStates.computeIfAbsent(plantState, (PlantState<?> plantStateArg) -> ConcurrentHashMap.newKeySet());
		System.out.println("Harvest Conditions Of PlantState: " + harvestConditionsOfPlantState);
		return harvestConditionsOfPlantState.add(harvestCondition);
	}
	
	public boolean removeHarvestConditionOfPlantState(PlantState<?> plantState, HarvestCondition harvestCondition) {
		if (harvestCondition == null) {
			return false;
		}
		Set<HarvestCondition> harvestConditionsOfPlantState = 
				this.harvestConditionsOfPlantStates.get(plantState);
		if (harvestConditionsOfPlantState == null) {
			return false;
		}
		return harvestConditionsOfPlantState.remove(harvestCondition);
	}
	
	public boolean removeAllHarvestConditionsOfPlantState(PlantState<?> plantState) {
		return this.harvestConditionsOfPlantStates.remove(plantState) != null;
	}
	
	public static HarvestConditionsManager getInstance() {
		if (instance == null) {
			instance = new HarvestConditionsManager();
		}
		return instance;
	}
	
	@EventHandler
	public void onHarvestEvent(HarvestEvent event) {
		System.out.println("HarvestEvent has occured: " + event);
		if (!event.isCancelled()) {
			Set<HarvestCondition> harvestConditions = this.harvestConditionsOfPlantStates.remove(event.getPlantState());
			if (harvestConditions == null) {
				return;
			}
			for (HarvestCondition harvestCondition : harvestConditions) {
				harvestCondition.onHarvest();
			}
		}
	}
	
}
