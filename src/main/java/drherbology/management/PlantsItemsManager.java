package drherbology.management;

import java.util.Collection;

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
import drherbology.plants.PlantType;
import drherbology.utils.messages.MessagesSender;

public class PlantsItemsManager {
	
	private String pluginID;
	
	private static PlantsItemsManager instance;
	
	private PlantsItemsManager(String pluginID) {
		this.pluginID = pluginID;
	}
	
	public static PlantsItemsManager getInstance(Plugin plugin) {
		if (instance == null) {
			instance = new PlantsItemsManager(plugin.getName());
		}
		return instance;
	}
	
	public static PlantsItemsManager getInstance() {
		return instance;
	}

	public void loadItems(Collection<PlantType> plantTypes) {
		for (PlantType plantType : plantTypes) {
			getPlantTypeItemStack(plantType);
		}
	}
	
	public ItemStack getPlantTypeItemStack(PlantType plantType) {
		ItemStack plantTypeItemStack = plantType.getItemStack();
		if (plantTypeItemStack == null || plantType == null) {
			return null;
		}
		String plantTypeID = plantType.getPlantTypeID();
		if (plantTypeID == null) {
			return null;
		}
		ItemStack itemStack = CustomItemStacksManager.getInstance().getCustomItemStack(pluginID, plantTypeID);
		if (itemStack == null) {
			itemStack = createPlantTypeItemStack(plantTypeItemStack, plantType);
		}
		return itemStack;
	}
	
	public ItemStack createPlantTypeItemStack(ItemStack iconItemStack, PlantType plantType) {
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
						MessagesSender.getInstance().sendErrorMessage("You aren't looking at a block!", player);
					} else {
						PlantsManager.getInstance().createPlant(plantSpawnLocation, plantTypeID);
					}
				})
				.addRightClickAction(new ConsumeAction()).setShouldCancelRightClick(true);
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
