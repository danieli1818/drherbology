package drherbology.plants.harvest.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import drherbology.plants.PlantState;

public class HarvestEvent extends Event implements Cancellable {
	
	private static final HandlerList handlers = new HandlerList();
	private boolean isCancelled;
	
	private PlantState<?> plantState;
	private Player player;
	private ItemStack mainHandHeldItem;
	private ItemStack offHandHeldItem;
	
	public HarvestEvent(PlantState<?> plantState, Player player, ItemStack mainHandHeldItem, ItemStack offHandHeldItem) {
		this.isCancelled = false;
		this.plantState = plantState;
		this.player = player;
		this.mainHandHeldItem = mainHandHeldItem;
		this.offHandHeldItem = offHandHeldItem;
	}

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.isCancelled;
	}

	@Override
	public void setCancelled(boolean cancel) {
		this.isCancelled = cancel;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public ItemStack getMainHandHeldItem() {
		return mainHandHeldItem;
	}
	
	public ItemStack getOffHandHeldItem() {
		return offHandHeldItem;
	}
	
	public PlantState<?> getPlantState() {
		return plantState;
	}

}
