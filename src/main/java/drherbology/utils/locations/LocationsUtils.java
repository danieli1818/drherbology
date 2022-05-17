package drherbology.utils.locations;

import org.bukkit.Chunk;
import org.bukkit.Location;

public class LocationsUtils {

	public static boolean isLocationInChunk(Location location, Chunk chunk) {
		if (location == null || chunk == null) {
			return false;
		}
		return location.getChunk().equals(chunk);
	}
	
}
