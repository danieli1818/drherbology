package drherbology.plants;

import java.util.Collection;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import drherbology.plants.conditions.ConditionsSet;
import drherbology.plants.harvest.HarvestRewardFunction;

public interface PlantStateDefinition {

	public PlantState<?> getPlantState();
	
	public PlantState<?> getPlantState(long time);
	
	public PlantType getPlantType();
	
	public void setPlantType(PlantType type);
	
	public Map<ConditionsSet, String> getConditions(PlantState<?> plantState);
	
	public void onHarvest(Player player, ItemStack mainHandHeldItem, ItemStack offHandHeldItem);
	
	public void setHarvestRewardFunctions(Collection<HarvestRewardFunction> harvestRewardFunctions);
	
	public boolean addHarvestRewardFunction(HarvestRewardFunction harvestRewardFunction);
	
	public boolean removeHarvestRewardFunction(HarvestRewardFunction harvestRewardFunction);
	
	public boolean canPlayerHarvestPlant(Player player, ItemStack mainHandHeldItem, ItemStack offHandHeldItem);
	
}
