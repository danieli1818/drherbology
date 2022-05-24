package drherbology.management;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import drcustomitems.items.CustomItemStacksManager;
import drcustomitems.items.CustomItemsBuilder;
import drcustomitems.items.actions.ActionsHolder;
import drcustomitems.items.actions.BaseActionsHolder;
import drcustomitems.items.actions.CustomItemsActionsManager;
import drcustomitems.items.actions.types.ConsumeAction;
import drguis.guis.GUI;
import drguis.guis.icons.BaseIcon;
import drguis.guis.icons.Icon;
import drguis.guis.types.general.ArrayGUI;
import drherbology.plants.PlantType;
import drherbology.utils.items.ItemsUtils;

public class PlantsGUIManager {
	
	private GUI plantsGUI;
	private String pluginID;
	
	public static PlantsGUIManager instance;
	
	private PlantsGUIManager(String pluginID) {
		this.plantsGUI = null;
		this.pluginID = pluginID;
	}
	
	public static PlantsGUIManager getInstance() {
		return instance;
	}
	
	public static PlantsGUIManager getInstance(Plugin plugin) {
		if (instance == null) {
			instance = new PlantsGUIManager(plugin.getName());
		}
		return instance;
	}
	
	public GUI getGUI() {
		return this.plantsGUI;
	}
	
	public GUI loadGUI(Map<ItemStack, PlantType> plantTypes) {
		ArrayGUI plantsGUI = new ArrayGUI(36, "Plants Menu");
		for (Map.Entry<ItemStack, PlantType> plantTypeEntry : plantTypes.entrySet()) {
			ItemStack itemStack = plantTypeEntry.getKey();
			PlantType plantType = plantTypeEntry.getValue();
			System.out.println("Loading ItemStack: " + itemStack + " With Plant Type: " + plantType);
			if (itemStack == null || plantType == null) {
				continue;
			}
			Icon icon = createPlantTypeIcon(itemStack, plantType);
			if (icon == null) {
				continue;
			}
			plantsGUI.addIcon(icon);
		}
		this.plantsGUI = plantsGUI;
		return this.plantsGUI;
	}
	
	private Icon createPlantTypeIcon(ItemStack itemStack, PlantType plantType) {
		Set<ItemStack> itemStacksToGivePlayer = new HashSet<>();
		itemStacksToGivePlayer.add(getPlantTypeItemStack(itemStack, plantType));
		Icon icon = new BaseIcon(itemStack).addClickAction((Player player) -> ItemsUtils.giveItemStacksToPlayer(player, itemStacksToGivePlayer));
		return icon;
	}
	
	private ItemStack getPlantTypeItemStack(ItemStack iconItemStack, PlantType plantType) {
		if (iconItemStack == null || plantType == null) {
			return null;
		}
		String plantTypeID = plantType.getPlantTypeID();
		if (plantTypeID == null) {
			return null;
		}
		ItemStack itemStack = CustomItemStacksManager.getInstance().getCustomItemStack(pluginID, plantTypeID);
		if (itemStack == null) {
			itemStack = createPlantTypeItemStack(iconItemStack, plantType);
		}
		return itemStack;
	}
	
	private ItemStack createPlantTypeItemStack(ItemStack iconItemStack, PlantType plantType) {
		if (iconItemStack == null || plantType == null) {
			return null;
		}
		String plantTypeID = plantType.getPlantTypeID();
		if (plantTypeID == null) {
			return null;
		}
		ItemStack itemStack = new CustomItemsBuilder(iconItemStack).create(plantTypeID, pluginID);
		ActionsHolder actionsHolder = new BaseActionsHolder().addRightClickAction(
				(Player player, ItemStack heldItemStack) -> {
					Location plantSpawnLocation = getPlantSpawnLocation(player);
					if (plantSpawnLocation == null) {
						// TODO Send error message to player "You aren't looking at a block!"
					} else {
						PlantsManager.getInstance().createPlant(plantSpawnLocation, plantTypeID);
					}
				})
				.addRightClickAction(new ConsumeAction());
		CustomItemsActionsManager.getInstance().addActionsHolderToItemStack(itemStack, actionsHolder);
		CustomItemStacksManager.getInstance().setCustomItemStack(pluginID, plantTypeID, itemStack);
		return itemStack;
	}
	
	private Location getPlantSpawnLocation(Player player) {
		Block lookingAtBlock = player.getTargetBlock(null, 10);
		if (lookingAtBlock == null || lookingAtBlock.getType() == Material.AIR) {
			return null;
		}
		Block block = lookingAtBlock.getRelative(BlockFace.UP);
		if (block == null || block.getType() != Material.AIR) {
			return null;
		}
		return block.getLocation();
	}
	

}
