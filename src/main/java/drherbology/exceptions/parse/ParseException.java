package drherbology.exceptions.parse;

public class ParseException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1373118484586434720L;

	public ParseException(String message) {
		super(message);
	}
	
	public ParseException(String message, Throwable err) {
		super(message, err);
	}
	
}
