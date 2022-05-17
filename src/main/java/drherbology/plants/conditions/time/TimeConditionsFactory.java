package drherbology.plants.conditions.time;

import drherbology.plants.PlantState;
import drherbology.plants.conditions.Condition;
import drherbology.plants.conditions.ConditionsFactory;

public class TimeConditionsFactory implements ConditionsFactory {

	private int time;
	
	public TimeConditionsFactory(int time) {
		this.time = time;
	}
	
	@Override
	public Condition createCondition(PlantState<?> plantState) {
		return new TimeCondition(time);
	}

}
