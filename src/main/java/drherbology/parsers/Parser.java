package drherbology.parsers;

public interface Parser<T, S> {

	public T parse(S object) throws ParseException;
	
}
