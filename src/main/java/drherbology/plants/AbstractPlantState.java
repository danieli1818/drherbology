package drherbology.plants;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import drherbology.plants.conditions.ConditionsSet;
import drherbology.plants.datatypes.PlantNextStateInfo;
import drherbology.plants.harvest.events.HarvestEvent;
import drherbology.utils.AbstractPropertyChangeObservable;

public abstract class AbstractPlantState<T extends PlantStateDefinition> extends AbstractPropertyChangeObservable implements PlantState<T>, PropertyChangeListener {

	private T plantStateDefinition;
	private Map<ConditionsSet, String> conditionsSetsMap;
	private AtomicBoolean isDespawning;
	private Boolean isHarvested;
	private Boolean haveConditionsStarted;
	private long spawnTime;

	public AbstractPlantState(T plantStateDefinition) {
		setPlantStateDefinitionAndReset(plantStateDefinition, 0);
	}
	
	public AbstractPlantState(T plantStateDefinition, long time) {
		setPlantStateDefinitionAndReset(plantStateDefinition, time);
	}
	
	protected boolean startConditions() {
		if (getSpawnTime() == 0) {
			setSpawnTime(System.currentTimeMillis());
		}
		if (haveConditionsStarted) {
			return false;
		}
		synchronized(haveConditionsStarted) {
			if (haveConditionsStarted) {
				return false;
			}
			haveConditionsStarted = true;
		}
		for (ConditionsSet conditionsSet : this.conditionsSetsMap.keySet()) {
			boolean hasOccured = conditionsSet.onStateStart(getSpawnTime());
			if (hasOccured) {
				long timePassed = conditionsSet.getTimePassed();
				String nextState = conditionsSetsMap.get(conditionsSet);
				if (nextState != null) {
					continueToNextState(nextState, timePassed);
					break;
				}
			}
		}
		return true;
	}
	
	public void logicSpawnPlant() {
		startConditions();
	}
	
	@Override
	public void despawnPlant() {
		this.isDespawning.set(true);
		for (ConditionsSet conditionsSet : this.conditionsSetsMap.keySet()) {
			conditionsSet.cancelAllConditionsTasks();
		}
	}

	public T getPlantStateDefinition() {
		return plantStateDefinition;
	}

	public void setPlantStateDefinition(T plantStateDefinition) {
		this.plantStateDefinition = plantStateDefinition;
	}
	
	public void setPlantStateDefinitionAndReset(T plantStateDefinition) {
		setPlantStateDefinition(plantStateDefinition);
		this.conditionsSetsMap = plantStateDefinition.getConditions(this);
		this.haveConditionsStarted = false;
		for (ConditionsSet conditionsSet : this.conditionsSetsMap.keySet()) {
			conditionsSet.addPropertyChangeListener(this);
		}
		this.isDespawning = new AtomicBoolean(false);
		this.isHarvested = false;
		this.spawnTime = 0;
	}
	
	public void setPlantStateDefinitionAndReset(T plantStateDefinition, long time) {
		setPlantStateDefinition(plantStateDefinition);
		this.conditionsSetsMap = plantStateDefinition.getConditions(this);
		this.haveConditionsStarted = false;
		for (ConditionsSet conditionsSet : this.conditionsSetsMap.keySet()) {
			conditionsSet.addPropertyChangeListener(this);
		}
		this.isDespawning = new AtomicBoolean(false);
		this.isHarvested = false;
		this.spawnTime = time;
	}
	
	public void continueToNextState(String nextState, long time) {
		firePropertyChange("nextState", null, new PlantNextStateInfo(nextState, time));
	}
	
	public void continueToNextState(String nextState) {
		firePropertyChange("nextState", null, new PlantNextStateInfo(nextState));
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		String nextState = conditionsSetsMap.get(evt.getSource());
		if (nextState != null) {
			continueToNextState(nextState);
		}
	}
	
	@Override
	public void onInteract(Player player, ItemStack mainHandHeldItem, ItemStack offHandHeldItem) {
		if (getPlantStateDefinition().canPlayerHarvestPlant(player, mainHandHeldItem, offHandHeldItem)) {
			Bukkit.getPluginManager().callEvent(new HarvestEvent(this, player, mainHandHeldItem, offHandHeldItem));
		}
	}
	
	@Override
	public void onHarvest(Player player, ItemStack mainHandHeldItem, ItemStack offHandHeldItem) {
		if (this.isHarvested) {
			return;
		}
		synchronized(this.isHarvested) {
			if (this.isHarvested) {
				return;
			}
			this.isHarvested = true;
		}
		plantStateDefinition.onHarvest(player, mainHandHeldItem, offHandHeldItem);
	}
	
	@Override
	public PlantState<?> transformToPlantStateDefinition(PlantStateDefinition plantStateDefinition) {
		return null;
	}
	
	@Override
	public PlantState<?> transformToPlantStateDefinition(PlantStateDefinition plantStateDefinition, long time) {
		return null;
	}
	
	@Override
	public long getSpawnTime() {
		return this.spawnTime;
	}
	
	private void setSpawnTime(long spawnTime) {
		this.spawnTime = spawnTime;
	}
	
}
