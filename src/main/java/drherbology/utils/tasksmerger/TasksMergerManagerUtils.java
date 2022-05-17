package drherbology.utils.tasksmerger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import drherbology.utils.SchedulerUtils;

public class TasksMergerManagerUtils {

	private Map<Long, MergedTask> tasksByTimeMap;
	private Map<Long, Long> tasksIDsToTimesMap;
	private AtomicLong idsCounter;
	
	private static TasksMergerManagerUtils instance;
	
	private TasksMergerManagerUtils() {
		this.tasksByTimeMap = new ConcurrentHashMap<>();
		this.tasksIDsToTimesMap = new ConcurrentHashMap<>();
		this.idsCounter = new AtomicLong(1);
	}
	
	public long scheduleSyncTask(Runnable task, long time) {
		if (task == null || time <= 0) {
			return -1;
		}
		long id = this.idsCounter.getAndIncrement();
		long timeToSchedule = System.currentTimeMillis() + time;
		MergedTask mt = this.tasksByTimeMap.get(timeToSchedule);
		if (mt == null) {
			mt = new MergedTask();
			mt.addNoIDsTask(() -> {
				this.tasksByTimeMap.remove(timeToSchedule);
			});
			this.tasksByTimeMap.put(timeToSchedule, mt);
			System.out.println("Before scheduling task with scheduler!");
			System.out.println(SchedulerUtils.getInstance().scheduleAsyncTask(time, mt));
			System.out.println("After scheduling task with scheduler!");
		}
		mt.addSyncTask(id, task);
		this.tasksIDsToTimesMap.put(id, timeToSchedule);
		return id;
	}
	
	public long scheduleAsyncTask(Runnable task, long time) {
		if (task == null || time <= 0) {
			return -1;
		}
		long id = this.idsCounter.getAndIncrement();
		long timeToSchedule = System.currentTimeMillis() + time;
		MergedTask mt = this.tasksByTimeMap.get(timeToSchedule);
		if (mt == null) {
			mt = new MergedTask();
			mt.addNoIDsTask(() -> {
				this.tasksByTimeMap.remove(timeToSchedule);
			});
			this.tasksByTimeMap.put(timeToSchedule, mt);
			System.out.println("Before scheduling task with scheduler!");
			System.out.println(SchedulerUtils.getInstance().scheduleAsyncTask(time, mt));
			System.out.println("After scheduling task with scheduler!");
		}
		mt.addAsyncTask(id, task);
		this.tasksIDsToTimesMap.put(id, timeToSchedule);
		return id;
	}
	
	public boolean cancelTask(long taskID) {
		MergedTask mergedTask = getMergedTaskOfTask(taskID);
		if (mergedTask == null) {
			return false;
		}
		mergedTask.removeTask(taskID);
		return true;
	}
	
	private Long getTimeOfTask(long taskID) {
		if (taskID <= 0) {
			return null;
		}
		return this.tasksIDsToTimesMap.get(taskID);
	}
	
	private MergedTask getMergedTaskOfTask(long taskID) {
		Long time = getTimeOfTask(taskID);
		if (time == null) {
			return null;
		}
		return this.tasksByTimeMap.get(time);
	}
	
	public static TasksMergerManagerUtils getInstance() {
		if (instance == null) {
			instance = new TasksMergerManagerUtils();
		}
		return instance;
	}
	
}
