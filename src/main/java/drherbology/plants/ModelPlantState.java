package drherbology.plants;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;

import drherbology.listeners.ModelPlantsListener;

public class ModelPlantState extends AbstractPlantState<ModelPlantStateDefinition> implements PlantState<ModelPlantStateDefinition> {
	
	private ArmorStand model;

	public ModelPlantState(ModelPlantStateDefinition plantStateDefinition) {
		super(plantStateDefinition);
	}
	
	public ModelPlantState(ModelPlantStateDefinition plantStateDefinition, long time) {
		super(plantStateDefinition, time);
	}
	
	public void spawnPlant(Location location) {
		ModelPlantStateDefinition modelPlantStateDefinition = getPlantStateDefinition();
		System.out.println("Spawning model: " + modelPlantStateDefinition.getStateModel().toString());
		Location finalLocation = location.clone();
		finalLocation.add(modelPlantStateDefinition.getOffsetVector());
		this.model = location.getWorld().spawn(finalLocation, ArmorStand.class);
		this.model.setVisible(false);
		this.model.setInvulnerable(true);
		this.model.setCanPickupItems(false);
		this.model.setCollidable(false);
		this.model.setGravity(false);
		finalizeSpawning();
		System.out.println("Finished Spawning model: " + modelPlantStateDefinition.getStateModel().toString());
	}
	
	private void finalizeSpawning() {
		System.out.println("Finalizing Spawning with: " + getPlantStateDefinition().getStateModel().getType());
		this.model.setBoots(getPlantStateDefinition().getStateModel());
		ModelPlantsListener.getInstance().registerModelPlantState(this, this.model.getLocation());
		super.startConditions();
	}

	@Override
	public void despawnPlant() {
		super.despawnPlant();
		physicDespawnPlant();
	}
	
	@Override
	public PlantState<?> transformToPlantStateDefinition(PlantStateDefinition plantStateDefinition) {
		System.out.println("PlantStateDefinition: " + plantStateDefinition);
		if (plantStateDefinition == null) {
			return super.transformToPlantStateDefinition(plantStateDefinition);
		}
		if (plantStateDefinition instanceof ModelPlantStateDefinition) {
			ModelPlantsListener.getInstance().unregisterModelPlantState(this.model.getLocation());
			ModelPlantStateDefinition newModelPlantStateDefinition = (ModelPlantStateDefinition)plantStateDefinition;
			Location newLocation = this.model.getLocation().subtract(getPlantStateDefinition().getOffsetVector());
			newLocation = newLocation.add(((ModelPlantStateDefinition) plantStateDefinition).getOffsetVector());
			setPlantStateDefinitionAndReset(newModelPlantStateDefinition);
			finalizeSpawning();
			return this;
		}
		return super.transformToPlantStateDefinition(plantStateDefinition);
	}
	
	@Override
	public PlantState<?> transformToPlantStateDefinition(PlantStateDefinition plantStateDefinition, long time) {
		if (time == 0) {
			return transformToPlantStateDefinition(plantStateDefinition);
		}
		System.out.println("PlantStateDefinition: " + plantStateDefinition);
		if (plantStateDefinition == null) {
			return super.transformToPlantStateDefinition(plantStateDefinition, time);
		}
		if (plantStateDefinition instanceof ModelPlantStateDefinition) {
			ModelPlantsListener.getInstance().unregisterModelPlantState(this.model.getLocation());
			ModelPlantStateDefinition newModelPlantStateDefinition = (ModelPlantStateDefinition)plantStateDefinition;
			Location newLocation = this.model.getLocation().subtract(getPlantStateDefinition().getOffsetVector());
			newLocation = newLocation.add(((ModelPlantStateDefinition) plantStateDefinition).getOffsetVector());
			setPlantStateDefinitionAndReset(newModelPlantStateDefinition, time);
			finalizeSpawning();
			return this;
		}
		return super.transformToPlantStateDefinition(plantStateDefinition, time);
	}

	@Override
	public void physicDespawnPlant() {
		ModelPlantsListener.getInstance().unregisterModelPlantState(this.model.getLocation());
		this.model.remove();
	}
	
}
