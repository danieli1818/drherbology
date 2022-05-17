package drherbology.plants.harvest;

import org.bukkit.entity.Player;

public interface HarvestRewardFunction {

	public void run(Player player);
	
	public void runInSyncMode(Player player);
	
}
