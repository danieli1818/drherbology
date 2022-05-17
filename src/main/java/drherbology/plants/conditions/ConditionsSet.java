package drherbology.plants.conditions;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashSet;
import java.util.Set;

import drherbology.utils.AbstractPropertyChangeObservable;

public class ConditionsSet extends AbstractPropertyChangeObservable implements PropertyChangeListener {

	private Set<Condition> conditions;
	private Set<TimePassCondition> timePassConditions;
	private boolean state;
	
	public ConditionsSet() {
		super();
		this.conditions = new HashSet<>();
		this.timePassConditions = new HashSet<>();
		this.state = false;
	}
	
	public boolean addCondition(Condition condition) {
		if (condition instanceof TimePassCondition) {
			return addTimePassCondition((TimePassCondition)condition);
		}
		boolean wasAdded = conditions.add(condition);
		if (wasAdded) {
			condition.addPropertyChangeListener(this);
			return true;
		}
		return false;
	}
	
	public boolean addTimePassCondition(TimePassCondition condition) {
		boolean wasAdded = timePassConditions.add(condition);
		if (wasAdded) {
			condition.addPropertyChangeListener(this);
			return true;
		}
		return false;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getNewValue() instanceof Boolean && (boolean)evt.getNewValue()) {
			if (areAllConditionsTrue()) {
				setState(true);
			} else {
				setState(false);
			}
		}
	}
	
	public boolean onStateStart(long spawnTime) {
		boolean areAllConditionsTrue = true;
		for (Condition condition : conditions) {
			areAllConditionsTrue = areAllConditionsTrue && condition.onStateStart(spawnTime);
		}
		for (Condition condition : timePassConditions) {
			areAllConditionsTrue = areAllConditionsTrue && condition.onStateStart(spawnTime);
		}
		return areAllConditionsTrue;
	}
	
	public void cancelAllConditionsTasks() {
		for (Condition condition : conditions) {
			condition.cancelTasks();
		}
	}
	
	private boolean setState(boolean state) {
		if (this.state == state) {
			return false;
		}
		this.state = state;
		firePropertyChange("state", !state, state);
		return true;
	}
	
	private boolean areAllConditionsTrue() {
		return this.conditions.stream().allMatch((Condition condition) -> condition.isTrue());
	}
	
	public long getTimePassed() {
		long timePassed = timePassConditions.stream().map((TimePassCondition condition) -> condition.getTimePassed()).reduce((long)0, (Long num1, Long num2) -> Math.max(num1, num2));
		System.out.println("Size of conditions: " + timePassConditions.size());
		System.out.println("Conditions:");
		for (Condition condition : timePassConditions) {
			System.out.println("Condition: " + condition);
		}
		System.out.println("Time Passed: " + timePassed);
		return timePassed;
	}
	
	public void printConditionsSet() {
		System.out.println("Printing ConditionsSet");
		System.out.println("Conditions size: " + conditions.size());
		System.out.println("Conditions:");
		for (Condition condition : conditions) {
			System.out.println(condition);
		}
		System.out.println("TimePassConditions size: " + timePassConditions.size());
		System.out.println("TimePassConditions:");
		for (Condition condition : timePassConditions) {
			System.out.println(condition);
		}
		System.out.println("Done");
	}
	
}
