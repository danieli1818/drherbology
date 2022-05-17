package drherbology.utils.files.parsers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.util.Vector;

public class MathParser {

	public static Vector parseVector(String vector) {
		if (vector == null) {
			return null;
		}
		String[] vectorStringArray = vector.split(",\" \"*");
		if (vectorStringArray.length != 3) {
			return null;
		}
		try {
			List<Float> vectorCoords = Arrays.asList(vectorStringArray).stream().map(
					(String coord) -> Float.parseFloat(coord)).collect(Collectors.toList());
			return new Vector(vectorCoords.get(0), vectorCoords.get(1), vectorCoords.get(2));
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
}
