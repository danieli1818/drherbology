package drherbology.listeners;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.PlayerInventory;

import drherbology.plants.ModelPlantState;

public class ModelPlantsListener implements Listener {

	private static ModelPlantsListener instance;
	
	private Map<Location, ModelPlantState> modelPlantStates;
	
	private ModelPlantsListener() {
		this.modelPlantStates = new ConcurrentHashMap<>();
	}
	
	public static ModelPlantsListener getInstance() {
		if (instance == null) {
			instance = new ModelPlantsListener();
		}
		return instance;
	}
	
	@EventHandler
	public void onPlayerInteractEntityEvent(PlayerInteractAtEntityEvent event) {
		System.out.println("Event has occured: " + event);
		Entity entity = event.getRightClicked();
		if (!(entity instanceof ArmorStand)) {
			return;
		}
		ArmorStand armorStand = (ArmorStand)entity;
		System.out.println(this.modelPlantStates);
		System.out.println(this.modelPlantStates.keySet());
		System.out.println(this.modelPlantStates.values());
		ModelPlantState modelPlantState = this.modelPlantStates.get(armorStand.getLocation());
		if (modelPlantState == null) {
			return;
		}
		event.setCancelled(true);
		PlayerInventory playerInventory = event.getPlayer().getInventory();
		modelPlantState.onInteract(event.getPlayer(), playerInventory.getItemInMainHand(), playerInventory.getItemInOffHand());
		System.out.println(event);
	}
	
	public ModelPlantState registerModelPlantState(ModelPlantState modelPlantState, Location location) {
		return this.modelPlantStates.put(location, modelPlantState);
	}
	
	public ModelPlantState unregisterModelPlantState(Location location) {
		return this.modelPlantStates.remove(location);
	}
	
}
