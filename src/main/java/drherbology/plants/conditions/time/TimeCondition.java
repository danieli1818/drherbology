package drherbology.plants.conditions.time;

import drherbology.plants.conditions.AbstractCondition;
import drherbology.plants.conditions.TimePassCondition;
import drherbology.utils.tasksmerger.TasksMergerManagerUtils;

public class TimeCondition extends AbstractCondition implements TimePassCondition {
	
	private long time;
	private long taskID;
	
	public TimeCondition(long time) {
		this.time = time;
		this.taskID = -1;
	}

	@Override
	public boolean onStateStart(long spawnTime) {
		super.onStateStart(spawnTime);
		long currentTime = System.currentTimeMillis();
		long timeDiff = currentTime - spawnTime;
		long timeDiffInTicks = timeDiff / 20;
		System.out.println("Before scheduling task!");
		System.out.println("Time diff in ticks: " + timeDiffInTicks);
		if (timeDiffInTicks > time) {
			setState(true);
			return true;
		}
		scheduleSettingTrueAsyncTask(time - timeDiffInTicks);
		System.out.println("After scheduling task!");
		return false;
	}
	
	private void scheduleSettingTrueAsyncTask(long delay) {
		this.taskID = TasksMergerManagerUtils.getInstance().scheduleAsyncTask(() -> {
			setState(true);
			firePropertyChange("state", false, true);
		}, delay);
	}
	
	@Override
	public void cancelTasks() {
		super.cancelTasks();
		TasksMergerManagerUtils.getInstance().cancelTask(taskID);
	}

	@Override
	public long getTimePassed() {
		return time;
	}

}
