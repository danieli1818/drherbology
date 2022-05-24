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
	
	public boolean loadPlant(String filename) {
		FileConfiguration fileConfig = FileConfigurationsManager.getInstance().getFileConfiguration(filename);
		if (fileConfig == null) {
			return false;
		}
		for (String key : fileConfig.getKeys(false)) {
			System.out.println("Values: " + fileConfig.getValues(false));
			Object valueObject = fileConfig.get(key);
			if (valueObject == null || !(valueObject instanceof MemorySection)) {
				// TODO Error loading key plant type
				System.out.println("Error loading key \"" + key + "\" key plant type!");
				System.out.println("Key \"" + key + "\" plant type: " + valueObject);
				continue;
			}
			PlantType plantType = PlantsTypesParser.getInstance().parse((MemorySection)valueObject);
			if (plantType == null) {
				// TODO Error parsing key plant type
				System.out.println("Error parsing key \"" + key + "\" key plant type!");
				continue;
			}
			this.plantsTypes.put(key, plantType);
		}

		return true;
	}
	
	public void loadPlants(Collection<String> filenames) {
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
	
	public Map<ItemStack, PlantType> getPlantTypesItemStacksMap() {
		Map<ItemStack, PlantType> plantTypesItemStacksMap = new HashMap<>();
		for (PlantType plantType : plantsTypes.values()) {
			ItemStack plantTypeItemStack = plantType.getItemStack();
			if (plantTypeItemStack != null) {
				plantTypesItemStacksMap.put(new ItemStack(plantTypeItemStack), plantType);
			}
		}
		return plantTypesItemStacksMap;
	}
	
}
