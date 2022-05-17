package drherbology.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import drherbology.plants.harvest.events.HarvestEvent;

public class GeneralPlantsListener implements Listener {
	
	private static GeneralPlantsListener instance;
	
	private GeneralPlantsListener() {
		
	}
	
	public static GeneralPlantsListener getInstance() {
		if (instance == null) {
			instance = new GeneralPlantsListener();
		}
		return instance;
	}

	@EventHandler
	public void onHarvestEvent(HarvestEvent event) {
		if (event.isCancelled()) {
			return;
		}
		event.getPlantState().onHarvest(event.getPlayer(), event.getMainHandHeldItem(), event.getOffHandHeldItem());
	}
	
}
