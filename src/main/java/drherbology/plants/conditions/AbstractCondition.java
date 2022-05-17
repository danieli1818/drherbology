package drherbology.plants.conditions;

import drherbology.utils.AbstractPropertyChangeObservable;

public abstract class AbstractCondition extends AbstractPropertyChangeObservable implements Condition {

	private boolean state;
	
	public AbstractCondition() {
		this.state = false;
	}
	
	@Override
	public boolean isTrue() {
		return this.state;
	}

	@Override
	public boolean onStateStart(long spawnTime) {
		this.state = false;
		return false;
	}
	
	protected void toggleState() {
		this.state = !this.state;
		firePropertyChange("state", !state, state);
	}
	
	protected boolean setState(boolean state) {
		if (this.state == state) {
			return false;
		}
		this.state = state;
		return true;
	}
	
	@Override
	public void cancelTasks() {
		
	}
	
}
