package drherbology.utils;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

public class SchedulerUtils {

	private static SchedulerUtils instance = null;
	
	private Plugin plugin;
	
	private SchedulerUtils(Plugin plugin) {
		this.plugin = plugin;
	}
	
	public static SchedulerUtils getInstance(Plugin plugin) {
		if (instance == null) {
			instance = new SchedulerUtils(plugin);
		}
		return instance;
	}
	
	public static SchedulerUtils getInstance() {
		return instance;
	}
	
	public int scheduleAsyncTask(long delay, Runnable runnable) {
		BukkitTask bukkitTask = (BukkitTask)getScheduler().runTaskLater(this.plugin, runnable, delay);
		if (bukkitTask == null) {
			return -1;
		}
		return bukkitTask.getTaskId();
	}

	public int scheduleSyncTask(long delay, Runnable runnable) {
		return getScheduler().scheduleSyncDelayedTask(this.plugin, runnable, delay);
	}
	
	private BukkitScheduler getScheduler() {
		return this.plugin.getServer().getScheduler();
	}
	
	public boolean cancelTask(int taskID) {
		if (getScheduler().isQueued(taskID)) {
			getScheduler().cancelTask(taskID);
			return true;
		}
		return false;
	}
	
}
