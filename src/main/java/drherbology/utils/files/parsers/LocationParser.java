package drherbology.utils.files.parsers;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

public class LocationParser {

	public static String parse(Location location) {
		String locationStr = location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ() + "," + location.getPitch() + "," + location.getYaw();
		return locationStr.replace(".", "^");
	}
	
	public static Location parse(String locationStr) {
		locationStr = locationStr.replace("^", ".");
		String[] splittedLocationStr = locationStr.split(",");
		if (splittedLocationStr.length != 4 && splittedLocationStr.length != 6) {
			return null;
		}
		String worldName = splittedLocationStr[0];
		World world = Bukkit.getWorld(worldName);
		if (world == null) {
			return null;
		}
		try {
			Double x = Double.parseDouble(splittedLocationStr[1]);
			Double y = Double.parseDouble(splittedLocationStr[2]);
			Double z = Double.parseDouble(splittedLocationStr[3]);
			if (splittedLocationStr.length == 6) {
				Float pitch = Float.parseFloat(splittedLocationStr[4]);
				Float yaw = Float.parseFloat(splittedLocationStr[5]);
				return new Location(world, x, y, z, pitch, yaw);
			}
			return new Location(world, x, y, z);
		} catch (Exception exception) {
			return null;
		}
	}
	
}
