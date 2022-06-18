package drherbology.management;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import drguis.guis.GUI;
import drguis.guis.icons.BaseIcon;
import drguis.guis.icons.Icon;
import drguis.guis.types.list.general.ArrayGUIsList;
import drherbology.plants.PlantType;
import drherbology.utils.items.ItemsUtils;

public class PlantsGUIManager {
	
	private GUI plantsGUI;
	
	public static PlantsGUIManager instance;
	
	private PlantsGUIManager() {
		this.plantsGUI = null;
	}
	
	public static PlantsGUIManager getInstance() {
		if (instance == null) {
			instance = new PlantsGUIManager();
		}
		return instance;
	}
	
	public GUI getGUI() {
		return this.plantsGUI;
	}
	
	public GUI loadGUI(Map<ItemStack, PlantType> plantTypes) {
		ArrayGUIsList<Icon> plantsGUI = new ArrayGUIsList<Icon>(36, "Plants Menu");
		for (Map.Entry<ItemStack, PlantType> plantTypeEntry : plantTypes.entrySet()) {
			ItemStack itemStack = plantTypeEntry.getKey();
			PlantType plantType = plantTypeEntry.getValue();
			System.out.println("Loading ItemStack: " + itemStack + " With Plant Type: " + plantType);
			if (itemStack == null || plantType == null) {
				continue;
			}
			Icon icon = createPlantTypeIcon(plantType);
			if (icon == null) {
				continue;
			}
			plantsGUI.addIcon(icon);
		}
		this.plantsGUI = plantsGUI;
		return this.plantsGUI;
	}
	
	public GUI loadGUI(Collection<PlantType> plantTypes) {
		ArrayGUIsList<Icon> plantsGUI = new ArrayGUIsList<Icon>(36, "Plants Menu");
		for (PlantType plantType : plantTypes) {
			ItemStack itemStack = plantType.getItemStack();
			System.out.println("Loading ItemStack: " + itemStack + " With Plant Type: " + plantType);
			if (itemStack == null || plantType == null) {
				continue;
			}
			Icon icon = createPlantTypeIcon(plantType);
			if (icon == null) {
				continue;
			}
			plantsGUI.addIcon(icon);
		}
		this.plantsGUI = plantsGUI;
		return this.plantsGUI;
	}
	
	private Icon createPlantTypeIcon(PlantType plantType) {
		ItemStack itemStack = plantType.getItemStack();
		if (itemStack == null) {
			return null;
		}
		Set<ItemStack> itemStacksToGivePlayer = new HashSet<>();
		itemStacksToGivePlayer.add(PlantsItemsManager.getInstance().getPlantTypeItemStack(plantType));
		Icon icon = new BaseIcon(itemStack).addClickAction((Player player) -> ItemsUtils.giveItemStacksToPlayer(player, itemStacksToGivePlayer));
		return icon;
	}
	
}
