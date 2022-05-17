package drherbology.plants.datatypes;

public class PlantNextStateInfo {

	private String nextState;
	private long time;
	
	public PlantNextStateInfo(String nextState) {
		this(nextState, 0);
	}
	
	public PlantNextStateInfo(String nextState, long time) {
		this.nextState = nextState;
		this.time = time;
	}
	
	public String getNextState() {
		return nextState;
	}
	
	public long getTime() {
		return time;
	}
	
}
