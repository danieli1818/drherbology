package drherbology.management;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.NullArgumentException;
import org.bukkit.Chunk;
import org.bukkit.Location;

import drherbology.exceptions.creation.PlantCreationException;
import drherbology.plants.PlantState;
import drherbology.plants.PlantStateDefinition;
import drherbology.plants.PlantType;
import drherbology.plants.PlantsTypes;
import drherbology.plants.datatypes.PlantNextStateInfo;
import drherbology.storage.PlantsStorage;
import drherbology.storage.YamlPlantsStorage;
import drherbology.utils.SchedulerUtils;
import drherbology.utils.locations.LocationsUtils;

public class PlantsManager implements PropertyChangeListener {

	private Map<Location, PlantState<?>> plants;
	
	private PlantsStorage storage;
	
	private static PlantsManager instance;
	
	public static PlantsManager getInstance() {
		if (instance == null) {
			instance = new PlantsManager();
		}
		return instance;
	}
	
	public static PlantsManager getInstance(PlantsStorage storage) {
		if (instance == null) {
			instance = new PlantsManager(storage);
		}
		return instance;
	}
	
	private PlantsManager(PlantsStorage storage) {
		this.plants = new HashMap<>();
		this.storage = storage;
	}
	
	private PlantsManager() {
		this.plants = new HashMap<>();
		this.storage = YamlPlantsStorage.getInstance("storage.yml");
	}
	
	public boolean createPlant(Location location, String plantTypeID) {
		return createPlant(location, plantTypeID, null);
	}
	
	public boolean createPlant(Location location, String plantTypeID, String stateID) throws PlantCreationException {
		PlantType plantType = PlantsTypes.getInstance().getPlantType(plantTypeID);
		if (plantType == null) {
			throw new PlantCreationException(plantTypeID, "Plant type doesn't exist!");
		}
		return createPlant(location, plantType, stateID);
	}
	
	public boolean createPlant(Location location, PlantType plantType, String stateID) throws NullArgumentException, PlantCreationException {
		if (plantType == null) {
			throw new NullArgumentException("plantType");
		}
		PlantStateDefinition state = null;
		if (stateID == null) {
			state = plantType.getInitialState();
		} else {
			state = plantType.getState(stateID);
		}
		if (state == null) {
			throw new PlantCreationException(plantType.getPlantTypeID(), stateID, "State doesn't exist!");
		}
		PlantState<?> plantState = state.getPlantState();
		return spawnPlant(location, plantState);
	}
	
	public boolean createPlant(Location location, PlantType plantType, String stateID, long time) throws NullArgumentException, PlantCreationException {
		if (time == 0) {
			createPlant(location, plantType, stateID);
		}
		if (plantType == null) {
			throw new NullArgumentException("plantType");
		}
		PlantStateDefinition state = null;
		if (stateID == null) {
			state = plantType.getInitialState();
		} else {
			state = plantType.getState(stateID);
		}
		if (state == null) {
			throw new PlantCreationException(plantType.getPlantTypeID(), stateID, "State doesn't exist!");
		}
		PlantState<?> plantState = state.getPlantState(time);
		return spawnPlant(location, plantState);
	}
	
	private boolean spawnPlant(Location location, PlantState<?> plantState) {
		if (location == null || plantState == null) {
			return false;
		}
		this.plants.put(location, plantState);
		plantState.addPropertyChangeListener(this);
		plantState.spawnPlant(location);
		return true;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (!(evt.getSource() instanceof PlantState) || !"nextState".equals(evt.getPropertyName()) || !(evt.getNewValue() instanceof PlantNextStateInfo)) {
			return;
		}
		PlantState<?> plantState = (PlantState<?>)evt.getSource();
		Location plantStateLocation = getLocationOfPlantState(plantState);
		SchedulerUtils.getInstance().scheduleSyncTask(0, () -> {
			PlantType plantType = plantState.getPlantStateDefinition().getPlantType();
			PlantNextStateInfo newPlantStateInfo = (PlantNextStateInfo)evt.getNewValue();
			if (plantState.transformToPlantStateDefinition(plantType.getState(newPlantStateInfo.getNextState()), newPlantStateInfo.getTime()) == null) {
				despawnPlant(plantState);
				createPlant(plantStateLocation, plantType, newPlantStateInfo.getNextState(), newPlantStateInfo.getTime());
			}
		});
	}
	
	public boolean despawnPlant(PlantState<?> plantState) {
		Location location = getLocationOfPlantState(plantState);
		if (location != null) {
			this.plants.remove(location);
			plantState.despawnPlant();
			return true;
		}
		return false;
	}
	
	private Location getLocationOfPlantState(PlantState<?> plantState) {
		for (Map.Entry<Location, PlantState<?>> locationAndPlantState : this.plants.entrySet()) {
			if (locationAndPlantState.getValue() == plantState) {
				return locationAndPlantState.getKey();
			}
		}
		return null;
	}
	
	public boolean loadChunk(Chunk chunk) {
		Map<Location, PlantState<?>> chunkPlants = this.storage.loadChunk(chunk);
		if (chunkPlants == null) {
			return false;
		}
		for (Map.Entry<Location, PlantState<?>> chunkPlantEntry : chunkPlants.entrySet()) {
			Location location = chunkPlantEntry.getKey();
			PlantState<?> chunkPlant = chunkPlantEntry.getValue();
			spawnPlant(location, chunkPlant);
		}
		return true;
	}
	
	public boolean saveChunk(Chunk chunk) {
		return this.storage.store(getSpawnedPlantsInChunk(chunk));
	}
	
	public boolean saveChunkAndDespawnPlants(Chunk chunk) {
		Map<Location, PlantState<?>> plantStatesInChunk = getSpawnedPlantsInChunk(chunk);
		plantStatesInChunk.forEach((Location location, PlantState<?> plantState) -> plantState.despawnPlant());
		return this.storage.store(plantStatesInChunk);
	}
	
	private Map<Location, PlantState<?>> getSpawnedPlantsInChunk(Chunk chunk) {
		Map<Location, PlantState<?>> plantsInChunk = new HashMap<>();
		for (Map.Entry<Location, PlantState<?>> plantStateEntry : this.plants.entrySet()) {
			Location location = plantStateEntry.getKey();
			if (LocationsUtils.isLocationInChunk(location, chunk)) {
				PlantState<?> plant = plantStateEntry.getValue();
				plantsInChunk.put(location, plant);
			}
		}
		return plantsInChunk;
	}
	
	public boolean saveAll() {
		for (Map.Entry<Location, PlantState<?>> plantStateEntry : this.plants.entrySet()) {
			Location location = plantStateEntry.getKey();
			PlantState<?> plant = plantStateEntry.getValue();
			this.storage.store(location, plant);
			plant.despawnPlant();
		}
		return true;
	}
	
}
