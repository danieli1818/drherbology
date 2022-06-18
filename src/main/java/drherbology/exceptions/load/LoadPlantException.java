package drherbology.exceptions.load;

public class LoadPlantException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4554181929231568411L;
	
	private String filename;
	
	public LoadPlantException(String message) {
		super(message);
	}
	
	public LoadPlantException(String message, Throwable err) {
		super(message, err);
	}
	
	public LoadPlantException(String filename, String message) {
		this(message);
		this.filename = filename;
	}
	
	public LoadPlantException(String filename, String message, Throwable err) {
		this(message, err);
		this.filename = filename;
	}
	
	@Override
	public String getMessage() {
		String message = super.getMessage();
		if (filename != null) {
			message = "Loading error in file: " + filename + " with the message: " + message;
		}
		return message;
	}
	
	public String getFilename() {
		return this.filename;
	}
	
	
}
