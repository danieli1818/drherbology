package drherbology.plants;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class ModelPlantStateDefinition extends AbstractPlantStateDefinition implements PlantStateDefinition {

	private ItemStack stateModel;
	private Vector offsetVector;
	
	public ModelPlantStateDefinition(PlantType type, ItemStack stateModel) {
		super(type);
		this.stateModel = stateModel;
		this.offsetVector = new Vector(0.5, 0, 0.5);
	}
	
	public ModelPlantStateDefinition(ItemStack stateModel) {
		super();
		this.stateModel = stateModel;
		this.offsetVector = new Vector(0.5, 0, 0.5);
	}
	
	public ModelPlantStateDefinition(PlantType type, ItemStack stateModel, Vector offsetVector) {
		super(type);
		this.stateModel = stateModel;
		this.offsetVector = offsetVector;
	}
	
	public ModelPlantStateDefinition(ItemStack stateModel, Vector offsetVector) {
		super();
		this.stateModel = stateModel;
		this.offsetVector = offsetVector;
	}
	
	public ModelPlantState getPlantState() {
		return new ModelPlantState(this);
	}
	
	@Override
	public PlantState<?> getPlantState(long time) {
		return new ModelPlantState(this, time);
	}
	
	public ItemStack getStateModel() {
		return this.stateModel;
	}
	
	public Vector getOffsetVector() {
		return this.offsetVector;
	}
	
	@Override
	public String toString() {
		return "ModelPlantStateDefinition with: " + this.stateModel.getAmount() + " x " + this.stateModel.getType() + " and an offset of: " + this.offsetVector;
	}
	
}
