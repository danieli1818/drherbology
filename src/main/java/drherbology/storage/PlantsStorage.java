package drherbology.storage;

import java.util.Map;
import java.util.Set;

import org.bukkit.Chunk;
import org.bukkit.Location;

import drherbology.plants.PlantState;

public interface PlantsStorage {

	public boolean store(Location location, PlantState<?> plantState);
	
	public boolean store(Map<Location, PlantState<?>> plantStates);
	
	public PlantState<?> load(Location location);
	
	public Map<Location, PlantState<?>> loadChunk(Chunk chunk);
	
	public Map<Location, PlantState<?>> loadAll();
	
	public Set<Location> getLocations();
	
}
