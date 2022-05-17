package drherbology.plants.conditions;

import drherbology.utils.PropertyChangeObservable;

public interface Condition extends PropertyChangeObservable {

	public boolean onStateStart(long spawnTime);
	
	public boolean isTrue();
	
	public void cancelTasks();
	
}
