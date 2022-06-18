package drherbology.plants;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import drherbology.exceptions.load.LoadPlantException;
import drherbology.parsers.PlantsTypesParser;
import drherbology.plants.conditions.ConditionsFactory;
import drherbology.plants.conditions.harvest.HarvestConditionsFactory;
import drherbology.plants.conditions.time.TimeConditionsFactory;
import drherbology.plants.harvest.ItemStackHarvestRewardFunction;
import drherbology.utils.files.FileConfigurationsManager;
import drherbology.utils.reloader.Reloadable;

public class PlantsTypes implements Reloadable {
	
	private static PlantsTypes instance;

	private Map<String, PlantType> plantsTypes;
	private Set<String> loadedFilenames;
	
	private PlantsTypes() {
		this.plantsTypes = new HashMap<>();
		this.loadedFilenames = new HashSet<>();
	}
	
	public static PlantsTypes getInstance() {
		if (instance == null) {
			instance = new PlantsTypes();
		}
		return instance;
	}
	
	public boolean loadPlant(String filename) throws LoadPlantException {
		FileConfiguration fileConfig = FileConfigurationsManager.getInstance().getFileConfiguration(filename);
		if (fileConfig == null) {
			return false;
		}
		for (String key : fileConfig.getKeys(false)) {
			Object valueObject = fileConfig.get(key);
			if (valueObject == null || !(valueObject instanceof MemorySection)) {
				throw new LoadPlantException(filename
						, "Error loading key \"" + key + "\" of the plant type: " + valueObject + " invalid value!");
			}
			PlantType plantType = PlantsTypesParser.getInstance().parse((MemorySection)valueObject);
			if (plantType == null) {
				throw new LoadPlantException(filename, "Error loading key \"" + key
						+ "\" of the plant type: " + valueObject + " couldn't parse plant type!");
			}
			System.out.println("Loading Key: " + key + " With Value: " + plantType);
			this.plantsTypes.put(key, plantType);
		}

		return true;
	}
	
	public void loadPlants(Collection<String> filenames) throws LoadPlantException {
		for (String filename : filenames) {
			loadPlant(filename);
		}
	}
	
	public boolean doesPlantTypeExist(String plantTypeID) {
		return this.plantsTypes.containsKey(plantTypeID);
	}
	
	public PlantType getPlantType(String plantTypeID) {
		return this.plantsTypes.get(plantTypeID);
	}
	
	public void loadExamplePlant() {
		PlantType exampleType = new PlantType("example_plant");
		ModelPlantStateDefinition initState = new ModelPlantStateDefinition(exampleType, new ItemStack(Material.CHAINMAIL_BOOTS));
		Set<ConditionsFactory> initToFinalConditionsFactories = new HashSet<>();
		initToFinalConditionsFactories.add(new TimeConditionsFactory(20));
		initState.setConditionsOfState("final", initToFinalConditionsFactories);
		ModelPlantStateDefinition finalState = new ModelPlantStateDefinition(exampleType, new ItemStack(Material.DIAMOND_BOOTS));
		Set<ItemStack> itemStacksFinalStateHarvestingReward = new HashSet<>();
		itemStacksFinalStateHarvestingReward.add(new ItemStack(Material.APPLE, 64));
		finalState.addHarvestRewardFunction(new ItemStackHarvestRewardFunction(itemStacksFinalStateHarvestingReward));
		Set<ConditionsFactory> finalToInitConditionsFactories = new HashSet<>();
		finalToInitConditionsFactories.add(new HarvestConditionsFactory());
		finalState.setConditionsOfState("init", finalToInitConditionsFactories);
		exampleType.setState("init", initState);
		exampleType.setState("final", finalState);
		this.plantsTypes.put("example", exampleType);
	}

	@Override
	public void reload() {
		this.plantsTypes.clear();
		loadPlants(loadedFilenames);
	}

	@Override
	public Collection<String> getReloadFilenames() {
		return this.loadedFilenames;
	}
	
	public Collection<PlantType> getPlantTypes() {
		return plantsTypes.values();
	}
	
}
