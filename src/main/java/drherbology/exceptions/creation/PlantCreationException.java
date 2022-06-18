package drherbology.exceptions.creation;

import org.apache.commons.lang.NullArgumentException;

public class PlantCreationException extends RuntimeException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3876460113779417171L;
	private String plantType;
	private String plantState;

	public PlantCreationException(String plantType, String message) {
		super(message);
		if (plantType == null) {
			throw new NullArgumentException("plantType");
		}
		this.plantType = plantType;
	}
	
	public PlantCreationException(String plantType, String message, Throwable err) {
		super(message, err);
		if (plantType == null) {
			throw new NullArgumentException("plantType");
		}
		this.plantType = plantType;
	}
	
	public PlantCreationException(String plantType, String plantState, String message) {
		this(plantType, message);
		this.plantState = plantState;
	}
	
	public PlantCreationException(String plantType, String plantState, String message, Throwable err) {
		this(plantType, message, err);
		this.plantState = plantState;
	}
	
	@Override
	public String getMessage() {
		String message = super.getMessage();
		if (plantState != null) {
			message = " in state: " + plantState + " with the message: " + message;
		}
		message = "Creation of plant type: " + plantType + message;
		return message;
	}
	
	public String getPlantType() {
		return plantType;
	}
	
	public String getPlantState() {
		return plantState;
	}
	
}
