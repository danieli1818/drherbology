package drherbology.parsers;

import org.bukkit.Bukkit;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;

import drherbology.exceptions.parse.ParseException;
import drherbology.plants.PlantStateDefinition;
import drherbology.plants.PlantType;
import drherbology.utils.files.ConfigurationsHelper;

public class PlantsTypesParser implements Parser<PlantType, MemorySection> {

	private static PlantsTypesParser instance;
	
	private PlantsTypesParser() {
		
	}
	
	public static PlantsTypesParser getInstance() {
		if (instance == null) {
			instance = new PlantsTypesParser();
		}
		return instance;
	}
	
	@Override
	public PlantType parse(MemorySection memorySection) throws ParseException {
		if (memorySection == null) {
			return null;
		}
		String plantTypeID = memorySection.getName();
		if (plantTypeID == null) {
			throw new ParseException("Plant Type ID is null!");
		}
		System.out.println("Loading Plant Type ID: " + plantTypeID);
		PlantType plantType = new PlantType(plantTypeID);
		Object statesMSObject = memorySection.get("states");
		if (statesMSObject == null) {
			Bukkit.getLogger().warning("Plant Type ID: " + plantTypeID + " doesn't have any states!");
			return plantType;
		}
		if (!(statesMSObject instanceof MemorySection)) {
			throw new ParseException("Plant Type ID: " + plantTypeID + "'s states aren't valid!");
		}
		MemorySection statesMS = (MemorySection)statesMSObject;
		for (String stateID : statesMS.getKeys(false)) {
			System.out.println("Loading stateID: " + stateID);
			Object stateObject = statesMS.get(stateID);
			if (stateObject != null && stateObject instanceof MemorySection) {
				PlantStateDefinition plantStateDefinition = PlantsStatesDefinitionsParser.getInstance()
						.parse((MemorySection)stateObject);
				if (plantStateDefinition == null) {
					continue;
				}
				plantStateDefinition.setPlantType(plantType);
				plantType.setState(stateID, plantStateDefinition);
			} else {
				throw new ParseException("Plant Type ID: " + plantTypeID + "'s state: " + stateID + " isn't valid!");
			}
		}
		MemorySection itemMS = ConfigurationsHelper.getInstance().getMemorySection(memorySection, "item");
		if (itemMS != null) {
			ItemStack itemStack = ItemStackParser.getInstance().parse(itemMS);
			if (itemStack != null) {
				plantType.setItemStack(itemStack);
			}
		}
		return plantType;
	}

}
