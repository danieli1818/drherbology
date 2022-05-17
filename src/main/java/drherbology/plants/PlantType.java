package drherbology.plants;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.inventory.ItemStack;

public class PlantType {

	private String initialStateID;
	private ItemStack itemStack;
	private String plantTypeID;
	private Map<String, PlantStateDefinition> plantStates;
	
	public PlantType(String plantTypeID, ItemStack itemStack) {
		this.initialStateID = "init";
		if (itemStack != null) {
			this.itemStack = new ItemStack(itemStack);
		}
		this.plantTypeID = plantTypeID;
		this.plantStates = new HashMap<>();
	}
	
	public PlantType(String plantTypeID) {
		this(plantTypeID, null);
	}
	
	public PlantStateDefinition getInitialState() {
		return this.plantStates.get(initialStateID);
	}
	
	public PlantStateDefinition getState(String stateID) {
		return this.plantStates.get(stateID);
	}
	
	public PlantStateDefinition setState(String stateID, PlantStateDefinition stateDefinition) {
		return this.plantStates.put(stateID, stateDefinition);
	}
	
	public ItemStack getItemStack() {
		if (itemStack == null) {
			return null;
		}
		return new ItemStack(itemStack);
	}
	
	public void setItemStack(ItemStack itemStack) {
		if (itemStack == null) {
			this.itemStack = null;
		} else {
			this.itemStack = new ItemStack(itemStack);
		}
	}
	
	public String getPlantTypeID() {
		return plantTypeID;
	}
	
	public String getPlantStateDefinitionID(PlantStateDefinition plantStateDefinition) {
		if (plantStateDefinition == null) {
			return null;
		}
		for (Map.Entry<String, PlantStateDefinition> plantStateDefinitionEntry : this.plantStates.entrySet()) {
			if (plantStateDefinitionEntry.getValue().equals(plantStateDefinition)) {
				return plantStateDefinitionEntry.getKey();
			}
		}
		return null;
	}
	
}
