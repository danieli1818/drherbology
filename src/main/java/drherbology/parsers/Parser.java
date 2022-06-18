package drherbology.parsers;

import drherbology.exceptions.parse.ParseException;

public interface Parser<T, S> {

	public T parse(S object) throws ParseException;
	
}
