package drherbology.plants.harvest;

import org.bukkit.entity.Player;

import drherbology.utils.SchedulerUtils;

public abstract class AbstractHarvestRewardFunction implements HarvestRewardFunction {

	public AbstractHarvestRewardFunction() {
		
	}
	
	@Override
	public void runInSyncMode(Player player) {
		SchedulerUtils.getInstance().scheduleSyncTask(0, () -> {
			run(player);
		});
		
	}

	
	
}
