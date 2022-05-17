package drherbology.plants.conditions;

import java.util.Set;

public class Animation {

	private String toStateID;
	private Set<ConditionsFactory> conditionsFactories;
	
	public Animation(String toStateID, Set<ConditionsFactory> conditionsFactories) {
		this.toStateID = toStateID;
		this.conditionsFactories = conditionsFactories;
	}
	
	public String getToStateID() {
		return toStateID;
	}
	
	public Set<ConditionsFactory> getConditionsFactories() {
		return conditionsFactories;
	}
	
}
