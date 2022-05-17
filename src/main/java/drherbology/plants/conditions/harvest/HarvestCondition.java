package drherbology.plants.conditions.harvest;

import drherbology.plants.conditions.AbstractCondition;

public class HarvestCondition extends AbstractCondition {

	public void onHarvest() {
		setState(true);
		firePropertyChange("state", false, true);
	}
	
}
