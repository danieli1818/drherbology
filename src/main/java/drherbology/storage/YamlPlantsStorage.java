package drherbology.storage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

import drherbology.plants.PlantState;
import drherbology.plants.PlantStateDefinition;
import drherbology.plants.PlantType;
import drherbology.plants.PlantsTypes;
import drherbology.utils.files.FileConfigurationsManager;
import drherbology.utils.files.parsers.LocationParser;
import drherbology.utils.locations.LocationsUtils;

public class YamlPlantsStorage implements PlantsStorage {

	private String filename;
	
	private static YamlPlantsStorage instance;
	
	private YamlPlantsStorage(String filename) {
		this.filename = filename;
	}
	
	public static YamlPlantsStorage getInstance(String filename) {
		if (instance == null || !instance.filename.equals(filename)) {
			FileConfigurationsManager.getInstance().createConfigurationFile(filename);
			instance = new YamlPlantsStorage(filename);
			instance.load();
		}
		return instance;
	}
	
	public static YamlPlantsStorage getInstance() {
		if (instance == null) {
			getInstance("storage.yml");
		}
		return instance;
	}
	
	public boolean load() {
		FileConfiguration fileConfig = FileConfigurationsManager.getInstance().reloadFile(filename);
		if (fileConfig == null) {
			return false;
		}
		return true;
	}

	@Override
	public boolean store(Location location, PlantState<?> plantState) {
		FileConfiguration fileConfig = FileConfigurationsManager.getInstance().getFileConfiguration(filename);
		if (!storeWithoutSaving(location, plantState, fileConfig)) {
			return false;
		}
		return FileConfigurationsManager.getInstance().saveFileConfigurationToFile(filename);
	}
	
	private boolean storeWithoutSaving(Location location, PlantState<?> plantState, FileConfiguration fileConfig) {
		if (location == null || plantState == null || fileConfig == null) {
			return false;
		}
		String locationStr = LocationParser.parse(location);
		PlantStateDefinition plantStateDefinition = plantState.getPlantStateDefinition();
		PlantType plantType = plantStateDefinition.getPlantType();
		String stateID = plantType.getPlantStateDefinitionID(plantStateDefinition);
		if (stateID == null) {
			return false;
		}
		String typeID = plantType.getPlantTypeID();
		fileConfig.set(locationStr, typeID + "," + stateID + "," + plantState.getSpawnTime());
		return true;
	}
	
	@Override
	public boolean store(Map<Location, PlantState<?>> plantStates) {
		if (plantStates == null) {
			return false;
		}
		if (plantStates.isEmpty()) {
			return true;
		}
		boolean resultFlag = true;
		FileConfiguration fileConfig = FileConfigurationsManager.getInstance().getFileConfiguration(filename);
		for (Map.Entry<Location, PlantState<?>> plantStateEntry : plantStates.entrySet()) {
			Location location = plantStateEntry.getKey();
			PlantState<?> plantState = plantStateEntry.getValue();
			if (location == null || plantState == null) {
				resultFlag = false;
				continue;
			}
			if (!storeWithoutSaving(location, plantState, fileConfig)) {
				resultFlag = false;
			}
		}
		FileConfigurationsManager.getInstance().saveFileConfigurationToFile(filename);
		return resultFlag;
	}
	
	private PlantState<?> load(String locationStr, FileConfiguration fileConfig) {
		String typeIDAndStateID = fileConfig.getString(locationStr);
		if (typeIDAndStateID == null) {
			return null;
		}
		String[] typeIDAndStateIDArray = typeIDAndStateID.split(",");
		if (typeIDAndStateIDArray.length != 3) {
			return null;
		}
		String typeID = typeIDAndStateIDArray[0];
		String stateID = typeIDAndStateIDArray[1];
		PlantType plantType = PlantsTypes.getInstance().getPlantType(typeID);
		if (plantType == null) {
			return null;
		}
		PlantStateDefinition plantStateDefinition = plantType.getState(stateID);
		if (plantStateDefinition == null) {
			return null;
		}
		try {
			Long spawnTime = Long.parseLong(typeIDAndStateIDArray[2]);
			return plantStateDefinition.getPlantState(spawnTime);
		} catch (NumberFormatException e) {
			return plantStateDefinition.getPlantState();
		}
	}

	@Override
	public PlantState<?> load(Location location) {
		if (location == null) {
			return null;
		}
		FileConfiguration fileConfig = FileConfigurationsManager.getInstance().getFileConfiguration(filename);
		if (fileConfig == null) {
			return null;
		}
		String locationStr = LocationParser.parse(location);
		return load(locationStr, fileConfig);
	}

	@Override
	public Map<Location, PlantState<?>> loadAll() {
		FileConfiguration fileConfig = FileConfigurationsManager.getInstance().getFileConfiguration(filename);
		if (fileConfig == null) {
			return null;
		}
		Map<Location, PlantState<?>> plantStates = new HashMap<>();
		for (String key : fileConfig.getKeys(false)) {
			Location location = LocationParser.parse(key);
			if (location == null) {
				System.err.println("Error loading location: " + key + "!");
				continue;
			}
			PlantState<?> plantState = load(key, fileConfig);
			if (plantState == null) {
				System.err.println("Error loading location: " + key + "!");
				continue;
			}
			plantStates.put(location, plantState);
		}
		return plantStates;
	}

	@Override
	public Set<Location> getLocations() {
		Set<Location> locations = new HashSet<>();
		FileConfiguration fileConfig = FileConfigurationsManager.getInstance().getFileConfiguration(filename);
		if (fileConfig == null) {
			return null;
		}
		for (String key : fileConfig.getKeys(false)) {
			Location location = LocationParser.parse(key);
			if (location == null) {
				System.err.println("Error loading location: " + key + "!");
				continue;
			}
			locations.add(location);
		}
		return locations;
	}
	
	@Override
	public Map<Location, PlantState<?>> loadChunk(Chunk chunk) {
		Set<Location> chunkLocations = getChunkLocations(chunk);
		if (chunkLocations == null) {
			return null;
		}
		Map<Location, PlantState<?>> plants = new HashMap<>();
		for (Location location : chunkLocations) {
			PlantState<?> plant = load(location);
			if (plant == null) {
				continue;
			}
			plants.put(location, plant);
		}
		return plants;
	}
	
	private Set<Location> getChunkLocations(Chunk chunk) {
		Set<Location> locations = getLocations();
		if (locations == null) {
			return null;
		}
		return locations.stream().filter((Location location) -> LocationsUtils.isLocationInChunk(location, chunk)).collect(Collectors.toSet());
	}
	
}
