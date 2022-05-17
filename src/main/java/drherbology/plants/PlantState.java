package drherbology.plants;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import drherbology.utils.PropertyChangeObservable;

public interface PlantState<T extends PlantStateDefinition> extends PropertyChangeObservable {

	public void spawnPlant(Location location);
	
	public void despawnPlant();
	
	public void logicSpawnPlant();
	
	public void physicDespawnPlant();
	
	public T getPlantStateDefinition();
	
	public void onInteract(Player player, ItemStack mainHandHeldItem, ItemStack offHandHeldItem);
	
	public void onHarvest(Player player, ItemStack mainHandHeldItem, ItemStack offHandHeldItem);
	
	public PlantState<?> transformToPlantStateDefinition(PlantStateDefinition plantStateDefinition);
	
	public PlantState<?> transformToPlantStateDefinition(PlantStateDefinition plantStateDefinition, long time);
	
	public long getSpawnTime();
	
}
