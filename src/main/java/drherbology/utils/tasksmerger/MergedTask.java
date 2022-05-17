package drherbology.utils.tasksmerger;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import drherbology.utils.SchedulerUtils;

public class MergedTask implements Runnable {

	private Map<Long, Runnable> syncTasks;
	private Map<Long, Runnable> asyncTasks;
	private Set<Runnable> noIDsTasks;
	
	public MergedTask() {
		this.syncTasks = new ConcurrentHashMap<>();
		this.asyncTasks = new ConcurrentHashMap<>();
		this.noIDsTasks = ConcurrentHashMap.newKeySet();
	}

	@Override
	public void run() {
		System.out.println("Starting to run!");
		for (Runnable runnable : this.syncTasks.values()) {
			SchedulerUtils.getInstance().scheduleSyncTask(0, runnable);
		}
		for (Runnable runnable : this.asyncTasks.values()) {
			runnable.run();
		}
		System.out.println("Finished running!");
	}
	
	public boolean addSyncTask(long taskID, Runnable task) {
		if (task == null || this.syncTasks.containsKey(taskID) || this.asyncTasks.containsKey(taskID)) {
			return false;
		}
		this.syncTasks.put(taskID, task);
		return true;
	}
	
	public boolean addAsyncTask(long taskID, Runnable task) {
		if (task == null || this.syncTasks.containsKey(taskID) || this.asyncTasks.containsKey(taskID)) {
			return false;
		}
		this.asyncTasks.put(taskID, task);
		return true;
	}
	
	public boolean removeTask(long taskID) {
		if (this.syncTasks.containsKey(taskID)) {
			return this.syncTasks.remove(taskID) != null;
		}
		return this.asyncTasks.remove(taskID) != null;
	}
	
	public boolean addNoIDsTask(Runnable task) {
		if (task == null) {
			return false;
		}
		this.noIDsTasks.add(task);
		return true;
	}
	
	public boolean removeNoIDsTask(Runnable task) {
		return this.noIDsTasks.remove(task);
	}
	
}
